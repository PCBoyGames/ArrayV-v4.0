package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class DebrisSortEquality extends Sort {
    public DebrisSortEquality(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Debris (Equality)");
        this.setRunAllSortsName("Equal Debris Sort");
        this.setRunSortName("Equal Debris Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void stableSegmentReversal(int[] array, int start, int end) {
        Writes.reversal(array, start, end, 0.075, true, false);
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (Reads.compareIndices(array, i, i + 1, 0.025, true) == 0 && i < end) i++;
            right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, 0.075, true, false);
                else Writes.reversal(array, left, right, 0.075, true, false);
            }
            i++;
        }
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
                boolean equalrun = false;
                boolean anynonequal = false;
                int start = i;
                for (int comp = Reads.compareIndices(array, i, i + 1, 0.025, true); comp >= 0 && i < last; i++, comp = Reads.compareIndices(array, i, i + 1, 0.025, true)) {
                    if (!firstfound) {
                        first = i;
                        firstfound = true;
                    }
                    nextlast = i + 1;
                    if (comp == 0) equalrun = true;
                    else anynonequal = true;
                }
                int end = i;
                if (start != end && anynonequal) {
                    if (end - start < 3 && !equalrun) Writes.swap(array, start, end, 0.075, true, false);
                    else {
                        if (equalrun) stableSegmentReversal(array, start, end);
                        else Writes.reversal(array, start, end, 0.075, true, false);
                    }
                    if (!equalrun || anynonequal) anyrev = true;
                }
            }
            if (nextlast + 1 < currentLength) last = nextlast + 1;
            else last = currentLength - 1;
        }
    }
}