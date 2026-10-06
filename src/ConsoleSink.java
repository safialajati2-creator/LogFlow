import java.time.format.DateTimeFormatter;

public class ConsoleSink implements Sink<LogRecord> {
    private static final DateTimeFormatter OUTPUT_TIME = DateTimeFormatter.ISO_INSTANT;

    @Override
    public void consume(LogRecord record) {
        System.out.printf("%s %s %s %s %d %d %s%n",
                OUTPUT_TIME.format(record.timestamp()), record.clientIp(), record.method(),
                record.path(), record.status(), record.bytes(), record.userAgent());
    }
}
