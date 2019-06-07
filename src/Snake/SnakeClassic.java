package Snake;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Timer;

public class SnakeClassic extends ModeDeJeu{

    public SnakeClassic(){
        // On a relancé le jeu
        stopped = false;

    	// Grille sur laquelle va se déplacer le serpent
	    terrain = new Terrain(0, 0, 25, 18, 32);
    }

	public void run(){
        while(!stopped){

            if(paused) {
                try{ Thread.sleep(25);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                continue;
            };

            // On met à jour 4 fois l'animation et on bouge le serpent
            for (int i = 0; i < 4; i++) {

                panel.updateAnim();

                try{ Thread.sleep(25);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            // On met à jour le terrain
            terrain.update();
        }
    }

    public void draw(){
        // La si on met pas 2 fois ya pas tout qui est dessiné ! WTF
        panel.dessiner("terrain");
        panel.dessiner("terrain");

        // On lance la génération de fruits
        ActionListener spawnFruit = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(!paused)
                    panel.drawTerrain(terrain.spawnFruit());

                if(stopped){
                    Timer timer = (Timer) e.getSource();
                    timer.stop();
                }
            }
        };

        Timer fruitClock = new Timer(1000, spawnFruit);
        fruitClock.start();
        fruitClock.setRepeats(true);

        ActionListener repaint = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(!paused)
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
                window.pause(1);
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