import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * A class that represents a red black tree.
 * @author nikhi
 *
 */
public class RedBlackTrees {
	
	/**
	 * A class that represents a node of a RBT.
	 * @author nikhi
	 *
	 */
	class Node{
		char color;
		int val;
		Node left;
		Node right;
		Node parent;
		
		/**
		 * A constructor for a RBT. New nodes are usually Red, except when it is the root.
		 * @param val the value for the node.
		 */
		public Node(int val) {
			this.color = 'R';
			this.val = val;
			this.left =null;
			this.right = null;
			this.parent = null;
		}
	}
	public Node root;
	
	public RedBlackTrees() {
		this.root = null;
	}
	
	/**
	 * Method to get the sorted order of the nodes.
	 */
	void Sort(Node root) {
		if(root == null)
			return;
		
		Sort(root.left);
		System.out.println(root.val);
		Sort(root.right);
	}
	
	/**
	 * A method to handle left rotation.
	 * @param node The root of the node to be rotated.
	 */
	void leftRotate(Node node) {
		Node X = node.right;
		Node Y = X.left;
		X.left = node;
		node.right = Y;
		X.parent = node.parent;
		
		if(node.parent == null)
			this.root = X;
		else if(node == node.parent.left) {
			node.parent.left = X;
		}
		else
			node.parent.right = X;
		
		node.parent = X;
		if(Y != null)
			Y.parent = node;
	}
	
	
	/**
	 * Method to handle right rotation.
	 * @param node The node to be right rotated.
	 */
	void rightRotate(Node node) {
		Node X = node.left;
		Node Y = X.right;
		X.right = node;
		node.left = Y;
		
		X.parent = node.parent;
		if(node.parent == null)
			this.root = X;
		else if(node == node.parent.left) {
			node.parent.left = X;
		}
		else
			node.parent.right = X;
		node.parent = X;
		
		if(Y != null)
			Y.parent = node;
	}
	
	/**
	 * A helper method for insert
	 * @param root the node of the tree.
	 * @param value the value to be inserted in the tree.
	 * @return The Node that is inserted, after creation.
	 */
	void insertUtil(Node node, int value) {
		//The root should always be black.
		if(node == null) {
			return;
		}
		Node newNode = new Node(value);
		Node x = node;
		Node y = null;
		
		//Recurse left or right till we find a null node
		while(x!=null) {
			y = x;
			if(x.val > value)
				x = x.left;
			else
				x = x.right;
		}
		//Once found place in appropriate spot.
		newNode.parent = y;
		if(y.val > value)
			y.left = newNode;
		else
			y.right = newNode;
		
		//if no parent or grandparent --> terminate.
		if(newNode.parent == null)
			return;
		if(newNode.parent.parent == null)
			return;
		
		handleRedRedConflict(newNode);
	}
	
	/**
	 * A method to handle red-red conflict.
	 */
	void handleRedRedConflict(Node node) {
		Node uncle;
		//Check whether the uncle node is red or black. If both red just recolor them and do nothing
		while(node.parent.color == 'R') {
			
			if(node.parent.parent.left == node.parent){
				uncle = node.parent.parent.right;
				if(uncle!=null && node.parent.color == uncle.color) {
					uncle.color = 'B';
					node.parent.color = 'B';
					node.parent.parent.color = 'R';
					node = node.parent.parent;
				}
				//Rotation case, either right rotate or right-left rotate.
				else {
					//The Left-Right rotate case. Node is on the right of the parent.
					if(node.parent.right == node) {
						node = node.parent;
						leftRotate(node);
					}
					node.parent.color = 'B';
					node.parent.parent.color = 'R';
					rightRotate(node.parent.parent);
				}
			}
			
			//the other side is the uncle of this node.
			else {
				uncle = node.parent.parent.left;
				if(uncle!=null && node.parent.color == uncle.color) {
					uncle.color = 'B';
					node.parent.color = 'B';
					node.parent.parent.color = 'R';
					node = node.parent.parent;
				}
				else {
					//Handle the Right-Left case.
					if(node.parent.left == node) {
						node = node.parent;
						rightRotate(node);
					}
					node.parent.color = 'B';
					node.parent.parent.color = 'R';
					leftRotate(node.parent.parent);
				}
			}
			//Break when we reach the root of the tree.
			if(node == this.root)
				break;
			}
		this.root.color = 'B';
	}
	
