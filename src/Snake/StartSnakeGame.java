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
    private ModeDeJeu[] mj = new ModeDeJeu[3];

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

        // Listeners
        addMouseMotionListener(mj[modeDeJeuCourant]);
        addMouseListener(mj[modeDeJeuCourant]);
        addKeyListener(mj[modeDeJeuCourant]);

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
        removeMouseListener(mj[modeDeJeuCourant]);
        removeMouseMotionListener(mj[modeDeJeuCourant]);
        removeKeyListener(mj[modeDeJeuCourant]);

        try {
            mj[modeDeJeuCourant].arreter();
            execGame.join();
            drawGame.join();
        }catch (Exception e){ e.printStackTrace(); }

    }

    // Initialiser tout les menus
    public void initMenus(){

        // Menu principal ////////////////
            mj[0] = new Menu(2);
            mj[0].setWindow(this);
            mj[0].setPanel(panel);
            Menu menuPrincipal = (Menu) mj[0];
            menuPrincipal.setBackgroundImage(panel.getSprite("fondAcceuil"));

            //Bouton jouer
            BufferedImage[] img0 = {panel.getSprite("Menu_text_Jouer_Standby"), panel.getSprite("Menu_text_Jouer_Selected"), panel.getSprite("Menu_text_Jouer_Validated")};
            menuPrincipal.setBouton(0 ,new Bouton(img0, mj[0]));
            menuPrincipal.getBouton(0).setPosXY(250, 280);
            menuPrincipal.getBouton(0).setActionListener(
                    new ActionBouton(){
                        @Override
                        public void execute() {
                            changerMJ(1);
                        }
                    }
            );

            // Bouton quitter
            BufferedImage[] img1 = {panel.getSprite("Menu_text_Quitter_Standby"), panel.getSprite("Menu_text_Quitter_Selected"), panel.getSprite("Menu_text_Quitter_Validated")};
            menuPrincipal.setBouton(1 ,new Bouton(img1, mj[0]));
            menuPrincipal.getBouton(1).setPosXY(250, 360);
            menuPrincipal.getBouton(1).setActionListener((ActionBouton)() -> System.exit(0));
        //////////////////////////////////////

        // Menu pause ////////////////////////
            mj[2] = new Menu(2);
            mj[2].setWindow(this);
            mj[2].setPanel(panel);
            Menu menuPause = (Menu) mj[2];
            menuPause.setBackgroundImage(panel.getSprite("fondPause"));

            //Bouton recommencer
            BufferedImage[] img2 = {panel.getSprite("Menu_text_Recommencer_Standby"), panel.getSprite("Menu_text_Recommencer_Selected"), panel.getSprite("Menu_text_Recommencer_Validated")};
            menuPause.setBouton(0 ,new Bouton(img2, mj[2]));
            menuPause.getBouton(0).setPosXY(250, 280);
            menuPause.getBouton(0).setActionListener(
                    new ActionBouton(){
                        @Override
                        public void execute() {
                            changerMJ(1);
                        }
                    }
            );

            // Bouton menu
            BufferedImage[] img3 = {panel.getSprite("Menu_text_Menu_Standby"), panel.getSprite("Menu_text_Menu_Selected"), panel.getSprite("Menu_text_Menu_Validated")};
            menuPause.setBouton(1 ,new Bouton(img3, mj[2]));
            menuPause.getBouton(1).setPosXY(250, 360);
            menuPause.getBouton(1).setActionListener(
                    new ActionBouton(){
                        @Override
                        public void execute() {
                            changerMJ(0);
                        }
                    }
            );
        //////////////////////////////////////
    }

    public void changerMJ(int n){
        killGame();
        modeDeJeuCourant = n;
        game();
    }

    public void initSnakeClassic(){
        // Mode de jeu classique
        mj[1] = new SnakeClassic();
        mj[1].setWindow(this);
        mj[1].setPanel(panel);

        panel.setMJ(mj[1]);
    }

    public void setModeDeJeuCourant(int modeDeJeuCourant) {
        this.modeDeJeuCourant = modeDeJeuCourant;
    }
}

interface ActionBouton {
    void execute();
}