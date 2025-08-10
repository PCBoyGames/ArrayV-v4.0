package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author aphitorite
 * 
 */
public class PairwiseCircleSort extends Sort {
    public PairwiseCircleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pairwise-Circle (aphitorite)");
        this.setRunAllSortsName("aphitorite's Pairwise-Circle Sort");
        this.setRunSortName("aphitorite's Pairwise-Circle Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public boolean singlePass(int[] array, int start, int end, double sleep) {
        if (end - start < 2) return false;
        int b = start + 1;
        boolean anySwap = false;
        while (b < end){
            if(Reads.compareIndices(array, b - 1, b, sleep, true) > 0)
                Writes.swap(array, b - 1, b, sleep, anySwap = true, false);
            b += 2;
        }
        int a = 1;
        while (a < (end - start)) a = (a * 2) + 1;
        b = start + 1;
        while (b + 1 < end){
            int c = a;
            while (c > 1){
                c /= 2;
                if (b + c < end){
                    if(Reads.compareIndices(array, b, b + c, sleep, true) > 0)
                        Writes.swap(array, b, b + c, sleep, anySwap = true, false);
                }
            }
            b += 2;
        }
        return anySwap;
    }

    public void sort(int[] array, int a, int b) {
        while (singlePass(array, a, b, 0.5));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);
    }
}
