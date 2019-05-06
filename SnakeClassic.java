import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

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
                panel.repaint();

                try{ Thread.sleep(25);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            // On met à jour le terrain
            terrain.update();
        }
    }

}