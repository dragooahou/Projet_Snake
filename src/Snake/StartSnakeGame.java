package Snake;// IMPORTS
    import java.awt.event.*;
    import java.awt.image.BufferedImage;
    import java.lang.reflect.Executable;

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
    private ModeDeJeu[] mj = new ModeDeJeu[2];

    // Pour ecouter les inputs
    private MyKeyListener listener = new MyKeyListener();

    private Panel panel = new Panel();

    private int modeDeJeuCourant = 0;

    // Constructeur
    public Window(){
        this.setTitle("Snake.Snake Game");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        // On dit que le contenu de la fenêtre va être notre panneau
        this.setContentPane(panel);


        this.setVisible(true);

        // On s'occupe maintenant des inputs
        addKeyListener(listener);
        setFocusable(true);

        // Les menus
        initMenus();


        // On execute le jeu
        game();
    }

    private Thread execGame;
    private Thread drawGame;

    // Le jeu s'execute ici
    private void game(){

        // Snake classique
        initSnakeClassic();

        // On crée les thread
        execGame = new Thread(){
            public void run(){
                mj[modeDeJeuCourant].run();
            }
        };

        drawGame = new Thread(){
            public void run(){
                mj[modeDeJeuCourant].draw();
            }
        };

        execGame.start();
        drawGame.start();

    }

    private void killGame(){

        try {
            mj[modeDeJeuCourant].arreter();
            execGame.join();
            drawGame.join();
        }catch (Exception e){ e.printStackTrace(); }

    }

    // Initialiser tout les menus
    public void initMenus(){

        // Menu principal////////////////
        mj[0] = new Menu(2);
        mj[0].setWindow(this);
        mj[0].setPanel(panel);
        Menu menu = (Menu) mj[0];
        panel.addMouseListener(menu);
        panel.addMouseMotionListener(menu);

        //Bouton jouer
        BufferedImage[] img0 = {panel.getSprite("Menu_text_test_Jouer"), panel.getSprite("Menu_text_test_Jouer_Selected"), panel.getSprite("Menu_text_test_Jouer_Validated")};
        menu.setBouton(0 ,new Bouton(img0, mj[0]));
        menu.getBouton(0).setPosXY(30, 100);
        menu.getBouton(0).setActionListener(
                new ActionBouton(){
                    @Override
                    public void execute() {
                        killGame();
                        modeDeJeuCourant = 1;
                        game();
                    }
                }
        );

        // Bouton quitter
        BufferedImage[] img1 = {panel.getSprite("Menu_text_test_Quitter"), panel.getSprite("Menu_text_test_Quitter_Selected"), panel.getSprite("Menu_text_test_Quitter_Validated")};
        menu.setBouton(1 ,new Bouton(img1, mj[0]));
        menu.getBouton(1).setPosXY(400, 400);
        menu.getBouton(1).setActionListener((ActionBouton)() -> System.exit(0));
    }

    public void initSnakeClassic(){
        // Mode de jeu classique
        mj[1] = new SnakeClassic();
        mj[1].setWindow(this);
        mj[1].setPanel(panel);

        panel.setMJ(mj[1]);
        listener.setMJ(mj[1]);
    }

    public void setModeDeJeuCourant(int modeDeJeuCourant) {
        this.modeDeJeuCourant = modeDeJeuCourant;
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

interface ActionBouton {
    void execute();
}