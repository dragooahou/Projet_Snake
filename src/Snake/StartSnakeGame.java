package Snake;// IMPORTS
    import java.awt.event.KeyEvent;
    import java.awt.event.KeyListener;

    import javax.swing.JFrame;

public class StartSnakeGame {
    // Point d'entré du jeu
    public static void main(String[] args) {
        // On créé une fenêtre et on execute la méthode game()
        new Window();
    }
}

// Classe qui crée automatiquement une fenêtre
class Window extends JFrame {

    // Tableau des mods de jeu
    private ModeDeJeu[] mj = new SnakeClassic[2];

    // Pour ecouter les inputs
    private MyKeyListener listener = new MyKeyListener();

    private Panel panel = new Panel();

    // Constructeur
    public Window(){

        mj[1] = new SnakeClassic();

        this.setTitle("Snake.Snake Game");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        panel.setMJ(mj[1]);
        listener.setMJ(mj[1]);
        
        // On dit que le contenu de la fenêtre va être notre panneau
        this.setContentPane(panel);

        mj[1].setWindow(this);
        mj[1].setPanel(panel);

        this.setVisible(true);

        // On s'occupe maintenant des inputs
        addKeyListener(listener);
        setFocusable(true);

        // On execute le jeu
        game();
    }

    // Le jeu s'execute ici
    private void game(){

        // On crée les thread
        Thread execGame = new Thread(){
            public void run(){
                mj[1].run();
            }
        };

        Thread drawGame = new Thread(){
            public void run(){
                mj[1].draw();
            }
        };

        execGame.start();
        drawGame.start();

    }

}

// Inputs
class MyKeyListener implements KeyListener {

    // Va tenir la ref de le fenetre
    private ModeDeJeu mj;

    // Va stocker la dernière touche pressé
    private String lastKey = "";

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        String key = KeyEvent.getKeyText(e.getKeyCode());

        //System.out.println("keyPressed="+key);

        // Si la touche est déjà pressé on ne fait rien
        if(key == lastKey)
            return;

        switch(key){
            case "Haut":
                mj.getTerrain().setSnakeDirection('N');
                break;
            case "Bas":
                mj.getTerrain().setSnakeDirection('S');
                break;
            case "Droite":
                mj.getTerrain().setSnakeDirection('E');
                break;
            case "Gauche":
                mj.getTerrain().setSnakeDirection('W');
                break;
            case "Echap":
                System.exit(0);
                break;
            default:
                break;
        }

        lastKey = key;
    }

    public void keyReleased(KeyEvent e) {
        //System.out.println("keyReleased="+KeyEvent.getKeyText(e.getKeyCode()));

        // Si on relache la touche on réinitialise lastKey
        lastKey = "";
    }

    public void setMJ(ModeDeJeu m){
        mj = m;
    }
}