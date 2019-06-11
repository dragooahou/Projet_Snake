package Snake;// Java program to play an Audio

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SimpleAudioPlayer
{


    private HashMap<String, Clip> sons = new HashMap<String, Clip>();
    // to store current position
    Long currentFrame;
    Clip clip;
    // current status of clip
    String status;

    AudioInputStream audioInputStream;
    static String filePath;

    // constructor to initialize streams and clip
    public SimpleAudioPlayer(String filePath, boolean boolier)
            throws UnsupportedAudioFileException,
            IOException, LineUnavailableException
    {
        /*sons.put("Manger",loadClip( ""+System.class.getResource("/sound/crocpomme.wav").getPath() ,false));
        sons.put("musicAmbiance",loadClip(""+ System.class.getResource("/sound/ambiance.wav").getPath(),true ));
        sons.put("Musique2",loadClip(""+ System.class.getResource("/sound/wii.wav").getPath(),true ));
        sons.put("Button",loadClip(""+ System.class.getResource("/sound/buttoninstant.wav").getPath(),false ));*/
        sons.put("Son",loadClip( ""+System.class.getResource("/sound/"+filePath+".wav").getPath() ,boolier));


    }

    // Method to play the audio 
    public void play()
    {
        //start the clip 
        clip.start();

        status = "play";
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
    public void stop() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException
    {
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
        try
        {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filename).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if (continuous){clip.loop(Clip.LOOP_CONTINUOUSLY);}
        return clip;
    }

} 