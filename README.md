# Projet_Snake

### Pour jouer
Compiler le jeu avec `./build.sh`  
Pour lancer le jeu : `cd build` puis `java Snake.StartSnakeGame`

--------------------------------------------

### Le problème du lag
Nous avons trouvé la solution aux lags de swing lorsque l'on dessine peu de choses à l'écran et que l'on ne fait aucun input.
L'explication complète est ici : https://pavelfatin.com/low-latency-painting-in-awt-and-swing/

La méthode pour dessiner :
```java
public void dessiner(){
    //Pour rafraichir le fond
    //super.paintComponent(g);

    // On recupère la classe Graphics
    Graphics g = getGraphics();
    Graphics2D g2d = (Graphics2D) g;


    // On dessine les différentes parties de notre jeu
    //drawTerrain(g2d);
    drawSnake(g2d);

    // Très important
    Toolkit.getDefaultToolkit().sync();

    // On libère la classe Graphics
    g.dispose();

}
```