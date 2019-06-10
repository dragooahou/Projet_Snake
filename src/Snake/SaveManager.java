package Snake;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

public class SaveManager {

    private static final String FILE_NAME = "save.props";
    private static Properties p = new Properties();

    public static void init() {
        try{
            p.load(new FileInputStream(FILE_NAME));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getSkin() {
        try{
            p.load(new FileInputStream(FILE_NAME));
            return p.getProperty("skin");
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Snake_default";
        }
    }

    public static void setSkin(String s) {
        try (OutputStream output = new FileOutputStream(FILE_NAME)) {
            p.setProperty("skin", s);
            p.store(output, null);
        }catch (Exception e){ e.printStackTrace(); }
    }
}
