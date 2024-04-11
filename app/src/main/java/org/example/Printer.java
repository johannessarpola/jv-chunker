package org.example;

public class Printer {
    public static void println(Object... args) {
        for (Object arg : args) {
            System.out.print(arg);
            System.out.print(" ");
        }
        System.out.println();
    }

    public static void println(String format, Object... args) {
        System.out.printf((format) + "%n", args);
    }
}
