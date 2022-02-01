package sorts.insert;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class FFTreeSort extends Sort {
    public FFTreeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("FF Tree");
        this.setRunAllSortsName("Tree Sort (Forest Fire Antibalanced)");
        this.setRunSortName("FF Treesort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    static class Node {
        public static int[] array;
        public Node childLeft = null,
                   childRight = null,
                       parent = null;
        public int index;
        public Node(int loc) {
            this.index = loc;
        }
        public Node(int loc, Node parent) {
            this.index = loc;
            this.parent = parent;
        }
        public Node() {
            this(-1);
        }
        public Node clone() {
            Node self = new Node(this.index, this.parent);
            self.childLeft = this.childLeft;
            self.childRight = this.childRight;
            return self;
        }
        public boolean isLeaf() {
            return childLeft == null && childRight == null;
        }
        public boolean isHalfLeaf() {
            return childLeft == null ^ childRight == null;
        }
        public boolean isStem() {
            return !isLeaf() && !isHalfLeaf();
        }
        public boolean edgeBranch() {
            return parent != null && parent.isHalfLeaf();
        }
        public void rotateLeft() {
            if(childRight == null)
                return;
            Node s = clone();

            s.childRight = this.childRight.childLeft;
            this.childLeft = s;
            this.index = childRight.index;
            this.childRight = this.childRight.childRight;
            if(this.childRight != null) {
                this.childRight.parent = this;
            }
        }
        public void rotateRight() {
            if(childLeft == null)
                return;
            Node s = clone();

            s.childLeft = this.childLeft.childRight;
            this.childRight = s;
            this.index = childLeft.index;
            this.childLeft = this.childLeft.childLeft;
            if(this.childLeft != null) {
                this.childLeft.parent = this;
            }
        }
        public void rotateRightLeft() {
            if(childRight == null)
                return;
            this.childRight.rotateRight();
            this.rotateLeft();
        }
        public void rotateLeftRight() {
            if(childLeft == null)
                return;
            this.childLeft.rotateLeft();
            this.rotateRight();
        }
        public int balance() {
            if(isStem())
                return childLeft.balance() + childRight.balance();
            else if(isLeaf())
                return 0;

            if(childLeft == null)
                return 1 + childRight.balance();
            return -1 + childLeft.balance();
        }
        public boolean isRoot() {
            return parent == null;
        }
        public int depth() {
            return isRoot() ? 0 : parent.depth() + 1;
        }
        public int getVal() {
            return array[index];
        }
    }
    public void insertBranch(Node root, int index) {
        int val = Node.array[index], tree = 1;
        while(true) {
            Highlights.markArray(tree++, root.index);
            Delays.sleep(5);
            if(Reads.compareValues(val, root.getVal()) < 0) {
                if(root.childLeft == null) {
                    root.childLeft = new Node(index, root);
                    while(true) {
                        if(root.balance() == 0) {
                            root.rotateLeft();
                        } else if(root.balance() == -1) {
                            root.rotateRight();
                        } else if(root.balance() == 2) {
                            root.rotateRightLeft();
                        }
                        if(root.parent == null)
                            break;
                        root = root.parent;
                    }
                    break;
                } else {
                    root = root.childLeft;
                }
            } else {
                if(root.childRight == null) {
                    root.childRight = new Node(index, root);
                    while(true) {
                        if(root.balance() == 0) {
                            root.rotateRight();
                        } else if(root.balance() < 0) {
                            root.rotateLeft();
                        } else if(root.balance() == 1) {
                            root.rotateLeftRight();
                        }
                        if(root.parent == null)
                            break;
                        root = root.parent;
                    }
                    break;
                } else {
                    root = root.childRight;
                }
            }
        }
        Highlights.clearAllMarks();
        Writes.changeAuxWrites(1);
        Writes.changeAllocAmount(1);
    }

    public void traverseTree(ArrayList<Integer> tree, Node root, int recursion) {
        Writes.recordDepth(recursion++);
        if(root.childLeft != null) {
            Writes.recursion();
            traverseTree(tree, root.childLeft, recursion);
        }
        Highlights.markArray(1, root.index);
        Delays.sleep(1);
        Writes.changeAuxWrites(1);
        tree.add(root.getVal()); // lose a value and gain
        if(root.childRight != null) {
            Writes.recursion();
            traverseTree(tree, root.childRight, recursion);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        Node.array = array;

        Node root = new Node(0);
        Writes.changeAllocAmount(1);
        for(int i=1; i<length; i++) {
            insertBranch(root, i);
        }

        ArrayList<Integer> fullTree = new ArrayList<>();

        traverseTree(fullTree, root, 0);

        int ptr = fullTree.size() - 1;
        while(fullTree.size() > 0) {
            Writes.write(array, ptr, fullTree.remove(ptr--), 1, true, false);
            Writes.changeAllocAmount(-1);
        }
    }
}
