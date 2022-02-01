package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class GnoomeSort extends Sort {
    public GnoomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Gnoome");
        this.setRunAllSortsName("Gnoome Sort");
        this.setRunSortName("Gnoomesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    private void stoogeSort(int[] A, int i, int j, int d) {
        if (Reads.compareValues(A[i], A[j]) == 1) {
            Writes.swap(A, i, j, 0.005, true, false);
        }

        Delays.sleep(0.0025);

        Highlights.markArray(1, i);
        Highlights.markArray(2, j);

        if (j - i + 1 >= 3) {
            int t = (j - i + 1) / 3;

            Highlights.markArray(3, j - t);
            Highlights.markArray(4, i + t);
            Writes.recordDepth(d++);

            Writes.recursion(5);
            this.stoogeSort(A, i, j-t, d);
            this.stoogeSort(A, i, i+t, d);
            this.stoogeSort(A, i+t, j, d);
            this.stoogeSort(A, i+t, j-t, d);
            this.stoogeSort(A, i, i+t, d);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for(int i=1; i<currentLength; i++) this.stoogeSort(array, 0, i, 0);
    }
}