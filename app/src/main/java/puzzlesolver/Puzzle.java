package puzzlesolver;

import java.awt.Color;

// test done

public class Puzzle {

    public String label;
    public boolean[][] shape;
    public String color;
    public int area;
    public int width;
    public int height;
    public int rotation_times;
    public int flip_times;
    public boolean extra_flip;
    public Color color_hex;

    public Puzzle(String label, String shape, String color, String color_hex) throws Exception {
        this.label = label;
        this.color = color;
        this.area = 0;

        // Split the string by unicode 8232?? cool :)
        String[] shape_string = shape.replaceAll("\"", "").split(String.valueOf((char)8232));

        this.shape = new boolean[shape_string.length][shape_string[0].length()];
        for(int i = 0; i < shape_string.length; i++) {
            for(int j = 0; j < shape_string[i].length(); j++) {
                if(shape_string[i].charAt(j) == '0') {
                    this.shape[i][j] = false;
                } 
                else if(shape_string[i].charAt(j) == '1') {
                    this.shape[i][j] = true;
                    this.area++;
                }
                else{
                    throw new Exception("Puzzle shape format is incorrect");
                }
            }
        }

        try {
            this.color_hex = Color.decode(color_hex);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Color hex format is incorrect!");
        }
        

        this.width = this.shape[0].length;
        this.height = this.shape.length;


        this.flip_times = 2;
        this.rotation_times = 4;
        this.extra_flip = false;

        boolean[][] r90 = this.rotate(90), r180 = this.rotate(180), r270 = this.rotate(270), flip = this.flip();
        this.flip();
        
        if(Solver.Compare_Shape(this.shape, flip)){
            this.flip_times = 1;
        }else if(Solver.Compare_Shape(r90, flip) || Solver.Compare_Shape(r180, flip) || Solver.Compare_Shape(r270, flip)){
            this.flip_times = 1;
            this.extra_flip = true;
        }

        if(Solver.Compare_Shape(this.shape, r90)){
            this.rotation_times = 1;
        }else if(Solver.Compare_Shape(this.shape, r180)){
            this.rotation_times = 2;
        }
    }

    // rotate the puzzle clockwise
    public void rotate(){ 
        int width = this.shape[0].length;
        int height = this.shape.length;
        boolean[][] new_shape = new boolean[width][height];
        for(int i = 0; i < width; i++) {
            for(int j = height-1; j >= 0; j--) {
                new_shape[i][height-j-1] = this.shape[j][i];
            }
        }

        this.shape = new_shape;
        this.width = height;
        this.height = width;
    }

    public boolean[][] rotate(int angle){
        if(angle == 90){
            int width = this.shape[0].length;
            int height = this.shape.length;
            boolean[][] new_shape = new boolean[width][height];
            for(int i = 0; i < width; i++) {
                for(int j = height-1; j >= 0; j--) {
                    new_shape[i][height-j-1] = this.shape[j][i];
                }
            }
            return new_shape;
        } else if (angle == 180){
            int width = this.shape[0].length;
            int height = this.shape.length;
            boolean[][] new_shape = new boolean[height][width];
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    new_shape[i][j] = this.shape[height-i-1][width-j-1];
                }
            }
            return new_shape;
        } else if (angle == 270){ //not yet!!!!!!!!!!!!!!!!!!
            int width = this.shape[0].length;
            int height = this.shape.length;
            boolean[][] new_shape = new boolean[width][height];
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    new_shape[width-j-1][i] = this.shape[i][j];
                }
            }
            return new_shape;
        } else if (angle == 360){  // simply return the same array but different address
            int width = this.shape[0].length;
            int height = this.shape.length;
            boolean[][] new_shape = new boolean[height][width];
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    new_shape[i][j] = this.shape[i][j];
                }
            }
            return new_shape;
        }
        else{
            return null;
        }
    }

    public boolean[][] flip(){
        int width = this.shape[0].length;
        int height = this.shape.length;
        boolean[][] new_shape = new boolean[height][width];
        for(int i = 0; i < height; i++) {
            for(int j = width-1; j >= 0; j--){
                new_shape[i][width-j-1] = this.shape[i][j];
            }
        }
        this.shape = new_shape;
        return new_shape;
    }

    public String toString(){
        String output = "";

        output += "label: " + this.label + "\n";
        output += "color: " + this.color + "\n";
        output += "area: " + this.area + "\n";
        output += "Rotation times: " + this.rotation_times + "\n";
        output += "Flip times: " + this.flip_times + "\n";
        output += "Extra flip: " + this.extra_flip + "\n";
        output += "shape:\n";
        for(boolean[] arr : this.shape){
            for(boolean b : arr) output += b + " ";
            output += "\n";
        }

        return output;
    }
}