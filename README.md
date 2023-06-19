# Sudoku Solver

A short Java application programed to visualize backtracking sudoku grid solving algorithms as well as sudoku grid generation.

The program generates sudoku grids by randomly filling 3 diagonal subgrids. If two sudoku subgrids that are both diagonal to eachother and not horizontal or vertical, the subrgrids are independant to eachother.
This means that in a standard sudoku grid, we can fill up to 3 diagonal subgrids without any risk of conflicting numbers. The next step is to solve the rest of the sudoku board using random number order backtracking.
If we try to solve the rest of the sudoku board using standard backtracking, the solution will have a numerical order bias due to the fact that there are multiple possible solutions to solving the rest of the board
and the standard sudoku backtracking algorithm tries numbers in numerical order. Therefore, we must use a version of the backtracking algorithm that tries remaining numbers in random order. This version in theory 
would have less consistant solving times than standard backtracking but is necessary for generating truely random sudoku boards. Finally, numbers from the board are randomly removed from the grid to create empty 
spaces, finishing the sudoku board.
