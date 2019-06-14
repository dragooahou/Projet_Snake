package Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class KebabMod extends ModeDeJeu {

    private Inventaire inventaire = new Inventaire();

    public KebabMod() {
        name = "kebab";

        stopped = false;
        paused = false;
        demarrage = true;
        hud = new Interface(this);

        // Grille sur laquelle va se déplacer le serpent
        terrain = new Terrain(0, 32, 25, 17, 32);
        terrain.setSnake(0,15,12);
    }

    public void run() {
        SaveManager.upNbGames();
        hud.setBackgound(panel.getSprite("interfaceKebab"));
        hud.draw("1");

        spawnFruitSet();

        while (!stopped) {

            if (paused || demarrage) {
                if(demarrage){
                    Graphics2D g2d = panel.getGraph();
                    g2d.setFont(new Font("Arial", Font.PLAIN, 24));
                    Color bgColor = new Color(0xFFC600);

                    FontMetrics fm = g2d.getFontMetrics();
                    Rectangle2D rect1 = fm.getStringBounds(" Ramassez les ingrédients dans le bon ordre pour faire des kebab ", g2d);
                    Rectangle2D rect2 = fm.getStringBounds(" Attention, il n'y a pas de pomme dans un kebab ! ", g2d);
                    g2d.setColor(bgColor);
                    g2d.fillRect(10,
                            250 - fm.getAscent(),
                            (int) rect1.getWidth(),
                            (int) rect1.getHeight());
                    g2d.fillRect(90,
                            280 - fm.getAscent(),
                            (int) rect2.getWidth(),
                            (int) rect2.getHeight());
                    g2d.setColor(Color.black);

                    g2d.drawString("Ramassez les ingrédients dans le bon ordre pour faire des kebab", 20, 250);
                    g2d.drawString("Attention, il n'y a pas de pommes dans un kebab !", 100, 280);
                }

                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            Graphics2D g2d = panel.getGraph();
            g2d.setFont(new Font("Arial", Font.PLAIN, 18));
            g2d.setColor(Color.black);
            g2d.drawString("Salade > Viande > Tomate > Oignon", 20, 50);

            // On met à jour 4 fois l'animation et on bouge le serpent
            for (int i = 0; i < 4; i++) {

                panel.updateAnim();

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // On met à jour le terrain
            // Si le serpent est mort
            switch (terrain.update(0)) {
                case 0:
                    break;
                case 1:
                    SoundManager.stop("musicAmbiance");
                    if(SaveManager.getHiscore(name) < hud.getScore()) SaveManager.setHiscore(name, hud.getScore());
                    die();
                    window.changerMJ(3);
                    window.getModeDeJeuCourant().setPausedMJ(10);
                    break;
                case 2:
                    miam();
                    if(ListeFruits.tmp.equals("Sprite_RedApple1") || ListeFruits.tmp.equals("Sprite_RedApple2")){
                        panel.drawTerrain(terrain.spawnFruit("Sprite_RedApple1"));
                        panel.drawTerrain(terrain.spawnFruit("Sprite_RedApple1"));
                        panel.drawTerrain(terrain.spawnFruit("Sprite_RedApple1"));
                        hud.addScore(-200, inventaire.getState());
                    }
                    if(inventaire.add(ListeFruits.tmp)) {
                        if (inventaire.isFini()) {
                            spawnFruitSet();
                            panel.drawTerrain(terrain.spawnFruit("Sprite_RedApple1"));
                            panel.drawTerrain(terrain.spawnFruit("Sprite_RedApple1"));
                            panel.drawTerrain(terrain.spawnFruit("Sprite_RedApple1"));
                            hud.addScore(1000, inventaire.getState());
                            inventaire.init();
                        }
                    }else spawnFruitSet();

                    for (Point p : terrain.tryAddRock(1, 50)) panel.drawTerrain(p);
                    for (Point p : terrain.tryAddRock(1, 20)) panel.drawTerrain(p);
                    for (Point p : terrain.tryAddRock(1, 12)) panel.drawTerrain(p);
                    hud.addScore(100, inventaire.getState());
                    SaveManager.upNbFruits();
                    break;
            }
        }
    }

    public void draw() {
        panel.dessiner("terrain");

        ActionListener repaint = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!paused && !demarrage)
                    panel.dessiner("snake");

                if (stopped) {
                    Timer timer = (Timer) e.getSource();
                    timer.stop();
                }
            }
        };

        Timer drawClock = new Timer(20, repaint);
        drawClock.start();
        drawClock.setRepeats(true);

        // On lance la génération de fruits
        ActionListener animFruit = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!paused && !demarrage) {
                    terrain.updateAnimFruit();
                    panel.dessiner("fruits");
                }

                if (stopped) {
                    Timer timer = (Timer) e.getSource();
                    timer.stop();
                }
            }
        };

        Timer animeFruitClock = new Timer(500, animFruit);
        animeFruitClock.start();
        animeFruitClock.setRepeats(true);

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // Va stocker la dernière touche pressée
    private String lastKey = "";

    public void keyPressed(KeyEvent e) {
        String key = KeyEvent.getKeyText(e.getKeyCode());



        //System.out.println("keyPressed="+key);

        // Si la touche est déjà pressé on ne fait rien
        if (key.equals(lastKey))
            return;

        switch (key) {
            case "Haut":
                terrain.setSnakeDirection(0,'N');
                break;
            case "Bas":
                terrain.setSnakeDirection(0,'S');
                break;
            case "Droite":
                terrain.setSnakeDirection(0,'E');
                break;
            case "Gauche":
                terrain.setSnakeDirection(0,'W');
                break;
            case "Echap":
                if(demarrage) break;
                window.pause();
                break;
            case "Espace":
                terrain.spawnFruit();
                break;
            default:
                break;
        }

        lastKey = key;

        if (demarrage) {
            demarrage = false;
            panel.dessiner("terrain");
            hud.draw(inventaire.getState());
        }
    }

    public void keyReleased(KeyEvent e) {
        //System.out.println("keyReleased="+KeyEvent.getKeyText(e.getKeyCode()));

        // Si on relache la touche on réinitialise lastKey
        lastKey = "";
    }

    public void miam() {
        SoundManager.playSmall("crocpomme.wav");
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    boolean f = false;
    public void die() {



        int i = (int)(Math.random() * SimpleAudioPlayer.getDeathSounds().size());
        if (!f){
            for (int u =0;u<SimpleAudioPlayer.getDeathSounds().size();u++){
                SoundManager.createSmall(SimpleAudioPlayer.getDeathSounds().get(u), SimpleAudioPlayer.getDeathSounds().get(u));
            }
            f = true;
        }
        SoundManager.playSmall(SimpleAudioPlayer.getDeathSounds().get(i));



    }


    public void spawnFruitSet(){
        for (String f : ListeFruits.getFruits()) if(f.endsWith("1")){panel.drawTerrain(terrain.spawnFruit(f));};
    }

}