package Snake;

import java.util.List;

public class Snake {

    // Stocke les positions de toutes les parties du serpent
    private SnakePart[] positions;

    // La position du serpent juste avant le dernier mouvement
    private SnakePart[] prevPositions;

    // Longueur actuelle du serpent
    private int length;

    // La longueur de serpent à laquelle on gagne
    private int maxLength;

    // L'apparence du serpent
    private String skin;

    // Constructeur
    // l = longueur
    // mLenght = maxlongeur
    // sk = skin
    public Snake(int l, int mLength, String sk){
        positions = new SnakePart[mLength];
        prevPositions = new SnakePart[mLength];
        length = l;
        skin = sk;
        maxLength = mLength;

        // On set les positions de départ
        for (int i = 0; i < length; i++) {
            positions[i] = new SnakePart(15, 13+i, 'N');
            prevPositions[i] = new SnakePart(15, 13+i, 'N');
        }

    }

    // Retourne vrai si le serpent a atteint sa taille max
    public boolean isGameWon(){
        if(length == maxLength)
            return true;
        return false;
    }

    // Met à jour le serpent (le fait avancer)
    // tab = le tableau du terrain dans lequel se deplace le serpent
    // Renvoie 0 si tout est ok
    //         1 si le serpent se plante
    // 		   2 si le serpent à atteint sa longueur max
    public int update(Square[][] tab){


        // On met a jour prevPositions
        for (int i = 0; i < length; i++) {
            prevPositions[i].setXPos(positions[i].getXPos());
            prevPositions[i].setYPos(positions[i].getYPos());
            prevPositions[i].setDirection(positions[i].getDirection());
            prevPositions[i].setElbow(positions[i].isElbow());
        }

        // On fait avancer le serpent
        move();

        // On vérifie que sa tête ne soit pas hors du terrain ou qu'il ne se morde pas la queue
        if(positions[0].getXPos() < 0 || positions[0].getXPos() > tab.length || positions[0].getYPos() < 0 || positions[0].getYPos() > tab[0].length ||
                isOnSnake(positions[0].getXPos(), positions[0].getYPos()))
            return 1;

        // On vérifie que on serait pas sur un fruit
        if(ListeFruits.isFruit(tab[positions[0].getXPos()][positions[0].getYPos()].getObject()))
            if(grow())
                return 2;

        return 0;
    }

    // Deplacer le serpent
    private void move(){

        // On enlève le coude de la tête
        positions[0].setElbow(false);

        for (int i = 0; i < length; i++) {

            switch(positions[i].getDirection()){
                case 'N':
                    positions[i].decY();
                    break;
                case 'S':
                    positions[i].incY();
                    break;
                case 'E':
                    positions[i].incX();
                    break;
                case 'W':
                    positions[i].decX();
            }

            if(i-1 >= 0){
                positions[i].setDirection(prevPositions[i-1].getDirection());
                positions[i].setElbow(prevPositions[i-1].isElbow());
            }

        }
    }

    // Retourne vrai si les coordonnées spécifiées sont sur le serpent
    public boolean isOnSnake(int x, int y){
        for (int i = 1; i < length; i++) {
            if(positions[i].getXPos() == x && positions[i].getYPos() == y)
                return true;
        }
        return false;
    }

    // Fait grandir le serpent
    // Retourne vrai si il a atteint sa longueur max
    private boolean grow(){
        // Sécurité
        if(!(length < positions.length))
            return true;

        positions[length] = new SnakePart(prevPositions[length-1].getXPos(), prevPositions[length-1].getYPos(), prevPositions[length-1].getDirection());
        prevPositions[length] = new SnakePart(prevPositions[length-1].getXPos(), prevPositions[length-1].getYPos(), prevPositions[length-1].getDirection());

        length++;

        if(!(length < positions.length))
            return true;

        return false;
    }

    public SnakePart[] getPositions(){
        SnakePart[] tmp = new SnakePart[length];
        for(int i = 0; i < length; i++){
            tmp[i] = positions[i];
        }
        return tmp;
    }

    public String getSkin(){
        return skin;
    }

    public void setHeadDirection(char dir){
        char partDir = positions[0].getDirection();

        // Si on veut aller tout droit ou à l'opposé un ne fait rien
        if(partDir == dir ||
                ( dir == 'N' && partDir == 'S' ) ||
                ( dir == 'S' && partDir == 'N' ) ||
                ( dir == 'E' && partDir == 'W' ) ||
                ( dir == 'W' && partDir == 'E' ))
            return;

        // On set la direction
        positions[0].setDirection(dir);
        // On set le coude
        positions[0].setElbow(true);
    }

}