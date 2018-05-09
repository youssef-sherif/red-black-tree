package tree;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

class RedBlackTreeUtility {

	/*
	 * 0 if they are equal 1 if word1 is greater 2 if word2 is greater
	 */
	public static int compare(String word1, String word2) {
		
		byte[] bytes1 = word1.getBytes();
		byte[] bytes2 = word2.getBytes();
		
		
		return Arrays.compare(bytes1, bytes2);			
	}
	
	
	public static void printInOrder(Node node) {
		if (node != null) {
			printInOrder(node.getLeftChild());
			System.out.println("\t" + node.getWord() + "  " + (node.isBlack() ? "BLACK" : "RED"));
			printInOrder(node.getRightChild());
		}
	}
	

	public static void printAllNodes(Node node) {
		if (node == null)
			return;

		Queue<Node> q = new LinkedBlockingQueue<>();
		q.add(node);

		while (!q.isEmpty()) {
			Node temp = q.peek();
			System.out.print("\tNode: { Word: " + temp.getWord() + ", Colour: " + (temp.isBlack() ? "BLACK" : "RED"));
			if (temp.getParent() != null) {
				System.out.print(", Parent: " + temp.getParent().getWord() + " }  ");
			} else {
				System.out.print(", ROOT }");
			}
			q.remove();

			if (temp.hasLeftChild()) {
				q.add(temp.getLeftChild());
			}
			if (temp.hasRightChild()) {
				q.add(temp.getRightChild());
			}
			System.out.println();
		}
	}
	
}
