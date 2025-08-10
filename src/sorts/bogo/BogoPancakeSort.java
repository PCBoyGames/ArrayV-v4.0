package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Flanlaina
in collaboration with aphitorite

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Flanlaina
 * @author aphitorite
 *
 */
public class BogoPancakeSort extends BogoSorting {
    public BogoPancakeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bogo Pancake");
        this.setRunAllSortsName("Bogo Pancake Sort");
        this.setRunSortName("Bogo Pancake Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(true);
    }

    protected void reverse(int[] array, int a, int b) {
        if (b - a >= 3) Writes.reversal(array, a, b, this.delay, true, false);
        else Writes.swap(array, a, b, this.delay, true, false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        while (!isArraySorted(array, sortLength))
            reverse(array, 0, BogoSorting.randInt(1, sortLength));
    }
}
