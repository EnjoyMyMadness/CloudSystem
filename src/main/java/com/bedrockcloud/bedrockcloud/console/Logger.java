package com.bedrockcloud.bedrockcloud.console;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.FileWriter;
import java.io.File;

public class Logger
{
    private final File cloudLog;

    public Logger() {
        this.cloudLog = new File("./local/cloud.log");
    }
    
    public void info(final String message) {
        this.log("§aINFO", message);
    }
    
    public void error(final String message) {
        this.log("§cERROR", message);
    }
    
    public void debug(final String message) {
        this.log("§eDEBUG", message);
    }
    
    public void warning(final String message) {
        this.log("§6WARNING", message);
    }

    public void command(final String message) {
        this.log("§bCOMMAND", message);
    }
    
    public void exception(final Exception e) {
        this.log("§cEXCEPTION", getStackTrace(e.getCause()));
    }
    
    public static String getStackTrace(final Throwable t) {

        if (t == null) return "§cCan't get stacktrace.";

        try {
            final StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        } catch (NullPointerException ignored){
            return "§cCan't get stacktrace.";
        }
    }
    
    public void log(final String prefix, final String message) {
        final LocalDateTime dateTime = LocalDateTime.now();
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println(Colors.toColor("§7[§b" + dateTime.format(dateTimeFormatter) + "§7] " + "§7[§r" + prefix + "§7]§r §8» §r" + message + "§r"));
        try {
            FileWriter cloudLogWriter;
            (cloudLogWriter = new FileWriter(this.cloudLog, true)).append(Colors.removeColor("§7[§b" + dateTime.format(dateTimeFormatter) + "§7] " + "§7[§r" + prefix + "§7]§r §8» §r" + message + "§r")).append("\n");
            cloudLogWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
