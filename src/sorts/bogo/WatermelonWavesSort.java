package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- FUNGAMER2'S SCRATCH VISUAL -
------------------------------

*/
public final class WatermelonWavesSort extends BogoSorting {
    public WatermelonWavesSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Watermelon Waves");
        this.setRunAllSortsName("Watermelon Waves Sort");
        this.setRunSortName("Watermelon Waves Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(11);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i;
        while (!this.isArraySorted(array, currentLength)) {
            i = BogoSorting.randInt(0, currentLength - 1);
            while (i < currentLength - 1) {
                if (Reads.compareIndices(array, i, i + 1, 0.001, true) == 0) break;
                else {
                    Writes.swap(array, i, i + 1, 0.001, true, false);
                    i++;
                }
            }
        }
    }
}