package sorts.select;

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

Ported from fungamer2's Python implementation.

 */

public final class RollSort extends Sort {
    public RollSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Rolling");
        this.setRunAllSortsName("Rolling Sort");
        this.setRunSortName("Rollsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 0; i < currentLength; i++) {
            int m = i;
            for (int j = i+1; j < currentLength; j++) {
                if (Reads.compareValues(array[j], array[m]) < 0) {
                    m = j;
                    Highlights.markArray(1, j);
                    Highlights.markArray(2, m);
                    Delays.sleep(0.01);
                }
            }
            int diff = m-i;
            for (int r = 0; r < diff; r++) {
                for (int j = i; j < currentLength-1; j++) {
                    Writes.swap(array, j, j+1, 0.01, true, false);
                }
            }
        }
    }
}
