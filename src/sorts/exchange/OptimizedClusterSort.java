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

I practical So ts· Sho eso t

 */

public class OptimizedClusterSort extends Sort {
    public OptimizedClusterSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Cluster");
        this.setRunAllSortsName("Optimized Cluster Sort");
        this.setRunSortName("Optimized Clustersort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4096);
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
                    Writes.swap(array, i+1, currentLength-1, 0.25, true, sorted = false);
                }
        }
    }
}
