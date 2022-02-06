package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SearchSort extends Sort {
    public SearchSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Search");
        this.setRunAllSortsName("Search Sort");
        this.setRunSortName("Search Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int binarySearch(int[] array, int a, int b, int value) {
        while (a < b) {
            int m = a + ((b - a) / 2);
            Highlights.markArray(1, a);
            Highlights.markArray(3, m);
            Highlights.markArray(2, b);
            Delays.sleep(1);
            if (Reads.compareValues(value, array[m]) < 0) b = m;
            else a = m + 1;
        }
        Highlights.clearMark(3);
        return a;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i = 0;
        while (i + 1 < currentLength) {
            boolean verify = true;
            while (i + 1 < currentLength && verify) {
                if (Reads.compareIndices(array, i, i + 1, 0.5, true) <= 0) i++;
                else verify = false;
            }
            if (i + 1 < currentLength) {
                Writes.swap(array, i + 1, binarySearch(array, 0, i, array[i + 1]), 0.5, true, false);
            }
        }
    }
}