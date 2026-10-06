public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Main <file-path>");
            return;
        }

        FileLineSource source = new FileLineSource(args[0]);
        ConsoleSink sink = new ConsoleSink();
        ParserStage parser = new ParserStage();

        Pipeline pipeline = new Pipeline(source, sink).addStage(parser);
        pipeline.run();
        System.out.println("Malformed lines skipped: " + parser.malformedCount());
    }
}
