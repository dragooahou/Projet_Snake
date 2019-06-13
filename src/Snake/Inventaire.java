package Snake;

import java.util.Arrays;

public class Inventaire {

    private String[] ingredients = new String[4];

    public Inventaire(){
        init();
    }

    public void init(){
        for(int i = 0; i < ingredients.length; i++) ingredients[i] = null;
    }

    public boolean add(String ing){
        if(!ListeFruits.isFruit(ing)) return false;

        switch(ing){

            case "Sprite_Salad1":
            case "Sprite_Salad2":
                if(ingredients[0] != null){
                    init();
                    return false;
                }
                ingredients[0] = ing;
                return true;

            case "Sprite_Meat1":
            case "Sprite_Meat2":
                if(checkNull(0) || ingredients[1] != null || !(ingredients[0].equals("Sprite_Salad1") || ingredients[0].equals("Sprite_Salad2"))){
                    init();
                    return false;
                }
                ingredients[1] = ing;
                return true;

            case "Sprite_Tomato1":
            case "Sprite_Tomato2":
                if(checkNull(1) || ingredients[2] != null || !(ingredients[1].equals("Sprite_Meat1") || ingredients[1].equals("Sprite_Meat2"))){
                    init();
                    return false;
                }
                ingredients[2] = ing;
                return true;

            case "Sprite_Onion1":
            case "Sprite_Onion2":
                if(checkNull(2) || ingredients[3] != null || !(ingredients[2].equals("Sprite_Tomato1") || ingredients[2].equals("Sprite_Tomato2"))){
                    init();
                    return false;
                }
                ingredients[3] = ing;
                return true;

        }
        return false;
    }

    public boolean isFini(){
        for (String s : ingredients)
            if(s == null) return false;
        return true;
    }

    public String getState() {
        for(int i = 0; i < ingredients.length; i++){
            if(ingredients[i] == null) return (i + 1) + "";
        }
        return "Final";
    }

    public boolean checkNull(int n){
        for(int i = n; i >= 0; i--) if(ingredients[i] == null) return true;
        return false;
    }

    @Override
    public String toString() {
        return "Inventaire{" +
                "ingredients=" + Arrays.toString(ingredients) +
                '}';
    }
}
