public class Logger {
    public static void fatal(String message) {
        Logger.log(message, Constants.FATAL);
    }

    public static void error(String message) {
        Logger.log(message, Constants.ERROR);
    }

    public static void warn(String message) {
        Logger.log(message, Constants.WARN);
    }

    public static void info(String message) {
        Logger.log(message, Constants.INFO);
    }

    public static void debug(String message) {
        Logger.log(message, Constants.DEBUG);
    }

    public static void trace(String message) {
        Logger.log(message, Constants.TRACE);
    }

    private static void log(String message, int level ) {
        if( Constants.LOG_LEVEL < level ) {
            return;
        }
        System.out.println(message + "\n");
    }
}
