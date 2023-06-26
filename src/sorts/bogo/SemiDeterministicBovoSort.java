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
        for (int i = 1; i + 1 <= currentLength; i++) {
            if (Reads.compareIndices(array, i - 1, i, delay, true) > 0) {
                Writes.multiSwap(array, randInt(i, currentLength), 0, delay, true, false);
                i = 0;
            }
        }
    }
}