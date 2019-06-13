package Snake;// Java program to play an Audio

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.JarFile;

public class SimpleAudioPlayer
{


    private HashMap<String, Clip> sons = new HashMap<>();
    // to store current position
    private Long currentFrame;
    private Clip clip;
    // current status of clip
    private String status;
    public static ArrayList <String> deathSounds = new ArrayList<>();
    private AudioInputStream audioInputStream;
    private static String filePath;

    // constructor to initialize streams and clip
    public SimpleAudioPlayer(String filePath, boolean boolier) {

            sons.put("Son", loadClip("sound/" + filePath + ".wav", boolier));
        SimpleAudioPlayer.filePath = filePath;

    }
    public SimpleAudioPlayer(String filePath) {

        sons.put("Son", loadClipSmall("/" + filePath));
        SimpleAudioPlayer.filePath = filePath;

    }

    // Method to play the audio
    public void play()
    {
        //start the clip
        clip.start();

        status = "play";
    }
    public void playSmall(){
        clip.start();
        clip.setMicrosecondPosition(0);
    }

    // Method to pause the audio
    public void pause()
    {
        if (status.equals("paused"))
        {
            System.out.println("audio is already paused");
            return;
        }
        this.currentFrame =
                this.clip.getMicrosecondPosition();
        clip.stop();
        status = "paused";
    }

    // Method to resume the audio
    public void resumeAudio() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException
    {
        if (status.equals("play"))
        {
            return;
        }
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(currentFrame);
        this.play();
    }

    // Method to restart the audio
    public void restart() throws IOException, LineUnavailableException,
            UnsupportedAudioFileException
    {
        clip.stop();
        clip.close();
        resetAudioStream();
        currentFrame = 0L;
        clip.setMicrosecondPosition(0);
        this.play();
    }

    // Method to stop the audio
    public void stop() {
        currentFrame = 0L;
        clip.stop();
        clip.close();
    }

    // Method to jump over a specific part
    public void jump(long c) throws UnsupportedAudioFileException, IOException,
            LineUnavailableException
    {
        if (c > 0 && c < clip.getMicrosecondLength())
        {
            clip.stop();
            clip.close();
            resetAudioStream();
            currentFrame = c;
            clip.setMicrosecondPosition(c);
            this.play();
        }
    }

    // Method to reset audio stream
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException,
            LineUnavailableException
    {
        audioInputStream = AudioSystem.getAudioInputStream(
                new File(filePath).getAbsoluteFile());
        clip.open(audioInputStream);
    }
    public HashMap<String, Clip> getSons() {
        return sons;
    }


    public Clip loadClip(String filename,boolean continuous)
    {

        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        if(jarFile.isFile()) {  // Run with JAR file
            JarFile jar = null;
            try {
                jar = new JarFile(jarFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert jar != null;
            try (InputStream in = jar.getInputStream(jar.getEntry(filename))) {
                InputStream bufferedIn = new BufferedInputStream(in);
                try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn)) {
                    clip = AudioSystem.getClip();
                    clip.open(audioIn);
                }
                jar.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("resources/" + filename));
                clip = AudioSystem.getClip();
                clip.open(audioIn);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        if (continuous){clip.loop(Clip.LOOP_CONTINUOUSLY);}
        return clip;
    }

    public Clip loadClipSmall(String filename)
    {
        final File jarFile = new File(SoundManager.class.getProtectionDomain().getCodeSource().getLocation().getPath());

        if(jarFile.isFile()) {  // Run with JAR file
            JarFile jar = null;
            try {
                jar = new JarFile(jarFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            filename = filename.substring(1);
            try (InputStream in = jar.getInputStream(jar.getEntry(filename))) {
                InputStream bufferedIn = new BufferedInputStream(in);
                try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn)) {
                    clip = AudioSystem.getClip();
                    clip.open(audioIn);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("resources/small_sound" + filename));
                clip = AudioSystem.getClip();
                clip.open(audioIn);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return clip;
    }

    public static void addDeathSounds(){
            deathSounds.add("oof.wav");
            deathSounds.add("nope.wav");
            deathSounds.add("poinn.wav");
            deathSounds.add("erro.wav");
    }

    public static ArrayList<String> getDeathSounds(){
        if (deathSounds.size()==0)addDeathSounds();
        return deathSounds;
    }
    @Override
    public String toString() {
        return filePath;
    }
} 