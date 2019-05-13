package Snake;

abstract class ModeDeJeu{

	protected Panel panel;
	protected Window window;
	protected Terrain terrain;

	public void run(){
		System.out.println("Ce mode de jeu n'a rien à executer.");
	}

	public void draw(){
		System.out.println("Ce mode de jeu n'a rien à afficher.");
	}

	public void setWindow(Window w){
        window = w;
    }

    public void setPanel(Panel p){
        panel = p;
    }


	public Terrain getTerrain(){
		return terrain;
	}
}