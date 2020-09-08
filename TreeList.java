

/**
 *
 * Tree list
 * 
 * 315838185
 * Noy Shabtay
 * noyshabtay
 * 
 * 312126436
 * Roy Naftaly
 * roynaftaly
 *
 * An implementation of a Tree list with  key and info
 *
 */
public class TreeList{
	 
	public AVLTree tree;
 
	public TreeList()
	{
		 tree = new AVLTree();
	}
 /**
   * public Item retrieve(int i)
   *
   * returns the item in the ith position if it exists in the list.
   * otherwise, returns null
   */
	public Item retrieve(int i)
	{
		AVLTree.IAVLNode node = tree.treeSelect(i+1);
		if (node == null)
			return null;
		return new Item(node.getKey(),node.getValue());  // to be replaced by student code
	}

  /**
   * public int insert(int i, int k, String s) 
   *
   * inserts an item to the ith position in list  with key k and  info s.
   * returns -1 if i<0 or i>n otherwise return 0.
   */
   public int insert(int i, int k, String s) {
	  if (i < 0 || i > tree.size())
		  return -1;
	  tree.listInsert(i+1, k, s);
	  return 0;
   }

  /**
   * public int delete(int i)
   *
   * deletes an item in the ith posittion from the list.
	* returns -1 if i<0 or i>n-1 otherwise returns 0.
   */
   public int delete(int i)
   {
	   if (i < 0 || i> tree.size() -1)
		   return -1;
	   tree.listDelete(i+1);
	   return 0;
   }
   
 }