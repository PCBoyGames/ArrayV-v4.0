package sorts.insert;

import java.util.Random;
import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Kiriko-chan
 * @author fungamer2
 *
 */
public final class RandomCiuraCocktailShellSort extends Sort {

    public RandomCiuraCocktailShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Random Cocktail Shell (Ciura Gaps)");
        this.setRunAllSortsName("Random Cocktail Shell Sort (Ciura Gaps)");
        this.setRunSortName("Random Cocktail Shellsort (Ciura Gaps)");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    protected int[] gaps = {1, 4, 10, 23, 57, 132, 301, 701};
    
    protected int ciura(int n) {
        if (n <= gaps.length) {
            return gaps[n - 1];
        }
        return (int)Math.pow(2.25, n);
    }
    
    public void shellSort(int[] array, int a, int b) {
        int sortLength = b - a;
        int gap = 1;
        int k;
        for (k = 1; gap < sortLength; k++) {
            gap = ciura(k);
        }
        Random rng = new Random();
        while (--k >= 1) {
            gap = ciura(k);
            if (rng.nextBoolean()) {
                for (int i = a + gap; i < b; i++) {
                    int tmp = array[i];
                    int j = i;
                    while (j >= a + gap && Reads.compareValues(array[j - gap], tmp) > 0) {
                        Highlights.markArray(2, j - gap);
                        Writes.write(array, j, array[j - gap], 0.7, true, false);
                        j -= gap;
                    }

                    if (j - gap >= a) {
                        Highlights.markArray(2, j - gap);
                    } else {
                        Highlights.clearMark(2);
                    }

                    Writes.write(array, j, tmp, 0.7, true, false);
                }
            } else {
                for (int i = b - gap; i >= a; i--) {
                    int tmp = array[i];
                    int j = i;
                    while (j < b - gap && Reads.compareValues(array[j + gap], tmp) < 0) {
                        Highlights.markArray(2, j + gap);
                        Writes.write(array, j, array[j + gap], 0.7, true, false);
                        j += gap;
                    }

                    if (j + gap < b) {
                        Highlights.markArray(2, j + gap);
                    } else {
                        Highlights.clearMark(2);
                    }

                    Writes.write(array, j, tmp, 0.7, true, false);
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        shellSort(array, 0, sortLength);

    }

}
