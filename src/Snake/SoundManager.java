package Snake;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private static HashMap<String, SimpleAudioPlayer> players = new HashMap<>();

    public static void create(String name, String clipName, boolean bool){
        try{
            if(players.get(name) == null)
                players.put(name, new SimpleAudioPlayer(clipName,bool));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void play(String name){
        players.get(name).play();
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
          pause(sound.getKey());
        }

    }

}
