package Snake;

import java.io.*;
import java.util.Properties;

public class SaveManager {

    private static final String FILE_NAME = "save.props";
    private static Properties p = new Properties();

    public static void init() {

        // Vérifie que le fichier existe et sinon le crée
        File f = new File(FILE_NAME);
        if(!f.exists() || f.isDirectory()){
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File(FILE_NAME)));
                // normalement si le fichier n'existe pas, il est crée à la racine du projet
                writer.close();
            } catch (IOException e) { e.printStackTrace(); }
            setSkin("snake_default");
        }

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
