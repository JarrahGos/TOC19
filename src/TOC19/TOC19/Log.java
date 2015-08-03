package TOC19;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Jarrah Gosbell
 */

public class Log {
	/** The settings object which will give the Log Location */
    private static Settings settings = new Settings();
	/** The location to store the log in */
    private static String logLocation = "./";
	/** The file writer which will append to the log */
    private static FileWriter fwriter;
	/** The buffered writer which will write to the log */
    private static BufferedWriter bwriter;
	/** whether an exception occured the first time. Stops recursive loops */
    private static boolean first = true;
	/** Whether to print to the database or to the console. True for console */
    private static boolean debug = false; // print to the console.
	/** 
	 * Set up the logLocation and writers ready to write the log
	 */
    private Log() {
        try {
            logLocation = settings.logSettings();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fwriter = new FileWriter(logLocation + "LOG", true);
            bwriter = new BufferedWriter(fwriter);
        }
        catch (IOException e) {
            print(e);
        }
    }
	/**
	 * Turn a throwable into a string stack trace
	 * @param e The throwable to be traced and turned into a string
	 * @return a string containing the stack trace of e
	 */
    private static String stackTraceToString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
	/**
	 * Print the stacktrace of a throwable to the log
	 * @param e The throwable which will be traced and printed to the log
	 */
    public static void print(Throwable e) {
        ExceptionManager.getInstance().showErrorDialog(e);
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
        else e.printStackTrace();
    }
	/**
	 * Print a predefined string to the Log
	 * @param s The string to print to the log. 
	 */
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
        else System.out.println(s);
    }
}
