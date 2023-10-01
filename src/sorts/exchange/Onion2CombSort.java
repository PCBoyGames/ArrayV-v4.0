package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class Onion2CombSort extends Sort {
    public Onion2CombSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Onion 2 Comb");
        this.setRunAllSortsName("Onion 2 Comb Sort");
        this.setRunSortName("Onion 2 Comb Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(128);
        this.setBogoSort(false);
    }

    protected boolean trial(int[] array, int currentLength) {
        for (int g = (int) (currentLength / 1.35); g >= 1; g /= 1.35) {
            for (int i = 0; i + g < currentLength; i++) {
                if (Reads.compareIndices(array, i, i + g, 0.1, true) > 0) {
                    Writes.swap(array, i, i + g, 0.1, true, false);
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean tryComb(int[] array, int currentLength) {
        for (int i = 2; i <= currentLength; i++) if (trial(array, i)) return true;
        return false;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (tryComb(array, currentLength));
    }
}