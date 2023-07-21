package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Ayako-chan
in collaboration with PCBoy and Meme Man

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author PCBoy
 * @author Meme Man
 *
 */
public class ExchangeVovoSort extends BogoSorting {

    public ExchangeVovoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Exchange Vovo");
        this.setRunAllSortsName("Exchange Vovo Sort");
        this.setRunSortName("Exchange Vovosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4096);
        this.setBogoSort(true);
    }
    
    public void pull(int[] array, int a, int b) {
        if (a < b) {
            for (int i = a; i < b; i++) {
                if (Reads.compareIndices(array, i, i + 1, delay, true) > 0) {
                    Writes.swap(array, i, i + 1, delay, true, false);
                }
            }
        } else {
            for (int i = a; i > b; i--) {
                if (Reads.compareIndices(array, i, i - 1, delay, true) < 0) {
                    Writes.swap(array, i, i - 1, delay, true, false);
                }
            }
        }
    }
    
    public void sort(int[] array, int a, int b) {
        while (!isRangeSorted(array, a, b, false, true)) {
            if (randBoolean()) pull(array, randInt(a, b), a);
            else pull(array, randInt(a, b), b - 1);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
