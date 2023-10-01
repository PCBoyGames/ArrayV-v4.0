package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class OptimizedReverseGrateSort extends Sort {
    public OptimizedReverseGrateSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Optimized Reverse Grate");
        setRunAllSortsName("Optimized Reverse Grate Sort");
        setRunSortName("Optimized Reverse Gratesort");
        setCategory("Exchange Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(true);
        setUnreasonableLimit(4096);
        setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) throws Exception {
        int bound = currentLength - 1;
        int left = 0;
        int firstswap = 0;
        int lastswap = 0;
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = left; i < bound; i++) {
                for (int j = i + 1; j <= bound; j++) {
                    if (Reads.compareIndices(array, i, j, 0.125, true) > 0) {
                        if (sorted) firstswap = i;
                        Writes.swap(array, lastswap = i, j, 0.125, true, sorted = false);
                        break;
                    }
                }
            }
            bound = lastswap;
            left = firstswap;
        }
    }
}