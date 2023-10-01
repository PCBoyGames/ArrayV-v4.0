package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class MeanPartitionSort extends Sort {
    public MeanPartitionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("MPartition");
        this.setRunAllSortsName("Mean Partition Sort");
        this.setRunSortName("Mean Partitionsort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int[] partition(int[] array, int start, int end) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = start; i < end; i++) {
            if (array[i] < min) min = array[i];
            if (array[i] > max) max = array[i];
        }
        int middle = min + ((max - min) / 2) + 1;
        arrayVisualizer.setExtraHeading(" / Assumption: " + middle);
        int itemslow = 0;
        int itemshigh = 0;
        for (int i = start; i < end; i++) {
            Highlights.markArray(1, i);
            Highlights.markArray(2, i);
            Delays.sleep(0.25);
            int cmp = Reads.compareValues(array[i], middle);
            if (cmp < 0) {
                if (start + itemslow != i) Writes.swap(array, i, start + itemslow, 0.25, true, false);
                itemslow++;
            } else {
                Delays.sleep(0.25);
                itemshigh++;
            }
        }
        return new int[] {itemslow, itemshigh};
    }

    public void presumepartitions(int[] array, int start, int end, int depth) {
        if (end - start > 1) {
            Writes.recordDepth(depth);
            int[] m = partition(array, start, end);
            if (m[0] > 0) {
                Writes.recursion();
                presumepartitions(array, start, start + m[0], depth + 1);
            }
            if (m[1] > 0) {
                Writes.recursion();
                presumepartitions(array, start + m[0], end, depth + 1);
            }
        }
        arrayVisualizer.setExtraHeading("");
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        presumepartitions(array, 0, currentLength, 0);
    }
}
