package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Gaming32
 * @author Kiriko-chan
 *
 */
public final class AdaptiveSwapMergeSort extends Sort {

    public AdaptiveSwapMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive SwapMerge");
        this.setRunAllSortsName("Adaptive SwapMerge Sort");
        this.setRunSortName("Adaptive SwapMergeSort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int rightBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m + 1;
        }

        return a;
    }

    private int rightExpSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while(b-i >= a && Reads.compareValues(val, array[b-i]) < 0) i *= 2;

        return this.rightBinSearch(array, Math.max(a, b-i+1), b-i/2, val);
    }

    private int leftBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
            i *= 2;

        return this.rightBinSearch(array, a + i / 2, Math.min(b, a - 1 + i), val);
    }

    private void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);

        if(a > b) {
            int temp = array[a];

            do Writes.write(array, a, array[--a], 0.25, true, false);
            while(a > b);

            Writes.write(array, b, temp, 0.25, true, false);
        }
    }

    protected void insertSort(int[] array, int a, int b) {
        for(int i = a + 1; i < b; i++)
            insertTo(array, i, rightExpSearch(array, a, i, array[i]));
    }

    protected void merge(int[] array, int a, int m, int b) {
        int i = a, j = m;
        while(i < j && j < b) {
            if(Reads.compareValues(array[i], array[j]) > 0)
                Writes.multiSwap(array, j++, i, 0.025, true, false);
            i++;
        }
    }

    public void smartMerge(int[] array, int a, int m, int b) {
        if(Reads.compareIndices(array, m - 1, m, 0.125, true) <= 0)
            return;
        a = leftBoundSearch(array, a, m, array[m]);
        merge(array, a, m, b);
    }

    public void mergeSort(int[] array, int a, int b) {
        if(b - a < 32) {
            insertSort(array, a, b);
            return;
        }
        int m = a + (b - a) / 2;
        mergeSort(array, a, m);
        mergeSort(array, m, b);
        smartMerge(array, a, m, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        mergeSort(array, 0, sortLength);

    }

}
