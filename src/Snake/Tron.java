package Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Tron extends ModeDeJeu {

    protected String msgFin = null;

    public Tron() {

        stopped = false;
        paused = false;
        demarrage = true;

        // Grille sur laquelle va se déplacer le serpent
        terrain = new Terrain(0, 0, 50, 36, 16);
        terrain.generateBackground(500);
        terrain.setSnake(0,47,25);
        terrain.setSnake(1,3,25);
        terrain.getSnake(0).setSkin("snake_blue");
        terrain.getSnake(1).setSkin("snake_red");

    }

    public void run() {
        SaveManager.upNbGames();


        while (!stopped) {

            if (paused || demarrage) {
                if (demarrage) {
                    Graphics2D g2d = panel.getGraph();
                    g2d.setColor(Color.white);
                    g2d.setFont(new Font("Arial", Font.PLAIN, 24));
                    g2d.drawString("Coupez la route à votre adversaire !", 180, 200);

                    g2d.drawImage(panel.getSprite("fleches"), 600, 420, 140, 92, panel);
                    g2d.drawImage(panel.getSprite("zqsd"), 70, 420, 140, 92, panel);
                }

                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;

            }

            // On met à jour 4 fois l'animation et on bouge le serpent
            for (int i = 0; i < 4; i++) {

                panel.updateAnim();

                try {
                    Thread.sleep(18);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // On met à jour le terrain
            // Si le serpent est mort
            for (int i = 0; i < 2; i++)
                switch (terrain.update(i)) {
                    case 0:
                        break;
                    case 1:
                        SoundManager.stop("musicAmbiance");
                        die();

                        msgFin = "Le joueur ";
                        if(i == 0) msgFin += "rouge a gagné";
                        else msgFin += "bleu a gagné";

                        window.changerMJ(3);
                        window.getModeDeJeuCourant().setPausedMJ(9);
                        break;
                    case 2:
                        break;
                }
        }
    }

    public void draw() {
        for(int i = 0; i < 10000; i++)
            terrain.spawnFruitTron();
        panel.dessiner("terrain");

        ActionListener repaint = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!paused && !demarrage)
                    panel.dessiner("snake");

                if (stopped) {
                    Timer timer = (Timer) e.getSource();
                    timer.stop();
                }
            }
        };

        Timer drawClock = new Timer(20, repaint);
        drawClock.start();
        drawClock.setRepeats(true);

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // Va stocker la dernière touche pressé
    private String lastKey = "";

    public void keyPressed(KeyEvent e) {
        String key = KeyEvent.getKeyText(e.getKeyCode());

        //System.out.println("keyPressed="+key);

        // Si la touche est déjà pressé on ne fait rien
        if (key.equals(lastKey))
            return;

        switch (key) {
            case "Haut":
                terrain.setSnakeDirection(0,'N');
                break;
            case "Bas":
                terrain.setSnakeDirection(0,'S');
                break;
            case "Droite":
                terrain.setSnakeDirection(0,'E');
                break;
            case "Gauche":
                terrain.setSnakeDirection(0,'W');
                break;

            case "Z":
            case "W":
                terrain.setSnakeDirection(1,'N');
                break;
            case "S":
                terrain.setSnakeDirection(1,'S');
                break;
            case "D":
                terrain.setSnakeDirection(1,'E');
                break;
            case "Q":
            case "A":
                terrain.setSnakeDirection(1,'W');
                break;

            case "Echap":
                if(demarrage) break;
                window.pause();
                break;
            default:
                break;
        }

        lastKey = key;

        if (demarrage) {
            demarrage = false;
            panel.dessiner("terrain");
        }
    }

    public void keyReleased(KeyEvent e) {
        //System.out.println("keyReleased="+KeyEvent.getKeyText(e.getKeyCode()));

        // Si on relache la touche on réinitialise lastKey
        lastKey = "";
    }



    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    boolean f = false;
    public void die() {



        int i = (int)(Math.random() * SimpleAudioPlayer.getDeathSounds().size());
        if (!f){
            for (int u =0;u<SimpleAudioPlayer.getDeathSounds().size();u++){
                SoundManager.createSmall(SimpleAudioPlayer.getDeathSounds().get(u), SimpleAudioPlayer.getDeathSounds().get(u));
            }
            f = true;
        }
        SoundManager.playSmall(SimpleAudioPlayer.getDeathSounds().get(i));



    }

}