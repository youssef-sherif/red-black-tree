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

	public Node(String string, boolean b) {
		word = string;
		colour = b;
	}

	public boolean isLeaf() {
		if (left == null && right == null) {
			return true;
		}
		return false;
	}

	public boolean hasOneChild() {
		if (left == null ^ right == null) {
			return true;
		}
		return false;
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

	public void setBlack() {
		this.colour = true;
		this.doubleBlack = false;
	}

	public void setRed() {
		this.colour = false;
		this.doubleBlack = false;
	}
}
