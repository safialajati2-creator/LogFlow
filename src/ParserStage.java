import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Parses Common Log Format / Combined Log Format lines into LogRecord values. */
public class ParserStage implements Stage<String, LogRecord> {
    private static final DateTimeFormatter LOG_TIME_FORMAT = DateTimeFormatter
            .ofPattern("dd/MMM/uuuu:HH:mm:ss xx", Locale.ENGLISH)
            .withResolverStyle(ResolverStyle.STRICT);

    private static final Pattern LOG_PATTERN = Pattern.compile(
            "^\\s*(?<ip>\\S+)\\s+\\S+\\s+\\S+\\s+\\[(?<time>[^]]+)]\\s+"
                    + "\\\"(?<method>\\S+)\\s+(?<path>\\S+)\\s+[^\\\"]+\\\"\\s+"
                    + "(?<status>\\d{3})\\s+(?<bytes>\\S+)\\s+"
                    + "(?:\\\"[^\\\"]*\\\"\\s+\\\"(?<agent>[^\\\"]*)\\\")?\\s*$");

    private long malformedCount;

    @Override
    public void process(String input, Emitter<LogRecord> out) {
        if (input == null || input.isBlank()) {
            malformedCount++;
            return;
        }

        Matcher matcher = LOG_PATTERN.matcher(input);
        if (!matcher.matches()) {
            malformedCount++;
            return;
        }

        try {
            Instant timestamp = OffsetDateTime.parse(matcher.group("time"), LOG_TIME_FORMAT).toInstant();
            int status = parseStatus(matcher.group("status"));
            long bytes = parseBytes(matcher.group("bytes"));
            String path = matcher.group("path");
            String userAgent = matcher.group("agent") == null ? "" : matcher.group("agent");
            out.emit(new LogRecord(timestamp, matcher.group("ip"), matcher.group("method"), path,
                    status, bytes, userAgent, Map.of("query", queryPart(path)), input));
        } catch (DateTimeParseException | IllegalArgumentException e) {
            malformedCount++;
        }
    }

    public long malformedCount() {
        return malformedCount;
    }

    private static int parseStatus(String value) {
        int status = Integer.parseInt(value);
        if (status < 100 || status > 599) {
            throw new IllegalArgumentException("Invalid HTTP status");
        }
        return status;
    }

    private static long parseBytes(String value) {
        return "-".equals(value) ? 0L : Long.parseLong(value);
    }

    private static String queryPart(String path) {
        int questionMark = path.indexOf('?');
        return questionMark >= 0 ? path.substring(questionMark + 1) : "";
    }
}
