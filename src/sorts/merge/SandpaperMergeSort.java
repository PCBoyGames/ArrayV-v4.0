package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Lancewer
 * @author Kiriko-chan
 *
 */
public class SandpaperMergeSort extends Sort {

    public SandpaperMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Sandpaper Merge");
        this.setRunAllSortsName("Sandpaper Merge Sort");
        this.setRunSortName("Sandpaper Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void sandpaper(int[] array, int a, int b) {
        for (int i = a; i < b - 1; i++) {
            for (int j = b - 1; j > i; j--) {
                if (Reads.compareIndices(array, i, j, 0.05, true) > 0)
                    Writes.swap(array, i, j, 0.05, true, false);

            }
        }
    }

    public void sort(int[] array, int a, int b) {
        if (b - a < 2)
            return;
        int m = a + (b - a) / 2;
        sort(array, a, m);
        sort(array, m, b);
        sandpaper(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
