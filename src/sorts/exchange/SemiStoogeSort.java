package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class SemiStoogeSort extends Sort {
    public SemiStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Semi-Stooge");
        this.setRunAllSortsName("Semi-Stooge Sort");
        this.setRunSortName("Semi-Stoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    private void semiStoogeSort(int[] A, int i, int j, int d) {
        if (j - i < 2 && Reads.compareValues(A[i], A[j]) == 1) {
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

            Writes.recursion(2);
            this.semiStoogeSort(A, i, j-t, d);
            this.semiStoogeSort(A, i+t, j, d);
        }
    }

    private void demiStoogeSort(int[] A, int i, int j, int d) {
        if(i >= j)
            return;

        Delays.sleep(0.0025);

        Highlights.markArray(1, i);
        Highlights.markArray(2, j);

        if (j - i + 1 >= 2) {
            int h = (j - i + 1) / 2;

            Highlights.markArray(3, i + h);
            Writes.recordDepth(d++);

            Writes.recursion(2);
            this.demiStoogeSort(A, i+h, j, d);
            this.demiStoogeSort(A, i, i+h-1, d);
        }
        if(j - i + 1 >= 3) {
            int t = (j - i + 1) / 3;

            Writes.recordDepth(d++);

            Writes.recursion(2);
            this.demiStoogeSort(A, i, j-t, d);
            this.demiStoogeSort(A, i+t, j, d);
        }
        this.semiStoogeSort(A, i, j, d);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.demiStoogeSort(array, 0, currentLength - 1, 0);
    }
}