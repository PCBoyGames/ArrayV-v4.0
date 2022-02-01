package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;
public final class MonolithicClurgeSort extends Sort {
    public MonolithicClurgeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Monolithic Clurge");
        this.setRunAllSortsName("Monolithic Clurge Sort");
        this.setRunSortName("Monolithic Clurge Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private void monolithicClurge(int[] array, int start, int mid, int end, int rec, int box) {
        if(mid <= start || end <= mid)
            return;

        Writes.recordDepth(rec++);

        if(box == 0) {
            Writes.recursion(3);
            this.monolithicClurge(array, start, start + (mid - start) / 2, mid, rec, 0);
            this.monolithicClurge(array, mid, mid + (end - mid) / 2, end, rec, 0);
            this.monolithicClurge(array, start, mid, end, rec, 1);
        } else {
            int comp = 0;
            if((comp = Reads.compareValues(array[start], array[mid])) == 1) {
                Writes.swap(array, start, mid, 1, true, false);
            }
            Writes.recursion();
            this.monolithicClurge(array, start+1, mid, end, rec, 2);
            if(box != 2) {
                Writes.recursion();
                this.monolithicClurge(array, start+(comp == -1 ? 2 : 1), mid+1, end, rec, 1);
            }
        }
    }
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.monolithicClurge(array, 0, currentLength/2, currentLength, 0, 0);
    }
}
