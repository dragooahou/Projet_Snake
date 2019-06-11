package Snake;// IMPORTS
    import java.awt.*;
    import java.awt.event.*;
    import java.awt.image.BufferedImage;
    import java.io.FileInputStream;
    import java.util.Map;

    import javax.imageio.ImageIO;
    import javax.swing.JFrame;

public class StartSnakeGame {
    // Point d'entré du jeu
    public static void main(String[] args) {
        SaveManager.init();
        // On créé une fenêtre et on execute la méthode game()
        new Window();
    }
}

// Classe qui crée automatiquement une fenêtre
class Window extends JFrame {

    // Tableau des mods de jeu
    private ModeDeJeu[] mj = new ModeDeJeu[6];

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
        // Snake classique
        initSnakeClassic();

        // On execute le jeu
        game();
    }

    // Le jeu s'execute ici
    private void game(){


        panel.setMJ(mj[modeDeJeuCourant]);


        // Listeners
        addMouseMotionListener(mj[modeDeJeuCourant]);
        addMouseListener(mj[modeDeJeuCourant]);
        addKeyListener(mj[modeDeJeuCourant]);

        // On crée les thread
        Thread execGame = new Thread(() -> mj[modeDeJeuCourant].run());

        Thread drawGame = new Thread(() -> mj[modeDeJeuCourant].draw());

        execGame.start();
        drawGame.start();

    }

    private void killGame(){

        removeAllListeners();
        mj[modeDeJeuCourant].arreter();

    }

    private void removeAllListeners(){
        for (MouseMotionListener ml : getListeners(MouseMotionListener.class))
            removeMouseMotionListener(ml);

        for (MouseListener ml : getListeners(MouseListener.class))
            removeMouseListener(ml);

        for (KeyListener kl : getListeners(KeyListener.class))
            removeKeyListener(kl);
    }

    // Initialiser tout les menus
    public void initMenus(){

        // Menu principal ////////////////
            mj[0] = new Menu(5);
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
                            pop();
                            SoundManager.create("musicAmbiance", "ambiance",true);
                            changerMJ(1);
                        }
                    }
            );

            //Bouton skin
            BufferedImage[] img5 = {panel.getSprite("Menu_text_Skin_Standby"), panel.getSprite("Menu_text_Skin_Selected"), panel.getSprite("Menu_text_Skin_Validated")};
            menuPrincipal.setBouton(1 ,new Bouton(img5, mj[0]));
            menuPrincipal.getBouton(1).setPosXY(250, 350);
            menuPrincipal.getBouton(1).setActionListener(
                    new ActionBouton(){
                        @Override
                        public void execute() {
                            SoundManager.create("wii", "wii",true);
                            pop();
                            changerMJ(5);
                        }
                    }
            );

            //Bouton credit
            BufferedImage[] img6 = {panel.getSprite("Menu_text_Credit_Standby"), panel.getSprite("Menu_text_Credit_Selected"), panel.getSprite("Menu_text_Credit_Validated")};
            menuPrincipal.setBouton(2 ,new Bouton(img6, mj[0]));
            menuPrincipal.getBouton(2).setPosXY(250, 420);
            menuPrincipal.getBouton(2).setActionListener(
                    new ActionBouton(){
                        @Override
                        public void execute() {
                            pop();
                            changerMJ(4);
                        }
                    }
            );

            //Bouton mute
            menuPrincipal.setBouton(4 ,new Bouton(new BufferedImage[]{panel.getSprite("Menu_Sound_" + SaveManager.isMuted()),
                                                                          panel.getSprite("Menu_Sound_" + SaveManager.isMuted()),
                                                                          panel.getSprite("Menu_Sound_" + SaveManager.isMuted())}, mj[0]));
            menuPrincipal.getBouton(4).setPosXY(720,500);
            menuPrincipal.getBouton(4).setTaille(60, 60);
            menuPrincipal.getBouton(4).setActionListener(
                    new ActionBouton(){
                        @Override
                        public void execute() {
                            pop();
                            if(SaveManager.isMuted().equals("on")) SaveManager.mute();
                            else SaveManager.unmute();
                            menuPrincipal.getBouton(4).setTabImages(new BufferedImage[]{panel.getSprite("Menu_Sound_" + SaveManager.isMuted()),
                                                                                            panel.getSprite("Menu_Sound_" + SaveManager.isMuted()),
                                                                                            panel.getSprite("Menu_Sound_" + SaveManager.isMuted())});
                            menuPrincipal.getBouton(4).draw();
                        }
                    }
            );



            // Bouton quitter
            BufferedImage[] img1 = {panel.getSprite("Menu_text_Quitter_Standby"), panel.getSprite("Menu_text_Quitter_Selected"), panel.getSprite("Menu_text_Quitter_Validated")};
            menuPrincipal.setBouton(3 ,new Bouton(img1, mj[0]));
            menuPrincipal.getBouton(3).setPosXY(250, 490);
            menuPrincipal.getBouton(3).setActionListener((ActionBouton)() -> System.exit(0));

        //////////////////////////////////////

        // Menu pause ////////////////////////
            mj[2] = new Menu(3);
            mj[2].setWindow(this);
            mj[2].setPanel(panel);
            Menu menuPause = (Menu) mj[2];
            menuPause.setBackgroundImage(panel.getSprite("Menu_Box_Pause"));
            menuPause.setBgCoords(new Point(200, 50));
            menuPause.setBgTaille(new Point(400, 500));

            //Bouton reprendre
            BufferedImage[] img4 = {panel.getSprite("Menu_text_Reprendre_Standby"), panel.getSprite("Menu_text_Reprendre_Selected"), panel.getSprite("Menu_text_Reprendre_Validated")};
            menuPause.setBouton(0 ,new Bouton(img4, mj[2]));
            menuPause.getBouton(0).setPosXY(250, 200);
            menuPause.getBouton(0).setActionListener(
                    new ActionBouton(){
                        @Override
                        public void execute() {
                            pop();
                            resume();
                        }
                    }
            );

            //Bouton recommencer
            BufferedImage[] img2 = {panel.getSprite("Menu_text_Recommencer_Standby"), panel.getSprite("Menu_text_Recommencer_Selected"), panel.getSprite("Menu_text_Recommencer_Validated")};
            menuPause.setBouton(1 ,new Bouton(img2, mj[2]));
            menuPause.getBouton(1).setPosXY(250, 280);
            menuPause.getBouton(1).setActionListener(
                    new ActionBouton(){
                        @Override
                        public void execute() {
                            SoundManager.stop("musicAmbiance");
                            pop();
                            SoundManager.create("musicAmbiance", "ambiance",true);
                            changerMJ(1);
                        }
                    }
            );

            // Bouton menu
            BufferedImage[] img3 = {panel.getSprite("Menu_text_RetourMenu_Standby"), panel.getSprite("Menu_text_RetourMenu_Selected"), panel.getSprite("Menu_text_RetourMenu_Validated")};
            menuPause.setBouton(2 ,new Bouton(img3, mj[2]));
            menuPause.getBouton(2).setPosXY(250, 360);
            menuPause.getBouton(2).setActionListener(
                    new ActionBouton(){
                        @Override
                        public void execute() {
                            SoundManager.stop("musicAmbiance");
                            pop();
                            changerMJ(0);
                        }
                    }
            );
        //////////////////////////////////////

        // Menu perdu ////////////////////////
            mj[3] = new Menu(2);
            mj[3].setWindow(this);
            mj[3].setPanel(panel);
            Menu menuPerdu = (Menu) mj[3];
            menuPerdu.setBackgroundImage(panel.getSprite("Menu_Box_Perdu"));
            menuPerdu.setBgCoords(new Point(200, 50));
            menuPerdu.setBgTaille(new Point(400, 500));


            //Bouton recommencer
            menuPerdu.setBouton(0 ,new Bouton(img2, mj[3]));
            menuPerdu.getBouton(0).setPosXY(250, 280);
            menuPerdu.getBouton(0).setActionListener(
                    new ActionBouton(){
                        @Override
                        public void execute() {
                            SoundManager.stop("musicAmbiance");
                            pop();
                            SoundManager.create("musicAmbiance", "ambiance",true);
                            changerMJ(1);
                        }
                    }
            );

            // Bouton menu
            menuPerdu.setBouton(1 ,new Bouton(img3, mj[3]));
            menuPerdu.getBouton(1).setPosXY(250, 360);
            menuPerdu.getBouton(1).setActionListener(
                    new ActionBouton(){
                        @Override
                        public void execute() {
                            SoundManager.stop("musicAmbiance");
                            pop();
                            changerMJ(0);
                        }
                    }
            );


        //////////////////////////////////////

        // Menu credit ///////////////////////
            mj[4] = new Menu(1);
            mj[4].setWindow(this);
            mj[4].setPanel(panel);
            Menu menuCredit = (Menu) mj[4];
            menuCredit.setBackgroundImage(panel.getSprite("credits"));


            // Bouton invisible
            BufferedImage[] img7 = {panel.getSprite(""), panel.getSprite(""), panel.getSprite("")};
            menuCredit.setBouton(0 ,new Bouton(img7, mj[4]));
            menuCredit.getBouton(0).setPosXY(0, 0);
            menuCredit.getBouton(0).setTaille(getHeight(), getWidth());
            menuCredit.getBouton(0).setActionListener(
                    new ActionBouton(){
                        @Override
                        public void execute() {
                            pop();
                            changerMJ(0);
                        }
                    }
            );
        //////////////////////////////////////

        // Menu skin ////////////////////////
        mj[5] = new Menu(4);
        mj[5].setWindow(this);
        mj[5].setPanel(panel);
        Menu menuSkin = (Menu) mj[5];
        menuSkin.setBackgroundImage(panel.getSprite("fondAcceuil"));


        // Bouton menu
        menuSkin.setBouton(0,new Bouton(img3, mj[5]));
        menuSkin.getBouton(0).setPosXY(250, 490);
        menuSkin.getBouton(0).setActionListener(
                new ActionBouton(){
                    @Override
                    public void execute() {
                        SoundManager.stop("wii");
                        pop();
                        changerMJ(0);
                    }
                }
        );

        // Bouton gauche
        BufferedImage[] img8 = {panel.getSprite("Menu_Fleche_Gauche"), panel.getSprite("Menu_Fleche_Gauche"), panel.getSprite("Menu_Fleche_Gauche")};
        menuSkin.setBouton(1,new Bouton(img8, mj[5]));
        menuSkin.getBouton(1).setPosXY(250, 350);
        menuSkin.getBouton(1).setTaille(60, 60);
        menuSkin.getBouton(1).setActionListener(
                new ActionBouton(){
                    @Override
                    public void execute() {
                        SaveManager.setSkin(panel.cycleSkin(SaveManager.getSkin(), true));
                        menuSkin.getBouton(3).setTabImages(new BufferedImage[] {panel.getSprite(SaveManager.getSkin() + "_show"), panel.getSprite(SaveManager.getSkin() + "_show"), panel.getSprite(SaveManager.getSkin() + "_show")});
                        menuSkin.getBouton(3).draw();
                    }
                }
        );

        // Bouton droite
        BufferedImage[] img9 = {panel.getSprite("Menu_Fleche_Droite"), panel.getSprite("Menu_Fleche_Droite"), panel.getSprite("Menu_Fleche_Droite")};
        menuSkin.setBouton(2,new Bouton(img9, mj[5]));
        menuSkin.getBouton(2).setPosXY(485, 350);
        menuSkin.getBouton(2).setTaille(60, 60);
        menuSkin.getBouton(2).setActionListener(
                new ActionBouton(){
                    @Override
                    public void execute() {
                        SaveManager.setSkin(panel.cycleSkin(SaveManager.getSkin(), false));
                        menuSkin.getBouton(3).setTabImages(new BufferedImage[] {panel.getSprite(SaveManager.getSkin() + "_show"), panel.getSprite(SaveManager.getSkin() + "_show"), panel.getSprite(SaveManager.getSkin() + "_show")});
                        menuSkin.getBouton(3).draw();
                    }
                }
        );

        // Bouton qui affiche enfaite le serpent
        menuSkin.setBouton(3,new Bouton(new BufferedImage[] {panel.getSprite(SaveManager.getSkin() + "_show"), panel.getSprite(SaveManager.getSkin() + "_show"), panel.getSprite(SaveManager.getSkin() + "_show")}, mj[5]));
        menuSkin.getBouton(3).setPosXY(384, 320);
        menuSkin.getBouton(3).setTaille(128, 32);
        menuSkin.getBouton(3).setActionListener(
                new ActionBouton(){
                    @Override
                    public void execute() {

                    }
                }
        );
        //////////////////////////////////////
    }

    public void changerMJ(int n){
        killGame();
        modeDeJeuCourant = n;
        if(n == 1) initSnakeClassic();
        game();
    }

    public void pause(){
        int i = modeDeJeuCourant;
        mj[modeDeJeuCourant].pause();
        modeDeJeuCourant = 2;
        mj[modeDeJeuCourant].setPausedMJ(i);
        removeAllListeners();
        game();
    }

    public void resume(){
        int i = mj[modeDeJeuCourant].getPausedMJ();
        killGame();
        modeDeJeuCourant = i;
        panel.setMJ(mj[modeDeJeuCourant]);
        if(modeDeJeuCourant == 1) panel.dessiner("terrain");
        mj[modeDeJeuCourant].setDemarrage(true);
        panel.dessiner("snake");
        mj[modeDeJeuCourant].reprendre();

        addMouseMotionListener(mj[modeDeJeuCourant]);
        addMouseListener(mj[modeDeJeuCourant]);
        addKeyListener(mj[modeDeJeuCourant]);
    }

    public void initSnakeClassic(){
        // Mode de jeu classique
        mj[1] = new SnakeClassic();
        mj[1].setWindow(this);
        mj[1].setPanel(panel);
    }

    public void setModeDeJeuCourant(int modeDeJeuCourant) {
        this.modeDeJeuCourant = modeDeJeuCourant;
    }
    public void pop(){
        SoundManager.stop("pop");
        SoundManager.create("pop", "buttoninstant",false);
        SoundManager.play("pop");
    }
}

interface ActionBouton {
    void execute();
}