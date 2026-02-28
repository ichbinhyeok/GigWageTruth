package com.gigwager;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EncodingCorruptionGuardTest {

    private static final Pattern[] CORRUPTION_PATTERNS = new Pattern[] {
            Pattern.compile("�"), // replacement char
            Pattern.compile("Ã|â€™|â€“|â€œ|â€"), // common mojibake fragments
            Pattern.compile("\\?\\?"), // broken emoji/token fallback
            Pattern.compile("\\?[\\p{IsHangul}\\p{InCJKUnifiedIdeographs}]") // '?뫛' style corruption
    };

    @Test
    public void templatesAndJavaShouldNotContainKnownEncodingCorruptionPatterns() throws IOException {
        List<String> offenders = new ArrayList<>();

        try (Stream<Path> files = Files.walk(Path.of("src/main"))) {
            files.filter(Files::isRegularFile)
                    .filter(path -> {
                        String p = path.toString().replace('\\', '/');
                        return p.endsWith(".jte") || p.endsWith(".java");
                    })
                    .forEach(path -> scanFile(path, offenders));
        }

        assertTrue(offenders.isEmpty(),
                "Found potential encoding corruption patterns:\n - " + String.join("\n - ", offenders));
    }

    private void scanFile(Path path, List<String> offenders) {
        List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            offenders.add(path + " (read failed: " + e.getMessage() + ")");
            return;
        }

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (Pattern pattern : CORRUPTION_PATTERNS) {
                if (pattern.matcher(line).find()) {
                    offenders.add(path + ":" + (i + 1) + " -> " + line.trim());
                    break;
                }
            }
        }
    }
}

