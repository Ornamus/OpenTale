package ryan.shavell.main.stuff;

public class Log {

    public static void i(String message) {
        tagLog("INFO", message);
    }

    public static void w(String message) {
        tagLog("WARNING", message);
    }

    public static void d(String message) {
        tagLog("DEBUG", message);
    }

    public static void e(String message) {
        tagLog("ERROR", message);
    }

    private static void tagLog(String tag, String message) {
        System.out.println("[" + tag + "] " + message);
    }
}
