package tree;

import static tree.RedBlackTreeUtility.*;

public class RedBlackTree {

	private Node root;
	private Node nullNode;

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
				
		if(root != null) {
			root.setBlack();
		}
		 
		print(root);
	}

	public void insert(String word) throws Exception {

		Node node = new Node(word);

		root = insert(root, node);

		fixInsertViolations(node);			

		print(getRoot());
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
		if (indicator > 0) {
			Node leftChild = delete(root.getLeftChild(), word);
			root.setLeftChild(leftChild);
			print(root);
			fixDeleteViolations(leftChild);
		} else if (indicator < 0) {
			Node rightChild = delete(root.getRightChild(), word);
			root.setRightChild(rightChild);
			print(root);
			fixDeleteViolations(rightChild);
		} else if (indicator == 0) {

			/*
			 * only one node
			 */
			if (root.getParent() == null && root.getLeftChild() == null && root.getRightChild() == null) {
				return null;
			}

			/*
			 * node with no children
			 */
			if (root.getLeftChild() == null && root.getRightChild() == null) {
				if (root.getLeftChild() == null) {
					nullNode = new Node("NULL");
					nullNode.setBlack();
					root.setLeftChild(nullNode);
					return redBlackDelete(nullNode, root);
				} else if (root.getRightChild() == null) {
					nullNode = new Node("NULL");
					nullNode.setBlack();
					root.setRightChild(nullNode);
					return redBlackDelete(nullNode, root);
				}
			}
			/*
			 * node with 1 child
			 */
			else if (root.getLeftChild() == null ^ root.getRightChild() == null) {
				if (root.getLeftChild() == null) {					
					return redBlackDelete(root.getRightChild(), root);
				} else if (root.getRightChild() == null) {
					return redBlackDelete(root.getLeftChild(), root);
				}
			}
			/*
			 * node with 2 children. get the word chronologically before word to be deleted
			 */
			Node predecessorNode = maxValue(root.getLeftChild());
			root.setWord(predecessorNode.getWord());
			Node leftChild = delete(predecessorNode, root.getWord());
			root.setLeftChild(leftChild);
			fixDeleteViolations(leftChild);

		}		
		return root;
	}

	private Node redBlackDelete(Node toReplace, Node toDelete) throws Exception {

		if (toDelete.isRed()) {
			toReplace.setBlack();
		} else if(toDelete.isBlack()){
			toReplace.setDoubleBlack(true);
		}
		toReplace.setParent(toDelete.getParent());
		return toReplace;
	}

	private void fixDeleteViolations(Node node) {		

		Node sibling = null;
		Node parent = null;
		while (node.isDoubleBlack() && node.getParent() != null) {
			print(node);
			sibling = null;
			parent = node.getParent();
			if (parent.getLeftChild() == node) {
				sibling = parent.getRightChild();
			} else if (parent.getRightChild() == node) {
				sibling = parent.getLeftChild();
			}
			if (sibling != null) {				
				if (sibling.isBlack() && hasChildRed(sibling)) {
					System.out.println("case 1");
					Node redChild = null;
					if (sibling.getRightChild() != null && sibling.getRightChild().isRed()) {
						redChild = sibling.getRightChild();
					} else if (sibling.getLeftChild() != null && sibling.getLeftChild().isRed()) {
						redChild = sibling.getLeftChild();
					}
					// Left Left
					if (parent.getLeftChild() == sibling
							&& (sibling.getLeftChild() == redChild || bothChildrenRed(sibling))) {
						rightRotate(parent);						
					}
					// Right Right
					else if (parent.getRightChild() == sibling
							&& (sibling.getRightChild() == redChild || bothChildrenRed(sibling))) {
						leftRotate(parent);
						redChild.setBlack();
						// Left Right
					} else if (parent.getLeftChild() == sibling && sibling.getRightChild() == redChild) {
						leftRotate(sibling);
						rightRotate(parent);
					}
					// Right Left
					else if (parent.getRightChild() == sibling && sibling.getLeftChild() == redChild) {
						rightRotate(sibling);
						leftRotate(parent);
					}
					redChild.setBlack();
					node.setDoubleBlack(false);

				} 
				else if(sibling.isBlack() && bothChildrenBlack(sibling)) {
					System.out.println("case 2");
					if(parent.isRed()) {
						parent.setBlack();
						sibling.setRed();
					} else if(parent.isBlack()) {
						parent.setDoubleBlack(true);
						if(sibling.isBlack()) 
							sibling.setRed();
						else {
							sibling.setBlack();
						}											
					}
					if(parent.isDoubleBlack()) {
						node.setDoubleBlack(false);						
					}
				} 
				else if(sibling.isRed()) {
					System.out.println("case 3");
					//Left Left
					if(parent.getLeftChild() == sibling) {
						rightRotate(parent);
					}
					//Right Right
					else if(parent.getRightChild() == sibling) {
						leftRotate(parent);
					}
					//Recolour
					parent.reColour();
					sibling.reColour();					
				}
				node = node.getParent();
			}
		}

		/*
		 * remove reference to null node
		 */
		if (nullNode != null) {
			if (nullNode.getParent() != null && nullNode.getParent().getLeftChild() == nullNode) {
				nullNode.getParent().setLeftChild(null);
			} else if (nullNode.getParent() != null && nullNode.getParent().getRightChild() == nullNode) {
				nullNode.getParent().setRightChild(null);
			}
			nullNode = null;
		}

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