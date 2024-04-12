package fi.johannes;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class ChunkyBoyoCli {
    Options options = new Options();
    CommandLine cmd = null;

    public ChunkyBoyoCli(String[] args) {

        options.addOption(Option.builder("i")
                .hasArg(true)
                .longOpt("input")
                .desc("input folder use --input or -i")
                .required(true)
                .build());
        options.addOption(Option.builder("o")
                .longOpt("output")
                .hasArg(true)
                .desc("output folder use --output or -o")
                .required()
                .build());
        options.addOption(Option.builder("cs")
                .longOpt("chunkSize")
                .hasArg(true)
                .desc("chunks to divide to use --chunkSize or -cs")
                .required(false)
                .build());
        options.addOption(Option.builder("t")
                .longOpt("threads")
                .hasArg(true)
                .desc("how many, use --threads or -t")
                .required(false)
                .build());

        options.addOption(Option.builder("v")
                .longOpt("verbose")
                .hasArg(false)
                .desc("verbose use --verbose or -v")
                .required(false)
                .build());

        try {
            cmd = new DefaultParser().parse(options, args);
            if (cmd == null) {
                throw new NullPointerException("cmd is null");
            }
        } catch (Exception e) {
            Printer.println("could not parse args with error: {}", e);
        }
    }


    public ChunkyBoyoConfig asConfiguration() {
        var bldr = ChunkyBoyoConfig.builder();

        String sv;
        int i;
        if (cmd.hasOption("cs")) {
            sv = cmd.getOptionValue("cs");
            i = Integer.parseInt(sv);
            bldr.chunkSize(i);
        }

        if (cmd.hasOption("i")) {
            bldr.inputFolder(cmd.getOptionValue("i"));
        }

        if (cmd.hasOption("o")) {
            bldr.outputFolder(cmd.getOptionValue("o"));
        }

        if (cmd.hasOption("t")) {
            sv = cmd.getOptionValue("t");
            i = Integer.parseInt(sv);
            bldr.executorSize(i);
        }

        if (cmd.hasOption("v")) {
            bldr.verbose(true);
        }

        return bldr.build();
    }
}
