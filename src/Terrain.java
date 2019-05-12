import java.util.Random;

public class Terrain {

	// Tableau qui va contenir toutes les cases du terrain 
	private Square[][] squareTab;

	// Le serpent qui va se depacer sur le terrain
	private Snake snake;

	// Taille des cases en pixels
	private int squareSize;

	// Positions du terrain dans me fenêtre
	private int posX;
	private int posY;

	// Constructeur
	// x et y : position du terrain dans la fenetre
	// width et height : taille du terrain et tuiles
	// sqSize : taille d'une tuile en pixels
	Terrain (int x, int y, int width, int height, int sqSize) {

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

		setCaseObject("fruit", 20, 20);
		setCaseObject("fruit", 22, 20);
		setCaseObject("fruit", 20, 20);
		setCaseObject("fruit", 12, 8);
		setCaseObject("fruit", 0, 5);
		setCaseObject("fruit", 19, 20);
		setCaseObject("fruit", 10, 24);
		setCaseObject("fruit", 4, 20);
	}

	// Retourne l'objet dans la case aux coordonnées spécifiées
	public String objectOnCase (int x, int y) {
		return squareTab[x][y].getObject();
	}

	public String backgroundOnCase(int x, int y){
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
    	snake = new Snake(5, 50, "snake_yoshi");

    	SnakePart[] p = snake.getPositions();
    	String skin = snake.getSkin();

    	squareTab[p[0].getXPos()][p[0].getYPos()].setObject(skin + "_tete" + p[0].getDirection()  + "4");
    	squareTab[p[1].getXPos()][p[1].getYPos()].setObject(skin + "_cou"  + p[0].getDirection() + "4");

    	for(int i = 2; i < p.length-1; i++)
    		squareTab[p[i].getXPos()][p[i].getYPos()].setObject(skin + "_corp" + p[0].getDirection() + "4");

    	squareTab[p[p.length-1].getXPos()][p[p.length-1].getYPos()].setObject(skin + "_queue" + p[0].getDirection()+ "4");
    }

    // Met à jour le terrain
    public void update(){
    	// Sécurité
    	if(snake == null){
    		System.out.println("pas de serpent");
    		return;
    	}

    	// On commence par vider les endroits ou il y a le serpent
    	SnakePart[] p = snake.getPositions();

    	for(int i = 0; i < p.length; i++)
    		squareTab[p[i].getXPos()][p[i].getYPos()].setObject("");

    	// On met a jour le serpent
    	switch(snake.update(squareTab)){
    		case 0:
    			break;
    		case 1:
    			System.out.println("AÏE");
    			break;
    		case 2:
    			System.out.println("GAGNÉ");
    			break;
    		default:
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