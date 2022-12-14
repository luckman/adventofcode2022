package tools;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class InputReader {
    private static final File file = new File("input.txt");

    public static List<String> readAllLines() {
        try {
            return Files.readAllLines(file.toPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
