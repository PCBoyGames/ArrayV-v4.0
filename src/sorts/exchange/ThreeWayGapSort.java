package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class ThreeWayGapSort extends Sort {
    public ThreeWayGapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("3-Way Gap");
        this.setRunAllSortsName("thatsOven's 3-Way Gap Sort");
        this.setRunSortName("thatsOven's 3-Way Gap Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public int[] backCheckSorted(int[] arr, int end, int start) {
        int[] returnarr = new int[2];
        for (int i=end-1; i > start+1; i--) {
            if (Reads.compareValues(arr[i], arr[i-1]) < 0) {
                returnarr[0] = 0;
                returnarr[1] = i+3;
                return returnarr;
            }
        }
        returnarr[0] = 1;
        returnarr[1] = end;
        return returnarr;
    }

    public void threeWayCompareAndSwap(int[] arr, int st_element, int nd_element, int rd_element) {
        if (Reads.compareValues(arr[st_element], arr[nd_element]) > 0) {
            Writes.swap(arr, st_element, nd_element, 1, true, false);
        }
        if (Reads.compareValues(arr[nd_element], arr[rd_element]) > 0) {
            Writes.swap(arr, nd_element, rd_element, 1, true, false);
        }
        if (Reads.compareValues(arr[st_element], arr[nd_element]) > 0) {
            Writes.swap(arr, st_element, nd_element, 1, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int[] condition = new int[2];
        int gap;
        int start;
        condition[0] = 0;
        condition[1] = currentLength;
        while (condition[0] == 0) {
            gap = condition[1]/3;
            while (gap > 0) {
                start = 0;
                while (start+(gap*2) < condition[1]) {
                    this.threeWayCompareAndSwap(array, start, start+gap, start+(gap*2));
                    start += gap;
                }
                gap = (gap*10)/13;
            }
            condition = this.backCheckSorted(array, condition[1], 0);
        }
    }
}