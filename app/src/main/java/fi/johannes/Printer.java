package fi.johannes;

public class Printer {
    public static void println(String format, Object... args) {
        System.out.println(String.format(format, args));
    }
}
