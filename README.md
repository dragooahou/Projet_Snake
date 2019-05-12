# Projet_Snake
<h3>Projet Snake : Chase of Kebabs.</h3> 
Pour lancer le jeu : `javac *.java` <br>
Puis `java StartSnakeGame` <br>
<hr>
Nous avons trouvé la solution des lags de swing lorsque l'on dessine peut de choses à l'écran et que l'on fait aucun input. <br>
L'explication complète est ici : [Low-latency painting in AWT and Swing](https://pavelfatin.com/low-latency-painting-in-awt-and-swing/) <br>

La méthode pour dessiner : <br>
```java
    public void dessiner(){
        //super.paintComponent(g);

        // On recupère la classe Graphics
        Graphics g = getGraphics();
        Graphics2D g2d = (Graphics2D) g;


        // On dessine les différentes parties de notre jeu
        //drawTerrain(g2d);
        drawSnake(g2d);

        // Très important mais je sais pas vraiment à quoi ca sert
        Toolkit.getDefaultToolkit().sync();

        // On libère la classe Graphics
        g.dispose();

    };
```