package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Ayako-chan

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 *
 */
public class ExchangeCocktailBogoSort extends BogoSorting {

    public ExchangeCocktailBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Exchange Cocktail Bogo");
        this.setRunAllSortsName("Exchange Cocktail Bogo Sort");
        this.setRunSortName("Exchange Cocktail Bogosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(true);
    }

    protected void bogoCompSwap(int[] array, int a, int b) {
        if (BogoSorting.randBoolean()) {
            for (int i = a; i < b; i++) {
                int j = BogoSorting.randInt(i, b);
                if (Reads.compareIndices(array, i, j, this.delay, true) > 0) {
                    Writes.swap(array, i, j, this.delay, true, false);
                }
            }
        } else {
            for (int i = b - 1; i > a; i--) {
                int j = BogoSorting.randInt(a, i);
                if (Reads.compareIndices(array, i, j, this.delay, true) < 0) {
                    Writes.swap(array, i, j, this.delay, true, false);
                }
            }
        }
    }

    public void sort(int[] array, int a, int b) {
        int min = a, max = b;
        while (min < max-1) {
            if (this.isRangeSorted(array, min, max, false, true))
                break;
            if (this.isMinSorted(array, min, max)) {
                Highlights.markArray(3, min);
                ++min;
                continue;
            }
            if (this.isMaxSorted(array, min, max)) {
                Highlights.markArray(4, max-1);
                --max;
                continue;
            }
            bogoCompSwap(array, min, max);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
