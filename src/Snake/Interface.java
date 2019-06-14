package Snake;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Interface {

    private ModeDeJeu mj;

    private BufferedImage backgound;
    private Point backGoundPosition = new Point(0,0);

    private int score = 0;
    private int hiscore = 0;
    private int kebabState = 0;

    public Interface(ModeDeJeu mj){
        this.mj = mj;
        hiscore = SaveManager.getHiscore(mj.name);
    }

    // Dessiner avec une icon
    public void draw(String state){
        draw();
        Graphics2D g2d = mj.panel.getGraph();
        g2d.drawImage(mj.panel.getSprite("Sprite_KebabIcon" + state), mj.panel.getWidth() - 49, -17, 50, 50, mj.panel);
        g2d.setColor(Color.black);
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        g2d.drawString("Salade > Viande > Tomate > Oignon", 20, 50);
    }


    public void draw(){
        Graphics2D g2d = mj.panel.getGraph();
        g2d.drawImage(backgound, backGoundPosition.x, backGoundPosition.y, mj.panel);
        if (score > hiscore) {
            hiscore = score;
        }
        writeScore(g2d, score, 450, 0);
        writeScore(g2d, hiscore, 180, 0);
    }

    private void writeScore(Graphics2D g2d ,int n, int x, int y){
        int offset = 0;
        //if(n < 0) offset += 16;


        for (char c : (n + "").toCharArray()) {
            g2d.drawImage(mj.panel.getSprite("Sprite_num_" + c), x + offset, y, 32, 32, mj.panel);
            offset += 32;
        }
    }

    public void addScore(int n){
        score+=n;
        draw();
    }

    public void addScore(int n, String state){
        score+=n;
        draw(state);
    }

    public BufferedImage getBackgound() {
        return backgound;
    }

    public void setBackgound(BufferedImage backgound) {
        this.backgound = backgound;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHiscore() {
        return hiscore;
    }

    public void setHiscore(int hiscore) {
        this.hiscore = hiscore;
    }

    public int getKebabState() {
        return kebabState;
    }

    public void setKebabState(int kebabState) {
        this.kebabState = kebabState;
    }

    public void setMj(ModeDeJeu mj) {
        this.mj = mj;
    }

    public void setBackGoundPosition(Point backGoundPosition) {
        this.backGoundPosition = backGoundPosition;
    }
}
