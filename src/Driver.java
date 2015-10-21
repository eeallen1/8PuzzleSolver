import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class Driver {
	public static void main(String[] args) {

		// "final" state for an 8-tile puzzle
		int[][] goalValues = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } }; // 0
																					// represents
																					// a
																					// blank

		// instantiate new "search" object. It has all the good stuff in it
		Search search = new Search();

		// define heuristic representations
		int manhattan = 1;
		int misplacedTiles = 2;
		int tilesForRowAndColumn = 3;
		// use value 1 for Manhattan distance, 2 for misplaced tiles, and 3 for
		// tiles out of row and column

		int numMoves = 100;// number of moves to make from
		// goal to generate
		// random puzzle

		// scanner
		Scanner scan = new Scanner(System.in);
		boolean err = true;
		// ask console to use A* or local search
		do {
			try {
				System.out.println("Select an option:\n1: A*\n2:Local Search");
				int choice = scan.nextInt();
				if (choice == 1) {
					// **************************************************************************************
					// ********************** perform A* with each heuristic
					// ********************************
					// *************************************************************************************

					// ask console how many random nodes to search for A*
					System.out.println("How many random nodes would you like to solve with A*?\n");
					do {
						try {

							choice = scan.nextInt();
							err = false;
							// call the method to generate random start state

							for (int i = 0; i < choice; ++i) {

								int[][] testValues = randomStartState(
										new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } }, numMoves);
								System.out.println(
										"-------------------------------------------------------------------------\n"
												+ "Solving puzzle " + (i + 1) + " of " + choice + ":\n"
												+ "-------------------------------------------------------------------------\n");
								// generate some random start states
								Node n = new Node(testValues);
								// print initial state of each puzzle and the
								// goal
								System.out.println("Solving the following 8-tile Puzzle:");
								search.printNode(testValues);
								System.out.println("With a goal configuration of:");
								search.printNode(goalValues);

								System.out.println(
										"*************************************************************************\n"
												+ "Performing A* with Manhattan Distance heuristic:\n"
												+ "*************************************************************************");
								long startTime = System.currentTimeMillis();
								search.AStar(n, goalValues, manhattan);
								long elapsedTime = System.currentTimeMillis() - startTime;
								System.out.println("time taken for Manhattan Distance heuristic was " + elapsedTime
										+ " milliseconds\n");

								System.out.println(
										"*************************************************************************\n"
												+ "Performing A* with Misplaced Tiles heuristic:\n"
												+ "*************************************************************************");
								startTime = System.currentTimeMillis();
								search.AStar(n, goalValues, misplacedTiles);
								elapsedTime = System.currentTimeMillis() - startTime;
								System.out.println("time taken for Misplaced Tiles heuristic was " + elapsedTime
										+ " milliseconds\n");

								System.out.println(
										"*************************************************************************\n"
												+ "Performing A* with Tiles for Row and Column heuristic:\n"
												+ "*************************************************************************");
								startTime = System.currentTimeMillis();
								search.AStar(n, goalValues, tilesForRowAndColumn);
								elapsedTime = System.currentTimeMillis() - startTime;
								System.out.println("time taken for Tiles for Row and Column heuristic was "
										+ elapsedTime + " milliseconds\n");
							} // end for loop
						} catch (Exception e) {
							System.out.println("Pick a real number");
							scan.next();
						}
					} while (err);
				} else if (choice == 2) {
					// ask how many 8-puzzles to try
					System.out.println("How many random 8-puzzles do you want to try with Local Search?");
					do {
						try {
							choice = scan.nextInt();
							err = false;
							int steps = 0, nodes = 0, solutions = 0;
							ArrayList<Node> nodeList = new ArrayList<Node>();

							for (int i = 0; i < choice; ++i) {
								// generate some random start states
								nodeList.add(new Node(randomStartState(
										new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } }, numMoves)));

							}

							// hill climbing with steepest ascent
							long startTime = System.currentTimeMillis();
							for (int i = 0; i < nodeList.size(); ++i) {
								int results[] = search.steepestAscent(nodeList.get(i), goalValues, manhattan);
								steps += results[0];
								nodes += results[1];
								solutions += results[2];
							}
							long elapsedTime = System.currentTimeMillis() - startTime;

							// print results
							System.out.println(
									"*************************************************************************\n"
											+ "Results for Steepest Ascent Hill Climbing\n"
											+ "*************************************************************************");

							System.out.println("For " + choice + " random nodes:\n" + "Time elapsed: " + elapsedTime
									+ " milliseconds\n" + "Generated an average of " + ((double)nodes / choice) + " nodes for each attempt\n"
									+ "Made an average of " + ((double)steps / choice) + " steps from initial state for each attempt\n"
									+ "Found a solution in " + solutions + "/" + choice + " puzzles");

							// hill climbing with first-choice
							
							// reset counters
							steps = 0;
							nodes = 0;
							solutions = 0;
							startTime = System.currentTimeMillis();
							for (int i = 0; i < nodeList.size(); ++i) {
								int results[] = search.firstChoice(nodeList.get(i), goalValues, manhattan);
								steps += results[0];
								nodes += results[1];
								solutions += results[2];
							}
							elapsedTime = System.currentTimeMillis() - startTime;
							
							System.out.println(
									"\n*************************************************************************\n"
											+ "Results for First-Choice Hill Climbing \n"
											+ "*************************************************************************");
							
							System.out.println(
									"For "+ choice + " random nodes:\n"
											+ "Time elapsed: " + elapsedTime + " milliseconds\n"
													+ "Generated an average of " + ((double)nodes/choice) + " nodes for each attempt\n"
															+ "Made an average of " + ((double)steps/choice) + " steps from initial state for each attempt\n"
															+ "Found a solution in " + solutions + "/" + choice +" puzzles");
					
							// hill climbing with random restart
							steps = 0;
							nodes = 0;
							solutions = 0;// reset counters
							
							startTime = System.currentTimeMillis();
							for (int i = 0; i < nodeList.size(); ++i) {
								int results[] = search.randomRestart(nodeList.get(i), goalValues, manhattan);
								steps += results[0];
								nodes += results[1];
								solutions += results[2];
							}
							elapsedTime = System.currentTimeMillis() - startTime;
							
							System.out.println(
									"\n*************************************************************************\n"
											+ "Results for Random Restart Hill Climbing \n"
											+ "*************************************************************************");
							
							System.out.println(
									"For "+ choice + " random nodes:\n"
											+ "Time elapsed: " + elapsedTime + " milliseconds\n"
													+ "Generated an average of " + ((double)nodes/choice) + " nodes for each solution\n"
															+ "Made an average of " + ((double)steps/choice) + " steps for each solution\n"
															+ "Found a solution in " + solutions + "/" + choice +" puzzles");
					
							startTime = System.currentTimeMillis();
							nodes = 0;
							for (int i = 0; i < nodeList.size(); ++i) {
								nodes += search.AStar(nodeList.get(i), goalValues, manhattan);
							}
							elapsedTime = System.currentTimeMillis() - startTime;
							
							System.out.println("\n**For contrast, A* on the same nodes took\n"
									 + elapsedTime + " miliseconds\n"
									 		+ " and expanded an average of " + (double)nodes/choice + " nodes");
						} catch (InputMismatchException e) {
							System.out.println("That's not gonna work, buddy");
							scan.next();
						}
					} while (err);
				} else {
					System.out.println("Pick a real option\n");
				}
			} catch (InputMismatchException e) {
				System.out.println("Stop inputting garbage\n");
				scan.next();
			}
		} while (err);

		//
	}

	//this method takes a goal and a number of moves to take, and makes that many random moves
	public static int[][] randomStartState(int[][] goal, int numMoves) {
		Random rand = new Random();
		int validMoves = 0;
		while (validMoves < numMoves) {
			int move = rand.nextInt(4);// 0 = up, 1 = down, 2 = left, 3 = right
			for (int i = 0; i < goal.length; ++i) {
				for (int j = 0; j < goal[i].length; ++j) {
					if (goal[i][j] == 0) {
						if (move == 0) {// move up
							if (i - 1 >= 0) {
								int tempVal = goal[i][j];
								goal[i][j] = goal[i - 1][j];
								goal[i - 1][j] = tempVal;
								validMoves++;
							}
						} else if (move == 1) {// down
							if (i + 1 < goal.length) {
								int tempVal = goal[i][j];
								goal[i][j] = goal[i + 1][j];
								goal[i + 1][j] = tempVal;
								validMoves++;
							}
						} else if (move == 2) {// left
							if (j - 1 >= 0) {
								int tempVal = goal[i][j];
								goal[i][j] = goal[i][j - 1];
								goal[i][j - 1] = tempVal;
								validMoves++;
							}
						} else if (move == 3) {// right
							if (j + 1 < goal[0].length) {
								int tempVal = goal[i][j];
								goal[i][j] = goal[i][j + 1];
								goal[i][j + 1] = tempVal;
								validMoves++;
							}
						} else {
							System.out.println("something busted");
						}
					}
				}
			}
		}
		return goal;
	}
}
