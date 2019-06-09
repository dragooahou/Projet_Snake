package Snake;

import java.util.Random;

public class ListeFruits {

    // La liste des fruits
    private static String[] fruits = {"Sprite_Meat2", "Sprite_Onion2", "Sprite_RedApple2", "Sprite_Salad2", "Sprite_Tomato2",
                                      "Sprite_Meat1", "Sprite_Onion1", "Sprite_RedApple1", "Sprite_Salad1", "Sprite_Tomato1"
                                     };

    // Renvoi un fruit aléatoire
    static String randomFruit() {
        return fruits[new Random().nextInt(fruits.length)];
    }

    // Si le string donné est un fruit
    static boolean isFruit(String fruit){
        if(fruit.equals("")) return false;
        for (String f : fruits)
            if (f.equals(fruit)) return true;
        return false;
    }


    // Getters et setters
    public static String[] getFruits() {
        return fruits;
    }

    public static void setFruits(String[] fruits) {
        ListeFruits.fruits = fruits;
    }
}
