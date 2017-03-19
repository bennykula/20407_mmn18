import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Comparator;
import java.util.Iterator;

public class RedBlackMax<Value> implements Iterable<Value>{ 

    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    protected Node root;     // root of the BST
    protected Comparator<Value> comp;
    protected Node max;
    // BST helper node data type
    protected class Node {
        protected Value val;         // associated data
        private Node left, right;  // links to left and right subtrees
        private Node parent;
        private boolean color;     // color of parent link
        private int size;          // subtree count

        public Node(Value val, boolean color, int size) {
            this.val = val;
            this.color = color;
            this.size = size;
            parent=null;
        }
        public void setRight(Node r) {
        	right=r;
        	if(right!=null)
        		right.parent=this;
        }
        public void setLeft(Node l) {
        	left=l;
        	if(left!=null)
        		left.parent=this;
        }

    }

    /**
     * Initializes an empty symbol table.
     */
    public RedBlackMax(Comparator<Value> comp) {
    	this.comp=comp;
    }

   /***************************************************************************
    *  Node helper methods.
    ***************************************************************************/
    // is node x red; false if x is null ?
    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    // number of node in subtree rooted at x; 0 if x is null
    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    } 


    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return size(root);
    }

   /**
     * Is this symbol table empty?
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }


   /***************************************************************************
    *  Standard BST search.
    ***************************************************************************/

    /**
     * Returns the value associated with the given key.
     */
    public Value get(Value val) {
        if (val == null) throw new IllegalArgumentException("argument to get() is null");
        return get(root, val);
    }

    private Value get(Node x, Value val) {
        while (x != null) {
            int cmp = comp.compare(val, x.val);
            if      (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else              return x.val;
        }
        return null;
    }

    /**
     * Does this symbol table contain the given key?
     */
    public boolean contains(Value val) {
        return get(val) != null;
    }

   /***************************************************************************
    *  Red-black tree insertion.
    ***************************************************************************/

    public void put(Value val) {
        if (val == null) throw new IllegalArgumentException("Argument to put() is null");
        root = put(root, val);
        root.color = BLACK;
        // assert check();
    }

    // insert  in the subtree rooted at h
    private Node put(Node h, Value val) { 
        if (h == null) {
        	Node n = new Node(val, RED, 1);
        	if(max==null) {
        		max=n;
        	} else if(comp.compare(n.val, max.val)>=0) {
        		max=n;
        	}
        	return n;
        }

        int cmp = comp.compare(val, h.val);
        if      (cmp < 0) h.setLeft(put(h.left, val)); 
        else if (cmp > 0) h.setRight(put(h.right, val)); 
        else              h.val   = val;

        // fix-up any right-leaning links
        if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
        if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);
        h.size = size(h.left) + size(h.right) + 1;

        return h;
    }

   /***************************************************************************
    *  Red-black tree deletion.
    ***************************************************************************/
    /**
     * Removes the smallest value from the symbol table.
     * @throws NoSuchElementException if the symbol table is empty
     */
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = deleteMin(root);
        if (!isEmpty()) root.color = BLACK;
        // assert check();
    }

    // delete the value with the minimum key rooted at h
    private Node deleteMin(Node h) { 
        if (h.left == null)
            return null;

        if (!isRed(h.left) && !isRed(h.left.left))
            h = moveRedLeft(h);

        h.setLeft(deleteMin(h.left));
        return balance(h);
    }


    public void delete(Value val) { 
        if (val == null) throw new IllegalArgumentException("argument to delete() is null");
        if (!contains(val)) return;
        if(comp.compare(val, max.val)==0) {
        	max=predecessor(max);
        }

        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = delete(root, val);
        if (!isEmpty()) root.color = BLACK;
    }

    // delete the value with the given key rooted at h
    private Node delete(Node h, Value val) { 
        if (comp.compare(val, h.val) < 0)  {
            if (!isRed(h.left) && !isRed(h.left.left))
                h = moveRedLeft(h);
            h.setLeft(delete(h.left, val));
        }
        else {
            if (isRed(h.left))
                h = rotateRight(h);
            if (comp.compare(val, h.val) == 0 && (h.right == null))
                return null;
            if (!isRed(h.right) && !isRed(h.right.left))
                h = moveRedRight(h);
            if (comp.compare(val, h.val) == 0) {
                Node x = min(h.right);
                h.val = x.val;
                h.setRight(deleteMin(h.right));
            }
            else h.setRight(delete(h.right, val));
        }
        return balance(h);
    }

   /***************************************************************************
    *  Red-black tree helper functions.
    ***************************************************************************/

    // make a left-leaning link lean to the right
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.setLeft(x.right);
        x.setRight(h);
        x.color = x.right.color;
        x.right.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // make a right-leaning link lean to the left
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.setRight(x.left);
        x.setLeft(h);
        x.color = x.left.color;
        x.left.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node h) {
        // h must have opposite color of its two children
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) { 
            h.setRight(rotateRight(h.right));
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) { 
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    // restore red-black tree invariant
    private Node balance(Node h) {
        if (isRed(h.right))                      h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right))     flipColors(h);

        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }


   /***************************************************************************
    *  Utility functions.
    ***************************************************************************/

    /**
     * Returns the height of the BST (for debugging).
     * @return the height of the BST (a 1-node tree has height 0)
     */
    public int height() {
        return height(root);
    }
    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }
    public Value min() {
        if (isEmpty()) throw new NoSuchElementException("called min() with empty symbol table");
        return min(root).val;
    } 

    private Node min(Node x) { 
        if (x.left == null) return x; 
        else                return min(x.left); 
    } 
    
    public Value max() {
        if (isEmpty()) throw new NoSuchElementException("called min() with empty symbol table");
        return max.val;
    } 
    
    private Node max(Node x) { 
        if (x.right == null) return x; 
        else                return max(x.right); 
    } 

    private Node predecessor(Node x) {
    	System.out.println("predeccesor was called");
    	System.out.println("X is " + x.val);
    	Node p;
    	Node curr,prev;
    	if(x==null)
    		return null;
    	if(x.left != null)
    		return max(x.left);
    	p = x.parent;
    	curr=p;
    	prev=x;
    	while(curr!=null && prev==curr.left) {
    		prev=curr;
    		curr=curr.parent;
    	}
    	System.out.println("pre is "+ curr.val);
    	return curr;
    }
    /*public Value max() {
        if (isEmpty()) throw new NoSuchElementException("called max() with empty symbol table");
        return root.max.val;
    }*/

	@Override
	public Iterator<Value> iterator() {
		return new BSTIterator(root);
	} 
	private class BSTIterator implements Iterator<Value> {
		private Stack<Node> stack;
		
		
		public BSTIterator(Node root) {
			stack=new Stack<Node>();
			stack.push(root);
		}
		
		@Override
		public boolean hasNext() {
			while(!stack.isEmpty() && stack.peek()==null) {
				stack.pop();
			}
			return !stack.isEmpty();
		}

		@Override
		public Value next() {
			Node current;
	        if (!hasNext()) {
	            throw new NoSuchElementException("No more nodes in tree!");
	        }
			current=stack.pop();
			while(current!=null) {
				stack.push(current);
				current=current.left;
			}
			if(current==null && !stack.isEmpty()) {
				return stack.pop().val;
			}
			return null;
		}
		
	}
}


