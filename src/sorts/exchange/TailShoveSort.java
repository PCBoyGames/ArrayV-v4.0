package sorts.exchange;

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

j

 */

public class TailShoveSort extends Sort {
    public TailShoveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Tail Shove");
        this.setRunAllSortsName("Tail Shove Sort");
        this.setRunSortName("Tail Shove Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = currentLength-1; i > 0;) {
            if (Reads.compareIndices(array, i-1, i, 0.1, true) > 0) Writes.multiSwap(array, i-1, i = currentLength-1, 0.1, true, false);
            else i--;
        }
    }
}
