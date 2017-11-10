/* Kalvin Suting
* 3/7/2016
* Assignment 8
* Grace Chen
* This program creates a tree of coded chars created from the counts of chars from the input
* file. Using counts of each character in the file, the tree is sorted in such a way that the
* The chars are stored in a shorter sequence of bits (codes) based on their frequency in the file.
* The codes are then written to an output file of the user's choice. User then has the option
* of encoding their file into a new file using Encode.Java. Once encoded, user can then use the
* Decode.java to return their file back to an uncompressed state.
*/

import java.io.*;
import java.util.*;

public class HuffmanTree{
	private HuffmanNode overallRoot;
	
	//pre:	must pass in an array of non 0 counts for each specified char. If counts are somehow
	//		less than 0 they will be ignored.
	//post:	constructs a HuffmanTree object based on frequencies of the chars passed in.
	public HuffmanTree(int[] count){	//create a treeNode for each frequency.
		Queue<HuffmanNode> q = new PriorityQueue<HuffmanNode>();
		int index = 0;
		for(int i: count){
			if(i > 0){
				HuffmanNode node = new HuffmanNode(i, index);
				q.add(node);
			}
			index++;
		}
		q.add(new HuffmanNode(1, count.length));	//adds eof character
		makeCodedTree(q);
		overallRoot = q.remove(); //we remove the last node which is the completed tree.
	}
	
	//pre:	must pass in a priority queue of counts of each char.
	//post:	takes counts from constructor above and constructs a tree out of them.
	//		Characters that have a higher frequency will end up on higher levels of
	//		the tree where as lower frequencies will be at the bottom. Each leaf node
	//		contains the counts of the character, and each branch node contains the sum
	//		of its left and right nodes.
	private void makeCodedTree(Queue<HuffmanNode> q){
		while(q.size() != 1){		//we stop after 1 node left because this means tree complete.
			HuffmanNode first = q.remove();
			HuffmanNode second = q.remove();
			int sum = first.frequency + second.frequency;
			HuffmanNode newNode = new HuffmanNode(sum, first, second);
			q.add(newNode);
		}
	}
	
	//pre:	must pass in an valid input file in standard format.
	//post:	constructs a HuffmanTree object from the given input of codes. Old tree
	//		will be replaced.
	public HuffmanTree(Scanner input){
		overallRoot = new HuffmanNode(-1, null, null);
		while(input.hasNextLine()){
			int n = Integer.parseInt(input.nextLine());
			String code = input.nextLine();
			overallRoot = buildNewTree(code, n, overallRoot);
		}
	}
	
	//pre:	must pass in valid code (sequence of 0's and 1's), value, and non null root.
	//post:	performs all necessary actions to create a new tree from the given file.
	//		Assumes the input file is in standard format. Standard format is as follows:
	//		A line containing the value of the character (its index), followed by a single
	//		line containing the code for the character.
	private HuffmanNode buildNewTree(String code, int value, HuffmanNode root){
		if(code.isEmpty()){
			return new HuffmanNode(-1, value);
		}else{
			if(root == null){
				root = new HuffmanNode(-1, null, null);
			}
			char a = code.charAt(0);
			if(a == '0'){
				root.left = buildNewTree(code.substring(1), value, root.left);
			}else{
				root.right = buildNewTree(code.substring(1),value, root.right);
			}
		}
		return root;
	}
	
	//pre:	assumes that input contains legal encoding of characters for Huffman coding.
	//		I.E in standard format, bitInputStream must not be empty overallRoot must not be null.
	//post:	reads individual bits from input and writes the characters to the output file.
	public void decode(BitInputStream input, PrintStream output, int eof){
		HuffmanNode node = overallRoot;
		while(node.index != eof){	//eof char indicates end of file.
			node = overallRoot;	//returns back to top of tree.
			while(!node.isLeaf()){
				int n = input.readBit();	//change variable name
				if(n == 0){
					node = node.left;
				}else{
					node = node.right;
				}
			}
			if(node.index != eof){		//doesn't print eof char to output, only for decoding.
				output.write(node.index);
			}
		}
	}
	
	//pre:	must pass in valid output file to write tree to.
	//post:	write the tree to the given output stream in standard format. Standard format
	//		is as follows: A single line containing the char value of each char, followed by
	//		a line containing that chars code created from the tree.
	public void write(PrintStream output){
		writeHelper(output, overallRoot, "");
	}
	
	//pre:	must pass in valid output file, non null root and string.
	//		performs all necessary actions to write the current tree to the given
	//		output file in standard format.
	private void writeHelper(PrintStream output, HuffmanNode root, String result){
		if(root.isLeaf()){
			output.println(root.index);
			output.println(result);
		}else{
			writeHelper(output, root.left, result + 0);
			writeHelper(output, root.right, result + 1);
		}
	}
}
