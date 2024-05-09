package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Haruki
extending code by Anonymous0726

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * An iterative implementation of a tree sort using a red-black tree, based on
 * what I learned on GitHub and Wikipedia.
 * <p>
 * To use this algorithm in another, use {@code treeSort()} from a reference
 * instance.
 * 
 * @author Haruki (Flandre-chan0331 on GitHub)
 */
public class RedBlackTreeSortIterative extends Sort {

    public RedBlackTreeSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Red-Black Tree (Iterative)");
        this.setRunAllSortsName("Tree Sort (Red-Black Balanced)");
        this.setRunSortName("Treesort (Red-Black Balanced)");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private static final int COLOR_BLACK = 0, COLOR_RED = 1;
    
    private Node nilNode = new Node(); // sentinel value to simplify logic
    
    /**
     * The fundamental building block of any programming tree. Each node is the root
     * of its own subtree.
     * <p>
     * In general, a node has the data being stored at that point, up to two
     * children nodes, up to one parent node, and (optionally) data for keeping the
     * tree balanced. In the case of a red-black tree, the data for balancing is a
     * boolean telling the node's "color": red or black.
     */
    class Node {
        int idx; // index in main array of the element contained here 
        int color; // color of node, can be either COLOR_RED or COLOR_BLACK
        Node left, right;
        Node parent; // required for iterative insertion and traversal
        
        // Default constructor, and constructor for nilNode
        public Node() {
            this.idx = -1; // Shouldn't point to anything by default
            this.left = this.right = this.parent = null;
            this.color = COLOR_BLACK; // All nilNodes being black will not violate black balance
        }
        
        // Constructor for a node with a pointer
        public Node(int idx) {
            this();
            this.idx = idx;
            this.color = COLOR_RED; // A node with data, when first created, is red
        }
    }
    
    /**
     * The class containing the info for a red-black tree.
     */
    class RedBlackTree {
        int[] array; // main array
        Node root; // == nilNode if tree is empty
        public RedBlackTree(int[] array) {
            root = nilNode;
            this.array = array;
        }
        
        // Wrapper for counting node writes and timing them
        private Node treeWrite(Node element) {
            Node node;
            Writes.changeAuxWrites(1);
            Writes.startLap();
            node = element;
            Writes.stopLap();
            Delays.sleep(0.25);
            return node;
        }
        private Node rotateLeft(Node p) {
            Node g = p.parent;
            Node s = p.right;
            Node c = s.left;
            p.right = treeWrite(c);
            if (c != nilNode) c.parent = treeWrite(p);
            s.left = treeWrite(p);
            p.parent = treeWrite(s);
            s.parent = treeWrite(g);
            if (g == nilNode) this.root = treeWrite(s);
            else if (p == g.right) g.right = treeWrite(s);
            else g.left = treeWrite(s);
            return s;
        }
        private Node rotateRight(Node p) {
            Node g = p.parent;
            Node s = p.left;
            Node c = s.right;
            p.left = treeWrite(c);
            if (c != nilNode) c.parent = treeWrite(p);
            s.right = treeWrite(p);
            p.parent = treeWrite(s);
            s.parent = treeWrite(g);
            if (g == nilNode) this.root = treeWrite(s);
            else if (p == g.right) g.right = treeWrite(s);
            else g.left = treeWrite(s);
            return s;
        }
        
        // iterative (Anonymous0726's RedBlackTreeSort.java goes recursive)
        private void insertFixup(Node n) { // color of node n is red
            // While the Red-Black tree invariants are violated...
            while (n.parent != root && n.parent.color == COLOR_RED) {
                /*
                 * p: parent of n
                 * g: grandparent of n
                 * u: uncle of n
                 */
                Node p = n.parent;
                Node g = p.parent;
                if (p == g.left) {
                    Node u = g.right;
                    // case I2 - uncle and parent are both red
                    // re-color both of them to black
                    if (u.color == COLOR_RED) {
                        p.color = COLOR_BLACK;
                        u.color = COLOR_BLACK;
                        g.color = COLOR_RED;
                        // grandparent was re-colored to red, so in next iteration we
                        // check if it does not break Red-Black property
                        n = g;
                    } else { // case I56: uncle is black and parent is red
                        if (n == p.right) { // case I5, first rotate left
                            rotateLeft(p);
                            n = p;
                            p = g.left;
                        }
                        // case I6
                        rotateRight(g);
                        p.color = COLOR_BLACK; // This will end the fixup loop
                        g.color = COLOR_RED;
                    }
                } else { // p == g.right
                    Node u = g.left;
                    // case I2 - uncle and parent are both red
                    // re-color both of them to black
                    if (u.color == COLOR_RED) {
                        p.color = COLOR_BLACK;
                        u.color = COLOR_BLACK;
                        g.color = COLOR_RED;
                        // grandparent was re-colored to red, so in next iteration we
                        // check if it does not break Red-Black property
                        n = g;
                    } else { // case I56: uncle is black and parent is red
                        if (n == p.left) { // case I5, first rotate left
                            rotateRight(p);
                            n = p;
                            p = g.right;
                        }
                        // case I6
                        rotateLeft(g);
                        p.color = COLOR_BLACK; // This will end the fixup loop
                        g.color = COLOR_RED;
                    }
                }
            }
            root.color = COLOR_BLACK; // Root of a red-black tree must always be black
        }
        public void insert(int idx) {
            Node x = root, y = nilNode;
            while (x != nilNode) {
                y = x;
                if (Reads.compareIndices(array, idx, x.idx, 0.25, true) < 0)
                    x = x.left;
                else
                    x = x.right;
            }
            Node z = new Node(idx); // z's color is red
            z.left = z.right = nilNode;
            Writes.changeAllocAmount(1);
            z.parent = treeWrite(y);
            if (y == nilNode)
                this.root = treeWrite(z);
            else if (Reads.compareIndices(array, z.idx, y.idx, 0.25, true) < 0)
                y.left = treeWrite(z);
            else
                y.right = treeWrite(z);
            insertFixup(z);
        }
        private Node bstMinimum(Node p) {
            if (p != nilNode)
                while (p.left != nilNode)
                    p = p.left;
            return p;
        }
        private Node bstSucessor(Node x) {
            if (x == nilNode) return nilNode;
            if (x.right != nilNode)
                return bstMinimum(x.right);
            Node y = x.parent;
            while (y != nilNode && x == y.right) {
                x = y;
                y = y.parent;
            }
            return y;
        }
        
        // also iterative (requires the parent node)
        public void writeToArray(int[] tmpArray) {
            int idx = 0;
            for (Node x = bstMinimum(root); x != nilNode; x = bstSucessor(x)) {
                Highlights.markArray(1, x.idx);
                Writes.write(tmpArray, idx++, array[x.idx], 0.1, false, true);
            }
        }
    }
    
    /**
     * Sorts the range {@code [a, b)} of {@code array} using Red-Black Tree Sort.
     * 
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     */
    public void treeSort(int[] array, int a, int b) {
        int n = b - a;
        RedBlackTree tree = new RedBlackTree(array);
        for (int i = a; i < b; i++) tree.insert(i);
        Highlights.clearAllMarks();
        int[] tmp = new int[n];
        tree.writeToArray(tmp);
        Writes.arraycopy(tmp, 0, array, a, n, 1, true, false);
        Writes.changeAllocAmount(-n);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        treeSort(array, 0, sortLength);

    }

}
