package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class SqueegeeSort extends Sort {
    public SqueegeeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Squeegee");
        this.setRunAllSortsName("Squeegee Sort");
        this.setRunSortName("Squeegeesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(128);
        this.setBogoSort(false);
    }

    private void stoogeSort(int[] A, int i, int j, int d) {
        if (Reads.compareValues(A[i], A[j]) == 1) {
            Writes.reversal(A, i, j, 0.05, true, false);
        }

        Delays.sleep(0.0025);

        Highlights.markArray(1, i);
        Highlights.markArray(2, j);

        if (j - i + 1 >= 3) {
            int t = (j - i + 1) / 3;

            Highlights.markArray(3, j - t);
            Highlights.markArray(4, i + t);
            Writes.recordDepth(d++);

            Writes.recursion(3);
            this.stoogeSort(A, i, j-t, d);
            this.stoogeSort(A, i+2*t, j, d);
            this.stoogeSort(A, i, j-t, d);
            if(j-i+1 > 3) {
                Writes.recursion(2);
                this.stoogeSort(A, j-2*t, j, d);
                this.stoogeSort(A, i, j-t, d);
            }
        }
        if (Reads.compareValues(A[i], A[j]) == 1) {
            Writes.reversal(A, i, j, 0.005, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.stoogeSort(array, 0, currentLength - 1, 0);
    }
}