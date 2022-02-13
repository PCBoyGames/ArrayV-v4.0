package sorts.quick;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.select.ExpliciumSort;
import sorts.templates.Sort;

final public class TrickSort extends Sort {
    public TrickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Trick");
        this.setRunAllSortsName("Trick Sort");
        this.setRunSortName("Tricksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    class Node {
        public ArrayList<Integer> values;
        public Node left, right, parent;
        public boolean container;
        public int index;
        public Node() {
            values = new ArrayList<>();
        }
        public Node(int startingVal, int sIndex) {
            this();
            this.index = sIndex;
            values.add(startingVal);
        }
        public Node clone() {
            Node self = new Node();
            self.values = this.values;
            self.left = this.left;
            self.right = this.right;
            return self;
        }
        public int height() {
            if(left == null) {
                if(right == null)
                    return 0;
                return right.height() + 1;
            } else if(right == null) {
                return left.height() + 1;
            } else {
                return Math.max(left.height(), right.height()) + 1;
            }
        }
        public int balance() {
            if(left == null) {
                if(right == null)
                    return 0;
                return right.height();
            } else if(right == null)
                return -left.height();
            return right.height() - left.height();
        }
        public void rotateLeft() {
            if(right == null)
                return;
            Node s = clone();

            s.right = this.right.left;
            this.left = s;
            this.values = right.values;
            this.right = this.right.right;
            if(this.right != null) {
                this.right.parent = this;
            }
        }
        public void rotateRight() {
            if(left == null)
                return;
            Node s = clone();

            s.left = this.left.right;
            this.right = s;
            this.values = left.values;
            this.left = this.left.left;
            if(this.left != null) {
                this.left.parent = this;
            }
        }
        public void rotateRightLeft() {
            if(right == null)
                return;
            this.right.rotateRight();
            this.rotateLeft();
        }
        public void rotateLeftRight() {
            if(left == null)
                return;
            this.left.rotateLeft();
            this.rotateRight();
        }
        public int compare(int val) {
            Highlights.markArray(1, index);
            Delays.sleep(0.5);
            return Reads.compareValues(values.get(0), val);
        }
    }

    public void nodeInsert(Node root, int v, int i) {
        while(true) {
            if(root.compare(v) <= 0) {
                if(root.right == null) {
                    root.right = new Node(v, i);
                    root.right.parent = root;
                    break;
                }
                root = root.right;
            } else {
                if(root.left == null) {
                    root.left = new Node(v, i);
                    root.left.parent = root;
                    break;
                }
                root = root.left;
            }
        }
        Writes.changeAllocAmount(1);
        Writes.changeAuxWrites(1);
        root = root.parent;
        while(root != null) {
            if(root.balance() > 1) {
                if(root.right != null && root.right.balance() < 0) {
                    root.rotateRightLeft();
                } else
                    root.rotateLeft();
                Writes.changeAuxWrites(1);
            } else if(root.balance() < 1) {
                if(root.left != null && root.left.balance() > 0) {
                    root.rotateLeftRight();
                } else
                    root.rotateRight();
                Writes.changeAuxWrites(1);
            }
            root = root.parent;
        }
    }

    public void insertVal(Node root, int v) {
        while(true) {
            if(root.compare(v) <= 0) {
                if(root.right.container) {
                    Writes.arrayListAdd(root.right.values, v);
                    Writes.changeAuxWrites(1);
                    break;
                }
                root = root.right;
            } else {
                if(root.left.container) {
                    Writes.arrayListAdd(root.left.values, v);
                    Writes.changeAuxWrites(1);
                    root.left.parent = root;
                    break;
                }
                root = root.left;
            }
        }
    }
    private void addEmpty(Node root) {
        // no need to parent, we aren't balancing the partitions
        if(root.left == null) {
            root.left = new Node();
            root.left.container = true;
        } else
            addEmpty(root.left);
        if(root.right == null) {
            root.right = new Node();
            root.right.container = true;
        } else
            addEmpty(root.right);
    }
    private int putContainers(int[] array, int loc, Node root, int bad) {
        if(root == null) return 0;
        if(root.container) {
            int n = loc;
            for(int i : root.values) {
                Writes.write(array, n++, i, 2.5, true, false);
            }
            Writes.deleteArrayList(root.values);
            trick(array, loc, n-1, bad-1);
            return root.values.size();
        }
        int l = putContainers(array, loc, root.left, bad);
        return l + putContainers(array, loc + l, root.right, bad);
    }
    private int putNonCont(int[] tmp, int loc, Node root) {
        if(root == null || root.container) return 0;
        int l = putNonCont(tmp, loc, root.left);
        Writes.write(tmp, loc+l++, root.values.get(0), 1, true, true);
        Writes.deleteArrayList(root.values);
        return l + putNonCont(tmp, loc + l, root.right);
    }
    private void oopmerge(int[] arr0, int start0, int end0, int[] arr1, int start1, int end1) {
        int[][] table = new int[][] {arr0, arr1};
        int[] ptrs = new int[] {start0, start1},
              vals = new int[] {end0-start0, end1-start1};
        int cmp, to = start1 - (end0 - start0);
        while(vals[0] > 0 && vals[1] > 0) {
            cmp = -(Reads.compareValues(arr1[ptrs[1]], arr0[ptrs[0]]) >> 31);
            Writes.write(arr1, to++, table[cmp][ptrs[cmp]++], 1, true, false);
            --vals[cmp];
        }
        while(vals[0] > 0) {
            Writes.write(arr1, to++, arr0[ptrs[0]++], 1, true, false);
            --vals[0];
        }
    }
    private void trick(int[] array, int i, int j, int bad) {
        if(j <= i) return;
        if(j-i < 32 || bad < 0) {
            ExpliciumSort e = new ExpliciumSort(arrayVisualizer);
            e.Explic(array, i, j+1);
            return;
        }
        int sz = (int) Math.sqrt(j-i+1);
        Node pivs = new Node(array[i], i);
        for(int k=i+1; k<i+sz; k++) {
            nodeInsert(pivs, array[k], k);
        }
        addEmpty(pivs);
        for(int k=i+sz; k<=j; k++) {
            Highlights.markArray(2, k);
            insertVal(pivs, array[k]);
        }
        Highlights.clearMark(2);
        putContainers(array, i+sz, pivs, bad);
        int[] merge = Writes.createExternalArray(sz);
        putNonCont(merge, 0, pivs);
        oopmerge(merge, 0, sz, array, i+sz, j+1);
        Writes.deleteExternalArray(merge);
    }
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int bad = 1;
        while(1 << ++bad <= currentLength / 2);
        trick(array, 0, currentLength-1, bad);
    }
}