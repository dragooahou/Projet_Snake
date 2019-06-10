package Snake;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class Menu extends ModeDeJeu implements MouseListener, MouseMotionListener, KeyListener {

	// boutons presents dans le menu
	private Bouton[] tabBouton;

	// image de l'arriere plan
	private BufferedImage backgroundImage;

	private Point bgCoords = new Point(0,0);
	private Point bgTaille = new Point(800, 600);

	// initialise les boutons, l'image de fond et le listener
	// je sais pas comment les initialiser :(
	public Menu(int nbBouton) {
		tabBouton = new Bouton[nbBouton];
	}

	// Boucle principale d'execution
	public void run() {
		while (!stopped) {




			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// Boucle principale de dessin
	public void draw() {

		// on dessine 1 fois l'arriere plan
		Graphics g2d = panel.getGraph();
		g2d.drawImage(backgroundImage, bgCoords.x, bgCoords.y, bgTaille.x, bgTaille.y, panel);

		for (Bouton bou : tabBouton)
			bou.draw();

			// on redessine le bouton a chaque fois qu'il y a un MouseEvent
		while (!stopped) {



			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setBouton(int no, Bouton bou) {
		this.tabBouton[no] = bou;
	}

	public Bouton getBouton(int i) {
		return tabBouton[i];
	}

	public void setBackgroundImage(BufferedImage backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public void setBgCoords(Point bgCoords) {
		this.bgCoords = bgCoords;
	}

	public void setBgTaille(Point bgTaille) {
		this.bgTaille = bgTaille;
	}

	////// LES INTERFACES /////////////////////////////////////////////////:

	@Override
	public void mouseClicked(MouseEvent e) {
		for (Bouton bou : tabBouton) {
			if(bou.isIn(e.getX(), e.getY() - window.getInsets().top)) {
			    bou.getActionAFaire().execute();
            }
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (Bouton bou : tabBouton) {
			if(bou.isIn(e.getX(), e.getY() - window.getInsets().top)) {
				bou.setValeurImage(2);
				bou.draw();
			}

		}
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
		Graphics g2d = panel.getGraph();
		g2d.drawImage(backgroundImage, bgCoords.x, bgCoords.y, bgTaille.x, bgTaille.y, panel);

		for (Bouton bou : tabBouton) {
			bou.draw();
			if(bou.isIn(e.getX(), e.getY() - window.getInsets().top)) {
				bou.setValeurImage(1);
			}
			else {
				bou.setValeurImage(0);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}