package mc.enderbro3d.goldixapi.utils.world;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class StacktraceUtil {
    public static void writeToLog(String s, Throwable t) {
        try {
            File f = new File("err_logs", s + ".log");
            File parent = f.getParentFile();
            if (parent.isFile()) parent.delete();
            if (!parent.exists()) parent.mkdirs();
            if (f.isDirectory()) f.delete();
            if (!f.exists()) f.createNewFile();

            Files.write(f.toPath(), (t.toString() + ": " + t.getMessage() + "\n").getBytes(), StandardOpenOption.APPEND);
            for(StackTraceElement element:t.getStackTrace()) {
                if(element == null || element.getClassName() == null|| element.getLineNumber() == -1) continue;
                Files.write(f.toPath(), (element.getClassName() + " > Method: " + element.getMethodName() + "() at line " + element.getLineNumber() + "\n").getBytes(), StandardOpenOption.APPEND);
            }
        } catch(Throwable t1) {
            t1.printStackTrace();
        }
    }
}