	/**
	 * Insert a value into the tree.
	 * @param value The value to be inserted into the tree.
	 */
	public void Insert(int value) {
		if(this.root == null) {
			//Root is always a black node.
			Node temp = new Node(value);
			temp.color = 'B';
			this.root = temp;
		}
		else
			insertUtil(this.root, value);
	}
	
	/**
	 * Method to find the height of the tree.
	 * @param node The root of the tree
	 * @return The height of the tree.
	 */
	public int height(Node node) {
		if(node == null)
			return 0;
		
		return Math.max(height(node.left)+1, height(node.right)+1);
	}
	
	/**
	 * Find the minimum value in the tree
	 * @param node The root of the tree.
	 * @return The min value in the tree.
	 */
	public Node min(Node root) {
		if(root == null)
			return null;
		
		if(root.left == null)
			return root;
		else
			return min(root.left);
	}
	
	/**
	 * Find the max in the tree.
	 * @param node The root of the tree.
	 * @return the max value in the tree.
	 */
	public Node max(Node root) {
		if(root == null)
			return null;
		
		if(root.right == null)
			return root;
		else
			return max(root.right);
	}
	
	// find the successor of a given node
	public Node successor(Node x) {
		if (x.right != null) {
			return min(x.right);
		}
		Node y = x.parent;
		while (y != null && x == y.right) {
			x = y;
			y = y.parent;
		}
		return y;
	}

	// find the predecessor of a given node
	public Node predecessor(Node x) {
		if (x.left != null) {
			return max(x.left);
		}

		Node y = x.parent;
		while (y != null && x == y.left) {
			x = y;
			y = y.parent;
		}

		return y;
	}
	
	/**
	 * Method to search for an element in the RBT.
	 * @param root The root of the tree.
	 * @param value The value to look for in the root.
	 * @return True if element found in the tree.
	 */
	public boolean search(Node root,int value) {
		if(root == null)
			return false;
		
		if(root.val == value)
			return true;
		
		if(root.val > value)
			return search(root.left, value);
		else
			return search(root.right, value);
	}
	
	
	public static void main(String [] args) {
		RedBlackTrees tree = new RedBlackTrees();
		File file = new File("src/input.txt");
		System.out.println(file.canRead());
		try 
		{
			Scanner input = new Scanner(file);
			while(input.hasNextInt())
				tree.Insert(input.nextInt());
		}
		catch (IOException e) {
			System.out.println("Encountered some issues while reading the file!");
		}
		System.out.println(String.format("The file has been read. Height of tree : %d", tree.height(tree.root)));
		while(true) {
			System.out.println("Enter 1 for insert, 2 for search, 3 for sort, 4 for min, 5 for max, 6 for successor, 7 for predecessor, 8 for exit");
			Scanner sc = new Scanner(System.in);
			int ip = sc.nextInt();
			
			if(ip > 7)
				break;
			
			int x;
			switch(ip) 
			{
			case 1:
				System.out.println("Enter the value to insert");
				x = sc.nextInt();
				tree.Insert(x);
				System.out.println("Height of the tree is " + tree.height(tree.root));
				break;
			case 2:
				System.out.println("Enter the value to insert");
				x = sc.nextInt();
				boolean found = tree.search(tree.root, x);
				if(found)
					System.out.println("Found the value");
				else
					System.out.println("Sorry we couldn't find the value");
				System.out.println("Height of the tree is " + tree.height(tree.root));
				break;
			case 3:
				System.out.println("The sorted tree is");
				tree.Sort(tree.root);
				System.out.println("Height of the tree is " + tree.height(tree.root));
				break;
			case 4:
				System.out.println("Minimum : " +tree.min(tree.root).val);
				System.out.println("Height of the tree is " + tree.height(tree.root));
				break;
			case 5:
				System.out.println("Maximum : " +tree.max(tree.root).val);
				System.out.println("Height of the tree is " + tree.height(tree.root));
				break;
			case 6:
				System.out.println("Successor : " +tree.successor(tree.root).val);
				System.out.println("Height of the tree is " + tree.height(tree.root));
				break;
			case 7:
				System.out.println("Predecessor : " +tree.predecessor(tree.root).val);
				System.out.println("Height of the tree is " + tree.height(tree.root));
				break;
			case 8:
				break;
			default:
				System.out.println("Incorrect choice!!");
			}
		}
	}
}
