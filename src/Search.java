import java.util.*;

public class Search {

	HashMap goalState;

	public int AStar(Node initial, int[][] goal, int heuristic) {
		boolean notSolved = true;
		goalState = new HashMap();
		int repeated = 0;
		HashSet<String> visited = new HashSet<String>();
		HashSet<String> queued = new HashSet<String>();
		PriorityQueue<Node> pending = new PriorityQueue<Node>(new Comparator<Node>() {
			public int compare(Node a, Node b) {
				if (a.getF() > b.getF()) {
					return 1;
				} else if (a.getF() < b.getF()) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		initial.setF(getHeuristic(initial, goal, heuristic));
		pending.add(initial);
		queued.add(initial.getKey());
		while (!pending.isEmpty() && notSolved) {
			Node current = pending.poll();
			visited.add(current.getKey());
			if (isEqual(current.getValues(), goal)) {
				notSolved = false;
				int moves = reconstructPath(current);
				//comment out these next three lines if you're comparing A* to Hill Climbing
//				System.out.println("this took " + moves + " moves to solve.");
//				System.out.println("there were " + repeated + " nodes skipped");
//				System.out.println("number of nodes expanded were " + visited.size());
				return visited.size();
			} else {
				current.generateChildren();
				// go through each child of current node
				for (Node currentChild : current.getChildren()) {
					if (visited.contains(currentChild.getKey())) {
						repeated++;
						continue;
					}
					int tempg = current.getG() + 1;// the edge cost between
													// nodes is always 1 in the
													// 8-puzzle problem
					currentChild.setG(tempg);
					int tempf = getHeuristic(currentChild, goal, heuristic) + tempg;
					currentChild.setF(tempf);

					//don't add it if it's already there
					if (!queued.contains(currentChild.getKey())) {
						currentChild.setParent(current);
						currentChild.setF(tempf);
						pending.add(currentChild);
					} else {
						repeated++;
					}
				}
			}
		}
		return 0;
	}

	// local searches
	
	//returns an int array that contains number of nodes generated, number of steps made, and 0 for solution or 1 for dead end
	public int[] steepestAscent(Node initial, int[][] goal, int heuristic) {
		goalState = new HashMap();
		Node current = initial;
		current.setF(getHeuristic(initial, goal, heuristic));
		boolean solution = false, deadEnd = false;
		int steps = 0, nodes = 0, sol = 1, ded = 0;
		while (!solution && !deadEnd) {
			if(isEqual(current.getValues(), goal)){
				solution = true; 
				continue; 
			}
			steps++;
			current.generateChildren();
			Node best = current;
			deadEnd = true;//will be reset if best changes
			for (Node currentChild : current.getChildren()) {
				nodes++;
				currentChild.setF(getHeuristic(currentChild, goal, heuristic));
				if (best.getF() >= currentChild.getF()) {
					best = currentChild;
					deadEnd = false;
					best.setParent(current);
				}
			}
			current = best;
		}
		if(solution){
			return new int[]{steps, nodes, sol};
		}
		else if(deadEnd){
			return new int[]{steps, nodes, ded};
		}
		else{//this should never happen if input is valid
			return new int[]{0,0,0};
		}
	}

	public int[] firstChoice(Node initial, int[][] goal, int heuristic){
		goalState = new HashMap();
		Node current = initial;
		current.setF(getHeuristic(initial, goal, heuristic));
		boolean solution = false, deadEnd = false;
		int steps = 0, nodes = 0, sol = 1, ded = 0;
		while (!solution && !deadEnd) {
			if(isEqual(current.getValues(), goal)){
				solution = true; 
				continue; 
			}
			steps++;
			current.generateChildren();
			Node best = current;
			deadEnd = true;//will be reset if best changes
			for (Node currentChild : current.getChildren()) {
				nodes++;
				currentChild.setF(getHeuristic(currentChild, goal, heuristic));
				if (best.getF() > currentChild.getF()){
					best = currentChild;
					deadEnd = false;
					best.setParent(current);
					continue;
				}
			}
			current = best;
		}
		if(solution){
			return new int[]{steps, nodes, sol};
		}
		else if(deadEnd){
			return new int[]{steps, nodes, ded};
		}
		else{//this should never happen if input is valid
			return new int[]{0,0,0};
		}
		
	}
	
	public int[] randomRestart(Node initial, int[][] goal, int heuristic){
		HashSet<String> added = new HashSet<String>(); 
		ArrayList<Node> collection = new ArrayList<Node>();
		Random random = new Random();
		int nodes = 0, steps = 0;
		Node current = initial;
		collection.add(current);
		added.add(current.getKey());
		int[] results = steepestAscent(current, goal, heuristic);
		while(results[2] != 1){
			current.generateChildren();
			for(Node currentChild: current.getChildren()){
				if(!added.contains(currentChild.getKey())){
					added.add(currentChild.getKey());
					collection.add(currentChild);
				}
				else{
					
				}
			}
			current = collection.remove(random.nextInt(collection.size()));
			results = steepestAscent(current, goal, heuristic);
			steps += results[0];
			nodes += results[1];
		}
		return new int[]{steps, nodes, 1};
	}
	private int reconstructPath(Node current) {
		int moves = 0;
		while (current.getParent() != null) {
			moves++;
			current = current.getParent();
		}
		return moves;
	}

	public void printNode(int[][] vals) {
		for (int i = 0; i < vals.length; ++i) {
			System.out.print("{ ");
			for (int j = 0; j < vals[i].length; ++j) {
				System.out.print(vals[i][j] + " ");
			}
			System.out.print("}\n");
		}
		System.out.println();
	}

	public int getHeuristic(Node initial, int[][] goal, int choice) {
		// case 1: Manhattan Distance
		switch (choice) {
		case 1:
			return getManhattan(initial, goal);

		case 2:
			return getMisplacedTiles(initial, goal);

		case 3:
			return getTilesOutOfRowAndColumn(initial, goal);

		default:
			break;

		}
		return 0;
	}

	private boolean isEqual(int[][] vals, int[][] goal) {
		// boolean proceed = false;
		// for(int i = 0; i < goal.length; ++i){
		// for(int j = 0; j < goal[i].length; ++j){
		// if(vals[i][j] == goal[i][j]){
		// proceed = true;
		// }
		// else{
		// return false;
		// }
		// }
		// }
		// return proceed;
		return Arrays.deepEquals(vals, goal);
	}

	// function for determining heuristic value for Manhattan distance
	public int getManhattan(Node initial, int[][] goal) {
		int h = 0;
		int[][] initialValues = new int[0][0];
		initialValues = initial.getValues();
		for (int i = 0; i < initialValues.length; ++i) {
			for (int j = 0; j < initialValues[i].length; ++j) {
				if (initialValues[i][j] != goal[i][j] && initialValues[i][j] != 0) {
					int[] goalPosition = getGoalPositionForVal(initialValues[i][j], goal);
					if (goalPosition != new int[] { -1, -1 }) {
						h += (Math.abs(i - goalPosition[0]) + Math.abs(j - goalPosition[1]));// distance
																								// between
																								// two
																								// points
																								// on
																								// a
																								// plane
						// System.out.println(initialValues[i][j] + "is at index
						// [" + i +"][" + j +"] and must move to index [" +
						// goalPosition[0] + "][" + goalPosition[1] + "]");
					} else {
						System.out.println("error in input values. Solution will not be correct.");
						break;
					}
				}
			}

		}
		// System.out.println("h for this node is " + h);
		return h;
	}

	public int getMisplacedTiles(Node initial, int[][] goal) {
		int h = 0;
		String currentKey = initial.getKey();
		String goalKey = Arrays.deepToString(goal);

		for (int i = 0; i < currentKey.length(); ++i) {
			if (currentKey.charAt(i) != goalKey.charAt(i) && currentKey.charAt(i) != '0') {
				h++;
			}
		}
		// int [][] initialValues = new int[0][0];
		// initialValues = initial.getValues();
		// for(int i = 0; i < initialValues.length; ++i){
		// for(int j = 0; j < initialValues[i].length; ++j){
		// if(initialValues[i][j] != goal[i][j] && initialValues[i][j] != 0){
		// h ++;
		// //System.out.println(initialValues[i][j] + "is at index [" + i +"]["
		// + j +"] and must move to index [" + goalPosition[0] + "][" +
		// goalPosition[1] + "]");
		// }
		// }
		// }
		// System.out.println("h for this node is " + h);
		return h;
	}

	public int getTilesOutOfRowAndColumn(Node initial, int[][] goal) {
		int h = 0;
		int[][] initialValues = new int[0][0];
		initialValues = initial.getValues();

		for (int i = 0; i < initialValues.length; ++i) {
			for (int j = 0; j < initialValues.length; ++j) {
				if (initialValues[i][j] != goal[i][j] && initialValues[i][j] != 0) {
					int[] goalPosition = getGoalPositionForVal(initialValues[i][j], goal);
					if (goalPosition != new int[] { -1, -1 }) {
						if (i != goalPosition[0]) {
							h++;
						}
						if (j != goalPosition[1]) {
							h++;
						}
					}
				}
			}
		}

		return h;
	}

	public int[] getGoalPositionForVal(int val, int[][] goal) {
		if (!goalState.containsKey(val)) {
			for (int i = 0; i < goal.length; ++i) {
				for (int j = 0; j < goal[i].length; ++j) {
					if (goal[i][j] == val) {
						goalState.put(val, new int[] { i, j });
						return new int[] { i, j };
					}
				}
			}
			return new int[] { -1, -1 };
		} else {
			return (int[]) goalState.get(val);
		}
	}
}
