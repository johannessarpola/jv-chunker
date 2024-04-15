package fi.johannes;
import java.util.HashMap;
public class CharUtils {
    private static final HashMap<String, String> escapeMap = new HashMap<>();

    static {
        escapeMap.put("\\n", "\n");
        escapeMap.put("\\t", "\t");
        escapeMap.put("\\r", "\r");
        escapeMap.put("\\v", "\u000b");
        escapeMap.put("\\f", "\f");
        escapeMap.put("\\b", "\b");
        escapeMap.put("\\0", "\0");
    }
    public static String decodeSeparator(String separatorString) {
        return escapeMap.getOrDefault(separatorString, separatorString);
    }
}
