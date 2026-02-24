package com.gigwager;

import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

public class DomainBuildGuardTest {

    @Test
    public void testNoHardcodedDomain() throws Exception {
        Path startJte = Paths.get("src/main/jte");
        Path startJava = Paths.get("src/main/java");

        checkDirectory(startJte);
        checkDirectory(startJava);
    }

    private void checkDirectory(Path startPath) throws Exception {
        if (!Files.exists(startPath))
            return;
        try (Stream<Path> stream = Files.walk(startPath)) {
            stream.filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            String content = Files.readString(path);
                            if (content.toLowerCase().contains("gigwagetruth.com")) {
                                fail("Hardcoded domain 'gigwagetruth.com' found in file: " + path);
                            }
                        } catch (Exception e) {
                            fail("Error reading file: " + path);
                        }
                    });
        }
    }
}
