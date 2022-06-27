package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class PapoSort extends BogoSorting {
    public PapoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Papo");
        this.setRunAllSortsName("Papo Sort");
        this.setRunSortName("Paposort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2048);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLength, int luck) {
        delay = 0.25;
        for (int i = 0; i < currentLength; i++) {
            for (int j = currentLength - 1; j > i; j--) {
                Highlights.markArray(3, i);
                Highlights.markArray(4, j);
                while (Reads.compareIndices(array, i, j, 0.25, true) > 0) bogoSwap(array, i, j + 1, false);
            }
        }
    }
}
