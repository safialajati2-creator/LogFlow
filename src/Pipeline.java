import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pipeline {
    private final Source<String> source;
    private final Sink<String> sink;
    private final List<Stage<String, String>> stages = new ArrayList<>();

    public Pipeline(Source<String> source, Sink<String> sink) {
        this.source = source;
        this.sink = sink;
    }

    public Pipeline addStage(Stage<String, String> stage) {
        stages.add(stage);
        return this;
    }

    public List<Stage<String, String>> stages() {
        return Collections.unmodifiableList(stages);
    }

    public void run() {
        Emitter<String> emitter = sink::consume;

        for (int i = stages.size() - 1; i >= 0; i--) {
            Stage<String, String> stage = stages.get(i);
            Emitter<String> next = emitter;
            emitter = item -> {
                try {
                    stage.process(item, next);
                } catch (StageException e) {
                    throw new RuntimeException("Pipeline stage failed", e);
                }
            };
        }

        for (Stage<String, String> stage : stages) {
            stage.open();
        }
        try {
            source.produce(emitter);
        } finally {
            for (int i = stages.size() - 1; i >= 0; i--) {
                stages.get(i).close();
            }
        }
    }
}
