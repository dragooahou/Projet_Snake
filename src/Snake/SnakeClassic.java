package Snake;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Timer;

public class SnakeClassic extends ModeDeJeu{

    public SnakeClassic(){

        stopped = false;
        paused = false;
        demarrage = true;

    	// Grille sur laquelle va se déplacer le serpent
	    terrain = new Terrain(0, 0, 25, 18, 32);
    }

	public void run(){
        panel.drawTerrain(terrain.spawnFruit());
        while(!stopped){

            if(paused || demarrage) {
                try{ Thread.sleep(25);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                continue;
            }

            // On met à jour 4 fois l'animation et on bouge le serpent
            for (int i = 0; i < 4; i++) {

                panel.updateAnim();

                try{ Thread.sleep(30);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            // On met à jour le terrain
            // Si le serpent est mort
            switch(terrain.update()) {
                case 0:
                    break;
                case 1:
                    window.changerMJ(3);
                    break;
                case 2:
                    miam();
                    panel.drawTerrain(terrain.spawnFruit());
                    for (Point p : terrain.tryAddRock(1, 50)) panel.drawTerrain(p);
                    for (Point p : terrain.tryAddRock(1, 20)) panel.drawTerrain(p);
                    for (Point p : terrain.tryAddRock(1, 12)) panel.drawTerrain(p);
                    break;
            }
        }
    }

    public void draw(){
        panel.dessiner("terrain");

        ActionListener repaint = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(!paused && !demarrage)
                    panel.dessiner("snake");

                if(stopped){
                    Timer timer = (Timer) e.getSource();
                    timer.stop();
                }
            }
        };

        Timer drawClock = new Timer(20, repaint);
        drawClock.start();
        drawClock.setRepeats(true);

        // On lance la génération de fruits
        ActionListener animFruit = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(!paused && !demarrage) {
                    terrain.updateAnimFruit();
                    panel.dessiner("fruits");
                }

                if(stopped){
                    Timer timer = (Timer) e.getSource();
                    timer.stop();
                }
            }
        };

        Timer animeFruitClock = new Timer(500, animFruit);
        animeFruitClock.start();
        animeFruitClock.setRepeats(true);

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // Va stocker la dernière touche pressé
    private String lastKey = "";

    public void keyPressed(KeyEvent e) {
        String key = KeyEvent.getKeyText(e.getKeyCode());

        if(demarrage)
            demarrage = false;

        //System.out.println("keyPressed="+key);

        // Si la touche est déjà pressé on ne fait rien
        if(key.equals(lastKey))
            return;

        switch(key){
            case "Haut":
                terrain.setSnakeDirection('N');
                break;
            case "Bas":
                terrain.setSnakeDirection('S');
                break;
            case "Droite":
                terrain.setSnakeDirection('E');
                break;
            case "Gauche":
                terrain.setSnakeDirection('W');
                break;
            case "Echap":
                window.pause();
                break;
            default:
                break;
        }

        lastKey = key;
    }

    public void keyReleased(KeyEvent e) {
        //System.out.println("keyReleased="+KeyEvent.getKeyText(e.getKeyCode()));

        // Si on relache la touche on réinitialise lastKey
        lastKey = "";
    }
    public void miam(){
        SoundManager.stop("crocpomme");
        SoundManager.create("crocpomme", "crocpomme",false);
        SoundManager.play("crocpomme");
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
}