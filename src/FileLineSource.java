import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileLineSource implements Source<String> {
    private final Path filePath;

    public FileLineSource(String fileName) {
        this.filePath = Path.of(fileName);
    }

    @Override
    public void produce(Emitter<String> out) {
        try (Stream<String> lines = Files.lines(filePath)) {
            lines.forEach(out::emit);
        } catch (IOException e) {
            throw new RuntimeException("Could not read file: " + filePath, e);
        }
    }
}
