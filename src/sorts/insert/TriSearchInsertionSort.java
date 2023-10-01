package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class TriSearchInsertionSort extends Sort {
    public TriSearchInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("TriSearch Insertion");
        this.setRunAllSortsName("thatsOven's TriSearch Insertion Sort");
        this.setRunSortName("thatsOven's TriSearch Insertion Sort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public int triSearch(int[] arr, int l, int h, int val) {
        int mid = l + ((h-l) / 2);
        Highlights.markArray(0, l);
        Highlights.markArray(1, h);
        Highlights.markArray(2, mid);
        Delays.sleep(40);
        if (Reads.compareValues(val, arr[l]) < 0) {
            return l;
        } else {
            if (Reads.compareValues(val, arr[h]) < 0) {
                if (Reads.compareValues(val, arr[mid]) < 0) {
                    return this.triSearch(arr, l+1, mid-1, val);
                } else {
                    return this.triSearch(arr, mid+1, h-1, val);
                }
            } else {
                return h+1;
            }
        }
    }

    public void triInsertSort(int[] array, int start, int end, double compSleep, double writeSleep) {
        for (int i = start+1; i < end; i++) {
            int num = array[i];
            int lo = start;

            lo = this.triSearch(array, start, i-1, num);
            Highlights.clearAllMarks();

            int j = i - 1;

            while (j >= lo)
            {
                Writes.write(array, j + 1, array[j], writeSleep, true, false);
                j--;
            }
            Writes.write(array, lo, num, writeSleep, true, false);

            Highlights.clearAllMarks();
        }
    }


    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.triInsertSort(array, 0, currentLength, 40, 1);
    }
}