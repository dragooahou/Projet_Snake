package Snake;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SoundManager {
    private final static File jarFile = new File(SoundManager.class.getProtectionDomain().getCodeSource().getLocation().getPath());

    private static HashMap<String, SimpleAudioPlayer> players = new HashMap<>();
    static File folder = new File("resources/small_sound");
    static File[] listOfFiles = folder.listFiles();
    public static void create(String name, String clipName, boolean bool){
        if(SaveManager.isMuted().equals("on")) {
            try {
                if (players.get(name) == null)
                    players.put(name, new SimpleAudioPlayer(clipName, bool));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void createSmall(String name, String clipName){
            try {
                if (players.get(name) == null)
                    players.put(name, new SimpleAudioPlayer(clipName));
            } catch (Exception e) {
                e.printStackTrace();
            }

    }
    public static <Clip> void initPetitsSons(){
        if(jarFile.isFile()) {  // Run with JAR file
            try {
                final JarFile jar = new JarFile(jarFile);
                final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while (entries.hasMoreElements()) {
                    JarEntry elem = entries.nextElement();

                    String name = elem.getName();
                    if (name.startsWith("small_sound") && name.endsWith(".wav")) { //filter according to the path
                        SoundManager.createSmall(name.substring(name.indexOf('/')+1), name);
                    }
                }

                jar.close();
            }catch(Exception e){ e.printStackTrace(); }
        }
        else { // Run in IDE
            for (File listOfFile : listOfFiles) {
                SoundManager.createSmall(listOfFile.getName(), listOfFile.getName());
            }
        }

    }

    public static void play(String name){
        if(SaveManager.isMuted().equals("on")) {
            players.get(name).play();
        }
    }
    public static void playSmall(String name){
        if(SaveManager.isMuted().equals("on")) {
            players.get(name).playSmall();
        }
    }
    public static void stop(String name){
        try{
            players.get(name).stop();
            players.remove(name);
        }catch (Exception e){
           // e.printStackTrace();
        }
    }

    public static void pause(String name){
        players.get(name).pause();
    }

    public static void resume(String name){
        try{
            players.get(name).resumeAudio();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void restart(String name){
        try {
            players.get(name).restart();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void mute(){
    	for (Map.Entry<String, SimpleAudioPlayer> sound : players.entrySet()) {
          stop(sound.getKey());
        }

    }

}
