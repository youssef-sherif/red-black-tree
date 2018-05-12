package tree;

import static tree.RedBlackTreeUtility.*;

public class RedBlackTree {

	private Node root;
	private Node doubleBlack;	

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

		fixDeleteViolations(doubleBlack);

		 print(root);
	}

	public void insert(String word) throws Exception {

		Node node = new Node(word);

		root = insert(root, node);

		fixInsertViolations(node);	

		print(root);
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
	 * Recursive function to search for node to delete and replace it with it's
	 * predecessor.
	 */
	private Node delete(Node root, String word) throws Exception {
		if (root == null) {
			/*
			 * reached end of branch without finding the word
			 */
			throw new Exception(" NOT FOUND");
		}

		int indicator = compare(root.getWord(), word);
		if (indicator > 0 ) {
			Node leftChild = delete(root.getLeftChild(), word);
			root.setLeftChild(leftChild);
			leftChild.setParent(root);			
		} else if (indicator < 0 ) {
			Node rightChild = delete(root.getRightChild(), word);
			root.setRightChild(rightChild);
			rightChild.setParent(root);
		}
		else if (indicator == 0) {

			/*
			 * only one node just delete it
			 */
			if (root.getParent() == null && root.getLeftChild() == null && root.getRightChild() == null) {

				return null;
			}

			/*
			 * node to delete is leaf
			 */
			if (root.getLeftChild() == null && root.getRightChild() == null) {
				if (root.getLeftChild() == null) {
					doubleBlack = new Node("NULL");
					doubleBlack.setBlack();
					return redBlackDelete(doubleBlack, root);
				} else if (root.getRightChild() == null) {
					doubleBlack = new Node("NULL");
					doubleBlack.setBlack();
					return redBlackDelete(doubleBlack, root);
				}
			}
			/*
			 * node to delete has only 1 child
			 */
			else if (root.getLeftChild() == null || root.getRightChild() == null) {
				if (root.getLeftChild() == null) {
					return redBlackDelete(root.getRightChild(), root);
				} else if (root.getRightChild() == null) {
					return redBlackDelete(root.getLeftChild(), root);
				}
			}
			/*
			 * node to delete has 2 children. get the word chronologically before word to be
			 * deleted
			 */
			String predecessor = maxValue(root.getLeftChild());
			root.setWord(predecessor);
			Node leftChild = delete(root.getLeftChild(), root.getWord());
			root.setLeftChild(leftChild);

		}
		return root;
	}

	private Node redBlackDelete(Node toReplace, Node toDelete) throws Exception {
		
		if (toReplace.getColour() != toDelete.getColour()) {
			toReplace.setBlack();
		} else if (toReplace.isBlack() && toDelete.isBlack()) {
			toReplace.setDoubleBlack(true);
		}
		toReplace.setParent(toDelete.getParent());
		return toReplace;
	}

	private void fixDeleteViolations(Node node) {

		if(node == null) {
			return;
		}
		Node sibling = null;
		Node parent = null;
		while (node.isDoubleBlack() && node.getParent() != null) {
			parent = node.getParent();
			if (parent.getLeftChild() == node) {
				sibling = node.getParent().getRightChild();
			} else if (parent.getRightChild() == node) {
				sibling = parent.getLeftChild();
			}
			if (sibling != null) {
				System.out.println(node.getParent() == sibling.getParent());
				if (sibling.isBlack() && hasChildRed(sibling)) {
					System.out.println("case 1");
					Node redChild = null;
					if (sibling.getRightChild() != null && sibling.getRightChild().isRed()) {
						redChild = sibling.getRightChild();
					} else if (sibling.getLeftChild() != null && sibling.getLeftChild().isRed()) {
						redChild = sibling.getLeftChild();
					} else {
						node.setDoubleBlack(false);
						sibling.reColour();
						node = node.getParent();
					}
					// Left Left
					if (parent.getLeftChild() == sibling
							&& (sibling.getLeftChild() == redChild || bothChildrenRed(sibling))) {
						rightRotate(sibling.getParent());										
						node.setDoubleBlack(false);
						redChild.setBlack();
					}
					// Right Right
					else if (parent.getRightChild() == sibling
							&& (sibling.getRightChild() == redChild || bothChildrenRed(sibling))) {
						leftRotate(sibling.getParent());
						node.setDoubleBlack(false);
						redChild.setBlack();
					}
					// Left Right
					else if (parent.getLeftChild() == sibling && sibling.getRightChild() == redChild) {
						leftRotate(sibling);
						redChild.reColour();
						sibling.reColour();
					}
					// Right Left
					else if (parent.getRightChild() == sibling && sibling.getLeftChild() == redChild) {					
						rightRotate(sibling);
						redChild.reColour();
						sibling.reColour();
					}

				} else if (sibling.isBlack() && bothChildrenBlack(sibling)) {
					System.out.println("case 2");
					if (parent.isRed()) {
						parent.setBlack();
						sibling.setRed();
					} else if (parent.isBlack()) {
						parent.setDoubleBlack(true);
						if (sibling.isBlack()) {
							sibling.setRed();
						} else {
							sibling.setBlack();
						}
					}
					if (parent.isDoubleBlack()) {
						node = node.getParent();
					}
				} else if (sibling.isRed()) {
					System.out.println("case 3");
					// Left Left
					if (parent.getLeftChild() == sibling) {
						rightRotate(sibling.getParent());
					}
					// Right Right
					else if (parent.getRightChild() == sibling) {
						leftRotate(sibling.getParent());
					}
					// Recolour
					parent.reColour();
					sibling.reColour();
				}
			}
		}

		/*
		 * remove reference to null node
		 */
		if (doubleBlack != null) {
			if (doubleBlack.getWord().equals("NULL")) {
				if (doubleBlack.getParent() != null && doubleBlack.getParent().getLeftChild() == doubleBlack) {
					doubleBlack.getParent().setLeftChild(null);
				} else if (doubleBlack.getParent() != null && doubleBlack.getParent().getRightChild() == doubleBlack) {
					doubleBlack.getParent().setRightChild(null);
				}
				doubleBlack = null;
			}
		}
		
		getRoot().setBlack();

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
		}

		return root;
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
		System.out.println("rotating right on " + node.getWord());
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
		System.out.println("rotating left on " + node.getWord());
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