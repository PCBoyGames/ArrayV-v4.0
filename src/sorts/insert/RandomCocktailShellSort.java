package sorts.insert;

import java.util.Random;
import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Kiriko-chan
 * @author fungamer2
 *
 */
public final class RandomCocktailShellSort extends Sort {

    public RandomCocktailShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Random Cocktail Shell");
        this.setRunAllSortsName("Random Cocktail Shell Sort");
        this.setRunSortName("Random Cocktail Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public void shellSort(int[] array, int a, int b) {
        int sortLength = b - a;
        int gap = sortLength / 2;
        Random rng = new Random();
        while (gap >= 1) {
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
                    while (j < b - gap && this.Reads.compareValues(array[j + gap], tmp) < 0) {
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
            gap /= 2;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        shellSort(array, 0, sortLength);

    }

}
