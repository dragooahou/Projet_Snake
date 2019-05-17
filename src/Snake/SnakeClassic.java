package Snake;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.awt.event.*;
import javax.swing.Timer;

public class SnakeClassic extends ModeDeJeu{

    public SnakeClassic(){

    	// Grille sur laquelle va se déplacer le serpent
	    terrain = new Terrain(0, 0, 40, 29, 20);

        // On lance la génération de fruits
        ActionListener spawnFruit = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                terrain.spawnFruit();
                panel.dessiner("terrain");
                panel.dessiner("snake");
            }
        };

        Timer fruitClock = new Timer(1000, spawnFruit);
        fruitClock.start();
        fruitClock.setRepeats(true);
    }

	public void run(){
        while(true){

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

        ActionListener repaint = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                panel.dessiner("snake");
            }
        };

        Timer drawClock = new Timer(20, repaint);
        drawClock.start();
        drawClock.setRepeats(true);

    }
}