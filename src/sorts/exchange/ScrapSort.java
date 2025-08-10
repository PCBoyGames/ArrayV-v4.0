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

somehow worse than clustersort

 */

public class ScrapSort extends Sort {
    public ScrapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Scrap");
        this.setRunAllSortsName("Scrap Sort");
        this.setRunSortName("Scrapsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(128);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i+1 < currentLength; i++)
                if (Reads.compareIndices(array, i, i+1, 0.5, true) > 0) {
                    Writes.swap(array, i, i+1, 0.25, true, false);
                    Writes.reversal(array, i+1, currentLength-1, 0.25, true, sorted = false);
                }
        }
    }
}
