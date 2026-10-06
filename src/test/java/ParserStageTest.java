import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ParserStageTest {
    private static final String VALID =
            "127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "
                    + "\"GET /apache_pb.gif HTTP/1.0\" 200 2326 \"-\" "
                    + "\"Mozilla/5.0 (Test Browser)\"";

    @Test
    void parsesValidLine() {
        List<LogRecord> records = parse(VALID);
        assertEquals(1, records.size());
        assertEquals("127.0.0.1", records.get(0).clientIp());
        assertEquals(200, records.get(0).status());
    }

    @Test
    void skipsMissingField() {
        List<LogRecord> records = parse(VALID.replace("2326", ""));
        assertEquals(0, records.size());
    }

    @Test
    void skipsInvalidTimestamp() {
        List<LogRecord> records = parse(VALID.replace("10/Oct/2000", "31/Feb/2000"));
        assertEquals(0, records.size());
    }

    @Test
    void skipsInvalidStatusCode() {
        List<LogRecord> records = parse(VALID.replace(" 200 2326", " 999 2326"));
        assertEquals(0, records.size());
    }

    @Test
    void skipsBlankLine() {
        List<LogRecord> records = parse("   ");
        assertEquals(0, records.size());
    }

    @Test
    void acceptsExtraWhitespace() {
        List<LogRecord> records = parse("  " + VALID.replace(" - frank", "   -   frank") + "  ");
        assertEquals(1, records.size());
    }

    @Test
    void keepsSpacesInsideQuotedUserAgent() {
        LogRecord record = parse(VALID).get(0);
        assertEquals("Mozilla/5.0 (Test Browser)", record.userAgent());
    }

    @Test
    void keepsQueryStringAsAnAttribute() {
        LogRecord record = parse(VALID.replace("/apache_pb.gif", "/items?id=42")).get(0);
        assertEquals("id=42", record.attributes().get("query"));
    }

    @Test
    void countsMalformedLines() {
        ParserStage parser = new ParserStage();
        parser.process("not a log line", collecting(parser));
        assertEquals(1, parser.malformedCount());
        assertTrue(parser.malformedCount() > 0);
    }

    private static List<LogRecord> parse(String line) {
        ParserStage parser = new ParserStage();
        List<LogRecord> records = new ArrayList<>();
        parser.process(line, records::add);
        return records;
    }

    private static Emitter<LogRecord> collecting(ParserStage parser) {
        return ignored -> { };
    }
}
