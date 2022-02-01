package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class PDPopSort extends Sort {
    public PDPopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating Pop");
        this.setRunAllSortsName("Pattern-Defeating Pop Sort");
        this.setRunSortName("Pattern-Defeating Pop Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void bubble(int[] array, int start, int end, int dir) {
        int rs = dir == 1 ? start : start + ((end - start) / 2);
        int r = rs;
        while (r + 1 < end) {
            Highlights.markArray(1, r);
            Highlights.markArray(2, r + 1);
            Delays.sleep(1);
            if (Reads.compareValues(array[r], array[r + 1]) == dir) r++;
            else break;
        }
        if (r - rs > 2) Writes.reversal(array, rs, r, 0.2, true, false);
        else if (r - rs > 0) Writes.swap(array, rs, r, 0.2, true, false);
        int c = 1;
        int s = start + ((end - start) / 2);
        int f = 1;
        boolean a = false;
        for (int j = end - 1; j > start; j -= c) {
            if (f - 1 < start) s = start;
            else s = f - 1;
            a = false;
            c = 1;
            for (int i = s; i < j; i++) {
                if (Reads.compareIndices(array, i, i + 1, 0.025, true) == dir) {
                    Writes.swap(array, i, i + 1, 0.075, true, false);
                    if (!a) f = i;
                    a = true;
                    c = 1;
                } else c++;
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int len = currentLength / 4;
        int index = 0;
        int dir = -1;
        while (len < currentLength) {
            index = 0;
            dir = -1;
            while (index + len <= currentLength) {
                bubble(array, index, index + len, dir);
                index += len;
                dir *= -1;
            }
            if (index != currentLength) bubble(array, index, currentLength, dir);
            len *= 2;
        }
        bubble(array, 0, currentLength, 1);
    }
}