## Java 8-Puzzle Solver

This program solves the 8-puzzle problem with the following algorithms: 

- A* search (complete an optimal)
- Hill climbing
	- Steepest Ascent (not complete)
	- First-choice (not complete)
	- Random restart (not optimal)

And uses the following admissible heuristics: 

- Manhattan Distance
- Hamming Distance
- Misplaced Columns and Rows

## Representation

A puzzle is a 2D int array. The goal state looks like this
:
	{8, 7, 6}

	{5, 4, 3}

	{2, 1, 0}

