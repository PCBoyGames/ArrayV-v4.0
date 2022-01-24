package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class StableMeanPartitionSort extends Sort {
    
    boolean inlow;
    boolean inhigh;
    
    public StableMeanPartitionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stable MPartition");
        this.setRunAllSortsName("Stable Mean Partition Sort");
        this.setRunSortName("Stable Mean Partitionsort");
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
        int middle = min + ((max - min) / 2) + 1;
        arrayVisualizer.setExtraHeading(" / Assumption: " + middle);
        int itemslow = 0;
        inlow = false;
        inhigh = false;
        for (int i = start; i < end; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.25);
            int cmp = Reads.compareValues(array[i], middle);
            if (cmp < 0) {
                int item = array[i];
                boolean w = false;
                for (int j = i; j > start + itemslow; j--) {
                    Writes.write(array, j, array[j - 1], 0.025, true, false);
                    w = true;
                }
                if (w) Writes.write(array, start + itemslow, item, 0.025, true, false);
                itemslow++;
                inlow = true;
            } else inhigh = true;
        }
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
