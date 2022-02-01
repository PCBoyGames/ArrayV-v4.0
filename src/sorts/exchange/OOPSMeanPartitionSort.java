package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class OOPSMeanPartitionSort extends Sort {

    boolean inlow;
    boolean inhigh;

    public OOPSMeanPartitionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("OopsMPartition");
        this.setRunAllSortsName("Out of Place Stable Mean Partition Sort");
        this.setRunSortName("OOP Stable Mean Partitionsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int partition(int[] array, int start, int end) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = start; i < end; i++) {
            if(array[i] < min) {
                min = array[i];
            }
            if(array[i] > max) {
                max = array[i];
            }
        }
        int[] low = Writes.createExternalArray(end - start);
        int[] high = Writes.createExternalArray(end - start);
        int middle = min + ((max - min) / 2) + 1;
        arrayVisualizer.setExtraHeading(" / Assumption: " + middle);
        int itemslow = 0;
        int itemshigh = 0;
        inlow = false;
        inhigh = false;
        for (int i = start; i < end; i++) {
            Highlights.markArray(1, i);
            int cmp = Reads.compareValues(array[i], middle);
            if (cmp < 0) {
                Writes.write(low, itemslow, array[i], 0.25, false, true);
                itemslow++;
                inlow = true;
            } else {
                Writes.write(high, itemshigh, array[i], 0.25, false, true);
                itemshigh++;
                inhigh = true;
            }
        }
        Writes.arraycopy(low, 0, array, start, itemslow, 0.25, true, false);
        Writes.arraycopy(high, 0, array, start + itemslow, itemshigh, 0.25, true, false);
        Writes.deleteExternalArray(low);
        Writes.deleteExternalArray(high);
        return itemslow;
    }

    public void presumepartitions(int[] array, int start, int end, int depth) {
        if (end - start >= 2) {
            Writes.recordDepth(depth);
            int m = partition(array, start, end);
            if (inlow && inhigh) {
                Writes.recursion();
                presumepartitions(array, start, start + m, depth + 1);
                Writes.recursion();
                presumepartitions(array, start + m, end, depth + 1);
            }
        }
        arrayVisualizer.setExtraHeading("");
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        presumepartitions(array, 0, currentLength, 0);
    }
}
