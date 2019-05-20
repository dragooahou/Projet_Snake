package Snake;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Menu extends ModeDeJeu implements MouseListener, MouseMotionListener {

	// boutons presents dans le menu
	private Bouton[] tabBouton;

	// image de l'arriere plan
	private BufferedImage backgroundImage;

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
		g2d.drawImage(backgroundImage, 0, 0, panel.getWidth(), panel.getHeight(), panel);

		// on redessine le bouton a chaque fois qu'il y a un MouseEvent
		while (!stopped) {

			for (Bouton bou : tabBouton) {
				bou.draw();
			}

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

	////// LES INTERFACES /////////////////////////////////////////////////:

	@Override
	public void mouseClicked(MouseEvent e) {
		for (Bouton bou : tabBouton) {
			if(bou.isIn(e.getX(), e.getY()))
				bou.getActionAFaire().execute();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (Bouton bou : tabBouton) {
			if(bou.isIn(e.getX(), e.getY()))
				bou.setValeurImage(2);
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
		for (Bouton bou : tabBouton) {
			if(bou.isIn(e.getX(), e.getY()))
				bou.setValeurImage(1);
			else
				bou.setValeurImage(0);
		}
	}
}