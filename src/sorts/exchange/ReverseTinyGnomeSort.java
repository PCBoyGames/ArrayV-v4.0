package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Harumi
 *
 */
public class ReverseTinyGnomeSort extends Sort {

    public ReverseTinyGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Reverse Tiny Gnome");
        this.setRunAllSortsName("Reverse Tiny Gnome Sort");
        this.setRunSortName("Reverse Tiny Gnomesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        for (int i = sortLength - 1; i >= 1;) {
            if (Reads.compareIndices(array, i, i - 1, 0.05, true) >= 0) {
                i--;
            } else {
                Writes.swap(array, i, i - 1, 0.1, true, false);
                i = sortLength - 1;
            }
        }

    }

}
