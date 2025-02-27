# PuzzleSolver
Given the size of puzzle frame(rectangle) and the shape of puzzles. This program calculates the number of distinct solutions and exports the solutions as images(.png).

Follow these instructions to run the program correctly:
1. Define the Frame Size: 
    	 - Open frame_size.txt 
	 - Enter the dimensions of the puzzle frame, separated by a space.
	 - For example, if the frame size is 3 x 4. Please enter "3 4".
2. Provide Puzzle Information
	 - Open puzzles.csv
	 - Enter the puzzle details including label, shape, color name, and the code of colors in hex_code. 
	 - Examples are given in puzzles.csv, modify the entries to match your puzzle pieces.
3. Run the Program
	 - Navigate to program directory.
	 - Execute the command `gradle run --console=plain`.
4. Results
	 - The number of solutions will be displayed on the terminal.
	 - You will be asked if you want to save the solution images, and they will be saved in `solutions` directory.
