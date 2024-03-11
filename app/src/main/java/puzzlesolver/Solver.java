package puzzlesolver;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Solver {

    public static boolean FitIn(int[][] frame, Puzzle puzzle, int i, int j) { // i = height, j = width
        //Out of bound
        if(i+puzzle.height > frame.length || j+puzzle.width > frame[0].length) return false;

        //Occupied by other puzzles
        for(int h = i; h < i+puzzle.height; h++) {
            for(int w = j; w < j+puzzle.width; w++) {
                if(frame[h][w] == -1 && puzzle.shape[h-i][w-j]) return false;
            }
        }
        return true;
    }

    public static boolean Put(int[][] frame, Puzzle puzzle, int i, int j) {
        if(FitIn(frame, puzzle, i, j)){
            for(int h = i; h < i+puzzle.height; h++) {
                for(int w = j; w < j+puzzle.width; w++) {
                    if(puzzle.shape[h-i][w-j]) frame[h][w] = -1;
                }
            }
            return true;
        }else return false;
    }

    public static void Draw(Color[][] colors, Puzzle puzzle, int i, int j) {
        for(int h = i; h < i+puzzle.height; h++) {
            for(int w = j; w < j+puzzle.width; w++) {
                if(puzzle.shape[h-i][w-j]) colors[h][w] = puzzle.color_hex;
            }
        }
    }

    public static int solve(int WIDTH, int HEIGHT, List<Puzzle> puzzles){
        int solution = 0;

        for(int i = 0; i < puzzles.size(); i++){
            if(puzzles.get(i).rotation_times == 4){
                if(i == 0) break;
                else{
                    Puzzle p = puzzles.get(0);
                    puzzles.set(0, puzzles.get(i));
                    puzzles.set(i, p);
                    break;
                }
            }
        }

        int total_iteration = (HEIGHT - puzzles.get(0).height + 1) * (WIDTH - puzzles.get(0).width + 1), counter = 0;
        System.out.print("Progress: 0%\r");
        for(int i = 0; i <= HEIGHT - puzzles.get(0).height; i++) {
            for(int j = 0; j <= WIDTH - puzzles.get(0).width; j++) {
                //-1 means occupied, other integers mean the possible ways can be covered
                int[][] possibility = new int[HEIGHT][WIDTH]; 
                Color[][] colors = new Color[HEIGHT][WIDTH];
                List<Puzzle> puzzles_copy = new ArrayList<>(puzzles);
                puzzles_copy.remove(0);
                Put(possibility, puzzles.get(0), i, j);
                Draw(colors, puzzles.get(0), i, j);
                Calculate_Possibility(possibility, puzzles_copy);
                
                solution += DFS(possibility, puzzles_copy, colors);

                double progress = (double)(++counter)/total_iteration*100;
                System.out.print("Progress: " + (int)progress + "%\r");
            }
        }
        System.out.println();

        return solution;
    }

    public static int DFS(int[][] possibility, List<Puzzle> puzzles, Color[][] colors) {

        if(puzzles.size() == 0) {
            App.finished_puzzles.add(colors);
            return 1;
        }

        int min = Integer.MAX_VALUE, HEIGHT = possibility.length, WIDTH = possibility[0].length, min_i = 0, min_j = 0;
        for(int i = 0; i < HEIGHT; i++){
            for(int j = 0; j < WIDTH; j++) {
                if(possibility[i][j] == -1) continue;
                if(min > possibility[i][j]) {
                    min = possibility[i][j];
                    min_i = i;
                    min_j = j;
                }

            }
        }

        if(min == 0) return 0;
        
        int solution = 0;

        for(Puzzle p : puzzles) {
            for(int flip = 0; flip < p.flip_times; flip++) {
                for(int rotation = 0; rotation < p.rotation_times; rotation++){
                    int init_i = Math.max(min_i - p.height + 1, 0), init_j = Math.max(min_j - p.width + 1, 0);
                    int end_i = Math.min(min_i, HEIGHT - p.height), end_j = Math.min(min_j, WIDTH - p.width);

                    for(int i = init_i; i <= end_i; i++){
                        for(int j = init_j; j <= end_j; j++){
                            if(FitIn(possibility, p, i, j) && p.shape[min_i-i][min_j-j]){
                                int[][] new_possiblity = Copy_2D_Array(possibility);
                                Color[][] new_colors = Copy_Colors(colors);
                                Clear_Possibility(new_possiblity);
                                Put(new_possiblity, p, i, j);
                                Draw(new_colors, p, i, j);
                                List<Puzzle> new_puzzles = new ArrayList<>(puzzles);
                                new_puzzles.remove(p);
                                Calculate_Possibility(new_possiblity, new_puzzles);
                                solution += DFS(new_possiblity, new_puzzles, new_colors);
                            }
                        }
                    }
                    p.rotate();
                }
                p.flip();
            }
            if(p.extra_flip) p.flip();
        }

        return solution;

    }

    public static Color[][] Copy_Colors(Color[][] colors) {
        Color[][] ret = new Color[colors.length][colors[0].length];
        for(int i = 0; i < colors.length; i++) {
            for(int j = 0; j < colors[0].length; j++) {
                ret[i][j] = colors[i][j];
            }
        }
        return ret;
    }

    public static int[][] Copy_2D_Array(int[][]possibility){
        int[][] ret = new int[possibility.length][possibility[0].length];
        for(int i = 0; i < possibility.length; i++){
            for(int j = 0; j < possibility[0].length; j++){
                ret[i][j] = possibility[i][j];
            }
        }
        return ret;
    }

    public static boolean[][] Copy_Shape(boolean[][] shape){
        boolean[][] new_shape = new boolean[shape.length][shape[0].length];
        for(int i = 0; i < shape.length; i++){
            for(int j = 0; j < shape[0].length; j++) {
                new_shape[i][j] = shape[i][j];
            }
        }
        return new_shape;
    }

    public static void Clear_Possibility(int[][] possibility){
        for(int i = 0; i < possibility.length; i++){
            for(int j = 0; j < possibility[0].length; j++){
                if(possibility[i][j] != -1) possibility[i][j] = 0;
            }
        }
    }

    public static boolean Compare_Shape(boolean[][] shape1, boolean[][] shape2) {
        if(shape1.length != shape2.length || shape1[0].length != shape2[0].length) return false;

        for(int i = 0; i < shape1.length; i++) {
            for(int j = 0; j < shape1[0].length; j++) {
                if(shape1[i][j] != shape2[i][j]) return false;
            }
        }

        return true;
    }

    public static void Calculate_Possibility(int[][] possibility, List<Puzzle> puzzles) {
        int HEIGHT = possibility.length, WIDTH = possibility[0].length;
        for(Puzzle p : puzzles) {
            for(int flip = 0; flip < p.flip_times; flip++){
                for(int rotation = 0; rotation < p.rotation_times; rotation++) {
                    for(int i = 0; i < HEIGHT - p.height + 1; i++) {
                        for(int j = 0; j < WIDTH - p.width + 1; j++) {
                            if(FitIn(possibility, p, i, j)){
                                for(int k = i; k < i+p.height; k++){
                                    for(int l = j; l < j+p.width; l++){
                                        if(p.shape[k-i][l-j]) possibility[k][l]++;
                                    }
                                }
                            }
                        }
                    }
                    p.rotate();
                }
                p.flip();
            }
            if(p.extra_flip) p.flip();
        }
            
    }
}