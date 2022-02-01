package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SnowballIterativePopSort extends Sort {
    public SnowballIterativePopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Snowball Iterative Pop");
        this.setRunAllSortsName("Snowball Iterative Pop Sort");
        this.setRunSortName("Snowball Iterative Pop Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2048);
        this.setBogoSort(false);
    }

    protected void snowballs(int[] array, int start, int end, int dir) {
        int gap = 0;
        int begin = start;
        while (gap != 1) {
            gap = 1;
            for (int i = begin - 1 > start ? begin - 1 : start; i + gap < end; i++) {
                if (Reads.compareIndices(array, i, i + gap, 0.001, true) == dir) {
                    if (gap > 2) Writes.reversal(array, i, i + gap, 0.01, true, false);
                    else Writes.swap(array, i, i + gap, 0.01, true, false);
                    if (gap == 1) begin = i;
                    gap *= 2;
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int len = 2;
        int index = 0;
        while (len < currentLength) {
            int dir = -1;
            index = 0;
            while (index + len <= currentLength) {
                if (len == 2) {
                    if (Reads.compareIndices(array, index, index + 1, 0.001, true) == dir) Writes.swap(array, index, index + 1, 0.1, true, false);
                } else snowballs(array, index, index + len, dir);
                index += len;
                dir *= -1;
            }
            if (index != currentLength) snowballs(array, index, currentLength, dir);
            len *= 2;
        }
        snowballs(array, 0, currentLength, 1);
    }
}