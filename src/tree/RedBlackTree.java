package tree;

import static tree.RedBlackTreeUtility.*;

public class RedBlackTree {

	private Node root;

	public RedBlackTree() {
		root = null;
	}

	public Node getRoot() {
		if (root == null) {
			return null;
		}
		while (root.getParent() != null) {
			root = root.getParent();
		}
		return root;
	}

	public int getSize() {
		return getSize(root);
	}

	private int getSize(Node node) {
		int leftCount = 0;
		int rightCount = 0;
		if (node == null) {
			return 0;
		} else {
			leftCount = getSize(node.getLeftChild());
			rightCount = getSize(node.getRightChild());
		}
		return leftCount + rightCount + 1;
	}

	public boolean Search(String word) {

		return search(root, word);
	}

	public void delete(String word) throws Exception {

		root = delete(root, word);

		System.out.println(String.format("%100s", "").replaceAll(" ", "-"));
		printAllNodes(root);
		System.out.println(String.format("\n%100s", "").replaceAll(" ", "-"));
		printInOrder(root);
		System.out.println(String.format("\n%100s", "").replaceAll(" ", "-"));

		System.out.println("HEIGHT: " + getHeight(root));

	}

	public void insert(String word) throws Exception {

		Node node = new Node(word);

		root = insert(root, node);

		fixInsertViolations(node);

		System.out.println(node.getWord());

		System.out.println(String.format("%100s", "").replaceAll(" ", "-"));
		printAllNodes(root);
		System.out.println(String.format("\n%100s", "").replaceAll(" ", "-"));
		printInOrder(root);
		System.out.println(String.format("\n%100s", "").replaceAll(" ", "-"));

		System.out.println("HEIGHT: " + getHeight(root));
	}

	public String minValue(Node root) {
		String minv = root.getWord();
		while (root.hasLeftChild()) {
			minv = root.getLeftChild().getWord();
			root = root.getLeftChild();
		}
		return minv;
	}

	public int getHeight() {
		return getHeight(root);
	}

	private int getHeight(Node node) {

		if (node == null) {
			return 0;
		} else {
			int leftDepth = getHeight(node.getLeftChild());
			int rightDepth = getHeight(node.getRightChild());

			if (leftDepth > rightDepth) {
				return leftDepth + 1;
			} else {
				return rightDepth + 1;
			}
		}
	}

	/*
	 * Recursive function to search for a word in tree. Same as BST
	 */
	private boolean search(Node root, String word) {
		if (root != null) {
			int indicator = root.getWord().compareToIgnoreCase(word);
			if (indicator == 0) {
				return true;
			} else if (indicator > 0) {
				return search(root.getLeftChild(), word);
			} else if (indicator < 0) {
				return search(root.getRightChild(), word);
			}
		}
		return false;
	}

	/*
	 * Recursive function to delete word from tree.
	 */
	private Node delete(Node root, String word) throws Exception {
		if (root == null) {
			throw new Exception(" NOT FOUND");
		}

		int indicator = compare(root.getWord().toLowerCase(), word);
		if (indicator > 0) {
			Node leftChild = delete(root.getLeftChild(), word);
			root.setLeftChild(leftChild);
			if (leftChild != null) {
				leftChild.setParent(root);
				fixDeleteViolations(leftChild, root);
			}
		} else if (indicator < 0) {
			Node rightChild = delete(root.getRightChild(), word);
			rightChild.setBlack();
			root.setRightChild(rightChild);
			if (rightChild != null) {
				rightChild.setParent(root);
				fixDeleteViolations(rightChild, root);
			}
		} else if (indicator == 0) {
			if (!root.hasLeftChild()) {
				return root.getRightChild();
			} else if (!root.hasRightChild()) {
				return root.getLeftChild();
			}
			String nextInOrder = minValue(root.getRightChild());
			root.setWord(nextInOrder);
			root.setRightChild(delete(root.getRightChild(), root.getWord()));
			return root;
		} else {
			throw new Exception(" UNKNOWN");
		}

		return root;
	}

	/*
	 * Recursive function to insert word in tree.
	 */
	private Node insert(Node root, Node node) throws Exception {

		if (root == null) {
			return node;
		}

		int indicator = compare(root.getWord().toLowerCase(), node.getWord().toLowerCase());
		if (indicator > 0) {
			Node leftChild = insert(root.getLeftChild(), node);
			root.setLeftChild(leftChild);
			if (leftChild != null) {
				leftChild.setParent(root);
			}
		} else if (indicator < 0) {
			Node rightChild = insert(root.getRightChild(), node);
			root.setRightChild(rightChild);
			if (rightChild != null) {
				rightChild.setParent(root);
			}
		} else if (indicator == 0) {
			throw new Exception(root.getWord() + " ALREADY EXISTS");
		} else {
			throw new Exception(" UNKNOWN");
		}

		return root;
	}

