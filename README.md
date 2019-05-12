# Projet_Snake

### Pour jouer
Pour lancer le jeu : `javac *.java`
Puis `java StartSnakeGame`

### Le problème du lag
Nous avons trouvé la solution aux lags de swing lorsque l'on dessine peut de choses à l'écran et que l'on fait aucun input.
L'explication complète est ici : https://pavelfatin.com/low-latency-painting-in-awt-and-swing/

La méthode pour dessiner :
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