package Snake;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

abstract class ModeDeJeu implements MouseListener, MouseMotionListener, KeyListener {

	protected Panel panel;
	protected Window window;
	protected Terrain terrain;
	protected Interface hud;

	protected boolean stopped;
	protected boolean paused;
	protected boolean demarrage;
	protected int pausedMJ;

	protected String name;

	public void run(){ System.out.println("Ce mode de jeu n'a rien à executer."); }

	public void draw(){
		System.out.println("Ce mode de jeu n'a rien à afficher.");
	}

	public void setWindow(Window w){
        window = w;
    }

    public void setPanel(Panel p){
        panel = p;
    }

    public void arreter(){ stopped = true; }

    public void pause(){
		paused = true;
	}

	public void reprendre(){
		paused = false;
	}

	public int getPausedMJ() {
		return pausedMJ;
	}

	public void setPausedMJ(int pausedMJ) {
		this.pausedMJ = pausedMJ;
	}

	public Terrain getTerrain(){
		return terrain;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setDemarrage(boolean demarrage) {
		this.demarrage = demarrage;
	}
}