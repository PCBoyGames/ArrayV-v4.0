package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class DebrisSort extends Sort {
    public DebrisSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Debris");
        this.setRunAllSortsName("Debris Sort");
        this.setRunSortName("Debris Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i = 0;
        int first = 1;
        int last = currentLength - 1;
        int nextlast = currentLength - 1;
        boolean anyrev = true;
        while (anyrev) {
            anyrev = false;
            boolean firstfound = false;
            if (first > 0) i = first - 1;
            else i = 0;
            for (; i < last; i++) {
                int start = i;
                for (; Reads.compareIndices(array, i, i + 1, 0.025, true) > 0 && i < last; i++) {
                    if (!firstfound) {
                        first = i;
                        firstfound = true;
                    }
                    nextlast = i + 1;
                }
                int end = i;
                if (start != end) {
                    if (end - start < 3) Writes.swap(array, start, end, 0.075, true, false);
                    else Writes.reversal(array, start, end, 0.075, true, false);
                    anyrev = true;
                }
            }
            if (nextlast + 1 < currentLength) last = nextlast + 1;
            else last = currentLength - 1;
        }
    }
}