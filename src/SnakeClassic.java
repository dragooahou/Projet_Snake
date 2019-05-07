import java.awt.event.*;
import javax.swing.Timer;

public class SnakeClassic extends ModeDeJeu{

    public SnakeClassic(){
    	
    	// Grille sur laquelle va se déplacer le serpent
	    terrain = new Terrain(0, 0, 40, 30, 20);

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

        ActionListener repaint = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                panel.repaint();
            }
        };

        Timer drawClock = new Timer(20, repaint);
        drawClock.start();
        drawClock.setRepeats(true);

    }
}