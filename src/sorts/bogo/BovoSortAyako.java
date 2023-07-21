package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Ayako-chan
in collaboration with PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author PCBoy
 *
 */
public final class BovoSortAyako extends BogoSorting {

    public BovoSortAyako(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Ayako's Bovo");
        this.setRunAllSortsName("Ayako's Bovo Sort");
        this.setRunSortName("Ayako's Bovosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(true);
    }
    
    void omegaMultiSwap(int[] array, int a, int b) {
        if (a > b) {
            for (int i = 0; i < a - b; i++)
                Writes.multiSwap(array, b, a, delay, true, false);
        } else
            for (int i = 0; i < b - a; i++)
                Writes.multiSwap(array, b, a, delay, true, false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        while (!isArraySorted(array, sortLength))
            omegaMultiSwap(array, randInt(1, sortLength), 0);

    }

}
