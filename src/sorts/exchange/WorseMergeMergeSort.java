package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;


public class WorseMergeMergeSort extends Sort {
    public WorseMergeMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Worse mergeMerge");
        this.setRunAllSortsName("thatsOven's Worse mergeMerge Sort");
        this.setRunSortName("thatsOven's Worse mergeMerge Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(20);
        this.setBogoSort(false);
    }

    public void worseMergeMergeFun(int[] arr, int l, int r) {
        if (r-l > 2) {
            int m = (l+(r-1))/2;
            this.worseMergeMergeFun(arr, l, m);
            this.worseMergeMergeFun(arr, m+1, r);
            for (int i = 1; l+i < r-i; i++) {
                this.worseMergeMergeFun(arr, l+i, r);
                this.worseMergeMergeFun(arr, l, r-i);
            }
        } else {
            if (Reads.compareIndices(arr, l, r, 0, true) > 0) {
                Writes.swap(arr, l, r, 1, true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.worseMergeMergeFun(array, 0, currentLength-1);
    }
}