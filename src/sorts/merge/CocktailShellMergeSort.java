package sorts.merge;

import sorts.insert.InsertionSort;
import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Kiriko-chan
 * @author fungamer2
 *
 */
public final class CocktailShellMergeSort extends Sort {

    public CocktailShellMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cocktail Shell Merge");
        this.setRunAllSortsName("Cocktail Shell Merge Sort");
        this.setRunSortName("Cocktail Shell Mergesort");
        this.setCategory("Merge Sorts");
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
        boolean dir = true;
        while (gap >= 1) {
            if (dir) {
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
            dir = !dir;
        }
        Highlights.clearMark(2);
    }

    public void sort(int[] array, int a, int b) {
        if(b - a < 32) {
            InsertionSort smallSort = new InsertionSort(arrayVisualizer);
            smallSort.customInsertSort(array, a, b, 0.7, false);
            return;
        }
        int m = a + (b - a) / 2;
        sort(array, a, m);
        sort(array, m, b);
        shellSort(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
