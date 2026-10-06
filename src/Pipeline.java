public class Pipeline {
    private final Source<String> source;
    private final Sink<String> sink;

    public Pipeline(Source<String> source, Sink<String> sink) {
        this.source = source;
        this.sink = sink;
    }

    public void run() {
        Emitter<String> emitter = sink::consume;
        source.produce(emitter);
    }
}
