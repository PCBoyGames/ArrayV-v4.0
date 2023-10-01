package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author aphitorite
 */
public class TriangularHeapSort extends Sort {

    public TriangularHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Triangular Heap");
        this.setRunAllSortsName("Triangular Heap Sort");
        this.setRunSortName("Triangular Heapsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public static int triangularRoot(int val) {
        return ((int) Math.sqrt((8 * val + 1)) - 1) / 2;
    }

    private void siftDown(int[] array, int end, int root, boolean shuffle) {
        int temp = array[root];
        int len = triangularRoot(root);
        int left = root + len + 1;
        int right = left + 1;

        while (left < end) {
            if (right >= end) {
                this.Highlights.markArray(2, left);
                if (this.Reads.compareValues(array[left], temp) == 1) {
                    this.Writes.write(array, root, array[left], shuffle ? 0 : 0.25D, true, false);
                }

                break;
            }
            int max = (this.Reads.compareValues(array[left], array[right]) >= 0) ? left : right;

            this.Highlights.markArray(2, max);
            if (this.Reads.compareValues(array[max], temp) == 1) {
                this.Writes.write(array, root, array[max], shuffle ? 0 : 0.25D, true, false);

                root = max;
                len = triangularRoot(root);
                left = root + len + 1;
                right = left + 1;
                continue;
            }
            break;
        }
        this.Writes.write(array, root, temp, shuffle ? 0 : 0.25D, true, false);
    }

    public void triangularHeapify(int[] array, int length, boolean shuffle) {
        for (int i = length - 1; i >= 0; i--) {
            siftDown(array, length, i, shuffle);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        triangularHeapify(array, length, false);
        for (int i = 1; i < length - 1; i++) {
            this.Writes.swap(array, 0, length - i, 0.5D, true, false);
            siftDown(array, length - i, 0, false);
        }
        if (this.Reads.compareValues(array[0], array[1]) == 1)
            this.Writes.swap(array, 0, 1, 0.5D, true, false);

    }

}
