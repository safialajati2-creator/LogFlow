import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pipeline {
    private final Source<String> source;
    private final Sink<LogRecord> sink;
    private final List<Stage<String, LogRecord>> stages = new ArrayList<>();

    public Pipeline(Source<String> source, Sink<LogRecord> sink) {
        this.source = source;
        this.sink = sink;
    }

    public Pipeline addStage(Stage<String, LogRecord> stage) {
        stages.add(stage);
        return this;
    }

    public List<Stage<String, LogRecord>> stages() {
        return Collections.unmodifiableList(stages);
    }

    public void run() {
        Emitter<LogRecord> sinkEmitter = sink::consume;
        Emitter<String> sourceEmitter = input -> {
            for (Stage<String, LogRecord> stage : stages) {
                try {
                    stage.process(input, sinkEmitter);
                } catch (StageException e) {
                    throw new RuntimeException("Pipeline stage failed", e);
                }
            }
        };

        for (Stage<String, LogRecord> stage : stages) {
            stage.open();
        }
        try {
            source.produce(sourceEmitter);
        } finally {
            for (int i = stages.size() - 1; i >= 0; i--) {
                stages.get(i).close();
            }
        }
    }
}
