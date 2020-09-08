/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */
public class AVLTree {
	
	private IAVLNode root = null;
	private int size = 0;
	private int index = 0;
	private IAVLNode minimum;
	private IAVLNode maximum;
//	protected int rightRotationsCounter = 0;
//	protected int leftRotationsCounter = 0;
	
	
  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
	public boolean empty()
	{
		return (root == null);
	}

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  public String search(int k)
  {
	  IAVLNode node = searchNode(k);
	  if (node != null)
		  return node.getValue();
	  return null;
  }
  
  /**
   * private IAVLNode searchNode(int k)
   *
   * Utility function for searching node with key k in the tree.
   * Performing a regular BST tree walk.
   */
  private IAVLNode searchNode(int k)
  {
	  IAVLNode node = root;
	  while (node != null)
	  {
		  if (node.getKey() == k)
			  return node;
		  else if (node.getKey() < k)
			  node = node.getRight();
		  else
			  node = node.getLeft();
	  }
	  return null;
  }

  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the AVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) {   
	   // normal BST insertion.
	   IAVLNode z = new AVLNode(k, i);
	   IAVLNode y = null;
	   IAVLNode x = root;
	   while (x != null) //regular BST tree walk.
	   {
		   y = x;
		   if (z.getKey() == x.getKey())
			   return -1; // terminate.
		   else if (z.getKey() < x.getKey())
			   x = x.getLeft();
		   else
			   x = x.getRight();
	   }
	   z.setParent(y);
	   if (y == null)
	   {
		   root = z; // tree was empty.
		   minimum = z;
		   maximum = z;
		   this.size++; //updates tree size.
		   return 0;
	   }
	   else if (z.getKey() < y.getKey()) // k is smaller key than it's parent, thus z should be a left son.
		   y.setLeft(z);
	   else // k is bigger key than it's parent, thus z should be a right son.
		   y.setRight(z);
	   this.size++; //updates tree size.
	   
	   // updates max & min.
	   if (k < this.minimum.getKey())
		   this.minimum = z;
	   else if (k > this.maximum.getKey())
		   this.maximum = z;
	   
	   // update loop.
	   int cnt = 0;
	   while (y != null)
	   {
		   ((AVLNode) y).setSize(updateSize(y));
		   int oldHeight = y.getHeight();
		   y.setHeight(updateHeight(y));
		   int BF = getBalance(y);
		   if ((Math.abs(BF) < 2) && (oldHeight == y.getHeight())) // No more changes needed. Still runs for updating heights.
		   {
			   y = y.getParent();
			   continue;
		   }
		   else if (Math.abs(BF) < 2) // continue checking this node's ancestor.
		   {
			   y = y.getParent();
			   continue;
		   }
		   else // Houston we have a problem. Rotation is needed.
		   {
			   cnt += rotationsManager(y, BF);
			   y = y.getParent();
		   }
	   }
	   return cnt;
	   
	   
   }
   /**
    * public int listInsert(int k, String i) { 
    * 
    * Used for inserting a new item to a list implemented with an AVLTree
    * Inserts an item with key k and value s as the item ranked i-th in the tree 
    * 
    * @pre: i > 0, i <= tree.size 
    */
   public void listInsert(int i, int k, String s) {   
	   this.size++;
	   IAVLNode z = new AVLNode(k, s);
	   if (this.empty()) //Tree is empty
	   {
		   root = z;
		   minimum = z;
		   maximum = z;
	   }
	   else if (i == this.size) //Insert-Last
	   {
		   maximum.setRight(z);
		   z.setParent(maximum);
		   maximum = z;
	   }
	   else if (i == 1) //Insert-First
	   {
		   minimum.setLeft(z);
		   z.setParent(minimum);
		   minimum = z;
	   }
	   else
	   {
		   IAVLNode successor = treeSelect(i); //
		   if (successor.getLeft() == null)
		   {
			   successor.setLeft(z);
			   z.setParent(successor);
		   }
		   else
		   {
			   IAVLNode predecessor = findPredecessor(successor);
			   predecessor.setRight(z);
			   z.setParent(predecessor);
		   }
	   }
	   IAVLNode y = z;
	   // update loop.
	   while (y != null)
	   {
		   ((AVLNode) y).setSize(updateSize(y));
		   int oldHeight = y.getHeight();
		   y.setHeight(updateHeight(y));
		   int BF = getBalance(y);
		   if ((Math.abs(BF) < 2) && (oldHeight == y.getHeight())) // No more changes needed. Still runs for updating heights.
		   {
			   y = y.getParent();
			   continue;
		   }
		   else if (Math.abs(BF) < 2) // continue checking this node's ancestor.
		   {
			   y = y.getParent();
			   continue;
		   }
		   else // Houston we have a problem. Rotation is needed.
		   {
			   rotationsManager(y, BF);
			   y = y.getParent();
		   }
	   }
   }
	/**
	 * public IAVLNode treeSelect(int k)
	 * 
	 * Given a tree and a number k, the function returns the node with the k-th key in the tree
	 * returns null if k is not a rank the exists in the tree
	 * 
	 */
	public IAVLNode treeSelect(int k)
	{
		if(this.empty() || k < 1 || k > this.size)
			return null;
		if (k==1)
			return this.minimum;
		if (k== this.size)
			return this.maximum;
		return select(this.root,k);
	}
	/**
	 * public static IAVLNode select(IAVLNode root, int k)
	 * 
	 * recursive function that finds the k-th node in the tree
	 * 
	 * @pre: k > 0, k <= tree.size
	 * @pre: !tree.empty()
	 */
	public static IAVLNode select(IAVLNode root, int k)
	{
		int rank;
		if (root.getLeft() != null)
			rank = ((AVLNode) root.getLeft()).getSize() + 1;
		else
			rank = 1;
		if (rank == k)
			return root;
		else if (k < rank)
			return select(root.getLeft(),k);
		else
			return select(root.getRight(),k-rank);
	}
	/**
	    * public static int getRank(IAVLNode x)
	    *
	    * Returns the Rank of a node x.
	    */
	public static int getRank(IAVLNode x)
	   {
		   if (x == null)
			   return 0;
		   int r = 1;
		   if (x.getLeft() != null)
			   r += ((AVLNode) x.getLeft()).getSize();
		   IAVLNode y = x;
		   while (y != null)
		   {
			   IAVLNode p = y.getParent();
			   if ((p != null) && (y == p.getRight())) // y is a right child.
			   {
				   if (p.getLeft() == null)
					   r += 1;
				   else
					   r += ((AVLNode) p.getLeft()).getSize() + 1;
			   }
			   y = y.getParent();
		   }
		   return r;
	   }
   /**
    * private int rotationsManager(IAVLNode x, int BF)
    *
    * Chooses the right rotation to perform 
    * and returns the number of rotations (1 or 2) that had been done.
    */
   private int rotationsManager(IAVLNode x, int BF)
   {
	   // x's BF is one of {-2, 2}.
	   // x's sons BF is one of {-1, 0 , 1}.
	   // There are 4 general cases.
	   int BFLeft = getBalance(x.getLeft());
	   int BFRight = getBalance(x.getRight());
	   // Right Case 
       if ((BF == 2) && (BFLeft > -1))
       {
    	   rightRotate(x);
//    	   this.rightRotationsCounter++;
    	   return 1;
       }
       // Left Case 
       if ((BF == -2) && (BFRight < 1))
       {
    	   leftRotate(x);
//    	   this.leftRotationsCounter++;
    	   return 1;
       }
       // Left then Right Case 
       if ((BF == 2) && (BFLeft == -1))
       { 
           leftRotate(x.getLeft());
           rightRotate(x);
//           this.leftRotationsCounter++;
//           this.rightRotationsCounter++;
           return 2;
       } 
       // Right then Left Case 
       if ((BF == -2) && (BFRight == 1))
       { 
           rightRotate(x.getRight()); 
           leftRotate(x);
//           this.leftRotationsCounter++;
//           this.rightRotationsCounter++;
           return 2;
       } 
	   return -1000;
   }

  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k)
   {
	   IAVLNode node = searchNode(k); //Retrieve the node we want to delete
	   IAVLNode updateFrom = null; //Where to begin updating information after deletion
	   int cnt = 0; //Counter for rotations
	   if (node == null)
		   return -1;
	   this.size = size() - 1;
	   if (node == minimum)
	   {
		   minimum = findSuccessor(node);
	   }
	   if (node == maximum)
	   {
		   maximum = findPredecessor(node);
	   }
	   if (node.getLeft() == null && node.getRight() == null) //Case 1: The node is a leaf
	   {
		   if (node == root) //Delete the root
		   {
			   root = null;
		   }
		   else
		   {
			   bypass(node,null); //Set parent's right/left child to null.  
			   updateFrom = node.getParent();
		   }
	   }
	   else if (node.getLeft() != null && node.getRight() == null) //Case 2: The node has only a left child
	   {
		   if (node == root) //Delete the root
		   {
			   root = node.getLeft();
			   root.setParent(null);
			   updateFrom = root;
		   }
		   else
		   {
			   bypass(node,node.getLeft()); //Bypass to the left child of the parent
			   node.getLeft().setParent(node.getParent());
			   updateFrom = node.getLeft();
		   }
	   }
	   else if (node.getRight() != null && node.getLeft() == null) //Case 2: The node has only a right child
	   {
		   if (node == root) //Delete the root
		   {
			   root = node.getRight();
			   root.setParent(null);
			   updateFrom = root;
		   }
		   else
		   {
			   bypass(node,node.getRight()); //Bypass to the right child of the parent
			   node.getRight().setParent(node.getParent());
			   updateFrom = node.getParent();
		   }
	   }
	   else //Case 3: The node has 2 children
	   {
		   IAVLNode successor = minInSubTree(node.getRight()); //The successor is certainly in the right sub-tree
		   if (node.getRight() != successor) //Successor is not the right child of the node
		   {
			   updateFrom = successor.getParent();
			   if (successor.getRight() != null) //Successor has a right child
			   {
				   bypass(successor,successor.getRight()); //remove successor from its place
				   successor.getRight().setParent(successor.getParent());
			   }
			   else //Successsor is a leaf
			   {
				   bypass(successor,null);
			   }
			   successor.setRight(node.getRight());
			   successor.getRight().setParent(successor);
		   }
		   else
		   {
			   updateFrom = successor;
		   }
		   successor.setLeft(node.getLeft());
		   successor.getLeft().setParent(successor);
		   if (node == root) //Deleted node is the root
		   {
			   root = successor;
			   successor.setParent(null);
		   }
		   else //Deleted node is not the root
		   {
			   bypass(node,successor);
			   successor.setParent(node.getParent());
		   }
	   }
	   node.setLeft(null); //Delete the node
	   node.setRight(null);
	   node.setParent(null);
	   
	   // update loop.
	   IAVLNode y = updateFrom;
	   while (y != null)
	   {
		   ((AVLNode) y).setSize(updateSize(y));
		   int oldHeight = y.getHeight();
		   y.setHeight(updateHeight(y));
		   int BF = getBalance(y);
		   if ((Math.abs(BF) < 2) && (oldHeight == y.getHeight())) // No more changes needed. Still runs for updating heights.
		   {
			   y = y.getParent();
			   continue;
		   }
		   else if (Math.abs(BF) < 2) // continue checking this node's ancestor.
		   {
			   y = y.getParent();
			   continue;
		   }
		   else // Houston we have a problem. Rotation is needed.
		   {
			   cnt += rotationsManager(y, BF);
			   y = y.getParent();
		   }
	   }
	   return cnt;
   }
   
   /**
    * private static void bypass(IAVLNode deletedNode, IAVLNode child)
    * 
    * Checks if a deleted node is the left of right child of its parent
    * Sets the left/right child of the parent to a new child, or to be a null pointer.
    */
   private static void bypass(IAVLNode deletedNode, IAVLNode child) 
   {
	   if (deletedNode.getParent().getRight() == deletedNode)  //The deleted node is the right child of its parent
		   		deletedNode.getParent().setRight(child);
	   else if (deletedNode.getParent().getLeft() == deletedNode) //The deleted node is the left child of its paret
		   		deletedNode.getParent().setLeft(child);
   }
   /**
    *    public void listDelete(int k)
    * 
    * Used for a deleting and item from a list implemented by an AVLTree.
    * Deletes the item in the k-th position
    * @pre: k > 0, k <= tree.size
    */
   public void listDelete(int k)
   {
	   IAVLNode node = treeSelect(k); //Retrieve the node with rank k
	   IAVLNode updateFrom = null; //Where to begin updating information after deletion
	   this.size = size() - 1;
	   if (node.getLeft() == null && node.getRight() == null) //Case 1: The node is a leaf
	   {
		   if (node == root) //Delete the root
		   {
			   root = null;
		   }
		   else
		   {
			   bypass(node,null); //Set parent's right/left child to null.  
			   updateFrom = node.getParent();
		   }
	   }
	   else if (node.getLeft() != null && node.getRight() == null) //Case 2: The node has only a left child
	   {
		   if (node == root) //Delete the root
		   {
			   root = node.getLeft();
			   root.setParent(null);
			   updateFrom = root;
		   }
		   else
		   {
			   bypass(node,node.getLeft()); //Bypass to the left child of the parent
			   node.getLeft().setParent(node.getParent());
			   updateFrom = node.getLeft();
		   }
	   }
	   else if (node.getRight() != null && node.getLeft() == null) //Case 2: The node has only a right child
	   {
		   if (node == root) //Delete the root
		   {
			   root = node.getRight();
			   root.setParent(null);
			   updateFrom = root;
		   }
		   else
		   {
			   bypass(node,node.getRight()); //Bypass to the right child of the parent
			   node.getRight().setParent(node.getParent());
			   updateFrom = node.getParent();
		   }
	   }
	   else //Case 3: The node has 2 children
	   {
		   IAVLNode successor = minInSubTree(node.getRight()); //The successor is certainly in the right sub-tree
		   if (node.getRight() != successor) //Successor is not the right child of the node
		   {
			   updateFrom = successor.getParent();
			   if (successor.getRight() != null) //Successor has a right child
			   {
				   bypass(successor,successor.getRight()); //remove successor from its place
				   successor.getRight().setParent(successor.getParent());
			   }
			   else //Successsor is a leaf
			   {
				   bypass(successor,null);
			   }
			   successor.setRight(node.getRight());
			   successor.getRight().setParent(successor);
		   }
		   else
		   {
			   updateFrom = successor;
		   }
		   successor.setLeft(node.getLeft());
		   successor.getLeft().setParent(successor);
		   if (node == root) //Deleted node is the root
		   {
			   root = successor;
			   successor.setParent(null);
		   }
		   else //Deleted node is not the root
		   {
			   bypass(node,successor);
			   successor.setParent(node.getParent());
		   }
	   }
	   
	   
	   // update loop.
	   IAVLNode y = updateFrom;
	   while (y != null)
	   {
		   ((AVLNode) y).setSize(updateSize(y));
		   int oldHeight = y.getHeight();
		   y.setHeight(updateHeight(y));
		   int BF = getBalance(y);
		   if ((Math.abs(BF) < 2) && (oldHeight == y.getHeight())) // No more changes needed. Still runs for updating heights.
		   {
			   y = y.getParent();
			   continue;
		   }
		   else if (Math.abs(BF) < 2) // continue checking this node's ancestor.
		   {
			   y = y.getParent();
			   continue;
		   }
		   else // Houston we have a problem. Rotation is needed.
		   {
			   rotationsManager(y, BF);
			   y = y.getParent();
		   }
	   }
	   if (node == minimum)
	   {
		   minimum = findSuccessor(node);
	   }
	   if (node == maximum)
	   {
		   maximum = findPredecessor(node);
	   }
	   node.setLeft(null); //Delete the node
	   node.setRight(null);
	   node.setParent(null);
	   
   }
   /**
    * private static IAVLNode findSuccessor(IAVLNode node)
    * 
    * Finds the successor of a node x.
    */
   public static IAVLNode findSuccessor(IAVLNode x)
   {
	   if (x.getRight() != null)
		   return minInSubTree(x.getRight());
	   IAVLNode y = x.getParent();
	   while (y != null && x == y.getRight())
	   {
		   x = y;
		   y = x.getParent();
	   }
	   return y;
   }
   /**
    * private static IAVLNode findPredecessor(IAVLNode node)
    * 
    * Finds the Predecessor of a node x.
    */
   public static IAVLNode findPredecessor(IAVLNode x)
   {
	   if (x.getLeft() != null)
		   return maxInSubTree(x.getLeft());
	   IAVLNode y = x.getParent();
	   while (y != null && x == y.getLeft())
	   {
		   x = y;
		   y = x.getParent();
	   }
	   return y;
   }
   /**
    * private int getBalance(IAVLNode x)
    * 
    * Returns the Balance Factor of node x. 
    */
   private static int getBalance(IAVLNode x) { 
       if (x == null) // x is null.
           return 0;
       IAVLNode l = x.getLeft();
	   IAVLNode r = x.getRight();
	   if ((l == null) && (r == null)) // no children.
		   return 0;
	   else if (l == null) // no left children.
		   return -1 - r.getHeight();
	   else if (r == null) // no right children.
		   return l.getHeight() + 1;
       return l.getHeight() - r.getHeight(); 
   }
   
   
   /**
    * private void leftRotate(IAVLNode x)
    * 
    * A utility function to left rotate subtree rooted with x.
    */
   private void leftRotate(IAVLNode x)
   {
	   IAVLNode y = x.getRight(); // set y.
	   x.setRight(y.getLeft()); // turn y's left subtree into x's right subtree.
	   if (y.getLeft() != null) //update parent for y's new subtree.
		   y.getLeft().setParent(x);
	   y.setParent(x.getParent()); // link x's parent to y.
	   if (x.getParent() == null) // root case.
		   this.root = y;
	   else if (x == x.getParent().getLeft()) // x-is-a-left-child case.
		   x.getParent().setLeft(y); //y should be a left child, as x was before.
	   else // x-is-a-right-son case.
		   x.getParent().setRight(y); //y should be a right child, as x was before.
	   y.setLeft(x); // put x as y's left child.
	   x.setParent(y); // set y as x's new parent.
	   
	   // updates heights of nodes.
	   x.setHeight(updateHeight(x));
	   y.setHeight(updateHeight(y));
	   
	   // updates size of nodes.
	   ((AVLNode) x).setSize(updateSize(x));
	   ((AVLNode) y).setSize(updateSize(y));
	   
   }
   
   /**
    * private void rightRotate(IAVLNode x)
    * 
    * A utility function to right rotate subtree rooted with x.
    */
   private void rightRotate(IAVLNode x)
   {
	   IAVLNode y = x.getLeft(); // set y.
	   x.setLeft(y.getRight());; // turn y's right subtree into x's left subtree.
	   if (y.getRight() != null) //update parent for y's new subtree.
		   y.getRight().setParent(x);
	   y.setParent(x.getParent()); // link x's parent to y.
	   if (x.getParent() == null) // root case.
		   this.root = y;
	   else if (x == x.getParent().getLeft()) // x-is-a-left-child case.
		   x.getParent().setLeft(y); //y should be a left child, as x was before.
	   else // x-is-a-right-son case.
		   x.getParent().setRight(y); //y should be a right child, as x was before.
	   y.setRight(x); // put x as y's right child.
	   x.setParent(y); // set y as x's new parent.
	   
	   // updates heights of nodes.
	   x.setHeight(updateHeight(x));
	   y.setHeight(updateHeight(y));
	   
	   // updates size of nodes.
	   ((AVLNode) x).setSize(updateSize(x));
	   ((AVLNode) y).setSize(updateSize(y));
	   
   }
   
   /**
    * private static int updateHeight(IAVLNode x)
    *
    * Utility function.
    * Returns the current height of node x.
    */
   private static int updateHeight(IAVLNode x)
   {
	   IAVLNode l = x.getLeft();
	   IAVLNode r = x.getRight();
	   if ((l == null) && (r == null)) // no children.
		   return 0;
	   else if (l == null) // no left children.
		   return r.getHeight() + 1;
	   else if (r == null) // no right children.
		   return l.getHeight() + 1;
	   return Math.max(l.getHeight(), r.getHeight()) + 1;
   }
   
   /**
    * private static int updateSize(IAVLNode x)
    *
    * Utility function.
    * Returns the current size of node x.
    */
   private static int updateSize(IAVLNode x)
   {
	   IAVLNode l = x.getLeft();
	   IAVLNode r = x.getRight();
	   if ((l == null) && (r == null)) // no children.
		   return 1;
	   else if (l == null) // no left child.
		   return ((AVLNode) r).getSize() + 1;
	   else if (r == null) // no right child.
		   return ((AVLNode) l).getSize() + 1;
	   return ((AVLNode) r).getSize() + ((AVLNode) l).getSize() + 1;
   }
   
   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min()
   {
	   if (minimum == null)
		   return null;
	   return minimum.getValue();
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max()
   {
	   if (maximum == null)
		   return null;
	   return maximum.getValue();
   }
   
   /**
    * public static IAVLNode minInSubTree(IAVLNode root)
    *
    * Returns the node with the smallest key in the sub-tree of a given root
    * or null if the sub-tree is empty
    */
   public static IAVLNode minInSubTree(IAVLNode root)
   {
	   IAVLNode node = root;
	   while (node.getLeft() != null)
	   {
		   node = node.getLeft();
	   }
	   return node;
   }
   /**
    * public static IAVLNode maxInSubTree(IAVLNode root)
    *
    * Returns the node with the biggest key in the sub-tree of a given root
    * or null if the sub-tree is empty
    */
   public static IAVLNode maxInSubTree(IAVLNode root)
   {
	   IAVLNode node = root;
	   while (node.getRight() != null)
	   {
		   node = node.getRight();
	   }
	   return node;
   }
  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
  public int[] keysToArray()
  {
	  int[] arr = new int[size];
	  index = 0;
	  keysOrderWalk(root, arr);
	  index = 0;
      return arr;
  }
  
  /**
   * private void keysOrderWalk(IAVLNode x, int[] arr)
   * 
   * Utility function for in-order walking along the tree.
   * It receives an array and updates each recursive call the appropriate index in an array with nodes' Keys in ascending order.
   * @pre keys in the tree are unique.
   * @post arr contains keys in ascending order.
   * */
  private void keysOrderWalk(IAVLNode x, int[] arr)
  {
	  if (x != null) {
		  keysOrderWalk(x.getLeft(), arr);
		  arr[index] = x.getKey();
		  index++;
		  keysOrderWalk(x.getRight(), arr);
	  }
	  
  }

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray()
  {
	  String[] arr = new String[size];
  	  index = 0;
  	  infoOrderWalk(root, arr);
  	  index = 0;
      return arr;
  }
  
  /**
   * private void infoOrderWalk(IAVLNode x, String[] arr)
   * 
   * Utility function for in-order walking along the tree.
   * It receives an array and updates each recursive call the appropriate index in an array with nodes' Value in ascending order.
   * @pre keys in the tree are unique.
   * @post arr contains Strings' of Keys which ordered ascending.
   * */
  private void infoOrderWalk(IAVLNode x, String[] arr)
  {
	  if (x != null) {
		  infoOrderWalk(x.getLeft(), arr);
		  arr[index] = x.getValue();
		  index++;
		  infoOrderWalk(x.getRight(), arr);
	  }
	  
  }

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   public int size()
   {
	   return size;
   }
   
     /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
   public IAVLNode getRoot()
   {
	   return root;
   }

	/**
	   * public interface IAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IAVLNode{	
		public int getKey(); //returns node's key 
		public String getValue(); //returns node's value [info]
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
    	public void setHeight(int height); // sets the height of the node
    	public int getHeight(); // Returns the height of the node 
	}

   /**
   * public class AVLNode
   *
   * If you wish to implement classes other than AVLTree
   * (for example AVLNode), do it in this file, not in 
   * another file.
   * This class can and must be modified.
   * (It must implement IAVLNode)
   */
  public class AVLNode implements IAVLNode{
	  	private int key;
		private String value;
		private IAVLNode left;
		private IAVLNode right;
		private IAVLNode parent;
		private int height = 0;
		private int size = 1;
	  	
		public AVLNode(int key, String value)
		{
			this.key = key;
			this.value = value;
			
		}
		
		public int getKey()
		{
			return key;
		}
		
		public String getValue()
		{
			return value;
		}
		
		public void setLeft(IAVLNode node)
		{
			left = node;
		}
		
		public IAVLNode getLeft()
		{
			return left;
		}
		
		public void setRight(IAVLNode node)
		{
			right = node;
		}
		
		public IAVLNode getRight()
		{
			return right;
		}
		
		public void setParent(IAVLNode node)
		{
			parent = node;
		}
		
		public IAVLNode getParent()
		{
			return parent;
		}
		
		public void setHeight(int height)
		{
		  this.height = height;
		}
		
		public int getHeight()
		{
			return height;
		}
		
		public void setSize(int size)
		{
			this.size = size;
		}
		
		public int getSize()
	    {
	    	return size;
	    }

  }

}




