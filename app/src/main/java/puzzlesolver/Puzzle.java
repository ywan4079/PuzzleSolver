package puzzlesolver;

public class Puzzle {

    public String label;
    public boolean[][] shape;
    public String color;
    public int area;

    public Puzzle(String label, String shape, String color) throws Exception {
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
    }
}