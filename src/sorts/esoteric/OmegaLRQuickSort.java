package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.Statistics;

public class OmegaLRQuickSort extends Sort {
    public OmegaLRQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Omega Left/Right Quick");
        this.setRunAllSortsName("Omega Quick Sort, Left/Right Pointers");
        this.setRunSortName("\u03a9 LR Quicksort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void omegaReversal(int[] array, int a, int b, double sleep) {
        Statistics.addStat("Omega Reversal");
        Writes.swap(array, a, b, sleep, true, false);

        for (int i = a + 1; i < a + ((b - a + 1) / 2); i++) {
            Writes.swap(array, i, a + b - i, sleep, true, false);
            omegaReversal(array, i + 1, a + b - i - 1, sleep);
            quickSort(array, i + 1, a + b - i - 1);
        }
    }

    private int partition(int[] array, int lo, int hi) {
        int pivot = array[hi];
        int left = lo, right = hi;
        while (left<right) {
            while (left<hi&&Reads.compareValues(array[left], pivot) == -1) {
                left++;
                Highlights.markArray(1, left);
                quickSort(array,left,right-1);
            }
            while (right>lo&&Reads.compareValues(array[right], pivot) == 1) {
                right--;
                Highlights.markArray(2, right);
                quickSort(array,left+1,right);
            }
            if (left<right) {
                omegaReversal(array, left++, right--, 0.01);
            }
        }
        return left;
    }

    private void quickSort(int[] array, int lo, int hi) {
        if (lo < hi) {
            int p = this.partition(array, lo, hi);
            if (p<hi)
                this.quickSort(array, lo, p);
            this.quickSort(array, p+1, hi);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        Statistics.putStats("Omega Reversal");
        this.quickSort(array, 0, currentLength - 1);
    }
}