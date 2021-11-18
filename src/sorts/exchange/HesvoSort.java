package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class HesvoSort extends Sort {
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
        int pull = 1;
        while (left < currentLength) {
            Highlights.markArray(1, left - 1);
            Highlights.markArray(2, currentLength - 1);
            Delays.sleep(0.5);
            if (Reads.compareValues(array[left - 1], array[currentLength - 1]) > 0) Writes.reversal(array, left - 1, currentLength - 1, 0.5, true, false);
            Highlights.markArray(1, left - 1);
            Highlights.markArray(2, left);
            Delays.sleep(0.5);
            if (Reads.compareValues(array[left - 1], array[left]) > 0) {
                pull = left;
                while (pull < currentLength) {
                    Writes.swap(array, pull - 1, pull, 0.0125, true, false);
                    pull++;
                }
                if (left > 1) left--;
            } else left++;
        }
    }
}