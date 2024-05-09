package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Harumi
 *
 */
public class ReverseUnoptimizedGnomeSort extends Sort {

    public ReverseUnoptimizedGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Reverse Unoptimized Gnome");
        this.setRunAllSortsName("Reverse Unoptimized Gnome Sort");
        this.setRunSortName("Reverse Unoptimized Gnomesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        for (int i = sortLength - 1; i >= 1;) {
            if (Reads.compareIndices(array, i, i - 1, 0.04, true) >= 0) {
                i--;
            } else {
                Writes.swap(array, i, i - 1, 0.02, true, false);
                if (i < sortLength - 1) {
                    i++;
                }
            }
        }

    }

}
