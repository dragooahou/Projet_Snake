package Snake;// IMPORTS
    import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class StartSnakeGame {
    // Point d'entré du jeu
    public static void main(String[] args) {
        SaveManager.init();
        SoundManager.initPetitsSons();
        // On créé une fenêtre et on execute la méthode game()
        new Window();
    }
}

/*
    Liste des modes de jeu :
        0 : Menu Principal
        1 : SnakeClassic
        2 : Menu pause
        3 : Menu perdu
        4 : Menu credits
        5 : Menu skin
        6 : Mode 2 joueurs
        7 : Menu choisir mode de jeu
        8 : Succes
        9 : Tron

*/


// Classe qui crée automatiquement une fenêtre
class Window extends JFrame {

    // Tableau des mods de jeu
    private ModeDeJeu[] mj = new ModeDeJeu[10];

    private Panel panel = new Panel();

    private int modeDeJeuCourant = 0;

    // Constructeur
    public Window(){
        this.setTitle("Snake : Chase of Kebab");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        // On dit que le contenu de la fenêtre va être notre panneau
        this.setContentPane(panel);

        this.setVisible(true);

        // On s'occupe maintenant des inputs
        setFocusable(true);

        initMenus();
        initSnakeClassic();
        initMultiplayer();
        initTron();

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
            mj[0] = new Menu(6);
            mj[0].setWindow(this);
            mj[0].setPanel(panel);
            Menu menuPrincipal = (Menu) mj[0];
            menuPrincipal.setBackgroundImage(panel.getSprite("fondAcceuil"));
            SoundManager.create("musicMenu", "Snake_menu",true);

            //Bouton jouer
            BufferedImage[] img0 = {panel.getSprite("Menu_text_Jouer_Standby"), panel.getSprite("Menu_text_Jouer_Selected"), panel.getSprite("Menu_text_Jouer_Validated")};
            menuPrincipal.setBouton(0 ,new Bouton(img0, mj[0]));
            menuPrincipal.getBouton(0).setPosXY(250, 280);
            menuPrincipal.getBouton(0).setActionListener(
                    new ActionBouton(){
                        @Override
                        public void execute() {
                            pop();
                            changerMJ(7);
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
                            SoundManager.stop("musicMenu");
                            pop();
                            SoundManager.create("wii", "wii",true);
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
                            if(SaveManager.isMuted().equals("on")){ SaveManager.mute();SoundManager.stop("musicMenu");}
                            else {SaveManager.unmute();SoundManager.create("musicMenu", "Snake_menu",true);}
                            menuPrincipal.getBouton(4).setTabImages(new BufferedImage[]{panel.getSprite("Menu_Sound_" + SaveManager.isMuted()),
                                                                                            panel.getSprite("Menu_Sound_" + SaveManager.isMuted()),
                                                                                            panel.getSprite("Menu_Sound_" + SaveManager.isMuted())});
                            menuPrincipal.getBouton(4).setTaille(0,0);
                            menuPrincipal.getBouton(4).draw();
                            menuPrincipal.getBouton(4).setTaille(60, 60);
                            menuPrincipal.mouseMoved(new MouseEvent((Component) panel, 0, 0L, 0,0,0,1, true));
                        }
                    }
            );

            //Bouton Achievement
            menuPrincipal.setBouton(5 ,new Bouton(new BufferedImage[]{panel.getSprite("Sprite_Success_icon"),
                                                                        panel.getSprite("Sprite_Success_icon"),
                    panel.getSprite("Sprite_Success_icon")}, mj[0]));
            menuPrincipal.getBouton(5).setPosXY(725,440);
            menuPrincipal.getBouton(5).setTaille(50, 50);
            menuPrincipal.getBouton(5).setActionListener(
                    new ActionBouton(){
                        @Override
                        public void execute() {
                            pop();
                            changerMJ(8);
                        }
                    }
            );



            // Bouton quitter

            BufferedImage[] img1 = {panel.getSprite("Menu_text_Quitter_Standby"), panel.getSprite("Menu_text_Quitter_Selected"), panel.getSprite("Menu_text_Quitter_Validated")};
            menuPrincipal.setBouton(3 ,new Bouton(img1, mj[0]));
            menuPrincipal.getBouton(3).setPosXY(250, 490);
            menuPrincipal.getBouton(3).setActionListener((ActionBouton)() -> {bye();System.exit(0);});

        //////////////////////////////////////

        // Menu pause ////////////////////////
            mj[2] = new Menu(4);
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
                            SoundManager.create("musicAmbiance","ambiance",true);
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
                            changerMJ(menuPause.getPausedMJ());
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
                            SoundManager.create("musicMenu", "Snake_menu",true);
                            changerMJ(0);
                        }
                    }
            );



            //Bouton mute
            menuPause.setBouton(3 ,new Bouton(new BufferedImage[]{panel.getSprite("Menu_Sound_" + SaveManager.isMuted()),
                    panel.getSprite("Menu_Sound_" + SaveManager.isMuted()),
                    panel.getSprite("Menu_Sound_" + SaveManager.isMuted())}, mj[2]));
            menuPause.getBouton(3).setPosXY(490,450);
            menuPause.getBouton(3).setTaille(60, 60);
            menuPause.getBouton(3).setActionListener(
                    new ActionBouton(){
                        @Override
                        public void execute() {
                            pop();
                            if(SaveManager.isMuted().equals("on")) {SaveManager.mute();SoundManager.stop("musicAmbiance");}
                            else SaveManager.unmute();
                            menuPause.getBouton(3).setTabImages(new BufferedImage[]{panel.getSprite("Menu_Sound_" + SaveManager.isMuted()),
                                    panel.getSprite("Menu_Sound_" + SaveManager.isMuted()),
                                    panel.getSprite("Menu_Sound_" + SaveManager.isMuted())});
                            menuPause.getBouton(3).setTaille(0,0);
                            menuPause.getBouton(3).draw();
                            menuPause.getBouton(3).setTaille(60, 60);
                            menuPause.mouseMoved(new MouseEvent((Component) panel, 0, 0L, 0,0,0,1, true));
                        }
                    }
            );
        //////////////////////////////////////

        // Menu perdu ////////////////////////
            mj[3] = new Menu(3);
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
                            changerMJ(menuPerdu.getPausedMJ());
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
                            SoundManager.create("musicMenu", "Snake_menu",true);
                            changerMJ(0);
                        }
                    }
            );

            //Bouton mute
            menuPerdu.setBouton(2 ,new Bouton(new BufferedImage[]{panel.getSprite("Menu_Sound_" + SaveManager.isMuted()),
                    panel.getSprite("Menu_Sound_" + SaveManager.isMuted()),
                    panel.getSprite("Menu_Sound_" + SaveManager.isMuted())}, mj[3]));
            menuPerdu.getBouton(2).setPosXY(490,450);
            menuPerdu.getBouton(2).setTaille(60, 60);
            menuPerdu.getBouton(2).setActionListener(
                    new ActionBouton(){
                        @Override
                        public void execute() {
                            pop();
                            if(SaveManager.isMuted().equals("on")) {SaveManager.mute();SoundManager.stop("musicAmbiance");}
                            else SaveManager.unmute();
                            menuPerdu.getBouton(2).setTabImages(new BufferedImage[]{panel.getSprite("Menu_Sound_" + SaveManager.isMuted()),
                                    panel.getSprite("Menu_Sound_" + SaveManager.isMuted()),
                                    panel.getSprite("Menu_Sound_" + SaveManager.isMuted())});
                            menuPerdu.getBouton(2).setTaille(0,0);
                            menuPerdu.getBouton(2).draw();
                            menuPerdu.getBouton(2).setTaille(60, 60);
                            menuPerdu.mouseMoved(new MouseEvent((Component) panel, 0, 0L, 0,0,0,1, true));
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
                            SoundManager.create("musicMenu", "Snake_menu",true);
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


            // Bouton retour menu
            BufferedImage[] img10 = {panel.getSprite("Sprite_FlecheRetour"), panel.getSprite("Sprite_FlecheRetour"), panel.getSprite("Sprite_FlecheRetour")};
            menuSkin.setBouton(0,new Bouton(img10, mj[5]));
            menuSkin.getBouton(0).setPosXY(365, 490);
            menuSkin.getBouton(0).setTaille(60, 60);
            menuSkin.getBouton(0).setActionListener(
                    new ActionBouton(){
                        @Override
                        public void execute() {
                            SoundManager.stop("wii");
                            pop();
                            SoundManager.create("musicMenu", "Snake_menu",true);
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
                            menuSkin.getBouton(3).setTaille(0,0);
                            menuSkin.getBouton(3).draw();
                            menuSkin.getBouton(3).setTaille(128, 32);
                            menuSkin.mouseMoved(new MouseEvent((Component) panel, 0, 0L, 0,0,0,1, true));
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
                            menuSkin.getBouton(3).setTaille(0,0);
                            menuSkin.getBouton(3).draw();
                            menuSkin.getBouton(3).setTaille(128, 32);
                            menuSkin.mouseMoved(new MouseEvent((Component) panel, 0, 0L, 0,0,0,1, true));
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

        // Menu mode de jeu ///////////////////////
        mj[7] = new Menu(4);
        mj[7].setWindow(this);
        mj[7].setPanel(panel);
        Menu menuChoose = (Menu) mj[7];
        menuChoose.setBackgroundImage(panel.getSprite("fondAcceuil"));

        //Bouton 1 joueur
        BufferedImage[] img11 = {panel.getSprite("Menu_text_UnJoueur_Standby"), panel.getSprite("Menu_text_UnJoueur_Selected"), panel.getSprite("Menu_text_UnJoueur_Validated")};
        menuChoose.setBouton(0 ,new Bouton(img11, mj[7]));
        menuChoose.getBouton(0).setPosXY(250, 290);
        menuChoose.getBouton(0).setActionListener(
                new ActionBouton(){
                    @Override
                    public void execute() {
                        SoundManager.stop("musicMenu");
                        pop();
                        SoundManager.create("musicAmbiance", "ambiance",true);
                        changerMJ(1);
                    }
                }
        );

        //Bouton 2 joueur
        BufferedImage[] img12 = {panel.getSprite("Menu_text_DeuxJoueur_Standby"), panel.getSprite("Menu_text_DeuxJoueur_Selected"), panel.getSprite("Menu_text_DeuxJoueur_Validated")};
        menuChoose.setBouton(1 ,new Bouton(img12, mj[7]));
        menuChoose.getBouton(1).setPosXY(250, 360);
        menuChoose.getBouton(1).setActionListener(
                new ActionBouton(){
                    @Override
                    public void execute() {
                        SoundManager.stop("musicMenu");
                        pop();
                        SoundManager.create("musicAmbiance", "ambiance",true);
                        changerMJ(6);
                    }
                }
        );

        //Bouton tron
        BufferedImage[] img13 = {panel.getSprite("Menu_Text_Tron_Standby"), panel.getSprite("Menu_Text_Tron_Selected"), panel.getSprite("Menu_Text_Tron_Validated")};
        menuChoose.setBouton(3 ,new Bouton(img13, mj[7]));
        menuChoose.getBouton(3).setPosXY(250, 410);
        menuChoose.getBouton(3).setActionListener(
                new ActionBouton(){
                    @Override
                    public void execute() {
                        SoundManager.stop("musicMenu");
                        pop();
                        SoundManager.create("musicAmbiance", "ambiance",true);
                        changerMJ(9);
                    }
                }
        );

        // Bouton retour menu
        menuChoose.setBouton(2,new Bouton(img10, mj[7]));
        menuChoose.getBouton(2).setPosXY(365, 490);
        menuChoose.getBouton(2).setTaille(60, 60);
        menuChoose.getBouton(2).setActionListener(
                new ActionBouton(){
                    @Override
                    public void execute() {
                        pop();
                        changerMJ(0);
                    }
                }
        );
        //////////////////////////////////////

        // Menu Succes ///////////////////////
        mj[8] = new Menu(3);
        mj[8].setWindow(this);
        mj[8].setPanel(panel);
        Menu menuSucces = (Menu) mj[8];
        menuSucces.setBackgroundImage(panel.getSprite("fondAcceuil"));


        // Bouton qui affiche succes 1
        menuSucces.setBouton(1,new Bouton(new BufferedImage[] {panel.getSprite("Sprite_success_1start"), panel.getSprite("Sprite_success_1start"), panel.getSprite("Sprite_success_1start")}, mj[8]));
        menuSucces.getBouton(1).setPosXY(364, 320);
        menuSucces.getBouton(1).setTaille(60, 60);
        menuSucces.getBouton(1).setActionListener(
                new ActionBouton(){
                    @Override
                    public void execute() {

                    }
                }
        );

        // Bouton qui affiche succes 2
        menuSucces.setBouton(2,new Bouton(new BufferedImage[] {panel.getSprite("Sprite_success_10fruits"), panel.getSprite("Sprite_success_10fruits"), panel.getSprite("Sprite_success_10fruits")}, mj[8]));
        menuSucces.getBouton(2).setPosXY(364, 390);
        menuSucces.getBouton(2).setTaille(60, 60);
        menuSucces.getBouton(2).setActionListener(
                new ActionBouton(){
                    @Override
                    public void execute() {

                    }
                }
        );


        // Bouton retour menu
        menuSucces.setBouton(0,new Bouton(new BufferedImage[] {panel.getSprite("Sprite_FlecheRetour"), panel.getSprite("Sprite_FlecheRetour"), panel.getSprite("Sprite_FlecheRetour")}, mj[8]));
        menuSucces.getBouton(0).setPosXY(365, 490);
        menuSucces.getBouton(0).setTaille(60, 60);
        menuSucces.getBouton(0).setActionListener(
                new ActionBouton(){
                    @Override
                    public void execute() {
                        pop();
                        changerMJ(0);
                    }
                }
        );



        //////////////////////////////////////
    }

    public void changerMJ(int n){
        killGame();
        modeDeJeuCourant = n;
        if(n == 1) initSnakeClassic();
        if(n == 6) initMultiplayer();
        if(n == 9) initTron();
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
        if(modeDeJeuCourant == 6) panel.dessiner("terrain");
        if(modeDeJeuCourant == 9) panel.dessiner("terrainfutur");

        mj[modeDeJeuCourant].setDemarrage(true);
        panel.dessiner("snake");
        mj[modeDeJeuCourant].reprendre();

        addMouseMotionListener(mj[modeDeJeuCourant]);
        addMouseListener(mj[modeDeJeuCourant]);
        addKeyListener(mj[modeDeJeuCourant]);
    }

    public void initSnakeClassic(){
        mj[1] = new SnakeClassic();
        mj[1].setWindow(this);
        mj[1].setPanel(panel);
    }

    public void initMultiplayer(){
        mj[6] = new Multiplayer();
        mj[6].setWindow(this);
        mj[6].setPanel(panel);
    }

    private void initTron() {
        mj[9] = new Tron();
        mj[9].setWindow(this);
        mj[9].setPanel(panel);
    }

    public void setModeDeJeuCourant(int modeDeJeuCourant) {
        this.modeDeJeuCourant = modeDeJeuCourant;
    }
    public void pop(){
        SoundManager.playSmall("buttoninstant.wav");
    }
    public void bye(){
        SoundManager.create("bye", "bye",false);
        SoundManager.play("bye");
    }

    public ModeDeJeu getModeDeJeuCourant(){
        return mj[modeDeJeuCourant];
    }
}

interface ActionBouton {
    void execute();
}