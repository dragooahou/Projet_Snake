// Une partie du serpent
public class SnakePart {

	private int posX;
	private int posY;

	// Direction de cette partie : N S E W
	private char direction;

	// Si sur cette partie se trouve un coude
	private boolean elbow;

	// Constructeur
	SnakePart(int x, int y, char d){
		posX = x;
		posY = y;
		direction = d;
		elbow = false;
	}

	public int getXPos(){
		return posX;
	}

	public int getYPos(){
		return posY;
	}

	public void setXPos(int x){
		posX = x;
	}

	public void setYPos(int y){
		posY = y;
	}

	public char getDirection(){
		return direction;
	}

	public char getInverseDirection(){
		switch(direction){
			case 'N':
				return 'S';
			case 'S':
				return 'N';
			case 'E':
				return 'W';
			case 'W':
				return 'E';
			default:
		}
		return 'N';
	}

	public void setDirection(char d){
		direction = d;
	}

	public boolean isElbow(){
		return elbow;
	}

	public void setElbow(boolean e){
		elbow = e;
	}

	public void decX(){
		posX--;
	}

	public void decY(){
		posY--;
	}

	public void incX(){
		posX++;
	}

	public void incY(){
		posY++;
	}

}