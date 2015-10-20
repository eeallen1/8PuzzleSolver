import java.util.*;

public class AStar {

	public void search(Node initial, int[][] goal, int heuristic){
		boolean notSolved = true;
		HashSet<Node> visited = new HashSet<Node>();
		PriorityQueue<Node> pending = new PriorityQueue<Node>(
				new Comparator<Node>(){
					public int compare(Node a, Node b){
						if (a.getF() > b.getF()){
							return 1;
						}
						else if (a.getF() < b.getF()){
							return -1;
						}
						else{
							return 0;
						}
					}
				});
		initial.f = getHeuristic(initial, goal, heuristic);
		pending.add(initial); 
		while (!pending.isEmpty() && notSolved){
			System.out.println(pending.size());
			Node current = pending.poll();
			if (isGoal(current.getValues(), goal)) {
				notSolved = false;
				visited.add(current);
				
				int moves = reconstructPath(current); 
				//printNode(initial.getValues());
				System.out.println("this took " + moves +" moves to solve.");
				continue;
			}
			else{
				visited.add(current); 
				//System.out.println("added to visited: " + current.values.toString());
				current.generateChildren();
				//System.out.println("finished getting children");
				//go through each child of current node
				for(Node currentChild : current.getChildren()){
					//int tempg = current.getG() + 1;//the edge cost between nodes is always 1 in the 8-puzzle problem
					int tempf = getHeuristic(currentChild, goal, heuristic);// + tempg;
					if(visited.contains(currentChild) && tempf >= currentChild.getF()){
						//System.out.println("repeating visited child");
						continue;  
					}
					
					
					
					if(tempf < currentChild.getF() || !pending.contains(currentChild)){
						currentChild.setParent(current);
						//currentChild.setG(tempg); 
						currentChild.setF(tempf);
						
						if(pending.contains(currentChild)){
							pending.remove(currentChild);
						}
						
						if (!(pending.contains(currentChild))){
							//System.out.println("added a child to pending");
							pending.add(currentChild); 
						}
					}
				}
			}
		}
	}
	
	private int reconstructPath(Node current) {
		int moves = 0; 
		printNode(current.getValues());
		while(current.getParent() !=null){
			moves++;
			printNode(current.getParent().getValues());
			current = current.getParent();
		}
		return moves; 
	}
	
	private void printNode(int[][] vals){
		for(int i = 0; i < vals.length; ++i){
			System.out.print("{ ");
			for(int j = 0; j < vals[i].length; ++j){
				System.out.print(vals[i][j] +" ");
			}
			System.out.print("}\n");
		}
		System.out.println();
	}

	public int getHeuristic(Node initial, int[][] goal, int choice){
		//case 1: Manhattan Distance 
		switch(choice){
		case 1: return getManhattan(initial, goal);
		
		case 2: return getMisplacedTiles(initial, goal);
			
		case 3: break;
			
		
		}
		return 0;
	}
	private boolean isGoal(int[][] vals, int[][] goal){
		boolean proceed = false;
		for(int i = 0; i < goal.length; ++i){
			for(int j = 0; j < goal[i].length; ++j){
				if(vals[i][j] == goal[i][j]){
					proceed = true; 
				}
				else{
					return false;
				}
			}
		}
		return proceed; 
	}
	
	//function for determining heuristic value for Manhattan distance
	public int getManhattan(Node initial, int[][] goal){
		int h = 0; 
		int [][] initialValues = new int[0][0];
		initialValues = initial.getValues();
		for(int i = 0; i < initialValues.length; ++i){
			for(int j = 0; j < initialValues[i].length; ++j){
				if(initialValues[i][j] != goal[i][j] && initialValues[i][j] != 0){
					int[] goalPosition = getGoalPositionForVal(initialValues[i][j], goal);
					if (goalPosition != new int[]{-1, -1}){
						h += (Math.abs(i - goalPosition[0]) + Math.abs(j - goalPosition[1]));//distance between two points on a plane
						//System.out.println(initialValues[i][j] + "is at index [" + i +"][" + j +"] and must move to index [" + goalPosition[0] + "][" + goalPosition[1] + "]");
					}
					else{
						System.out.println("error in input values. Solution will not be correct.");
						break; 
					}
				}
			}
			
		}
		//System.out.println("h for this node is " + h);
		return h; 
	}
	
	public int getMisplacedTiles(Node initial,int[][] goal){
		return 0; 
	}
	
	public int[] getGoalPositionForVal(int val, int[][] goal){
		for(int i = 0; i < goal.length; ++i){
			for(int j = 0; j < goal[i].length; ++j){
				if(goal[i][j] == val){
					return new int[]{i, j};
				}
			}
		}
		return new int[]{-1, -1};
	}
}
