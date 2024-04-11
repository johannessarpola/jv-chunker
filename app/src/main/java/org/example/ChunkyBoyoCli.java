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
                .longOpt("interactionId")
                .hasArg(true)
                .desc("interaction id ([REQUIRED] or use --clientId)")
                .required(false)
                .build());
        options.addOption(Option.builder("c")
                .longOpt("clientId")
                .hasArg(true)
                .desc("client id ([REQUIRED] or use --interactionId)")
                .required(false)
                .build());
        options.addOption(Option.builder("f")
                .longOpt("file")
                .hasArg(true)
                .desc("[REQUIRED] one log-file or list of many log-files as input for log-parser")
                .numberOfArgs(Option.UNLIMITED_VALUES)
                .required()
                .build());

        try {
            CommandLine cmd = new DefaultParser().parse(options, args);
            if (cmd == null) {
                throw new NullPointerException("cmd is null");
            }
        } catch (Exception e) {
            Printer.println("could not parse args with error: {}", e);
        }
    }


    public ChunkyBoyoConfig Configuration() {
        var bldr = ChunkyBoyoConfig.builder();

        String sv, int i;
        if (cmd.hasOption("bufferSize")) {
            sv = cmd.getOptionValue("bufferSize");
            i = Integer.parseInt(sv);
            bldr.bufferSize(i);
        }

        if (cmd.hasOption("rowSize")) {
            sv = cmd.getOptionValue("rowSize");
            i = Integer.parseInt(sv);
            bldr.rowsize(i);
        }

        if (cmd.hasOption("outputFolder")) {
            bldr.outputFolder(cmd.getOptionValue("outputFolder"));
        }

        if (cmd.hasOption("inputFolder")) {
            bldr.outputFolder(cmd.getOptionValue("inputFolder"));
        }

        return bldr.build();
    }
}
