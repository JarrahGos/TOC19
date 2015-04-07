package TOC19;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by jarrah on 7/04/15.
 */
public class Log {
    private static Settings settings = new Settings();
    private static String logLocation = "./";
    private static FileWriter fwriter;
    private static BufferedWriter bwriter;
    private static boolean first = true;
    private static boolean debug = true; // print to the console.
    private Log() {
        try {
            logLocation = settings.logSettings();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fwriter = new FileWriter(logLocation + "LOG");
            bwriter = new BufferedWriter(fwriter);
        }
        catch (IOException e) {
            print(e);
        }
    }
    private static String stackTraceToString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
    public static void print(Throwable e) {
        if(!debug) {
            if (bwriter == null) new Log();
            String stackTrace = stackTraceToString(e);
            try {
                bwriter.write(stackTrace, 0, stackTrace.length());
                bwriter.newLine();
                bwriter.flush();
            } catch (IOException e1) {
                if (first) {
                    first = false;
                    print(e1);
                } else {
                    first = true;
                    e1.printStackTrace();
                    return;
                }
            }
        }
    }
    public static void print(String s) {
        if(!debug) {
            if (bwriter == null) new Log();
            try {
                bwriter.write(s, 0, s.length());
                bwriter.newLine();
                bwriter.flush();
            } catch (IOException e1) {
                if (first) {
                    first = false;
                    print(e1);
                } else {
                    first = true;
                    e1.printStackTrace();
                    return;
                }
            }
        }
    }
}
