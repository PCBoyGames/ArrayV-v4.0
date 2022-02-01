package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.MergeSorting;


final public class SprawlSort extends MergeSorting {
    public SprawlSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Sprawl");
        this.setRunAllSortsName("Sprawl Sort");
        this.setRunSortName("Sprawlsort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    void insert(int[] array, int key, int start, int end) {
        while(end >= 0 && Reads.compareValues(key, array[end]) == -1) {
            Writes.write(array, end+1, array[end--], 1, true, false);
        }
        Writes.write(array, end+1, key, 1, true, false);
    }
    private void sprawl(int[] array, int start, int mid, int end) {
        int[] buff = Writes.createExternalArray(end-start);
        int low = start, high = mid, to = 0;
        while(low < mid && high < end) {
            Highlights.markArray(1, low);
            Highlights.markArray(2, high);
            if(Reads.compareValues(array[low], array[high]) == 1) {
                Writes.write(buff, to++, array[high++], 1, true, true);
                Writes.write(buff, to++, array[low++], 1, true, true);
            } else {
                Writes.write(buff, to++, array[low++], 1, true, true);
                Writes.write(buff, to++, array[high++], 1, true, true);
            }
        }
        while(low < mid) {
            Writes.write(buff, to++, array[low++], 1, true, true);
        }
        while(high < end) {
            Writes.write(buff, to++, array[high++], 1, true, true);
        }
        int lead = -1;
        for(int i=0; i<to; i++) {
            if(lead++ == 1) {
                this.insert(array, buff[i], start, start+i-1);
                lead = 0;
            } else {
                Writes.write(array, start+i, buff[i], 1, true, false);
            }
        }
        if(lead == 2) {
            this.insert(array, array[end-1], start, end-2);
        }
        Writes.deleteExternalArray(buff);
    }
    public void merge(int[] array, int start, int end) {
        if(start == end)
            return;

        int mid = start + ((end - start) / 2);
        if(start == mid)
            return;
        this.merge(array, start, mid);
        this.merge(array, mid, end);

        this.sprawl(array, start, mid, end);
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.merge(array, 0, length);
    }
}