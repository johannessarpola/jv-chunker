package org.example;

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
                .option("i")
                .desc("input folder use --input or -i")
                .required(true)
                .build());
        options.addOption(Option.builder("o")
                .longOpt("output")
                .option("o")
                .hasArg(true)
                .desc("output folder use --output or -o")
                .required()
                .build());

        // TODO add rest stuff

        try {
            cmd = new DefaultParser().parse(options, args);
            if (cmd == null) {
                throw new NullPointerException("cmd is null");
            }
        } catch (Exception e) {
            Printer.println("could not parse args with error: {}", e);
        }
    }


    public ChunkyBoyoConfig Configuration() {
        var bldr = ChunkyBoyoConfig.builder();

        String sv;
        int i;
        if (cmd.hasOption("bs")) {
            sv = cmd.getOptionValue("bufferSize");
            i = Integer.parseInt(sv);
            bldr.bufferSize(i);
        }

        if (cmd.hasOption("rs")) {
            sv = cmd.getOptionValue("rowSize");
            i = Integer.parseInt(sv);
            bldr.rowsize(i);
        }

        if (cmd.hasOption("i")) {
            bldr.outputFolder(cmd.getOptionValue("i"));
        }

        if (cmd.hasOption("o")) {
            bldr.outputFolder(cmd.getOptionValue("o"));
        }

        return bldr.build();
    }
}
