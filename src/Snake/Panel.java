package Snake;

import java.awt.*;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

import java.net.URISyntaxException;
import java.util.*;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

import java.util.jar.*;

import java.nio.file.*;

public class Panel extends JPanel {

    // Répertoire des sprites du terrain
    private final String TERRAIN_SPRITE_DIR = "/sprites/terrain/";
    // Répertoire des sprites du serpent
    private final String SNAKE_SPRITE_DIR = "/sprites/snake/";
    // Répertoire des sprites des fruits
    private final String FRUITS_SPRITE_DIR = "/sprites/fruits/";
    // Répertoire des sprites du menu
    private final String MENU_SPRITE_DIR = "/sprites/menu/";

    // Va contenir tous les sprites avec leur nom
    private Map<String, BufferedImage> sprites = new HashMap<String, BufferedImage>();

    // Contient le numero de la frame d'animation à afficher
    private int frameNo = 4;

    // La ref du mode de jeu
    private ModeDeJeu mj;

    private boolean init = true;

    // Constructeur
    public Panel(){
        // On charge les sprites automatiquement
        try{
            for(String file : searchSprite(FRUITS_SPRITE_DIR))
                sprites.put(fileName(file), loadImage(file));

            for(String file : searchSprite(TERRAIN_SPRITE_DIR))
                sprites.put(fileName(file), loadImage(file));

            for(String file : searchSprite(SNAKE_SPRITE_DIR))
                loadSnakeSprites(file);

            for(String file : searchSprite(MENU_SPRITE_DIR))
                sprites.put(fileName(file), loadImage(file));


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // Dessiner le jeu
    public void dessiner(String target){
        //super.paintComponent(g);

        // On recupère la classe Graphics
        Graphics g = getGraphics();
        Graphics2D g2d = (Graphics2D) g;


        // On dessine les différentes parties de notre jeu
        switch(target){
            case "snake":
                drawSnake(g2d);
                break;

            case "terrain":
                drawTerrain(g2d);
                break;

        }


        // Très important mais je sais pas vraiment à quoi ca sert
        Toolkit.getDefaultToolkit().sync();

        // On libère la classe Graphics
        g.dispose();

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

                // Si il y a un objet sur cette tuile le dessiner aussi
                if(!terrain.objectOnCase(i,j).equals("")){
                    g2d.drawImage(sprites.get(terrain.objectOnCase(i,j)), i*squareSize + posX, j*squareSize + posY, squareSize, squareSize, this);
                }

            }
        }
    }
    // Dessiner une case précise du terrain
    public void drawTerrain(Point p){
        Graphics2D g2d = (Graphics2D) getGraphics();


        // On recupère le terrain dans la classe fenetre
        Terrain terrain = mj.getTerrain();

        // On recupère la taille d'une tuile et la position x et y du terrain dans la fenetre
        int squareSize = terrain.getSquareSize();
        int posX = terrain.getXposition();
        int posY = terrain.getYposition();

        // On dessine la tuile de terrain
        g2d.drawImage(sprites.get(terrain.backgroundOnCase(p.x,p.y)), p.x*squareSize + posX, p.y*squareSize + posY, squareSize, squareSize, this);

        // Si il y a un objet sur cette tuile le dessiner aussi
        if(!terrain.objectOnCase(p.x,p.y).equals("")){
            g2d.drawImage(sprites.get(terrain.objectOnCase(p.x,p.y)), p.x*squareSize + posX, p.y*squareSize + posY, squareSize, squareSize, this);
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


        // On dessine le serpent : on parcour le serpent et on redessine le terrain et le serpent
        SnakePart[] partPositions = terrain.getSnake().getPositions();

        for (SnakePart part : partPositions) {
            int x = part.getXPos();
            int y = part.getYPos();
            g2d.drawImage(sprites.get(terrain.backgroundOnCase(x,y)), x*squareSize + posX, y*squareSize + posY, squareSize, squareSize, this);
            g2d.drawImage(sprites.get(terrain.objectOnCase(part.getXPos(),part.getYPos()) + frameNo), part.getXPos()*squareSize + posX, part.getYPos()*squareSize + posY, squareSize, squareSize, this);
        }
    }

    // Methode qui raccourci le chargement des sprites
    // et qui le fait en toute sécurité
    public BufferedImage loadImage(String file){
        try{
            return ImageIO.read(System.class.getResourceAsStream(file));
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }


    // Charge automatiquement les sprites de serpent dans la hashmap
    public void loadSnakeSprites(String file){
        BufferedImage img = loadImage(file);

        // Cette partie du code est vraiment dégueulasse (à refaire si possible)
        char[] cards = {'N', 'E', 'S', 'W'};
        int rot = 0;
        for(char n : cards){
            for (int i = 1; i < 5; i++) {
                sprites.put("snake_" + fileName(file) + "_tete" + n + i, rotate(img.getSubimage(0,4*i,16,16), rot));
                sprites.put("snake_" + fileName(file) + "_cou" + n + i, rotate(img.getSubimage(0,16+4*i,16,16), rot));
                sprites.put("snake_" + fileName(file) + "_corp" + n + i, rotate(img.getSubimage(0,32+4*i,16,16), rot));
                sprites.put("snake_" + fileName(file) + "_arriere" + n + i, rotate(img.getSubimage(0,48+4*i,16,16), rot));
                sprites.put("snake_" + fileName(file) + "_queue" + n + i, rotate(img.getSubimage(0,64+4*i,16,16), rot));
                sprites.put("snake_" + fileName(file) + "_coude" + n + cards[(rot/90+3)%4] + i, rotate(img.getSubimage(0,80+16*i,16,16), rot));
                sprites.put("snake_" + fileName(file) + "_coude" + cards[(rot/90+3)%4] + n + i, rotate((img.getSubimage(0,80+16*i,16,16)), rot));
                //System.out.println("snake_" + fileName(file) + "_coude" + n + cards[(rot/90+3)%4] + i);
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


    // Retourne le nom du fichier du chemin sans extension
    private String fileName(String path){
        return path.toString().substring(path.toString().lastIndexOf('/') + 1, path.toString().lastIndexOf('.'));
    }

    private String removeExt(String str){
        return str.toString().substring(0, str.toString().lastIndexOf('.'));
    }

    // Parcour un repertoire à la recherche de sprites ////////////////////////////
    private ArrayList<String> searchSprite(String path) throws IOException {

        ArrayList<String> filenames = new ArrayList<>();
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        if(jarFile.isFile()) {  // Run with JAR file
            final JarFile jar = new JarFile(jarFile);
            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            while(entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if(("/" + name).startsWith(path) && name.endsWith(".png")) { //filter according to the path
                    filenames.add("/" + name);                }
            }
            jar.close();
        }
        else { // Run in IDE

            try (
                InputStream in = getResourceAsStream(path);
                BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                String resource;

                while ((resource = br.readLine()) != null) {
                    if (resource.endsWith(".png"))
                        filenames.add(path + resource);
                }
            }
        }
        return filenames;
    }

    private InputStream getResourceAsStream(String resource) {
        final InputStream in
                = getContextClassLoader().getResourceAsStream(resource);

        return in == null ? getClass().getResourceAsStream(resource) : in;
    }

    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    //////////////////////////////////////////////////////////


    public Graphics2D getGraph(){
        return (Graphics2D) getGraphics();
    }

    public BufferedImage getSprite(String nom) {
        return sprites.get(nom);
    }
}