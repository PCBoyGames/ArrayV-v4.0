package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

Inva's resurrection.

 */

public class BinaryInvaSort extends BogoSorting {
    public BinaryInvaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Binary Inva");
        this.setRunAllSortsName("Binary Inva Sort");
        this.setRunSortName("Binary Invasort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int s = 0;
        while (s < currentLength && Reads.compareIndices(array, s, s + 1, 0.1, true) <= 0) s++;
        for (int i = 1; i < currentLength; i++) {
            int r = randInt(i, currentLength);
            int num = array[r];
            int lo = 0;
            int hi = i;
            while (lo < hi) {
                int mid = lo + ((hi - lo) / 2);
                Highlights.markArray(1, lo);
                Highlights.markArray(3, mid);
                Highlights.markArray(2, hi);
                Delays.sleep(1);
                if (Reads.compareValues(num, array[mid]) < 0) {
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            }
            Highlights.clearMark(1);
            Highlights.clearMark(2);
            int j = i;
            Writes.multiSwap(array, r, i, 0.1, true, false);
            while (j >= 0 && j > lo) {
                Writes.swap(array, j, j - 1, 0.1, true, false);
                j--;
            }
        }
    }
}
