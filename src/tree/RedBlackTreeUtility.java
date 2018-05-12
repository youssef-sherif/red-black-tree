package tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RedBlackTreeUtility {

	public static boolean bothChildrenRed(Node node) {
		if(node.getLeftChild() == null || node.getRightChild() == null) {
			return false;
		}
		return (node.getLeftChild().isRed() && node.getRightChild().isRed());

	}
	
	public static boolean hasChildRed(Node node) {
		if(node.getLeftChild() == null && node.getRightChild() == null) { 
			return false;			
		} else if(node.getLeftChild().isBlack() && node.getRightChild().isBlack()) {
			return false;
		}
		return true;
	}
	
	public static boolean bothChildrenBlack(Node node) {
		if(node.getLeftChild() == null && node.getRightChild() == null) {
			return true;
		} else  {
			if(node.getLeftChild().isBlack() && node.getRightChild().isBlack()) {
				return true;
			}
		}
		return false;
	}

	public static Node maxValue(Node root) {
		Node maxValue = root;
		while (root.getRightChild() != null) {
			maxValue = root.getRightChild();
			root = root.getRightChild();
		}
		return maxValue;
	}

	/*
	 * 0 if they are equal 1 if word1 is greater 2 if word2 is greater
	 */
	public static int compare(String word1, String word2) {

		byte[] bytes1 = word1.getBytes();
		byte[] bytes2 = word2.getBytes();

		return Arrays.compare(bytes1, bytes2);
	}

	public static void print(Node root) {
		System.out.println(String.format("%100s", "").replaceAll(" ", "-"));
		printAllNodes(root);
		System.out.println(String.format("\n%100s", "").replaceAll(" ", "-"));
		printInOrder(root);
		System.out.println(String.format("\n%100s", "").replaceAll(" ", "-"));
	}

	public static void printInOrder(Node node) {
		if (node != null) {
			printInOrder(node.getLeftChild());
			System.out.println("\t" + node.getWord() + "  " + (node.isBlack() ? "BLACK" : "RED")
					+ (node.isDoubleBlack() ? " BLACK" : ""));
			printInOrder(node.getRightChild());
		}
	}

	public static void printAllNodes(Node node) {
		if (node == null)
			return;

		List<Node> nodesList = new ArrayList<>();

		Queue<Node> q = new LinkedList<>();
		q.add(node);

		while (!q.isEmpty()) {
			Node temp = q.remove();
			System.out.print("\tNode: { Word: " + temp.getWord() + (temp.isBlack() ? " BLACK" : " RED")
					+ (temp.isDoubleBlack() ? " BLACK" : ""));
			if (temp.getParent() != null) {
				System.out.print(", Parent: " + temp.getParent().getWord() + " }  ");
			} else {
				System.out.print(", ROOT }");
			}

			if (temp.getLeftChild() != null) {
				q.add(temp.getLeftChild());
			}
			if (temp.getRightChild() != null) {
				q.add(temp.getRightChild());
			}
			System.out.println();
		}

	}

}
