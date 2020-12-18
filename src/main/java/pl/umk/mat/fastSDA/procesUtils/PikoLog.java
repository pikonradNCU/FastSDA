package pl.umk.mat.fastSDA.procesUtils;

import lombok.Getter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public class PikoLog {
    @Getter private static final boolean SDA_TEST_MODE = checkSdaModeFlag();
    PrintWriter pw;
    private static volatile PikoLog instance = null;
    private static String log="Piko LOG info inited "+ LocalDate.now();
    private static String FILE_NAME = "pikoLog.txt";
    public static PikoLog getInstance(){
        if (instance==null)
        synchronized (PikoLog.class){
            if (instance == null)
                instance = new PikoLog();
        }
        return instance;
    }

    private PikoLog() {
        flushLog();
    }
    private void flushLog(){
        if (SDA_TEST_MODE) {
            try{
                pw =new PrintWriter(new FileWriter(FILE_NAME));
                pw.println(log);
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void info (String s){
        log+="\n"+s;
        flushLog();
    }

    private static boolean checkSdaModeFlag() {
        try{
            String sdaTestMOdeSysVariable = System.getenv("SDATESTMODE");
            return (sdaTestMOdeSysVariable.equalsIgnoreCase("True")
                    || sdaTestMOdeSysVariable.equalsIgnoreCase("Yes"));
        } catch (Exception e) {return false;}
    }
}
