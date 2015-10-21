import java.util.*;
public class Node {
	
	public int g;
	public int f;
	public int[][] values;
	public LinkedList<Node> children; 
	public Node parent; 
	public String lastMove;
	
	public Node(int[][] vals){
		g = 0; 
		f = 0;
		values = vals;
		children = new LinkedList<Node>();
		parent = null; 
		lastMove = "";
;
	}
	
	public int[][] getValues(){
		int[][] vals = new int[values.length][];
		//copy vals array and return it
		for(int i = 0; i < vals.length; ++i){
			vals[i] = Arrays.copyOf(values[i], values[i].length);
		}
		return vals; 
	}
	public int getG(){
		return this.g;
	}
	
	public int getF(){
		return this.f;
	}
	public void setLastMove(String m){
		this.lastMove = m;
	}
	public String getLastMove(){
		return this.lastMove;
	}
	public void setG(int g){
		this.g = g;
	}
	
	public void setF(int f){
		this.f = f;
	}
	public void setParent(Node p){
		this.parent = p;
	}
	public Node getParent(){
		return this.parent;
	}
	public LinkedList<Node> getChildren(){
		return this.children;
	}
	public String getKey(){
		return Arrays.deepToString(getValues());

	}
	public int generateChildren(){
		int count = 0;
		//need to make this more efficient 
		for(int i = 0; i < values.length; ++i){
			for(int j = 0; j < values[i].length; ++j){
				//check to see if current iteration is 0 (blank space), and if it is, make sure the moves in each direction are valid
				//if the move is valid, add a new child node
				if(values[i][j] == 0){

					//check move up
					if(i-1 >= 0 ){
						int[][] tempVals =  getValues();
						tempVals[i][j] = getValues()[i - 1][j];
						tempVals[i - 1][j] = 0;
						children.add(new Node(tempVals));
						count++;
					}
					//check move down
					if(i+1 < values.length){
						int[][] tempVals = getValues();
						tempVals[i][j] = getValues()[i+1][j]; 
						tempVals[i + 1][j] = 0;
						children.add(new Node(tempVals));
						count++;
					}
					//check move to the right
					if(j+1 < values[i].length ){
						int[][] tempVals = getValues();
						tempVals[i][j] = getValues()[i][j+1];
						tempVals[i][j + 1] = 0;
						children.add(new Node(tempVals));
						count++;
					}
					//check move to the left
					if(j-1 >= 0){
						int[][] tempVals =  getValues();
						tempVals[i][j] = getValues()[i][j - 1];
						tempVals[i][j - 1] = 0;
						children.add(new Node(tempVals));
						count++;
					}
					
				}
				
			}
		}
		return count;
	}
}
