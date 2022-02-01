package sorts.insert;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class SimplifiedTreeSort extends Sort {
    public SimplifiedTreeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Simplified Tree");
        this.setRunAllSortsName("Simplified Tree Sort (Unbalanced)");
        this.setRunSortName("Simplified Treesort (Unbalanced)");
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
                   childRight = null;
        public int index;
        public Node(int loc) {
            this.index = loc;
        }
        public Node() {
            this(-1);
        }
        public int getVal() {
            return array[index];
        }
    }

    public void insertBranch(Node root, int index) { // iterative (A0726's TreeSort.java goes recursive)
        int val = Node.array[index];
        int tree = 1;
        while(true) {
            Highlights.markArray(tree++, root.index);
            Delays.sleep(5);
            if(Reads.compareValues(val, root.getVal()) < 0) {
                if(root.childLeft == null) {
                    root.childLeft = new Node(index);
                    break;
                } else {
                    root = root.childLeft;
                }
            } else {
                if(root.childRight == null) {
                    root.childRight = new Node(index);
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

    public void traverseTree(ArrayList<Integer> tree, Node root, int recursion) { // also recursive (otherwise I'd have to do some weird and messy shenanigans to get it iterative)
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
        int ptr = 0;
        while(fullTree.size() > 0) {
            Writes.write(array, ptr++, fullTree.remove(0), 1, true, false);
            Writes.changeAllocAmount(-1);
        }
    }
}