	private void fixDeleteViolations(Node u, Node v) {
		if (u.getColour() != v.getColour()) {
			u.setBlack();
		}
//        else if (u.isBlack() && v.isBlack()) {
//			u.setDoubleBlack(true);
//
//			while (u.isDoubleBlack() && u.getParent() != null) {
//				Node sibling = null;
//				if (v.getRightChild() != u) {
//					sibling = v.getRightChild();
//				} else {
//					sibling = v.getLeftChild();
//				}
//				if (sibling.isBlack()) {
//					Node redChild = null;
//					if (sibling.getLeftChild().isRed()) {
//						redChild = sibling.getLeftChild();
//						if (sibling.getRightChild().isRed()) {
//							redChild = sibling.getRightChild();
//							// Left Left Case
//							if (sibling.getParent().getLeftChild() == sibling) {
//								rightRotate(sibling.getParent());
//							}
//							// Right Right Case
//							else if (sibling.getParent().getRightChild() == sibling) {
//								leftRotate(sibling.getParent());
//							}
//						}
//					}
//					// Left Right Case
//					if (sibling.getParent().getLeftChild() == sibling && sibling.getRightChild() == redChild) {
//						leftRotate(sibling);
//						rightRotate(sibling.getParent());
//					}
//					// Right Left Case
//					else if (sibling.getParent().getRightChild() == sibling && sibling.getLeftChild() == redChild) {
//						rightRotate(sibling);
//						leftRotate(sibling.getParent());
//					}
//
//					Node blackChild = null;
//					
//				}
//
//			}
//		}
	}
	

	private void fixInsertViolations(Node node) {

		Node parent = null;
		Node grandParent = null;

		while (node != root && !node.isBlack() && node.getParent().isRed()) {

			parent = node.getParent();
			grandParent = node.getParent().getParent();

			if (parent == grandParent.getLeftChild()) {

				Node uncle = grandParent.getRightChild();

				if (uncle != null && uncle.isRed()) {

					grandParent.setRed();
					parent.setBlack();
					uncle.setBlack();
					node = grandParent;

				} else {
					// Left Right Case
					if (node == parent.getRightChild()) {
						leftRotate(parent);
						node = parent;
						parent = node.getParent();
					}

					// Left Left Case
					else if (node == parent.getLeftChild()) {
						rightRotate(grandParent);
						boolean temp = parent.getColour();
						parent.setColour(grandParent.getColour());
						grandParent.setColour(temp);
						node = parent;
					}

				}
			} else if (parent == grandParent.getRightChild()) {

				Node uncle = grandParent.getLeftChild();

				if (uncle != null && uncle.isRed()) {
					grandParent.setRed();
					parent.setBlack();
					uncle.setBlack();
					node = grandParent;
				} else {

					// Right Left Case
					if (node == parent.getLeftChild()) {
						rightRotate(parent);
						node = parent;
						parent = node.getParent();
					}

					// Right Right Case
					else if (node == parent.getRightChild()) {
						leftRotate(grandParent);
						boolean temp = parent.getColour();
						parent.setColour(grandParent.getColour());
						grandParent.setColour(temp);
						node = parent;
					}
				}
			}
		}

		getRoot().setBlack();
	}

	private void rightRotate(Node node) {
		Node left = node.getLeftChild();

		node.setLeftChild(left.getRightChild());

		if (node.getLeftChild() != null) {
			node.getLeftChild().setParent(node);
		}

		left.setParent(node.getParent());

		if (node.getParent() == null) {
			root = left;
		} else if (node == node.getParent().getLeftChild()) {
			node.getParent().setLeftChild(left);
		} else {
			node.getParent().setRightChild(left);
		}

		left.setRightChild(node);
		node.setParent(left);

	}

	private void leftRotate(Node node) {
		Node right = node.getRightChild();

		node.setRightChild(right.getLeftChild());

		if (node.getRightChild() != null) {
			node.getRightChild().setParent(node);
		}

		right.setParent(node.getParent());

		if (node.getParent() == null) {
			root = right;
		} else if (node == node.getParent().getLeftChild()) {
			node.getParent().setLeftChild(right);
		} else {
			node.getParent().setRightChild(right);
		}

		right.setLeftChild(node);
		node.setParent(right);
	}
}