
public class Driver {
	public static void main(String[] args) {
		
		System.out.println("doing AStar");
		// set goal state. Note this is hardcoded for 8-tile puzzle, but could
		// be changed for n-tile puzzles
		int[][] goalValues = new int[][]{
			{0, 1, 2},
			{3, 4, 5},
			{6, 7, 8}
		}; // 0 represents a blank

//		int[][] goalValues = new int[][]{
//			{1, 2, 3},
//			{4, 5, 6},
//			{7, 8, 0}
//		}; // 0 represents a blank
//		int[][] testValues = new int[][]{
//			{3, 1, 0},
//			{6, 5, 2},
//			{7, 4, 8}
//		}; // 0 represents a blank
		int[][] testValues = new int[][]{
			{8, 6, 7},
			{2, 5, 4},
			{3, 0, 1}
		};
		
		
		// generate some random start states
		Node n = new Node(testValues);
		
		AStar search = new AStar(); 
		
		
		
		// determine heuristic.
		int heuristic = 1; 
		// use value 1 for Manhattan distance, 2 for misplaced tiles, and 3 for
		// other
		
		long startTime = System.currentTimeMillis();
		search.search(n, goalValues, heuristic);
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println("time taken was " + elapsedTime + " milliseconds");
		
	}
}
