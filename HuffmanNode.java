/* Kalvin Suting
 * 3/7/2016
 * Assignment 8
 * Grace Chen
 * This class creates a HuffmanNode to be used as nodes for the HuffmanTree class. 
 */

public class HuffmanNode implements Comparable<HuffmanNode> {
	public HuffmanNode left;
	public HuffmanNode right;
	public int frequency; //counts of and individual char
	public int index; 	//its index in the array (its char value)
	
	
   //post: constructs a leaf node to be used in the HuffmanTree.
   public HuffmanNode(int frequency, int index){
		this.frequency = frequency;
		this.index = index;
	}	
	
   //post: constructs a "branch" to be used in the HuffmanTree 
   public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right){
	   this.frequency = frequency;
	   this.left = left;
	   this.right = right;
   }
   	
   //post: compares the nodes based on frequency in a traditional sense. Higher 
   //	   frequencies > lower frequencies.
   public int compareTo(HuffmanNode other) {
	   return this.frequency - other.frequency;
		}
	
   //post: returns whether or not the node is a leaf node. 
   public boolean isLeaf(){
	   return this.left == null && this.right  == null;
	}
}

