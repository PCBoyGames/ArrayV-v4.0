package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class StrongHeapSort extends Sort {

    public StrongHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Strong Heap");
        this.setRunAllSortsName("Strong Heap Sort");
        this.setRunSortName("Strong Heapsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int leftChild(int i) {
        return 2 * i + 1;
    }

    private int sibling(int i) {
        if (i == 0) return 0;
        return i + i % 2 - (1 - i % 2);
    }

    private boolean isLeaf(int i, int n) {
        return (i % 2 != 0 ? sibling(i) : leftChild(i)) >= n;
    }

    private void strengtheningSiftDown(int[] array, int a, int i, int n) {
        int x = array[a + i];
        while (!isLeaf(i, n)) {
            int j = sibling(i);
            if (i % 2 == 0) {
                j = leftChild(i);
            } else if (j < n && leftChild(i) < n
                    && Reads.compareIndices(array, a + leftChild(i), a + j, 1, true) >= 0) {
                j = leftChild(i);
            }
            Highlights.markArray(0, a + j);
            Delays.sleep(1);
            if (Reads.compareValues(x, array[a + j]) >= 0) break;
            Writes.write(array, a + i, array[a + j], 0.25, true, false);
            i = j;
        }
        Writes.write(array, a + i, x, 0.25, true, false);
    }

    public void sort(int[] array, int a, int b) {
        int sortLength = b - a;
        for (int i = sortLength - 1; i >= 0; i--) {
            strengtheningSiftDown(array, a, i, sortLength);
        }
        for (int i = sortLength - 1; i >= 0; i--) {
            int tmp2 = array[a];
            Writes.write(array, a, array[a + i], 0.25, true, false);
            strengtheningSiftDown(array, a, 0, i);
            Writes.write(array, a + i, tmp2, 0.25, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
