import java.nio.file.*;
import java.util.*;

public class UpdateJte {
    public static void main(String[] args) throws Exception {
        List<String> files = Arrays.asList(
                "src/main/jte/uber.jte",
                "src/main/jte/doordash.jte",
                "src/main/jte/terms.jte",
                "src/main/jte/salary/directory.jte",
                "src/main/jte/salary/city-work-level.jte",
                "src/main/jte/salary/city-report.jte",
                "src/main/jte/privacy.jte",
                "src/main/jte/pages/calculator.jte",
                "src/main/jte/methodology.jte",
                "src/main/jte/index.jte",
                "src/main/jte/contact.jte",
                "src/main/jte/about.jte",
                "src/main/jte/blog/uber-vs-doordash.jte",
                "src/main/jte/blog/tax-guide.jte",
                "src/main/jte/blog/multi-apping-guide.jte",
                "src/main/jte/blog/index.jte",
                "src/main/jte/blog/hidden-costs.jte");
        for (String f : files) {
            Path p = Paths.get(f);
            if (!Files.exists(p))
                continue;
            String content = Files.readString(p);

            // Add param
            if (!content.contains("@param Boolean noIndex = false")) {
                String[] lines = content.split("\\r?\\n");
                StringBuilder sb = new StringBuilder();
                boolean inserted = false;
                for (String line : lines) {
                    if (!inserted && !line.startsWith("@param") && !line.startsWith("@import")
                            && !line.trim().isEmpty()) {
                        sb.append("@param Boolean noIndex = false\n");
                        inserted = true;
                    }
                    sb.append(line).append("\n");
                }
                content = sb.toString();
            }
            // Add layout argument
            if (content.contains("@template.layout.main(") && !content.contains("noIndex = noIndex")) {
                content = content.replace("@template.layout.main(", "@template.layout.main(\n    noIndex = noIndex,");
            }
            Files.writeString(p, content);
        }
        System.out.println("Done processing JTE files from java");
    }
}
