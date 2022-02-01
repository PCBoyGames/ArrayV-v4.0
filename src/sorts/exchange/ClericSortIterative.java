package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.IterativeClericSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ClericSortIterative extends IterativeClericSorting {
    public ClericSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cleric (Iterative)");
        this.setRunAllSortsName("Iterative Cleric Sort");
        this.setRunSortName("Iterative Clericsort");
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
        clericSortRoutine(array, length, 0.05);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.end = sortLength;
        int n = 1;
        for(; n < sortLength; n*=2);
        int numberOfSwaps = 0;
        do numberOfSwaps = clericSortRoutine(array, n, 0.05); while (numberOfSwaps != 0);
    }
}