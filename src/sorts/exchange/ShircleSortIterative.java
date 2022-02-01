package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.IterativeShircleSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ShircleSortIterative extends IterativeShircleSorting {
    public ShircleSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Shircle (Iterative)");
        this.setRunAllSortsName("Iterative Shircle Sort");
        this.setRunSortName("Iterative Shirclesort");
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
        shircleSortRoutine(array, length, 0.01);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        end = sortLength;
        int n = 1;
        for(; n < sortLength; n*=2);
        int numberOfSwaps = 0;
        do numberOfSwaps = shircleSortRoutine(array, n, 0.01); while (numberOfSwaps != 0);
    }
}