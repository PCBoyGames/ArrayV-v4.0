package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author PiotrGrochowski
 *
 */
public class CocktailSlowSort extends Sort {

    public CocktailSlowSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Cocktail Slow");
        setRunAllSortsName("Cocktail Slow Sort");
        setRunSortName("Cocktail Slowsort");
        setCategory("Impractical Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(true);
        setUnreasonableLimit(150);
        setBogoSort(false);
    }

    private void cocktailslowSort(int[] A, int i, int j) {
        if (i >= j) {
            return;
        }

        int m = i + (j - i) / 2;

        Highlights.markArray(3, m);

        this.cocktailslowSort(A, i, m);
        this.cocktailslowSort(A, m + 1, j);

        if (Reads.compareIndices(A, m, j, 0, true) == 1) {
            Writes.swap(A, m, j, 1, true, false);
        }
        if (j - i > 1 && Reads.compareIndices(A, i, m + 1, 0, true) == 1) {
            Writes.swap(A, i, m + 1, 1, true, false);
        }

        Highlights.markArray(1, j);
        Highlights.markArray(2, m);

        this.cocktailslowSort(A, i + 1, j - 1);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        cocktailslowSort(array, 0, sortLength - 1);

    }

}
