package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class RecursiveTinyGnomeSort extends Sort {

    public RecursiveTinyGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Recursive Tiny Gnome");
        this.setRunAllSortsName("Recursive Tiny Gnome Sort");
        this.setRunSortName("Recursive Tiny Gnomesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }
    
    public void sort(int[] array, int a, int b, int depth) {
        Writes.recordDepth(depth++);
        if (b - a < 2) return;
        Writes.recursion();
        sort(array, a, b - 1, depth);
        if (Reads.compareIndices(array, b - 2, b - 1, 0.5, true) > 0) {
            Writes.swap(array, b - 2, b - 1, 0.5, true, false);
            Writes.recursion();
            sort(array, a, b - 1, depth);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength, 0);

    }

}
