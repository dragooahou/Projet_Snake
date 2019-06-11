package Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Terrain {

	// Tableau qui va contenir toutes les cases du terrain 
	private Square[][] squareTab;
	private int width;
	private int height;

	// Le serpent qui va se depacer sur le terrain
	private Snake snake;

	// Taille des cases en pixels
	private int squareSize;

	// Positions du terrain dans me fenêtre
	private int posX;
	private int posY;

	// Conteur pour ajouter des rochers
	private int rockCounter = 0;

	// Constructeur
	// x et y : position du terrain dans la fenetre
	// width et height : taille du terrain et tuiles
	// sqSize : taille d'une tuile en pixels
	Terrain (int x, int y, int width, int height, int sqSize) {
		this.width = width;
		this.height = height;

		squareTab = new Square[width][height];

		// Instantie les cases dans le tableau
		for (int i = 0; i < squareTab.length; i++) {
			for (int j = 0; j < squareTab[i].length; j++) {
				squareTab[i][j] = new Square();	
			}	
		}

		squareSize = sqSize;
		posX = x;
		posY = y;

		generateBackground();

		setSnake();

	}

	// Retourne l'objet dans la case aux coordonnées spécifiées
	public String objectOnCase (int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height)
			return "";
		return squareTab[x][y].getObject();
	}

	public String backgroundOnCase(int x, int y){
		if(x < 0 || y < 0 || x >= width || y >= height)
			return "";
		return squareTab[x][y].getBackground();
	}

	// Met un objet dans la case aux coordonnées spécifiées
	public void setCaseObject (String object, int x, int y) {
		squareTab[x][y].setObject(object);
	}

	// Génère un background aléatoire
	private void generateBackground(){
		Random r = new Random(System.nanoTime());
		for (int i = 0; i < squareTab.length; i++) {
			for (int j = 0; j < squareTab[i].length; j++) {

				if(squareTab[i][j].getBackground() == ""){
					int rand = r.nextInt(100);
					switch(rand){
						case 0:
							squareTab[i][j].setBackground("herbe_point");
							break;

						case 1:
							squareTab[i][j].setBackground("herbe_touffe_gauche");
							if(i+1 < squareTab.length)
								squareTab[i+1][j].setBackground("herbe_touffe_droite");
							break;

						case 2:
							squareTab[i][j].setBackground("herbe_touffe_droite");
							if(i-1 >= 0)
								squareTab[i-1][j].setBackground("herbe_touffe_gauche");
							break;

						default:
							squareTab[i][j].setBackground("herbe");
					}
				}
			}
		}
    }

    // Instantie un serpent et le rend visible
    private void setSnake(){
    	snake = new Snake(5, 1000, SaveManager.getSkin());

    	SnakePart[] p = snake.getPositions();
    	String skin = snake.getSkin();

    	squareTab[p[0].getXPos()][p[0].getYPos()].setObject(skin + "_tete" + p[0].getDirection()  + "4");
    	squareTab[p[1].getXPos()][p[1].getYPos()].setObject(skin + "_cou"  + p[0].getDirection() + "4");

    	for(int i = 2; i < p.length-1; i++)
    		squareTab[p[i].getXPos()][p[i].getYPos()].setObject(skin + "_corp" + p[0].getDirection() + "4");

    	squareTab[p[p.length-1].getXPos()][p[p.length-1].getYPos()].setObject(skin + "_queue" + p[0].getDirection()+ "4");
    }

    // Met à jour le terrain
    public int update(){
    	// Sécurité
    	if(snake == null){
    		System.out.println("pas de serpent");
    		return 1;
    	}

		//Sécurité
		SnakePart[] p = snake.getPositions();
		int x = p[0].getXPos();
		int y = p[0].getYPos();
		if(x < 0 || y < 0 || x > width || y > height)
			return 1;

		boolean isFruit = false;

    	// On commence par vider les endroits ou il y a le serpent

    	for(int i = 0; i < p.length; i++)
    		squareTab[p[i].getXPos()][p[i].getYPos()].setObject("");

    	try{
			// On met a jour le serpent
			switch(snake.update(squareTab)){
				case 0:
					if(objectOnCase(p[0].getXPos(), p[0].getYPos()).equals("Sprite_Rock")) return 1;
					break;
				case 1:
					return 1;
				case 2:
					isFruit = true;
					break;
				default:
			}
    	}catch(Exception e){
    		if(e instanceof ArrayIndexOutOfBoundsException) return 1;
		}

    	// On remet le serpent sur le terrain
    	p = snake.getPositions();
    	String skin = snake.getSkin();

    	squareTab[p[0].getXPos()][p[0].getYPos()].setObject(skin + "_tete" + p[0].getDirection());
    	squareTab[p[1].getXPos()][p[1].getYPos()].setObject(skin + "_cou"  + p[1].getDirection());

    	for(int i = 2; i < p.length-2; i++)
    		if(!p[i].isElbow())
    			squareTab[p[i].getXPos()][p[i].getYPos()].setObject(skin + "_corp" + p[i].getDirection());
    		else{
    			squareTab[p[i].getXPos()][p[i].getYPos()].setObject(skin + "_coude"  + p[i].getDirection() + p[i+1].getInverseDirection());
    			//System.out.println("------>" + skin + "_coude"  + p[i].getDirection() + p[i+1].getInverseDirection());
    		}

    	squareTab[p[p.length-2].getXPos()][p[p.length-2].getYPos()].setObject(skin + "_arriere" + p[p.length-2].getDirection());
    	squareTab[p[p.length-1].getXPos()][p[p.length-1].getYPos()].setObject(skin + "_queue" + p[p.length-1].getDirection());

    	if(isFruit) return 2;
    	return 0;
    }

    public Square[][] getSquareTab(){
    	return squareTab;
    }

	public int getSquareSize(){
		return squareSize;
	}

	public int getXposition(){
		return posX;
	}

	public int getYposition(){
		return posY;
	}

	public void setSnakeDirection(char dir){
        snake.setHeadDirection(dir);
    }

    public Snake getSnake(){
		return snake;
	}

	// Fait spawn un fruit sur le terrain à un emplacement aléatoire
	public Point spawnFruit(){
		Random r = new Random();
		int x;
		int y;
		do {
			x = r.nextInt(width);
			y = r.nextInt(height);
		}while(snake.isOnSnake(x, y));
		setCaseObject(ListeFruits.randomFruit(), x, y);
		return new Point(x,y);
	}



	// Met a jour l'animation de fruit
	public void updateAnimFruit(){
		for (int i = 0; i < squareTab.length; i++) {
			for (int j = 0; j < squareTab[i].length; j++) {

				// Si il y a un objet sur cette tuile le dessiner aussi
				if(ListeFruits.isFruit(objectOnCase(i,j))){
					int length = objectOnCase(i,j).length();
					int no = Integer.parseInt(objectOnCase(i,j).substring(length - 1, length));
					if (no == 1) no = 2;
					else if (no == 2) no = 1;
					setCaseObject(objectOnCase(i,j).substring(0, length - 1) + no ,i,j);
				}

			}
		}
	}

	// Essaye d'ajouter un rocher
	// nb : le nombre
	// chance : la probabilité d'en faire apparaitre un en pourcent
	public Point[] tryAddRock(int nb, int chance){
		Random r = new Random();
		int prob = r.nextInt(100);
		if(prob > chance)
			return new Point[]{new Point(0,0)};

		int x;
		int y;
		Point[] tab = new Point[nb];
		for (int i = 0; i < nb; i++) {
			do {
				x = r.nextInt(width);
				y = r.nextInt(height);
			} while (snake.isOnSnake(x, y) || x == snake.getPositions()[0].getXPos() ||  y == snake.getPositions()[0].getYPos());
			tab[i] = new Point(x,y);
		}
		for (Point p: tab) setCaseObject("Sprite_Rock", p.x, p.y);
		return tab;
	}

}


class Square {
	private String object;
	private String background;

	Square(){	
		object = "";
		background = "";
	}

	public void setObject(String obj){
		object = obj;
	}

	public String getObject(){
		return object;
	}

	public void setBackground(String bg){
		background = bg;
	}

	public String getBackground(){
		return background;
	}

}