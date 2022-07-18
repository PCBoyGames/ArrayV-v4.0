package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class MiraclePancakeSort extends BogoSorting {
    public MiraclePancakeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Miracle Pancake");
        this.setRunAllSortsName("Miracle Pancake Sort");
        this.setRunSortName("Miracle Pancake Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2);
        this.setBogoSort(true); // Won't be certain to sort 3 or more.
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (!isArraySorted(array, currentLength)) Writes.reversal(array, 0, currentLength - 1, 0.25, true, false);
    }
}