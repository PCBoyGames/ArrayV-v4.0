package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class SemiDeterministicBovoSort extends BogoSorting {
    public SemiDeterministicBovoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Semi-Deterministic Bovo");
        this.setRunAllSortsName("Semi-Deterministic Bovo Sort");
        this.setRunSortName("Semi-Deterministic Bovosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(13);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i = 1;
        int pull = 1;
        while (i + 1 <= currentLength) {
            Highlights.markArray(1, i - 1);
            Highlights.markArray(2, i);
            Delays.sleep(this.delay);
            if (Reads.compareValues(array[i - 1], array[i]) > 0) {
                pull = BogoSorting.randInt(i, currentLength);;
                while (pull > 0) {
                    Writes.swap(array, pull - 1, pull, this.delay, true, false);
                    pull--;
                }
                i = 1;
            } else {
                i++;
            }
        }
    }
}