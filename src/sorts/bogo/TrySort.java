package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES
IN COLLABORATION WITH NAOAN1201

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class TrySort extends BogoSorting {
    public TrySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Try");
        this.setRunAllSortsName("Try Sort");
        this.setRunSortName("Trysort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(13); // unreasonable at 7 cycles
        this.setBogoSort(true);
    }

    protected boolean correctPlace(int[] array, int currentLength, int n) {
        int pos = 0;
        for (int i = 0; i < currentLength; i++) if (Reads.compareIndices(array, i, n, delay, true) < 0) pos++;
        return pos == n;
    }

    protected boolean findSwappedPair(int[] array, int currentLength) {
        while (true) {
            for (int i = 0; i < currentLength; i++) {
                for (int j = 0; j < currentLength; j++) {
                    if (i != j) {
                        Writes.swap(array, i, j, delay, true, false);
                        if (correctPlace(array, currentLength, i) && correctPlace(array, currentLength, j)) return false;
                        else Writes.swap(array, i, j, delay, true, false);
                    }
                }
            }
            if (isArraySorted(array, currentLength)) return true;
            bogoSwap(array, 0, currentLength, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (true) {
            if (findSwappedPair(array, currentLength)) break;
        }
    }
}