package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.ShircleSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ShircleSortRecursive extends ShircleSorting {
    public ShircleSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Shircle (Recursive)");
        this.setRunAllSortsName("Recursive Shircle Sort");
        this.setRunSortName("Shirclesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void singleRoutine(int[] array, int length) {
        end = length;
        shircleSortRoutine(array, 0, length - 1, 0, 0.01, 0);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        end = sortLength;
        int n = 1;
        for(; n < sortLength; n*=2);
        int numberOfSwaps = 0;
        do numberOfSwaps = shircleSortRoutine(array, 0, n - 1, 0, 0.01, 0); while (numberOfSwaps != 0);
    }
}