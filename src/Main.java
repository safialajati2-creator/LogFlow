public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Main <file-path>");
            return;
        }

        FileLineSource source = new FileLineSource(args[0]);
        ConsoleSink sink = new ConsoleSink();

        Pipeline pipeline = new Pipeline(source, sink);
        pipeline.run();
    }
}
