public class Pipeline {
    private final Source<String> source;
    private final Stage<String, LogRecord> parser;
    private final Sink<LogRecord> sink;

    public Pipeline(Source<String> source, Stage<String, LogRecord> parser, Sink<LogRecord> sink) {
        this.source = source;
        this.parser = parser;
        this.sink = sink;
    }

    public void run() {
        Emitter<LogRecord> sinkEmitter = sink::consume;
        Emitter<String> parserInput = input -> process(input, sinkEmitter);

        parser.open();
        try {
            source.produce(parserInput);
        } finally {
            parser.close();
        }
    }

    private void process(String input, Emitter<LogRecord> output) {
        try {
            parser.process(input, output);
        } catch (StageException e) {
            throw new RuntimeException("Pipeline stage failed", e);
        }
    }
}
