package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class HesvoSort extends Sort {
    public HesvoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Hesvo");
        this.setRunAllSortsName("Hesvo Sort");
        this.setRunSortName("Hesvosort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 1;
        while (left < currentLength) {
            if (Reads.compareIndices(array, left - 1, currentLength - 1, 0.5, true) > 0) Writes.reversal(array, left - 1, currentLength - 1, 0.5, true, false);
            if (Reads.compareIndices(array, left - 1, left, 0.5, true) > 0) {
                Writes.multiSwap(array, left - 1, currentLength - 1, 0.0125, true, false);
                if (left > 1) left--;
            } else left++;
        }
    }
}