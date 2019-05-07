// IMPORTS
    import java.awt.Color;
    import java.awt.Graphics;
    import java.awt.Graphics2D;
    import java.awt.Transparency;
    import javax.swing.JPanel;
    import java.awt.Image;
    import java.awt.image.BufferedImage;
    import java.io.File;
    import java.io.IOException;    
    import javax.imageio.ImageIO;

    import java.util.HashMap;
    import java.util.Map;

    import java.awt.geom.AffineTransform;
    import java.awt.image.AffineTransformOp;

public class Panel extends JPanel {

    // Répertoire des sprites du terrain
    private String TERRAIN_SPRITE_DIR = "../sprites/terrain/";
    // Répertoire des sprites du serpent
    private String SNAKE_SPRITE_DIR = "../sprites/snake/";
    // Répertoire des sprites des fruits
    private String FRUITS_SPRITE_DIR = "../sprites/fruits/";

    // Va contenir tous les sprites avec leur nom
    private Map<String, BufferedImage> sprites = new HashMap<String, BufferedImage>();

    // Contient le numero de la frame d'animation à afficher
    private int frameNo = 4;

    // La ref du mode de jeu
    private ModeDeJeu mj;

    private boolean init = true;

    // Constructeur
    public Panel(){
        // On charge les sprites
            sprites.put("herbe", loadImage(TERRAIN_SPRITE_DIR + "herbe.png"));
            sprites.put("herbe_point", loadImage(TERRAIN_SPRITE_DIR + "herbe_point.png"));
            sprites.put("herbe_touffe_gauche", loadImage(TERRAIN_SPRITE_DIR + "herbe_touffe_gauche.png"));
            sprites.put("herbe_touffe_droite", loadImage(TERRAIN_SPRITE_DIR + "herbe_touffe_droite.png"));
            sprites.put("fruit", loadImage(FRUITS_SPRITE_DIR + "fruit.png"));

            loadSnakeSprites("default");

    }

    // Methode appelé à chaque appel de repaint()
    public void paintComponent(Graphics g){

        Graphics2D g2d = (Graphics2D) g;

        drawTerrain(g2d);

        drawSnake(g2d);

    }

    // Dessiner le terrain
    private void drawTerrain(Graphics2D g2d){
        // On recupère le terrain dans la classe fenetre
        Terrain terrain = mj.getTerrain();

        // On recupère la taille d'une tuile et la position x et y du terrain dans la fenetre
        int squareSize = terrain.getSquareSize();
        int posX = terrain.getXposition();
        int posY = terrain.getYposition();

        for (int i = 0; i < terrain.getSquareTab().length; i++) {
            for (int j = 0; j < terrain.getSquareTab()[i].length; j++) {
                //g.drawRect(i*squareSize + posX, j*squareSize + posY, squareSize, squareSize);

                // On dessine la tuile de terrain
                g2d.drawImage(sprites.get(terrain.backgroundOnCase(i,j)), i*squareSize + posX, j*squareSize + posY, squareSize, squareSize, this);
/*
                // Si il y a un objet sur cette tuile le dessiner aussi
                if(terrain.objectOnCase(i,j) != ""){
                    //System.out.println(terrain.getSquareTab()[i][j].getObject() + frameNo);
                    g2d.drawImage(sprites.get(terrain.objectOnCase(i,j) + frameNo), i*squareSize + posX, j*squareSize + posY, squareSize, squareSize, this);
                }
*/
            }
        }
    }


    // Dessiner le serpent et les objets
    public void drawSnake(Graphics2D g2d){

        // On recupère le terrain dans la classe fenetre
        Terrain terrain = mj.getTerrain();

        // On recupère la taille d'une tuile et la position x et y du terrain dans la fenetre
        int squareSize = terrain.getSquareSize();
        int posX = terrain.getXposition();
        int posY = terrain.getYposition();


        // On dessine le serpent : on parcour le serpent et on redessine
        SnakePart[] partPositions = terrain.getSnake().getPositions();

        for (SnakePart part : partPositions) {
            g2d.drawImage(sprites.get(terrain.objectOnCase(part.getXPos(),part.getYPos()) + frameNo), part.getXPos()*squareSize + posX, part.getYPos()*squareSize + posY, squareSize, squareSize, this);
        } 
    }      

    // Methode qui raccourci le chargement des sprites
    // et qui le fait en toute sécurité
    public BufferedImage loadImage(String file){
        try{
            BufferedImage img = ImageIO.read(new File(file));
            return img;
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;                
    }


    // Charge automatiquement les sprites de serpent dans la hashmap
    public void loadSnakeSprites(String file){
        BufferedImage img = loadImage(SNAKE_SPRITE_DIR + file + ".png");

        // Cette partie du code est vraiment dégueulasse (à refaire si possible)
        char[] cards = {'N', 'E', 'S', 'W'};
        int rot = 0;
        for(char n : cards){
            for (int i = 1; i < 5; i++) {
                sprites.put("snake_" + file + "_tete" + n + i, rotate(img.getSubimage(0,4*i,16,16), rot));
                sprites.put("snake_" + file + "_cou" + n + i, rotate(img.getSubimage(0,16+4*i,16,16), rot));
                sprites.put("snake_" + file + "_corp" + n + i, rotate(img.getSubimage(0,32+4*i,16,16), rot));
                sprites.put("snake_" + file + "_arriere" + n + i, rotate(img.getSubimage(0,48+4*i,16,16), rot));
                sprites.put("snake_" + file + "_queue" + n + i, rotate(img.getSubimage(0,64+4*i,16,16), rot));

                sprites.put("snake_" + file + "_coude" + n + cards[(rot/90+3)%4] + i, rotate(img.getSubimage(0,80+16*i,16,16), rot));
                sprites.put("snake_" + file + "_coude" + cards[(rot/90+3)%4] + n + i, rotate((img.getSubimage(0,80+16*i,16,16)), rot));
                //System.out.println("snake_" + file + "_coude" + n + cards[(rot/90+3)%4] + i);
            }
            rot += 90;
        }
    }

    public void setMJ(ModeDeJeu m){
        mj = m;
    }

    // Différentes méthodes de manipulation des sprites

    public BufferedImage flipVerticaly(BufferedImage image){
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -image.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);
        return image;
    }   

    public BufferedImage flipHorizontaly(BufferedImage image){
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);
        return image;
    }

    public BufferedImage flipVerticalyAndHorizontaly(BufferedImage image){
        AffineTransform tx = AffineTransform.getScaleInstance(-1, -1);
        tx.translate(-image.getWidth(null), -image.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);
        return image;
    }

    // Rotation (angle en degrés)
    // ATTENTION : fonctionne correctement uniquement avec les rotations de 90, 180 et 270 degrés
    public BufferedImage rotate(BufferedImage image, double angle) {
        int h = image.getHeight();
        int w = image.getWidth();
        angle =  Math.toRadians(angle);
        AffineTransform tx = AffineTransform.getRotateInstance(angle, h/2, w/2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);
        return image;
    }

    // Incrémente le numéro de frame et reste tjr entre 0 et 4
    public void updateAnim(){
        frameNo = (frameNo++)%4 + 1;
    }
}