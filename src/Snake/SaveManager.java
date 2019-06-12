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
            unmute();
            setHiscore(0);
            setNbgames(0);
            setNbFruits(0);
        }

        try{
            p.load(new FileInputStream(FILE_NAME));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //////// SKIN ////////////////////////////////////
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

    ///////// SONS ////////////////////////////////////
    public static String isMuted() {
        try{
            p.load(new FileInputStream(FILE_NAME));
            return p.getProperty("sound");
        }
        catch (Exception e) {
            e.printStackTrace();
            return "on";
        }
    }

    public static void unmute() {
        try (OutputStream output = new FileOutputStream(FILE_NAME)) {
            p.setProperty("sound", "on");
            p.store(output, null);
        }catch (Exception e){ e.printStackTrace(); }
    }

    public static void mute() {
        try (OutputStream output = new FileOutputStream(FILE_NAME)) {
            p.setProperty("sound", "off");
            p.store(output, null);
        }catch (Exception e){ e.printStackTrace(); }
    }

    ///////// HISCORE ////////////////////////////////////
    public static int getHiscore() {
        try{
            p.load(new FileInputStream(FILE_NAME));
            return Integer.parseInt(p.getProperty("hiscore"));
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void setHiscore(int i) {
        String s = i + "";
        try (OutputStream output = new FileOutputStream(FILE_NAME)) {
            p.setProperty("hiscore", s);
            p.store(output, null);
        }catch (Exception e){ e.printStackTrace(); }
    }

    ///////// SUCCESS /////////////////////////////////////
    private static String getSuccesList() {
        String s;
        try{
            p.load(new FileInputStream(FILE_NAME));
            return p.getProperty("succes");
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean getSucces(String succes) {
        String s;
        try{
            p.load(new FileInputStream(FILE_NAME));
            s = p.getProperty("succes");
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        for (String str : s.split(" ")) if(succes.equals(str)) return true;

        return false;
    }

    public static void setSucces(String str){
        if(getSucces(str))
            return;

        try (OutputStream output = new FileOutputStream(FILE_NAME)) {
            p.setProperty("succes", getSuccesList() + " " + str);
            p.store(output, null);
        }catch (Exception e){ e.printStackTrace(); }
    }


    ///////// NbGames ////////////////////////////////////
    public static int getNbGames() {
        try{
            p.load(new FileInputStream(FILE_NAME));
            return Integer.parseInt(p.getProperty("nbgames"));
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void setNbgames(int i) {
        String s = i + "";
        try (OutputStream output = new FileOutputStream(FILE_NAME)) {
            p.setProperty("nbgames", s);
            p.store(output, null);
        }catch (Exception e){ e.printStackTrace(); }
    }

    public static void upNbGames(){
        String s = (getNbGames()+1)+"";
        try (OutputStream output = new FileOutputStream(FILE_NAME)) {
            p.setProperty("nbgames", s);
            p.store(output, null);
        }catch (Exception e){ e.printStackTrace(); }
    }


    ///////// NbFruits ////////////////////////////////////
    public static int getNbFruits() {
        try{
            p.load(new FileInputStream(FILE_NAME));
            return Integer.parseInt(p.getProperty("nbfruits"));
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void setNbFruits(int i) {
        String s = i + "";
        try (OutputStream output = new FileOutputStream(FILE_NAME)) {
            p.setProperty("nbfruits", s);
            p.store(output, null);
        }catch (Exception e){ e.printStackTrace(); }
    }

    public static void upNbFruits(){
        String s = (getNbFruits()+1)+"";
        try (OutputStream output = new FileOutputStream(FILE_NAME)) {
            p.setProperty("nbfruits", s);
            p.store(output, null);
        }catch (Exception e){ e.printStackTrace(); }
    }


}
