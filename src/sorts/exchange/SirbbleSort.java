package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SirbbleSort extends Sort {
    public SirbbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Sirbble");
        this.setRunAllSortsName("Sirbble Sort");
        this.setRunSortName("Sirbble Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int circle(int[] array, int a, int b) {
        for (; a < b; a++, b--) if (Reads.compareIndices(array, a, b, 0.1, true) > 0) Writes.swap(array, a, b, 0.1, true, false);
        return a - 1;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 0; i < currentLength; i++) {
            int mid = circle(array, i, currentLength - 1);
            for (int j = mid; j >= i; j--) if (Reads.compareIndices(array, j, j + 1, 0.1, true) > 0) Writes.swap(array, j, j + 1, 0.1, true, false);
        }
    }
}