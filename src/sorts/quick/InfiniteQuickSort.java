package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class InfiniteQuickSort extends Sort {
    public InfiniteQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Infinite Quick");
        this.setRunAllSortsName("Infinite Quick Sort");
        this.setRunSortName("Infinite Quick Sort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private int squarePartition(int[] array, int start, int end) {
        if(start>=end || end-1 < 0)
            return 0;
        int pivot = array[end];
        if(Reads.compareValues(array[start], array[end]) == 1)
        {
            Writes.swap(array, start, end, 1, true, false);
        } else {
            Highlights.markArray(1, start);
            Delays.sleep(0.5);
        }
        int z=start;
        for(int k=start; k<=end; k++) {
            if(Reads.compareValues(array[k], pivot) == -1) {
                this.squarePartition(array, z++, k);
                if(z<end)
                    this.squarePartition(array, start, k);
            } else {
                if(z<end)
                    this.squarePartition(array, start, z);
                if(z!=start)
                    this.squarePartition(array, z, k);
            }
        }
        //this.squarePartition(array, start, z);
        return z;
    }

    private boolean sorted(int[] array, int s, int e) {
        for(int i=s+1; i<e; i++) {
            if(Reads.compareValues(array[i-1], array[i]) == 1)
                return false;
        }
        return true;
    }
    /**
     * Completes in O((float)infinity?)
     */
    private void sort(int[] array, float start, float end) {
        if(start == end)
            return;
        int is=(int)start, ie=(int)Math.ceil(end);
        this.squarePartition(array, is, ie);
        float mid=(start+end)/2f;
        if(start==mid)
            return;
        try {
            this.sort(array, start, mid);
            this.sort(array, mid, end);
        } catch(StackOverflowError no) {
            //don't handle it
        }
        if(!this.sorted(array,is,ie))
            this.sort(array, start, end);
    }

    @Override
    public void runSort(int[] array, int length, int buckets) {
        this.sort(array, 0, length-1);
    }
}