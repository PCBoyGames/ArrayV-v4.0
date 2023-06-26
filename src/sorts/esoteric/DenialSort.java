package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

As long as the first 2 items are sorted, the whole array is sorted.

 */

public final class DenialSort extends Sort {
    public DenialSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Denial");
        this.setRunAllSortsName("Denial Sort");
        this.setRunSortName("Denial Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (Reads.compareValues(array[0], array[1]) > 0) {}
    }
}
