package tree;

/**
 * @author youssef
 *
 */
class Node {

	/* true for black and false for red */
	private boolean colour;
	private boolean doubleBlack;

	private String word;

	private Node parent;
	private Node left;
	private Node right;
	
	

	public Node(String word) {
		this.word = word;
		this.colour = false; // color the node red
	}

	public boolean isBlack() {
		return colour;
	}

	public boolean isRed() {
		return !colour;
	}

	public void reColour() {
		colour = !colour;
	}

	public boolean getColour() {
		return colour;
	}
	
	public void setColour(boolean colour) {
		this.colour = colour;
	}

	public boolean isDoubleBlack() {
		return doubleBlack;
	}

	public void setDoubleBlack(boolean doubleBlack) {
		this.doubleBlack = doubleBlack;
	}

	public Node getLeft() {
		return left;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public Node getRight() {
		return right;
	}

	public void setRight(Node right) {
		this.right = right;
	}

	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}

	public Node getRightChild() {
		return right;
	}

	public void setRightChild(Node right) {		
		this.right = right;
	}
	
	public Node getLeftChild() {
		return left;
	}
	
	public void setLeftChild(Node left) {
		this.left = left;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public boolean hasLeftChild() {
		return (left != null);
	}
	
	public boolean hasRightChild() {
		return (right != null);
	}

	public void setBlack() {
		this.colour = true;
	}

	public void setRed() {
		this.colour = false;
	}
}
