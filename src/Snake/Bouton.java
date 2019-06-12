package Snake;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Bouton {

    // position et taille du bouton
    private int posX;
    private int posY;
    private int height;
    private int width;

    // 3 etats d'une image (au repos, quand le curseur le survole,
    // quand le curseur clique dessus) stockes dans un tableau
    private BufferedImage[] tabImages;

    // valeur de l'etat : 0, 1 ou 2
    private int valeurImage = 0;

    // reference du mode de jeu
    private ModeDeJeu modeJeu;

    // action a realiser
    private ActionBouton actionAFaire;

    // initialise un bouton avec parametres et initialise ses coordonnees
    //  a 0 et sa hauteur à 300x60
    public Bouton(BufferedImage[] images, ModeDeJeu mode) {
        tabImages = images;
        modeJeu = mode;
        posX = 0;
        posY = 0;
        height = 60;
        width = 300;
    }

    // retourne true si les coordonees donnees en parametres
    // sont dans le bouton (toute la zone je suppose ??)
    public boolean isIn(int x, int y) {
        return (x <= posX + width) && (x >= posX) && (y <= posY + height) && (y >= posY);
    }

    // dessine un bouton
    public void draw() {
        Graphics2D g2d = modeJeu.panel.getGraph();

        g2d.drawImage(tabImages[valeurImage], posX, posY, width, height, modeJeu.panel);
    }

    // setters
    public void setPosXY(int x, int y) {
        posX = x;
        posY = y;
    }

    public void setTaille(int hauteur, int largeur) {
        height = hauteur;
        width = largeur;
    }

    public void setValeurImage(int val) {
        valeurImage = val;
    }

    public void setModeDeJeu(ModeDeJeu mode) {
        modeJeu = mode;
    }

    public void setActionListener(ActionBouton a) {
        actionAFaire = a;
    }

    // getters
    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getHauteur() {
        return height;
    }

    public int getLargeur() {
        return width;
    }

    public BufferedImage[] getTabImage() {
        return tabImages;
    }

    public void setTabImages(BufferedImage[] tabImages) {
        this.tabImages = tabImages;
    }

    public int getValeurImage() {
        return valeurImage;
    }

    public ModeDeJeu getModeDeJeu() {
        return modeJeu;
    }

    public ActionBouton getActionAFaire() {
        return actionAFaire;
    }

    @Override
    public String toString() {
        return "Bouton{" +
                "posX=" + posX +
                ", posY=" + posY +
                ", height=" + height +
                ", width=" + width +
                ", tabImages=" + Arrays.toString(tabImages) +
                ", valeurImage=" + valeurImage +
                ", modeJeu=" + modeJeu +
                ", actionAFaire=" + actionAFaire +
                '}';
    }
}