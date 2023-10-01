package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class InvodoSort extends BogoSorting {
    public InvodoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Invodo");
        this.setRunAllSortsName("Invodo Sort");
        this.setRunSortName("Invodosort");
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
        int s = 0;
        while (s < currentLength && Reads.compareIndices(array, s, s + 1, delay, true) <= 0) s++;
        while (s + 1 < currentLength) {
            Writes.multiSwap(array, randInt(s + 1, currentLength), randInt(0, s + 1), delay, true, false);
            s = 0;
            while (s < currentLength && Reads.compareIndices(array, s, s + 1, delay, true) <= 0) s++;
        }
    }
}