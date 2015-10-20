import java.util.*;
public class Node {
	
	public int g;
	public int f;
	public int[][] values;
	public LinkedList<Node> children; 
	public Node parent; 
	
	public Node(int[][] vals){
		g = 0; 
		f = 0;
		values = vals;
		children = new LinkedList<Node>();
		parent = null; 
//		for(int i = 0; i < values.length; ++i){
//			for(int j = 0; j < values[i].length; ++j){
//				System.out.print("[" + i + "][" +j +"] =" + values[i][j] + ", ");
//			}
//		}
//		System.out.println();
	}
	
	public int[][] getValues(){
		int[][] vals = new int[this.values.length][this.values[0].length];
		for(int i = 0; i < this.values.length; ++i){
			for(int j = 0; j < this.values[i].length; ++j){
				vals[i][j] = this.values[i][j];
			}
		}
		return vals;
	}
	public int getG(){
		return this.g;
	}
	
	public int getF(){
		return this.f;
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
	public void generateChildren(){
		for(int i = 0; i < values.length; ++i){
			for(int j = 0; j < values[i].length; ++j){
				//System.out.print("[" + i + "][" +j +"] =" + values[i][j] + ", ");
			
				//check to see if current iteration is 0 (blank space), and if it is, make sure the moves in each direction are valid
				//if the move is valid, add a new child node
				if(values[i][j] == 0){
					//System.out.println("found 0 at index ["+ i + "][" + j + "]");
					//check move to the right
					if(j+1 < values[i].length ){
						int[][] tempVals = new int[values.length][values[i].length];
						tempVals = getValues();
						tempVals[i][j] = getValues()[i][j+1];
						tempVals[i][j + 1] = 0;
						children.add(new Node(tempVals));
					}
					//check move to the left
					if(j-1 >= 0){
						int[][] tempVals = new int[values.length][values[i].length];
						tempVals = getValues();
						tempVals[i][j] = getValues()[i][j - 1];
						tempVals[i][j - 1] = 0;
						children.add(new Node(tempVals));
					}
					//check move up
					if(i-1 >= 0){
						int[][] tempVals = new int[values.length][values[i].length];
						tempVals = getValues();
						tempVals[i][j] = getValues()[i - 1][j];
						tempVals[i - 1][j] = 0;
						children.add(new Node(tempVals));
					}
					//check move down
					if(i+1 < values.length){
						int[][] tempVals = new int[values.length][values[i].length];
						tempVals = getValues();
						tempVals[i][j] = getValues()[i+1][j]; 
						tempVals[i + 1][j] = 0;
						children.add(new Node(tempVals));
					}
				}
				
			}
		}
		//System.out.println("foudn " + count + " children");
	}
}
