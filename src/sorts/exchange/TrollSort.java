package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class TrollSort extends Sort {
    public TrollSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Troll");
        this.setRunAllSortsName("Troll Sort");
        this.setRunSortName("Trollsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(128);
        this.setBogoSort(false);
    }

    public void trollSort(int[] array, int start, int sortLength, int bucketCount, int trueLength) throws Exception {
        if (sortLength - start > 1) {
            trollSort(array, start, (start + sortLength) / 2, bucketCount, sortLength);
            trollSort(array, (start + sortLength) / 2, sortLength, bucketCount, sortLength);
        }
        for (int i = start + 1; i < sortLength; i++) {
            int num = array[i];
            int lo = start, hi = i;
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                this.Highlights.markArray(1, lo);
                this.Highlights.markArray(3, mid);
                this.Highlights.markArray(2, hi);
                this.Delays.sleep(1.0D);
                if (this.Reads.compareValues(num, array[mid]) < 0) {
                    hi = mid;
                    continue;
                }
                lo = mid + 1;
            }
            this.Highlights.clearMark(1);
            this.Highlights.clearMark(2);
            int j = i;
            this.Writes.swap(array, j, lo, 0.05D, true, false);
        }
        if (sortLength - start > 1) {
        trollSort(array, (start + sortLength) / 2, sortLength, bucketCount, sortLength);
        trollSort(array, start, (start + sortLength) / 2, bucketCount, sortLength);
        }
    }

    private boolean isArraySorted(int[] array, int currentLength) {
        for (int i = 0; i < currentLength; i++) {
            if (this.Reads.compareIndices(array, i, i + 1, 0.1D, true) > 0) {
                this.Highlights.incrementFancyFinishPosition();
                return false;
            }
        }
        return true;
    }

    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        while (!isArraySorted(array, sortLength)) trollSort(array, 0, sortLength, bucketCount, sortLength);
    }
}
