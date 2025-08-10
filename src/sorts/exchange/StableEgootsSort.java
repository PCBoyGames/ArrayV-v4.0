package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

+---------------------------+
| SORTING ALGORITHM SCARLET |
+---------------------------+
|    A sorting algorithm    |
|    studio by Flanlaina    |
|    (a.k.a Ayako-chan)     |
+---------------------------+

 */

/**
 * @author Lancewer
 * @author Flanlaina
 *
 */
public class StableEgootsSort extends Sort {
    public StableEgootsSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stable Egoots");
        this.setRunAllSortsName("Stable Egoots Sort");
        this.setRunSortName("Stable Egootssort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    protected void stableStooge(int[] array, int start, int end) {
        if (end - start + 1 == 2) {
            if (Reads.compareIndices(array, start, end, 0.0025, true) > 0) {
    	        Writes.swap(array, start, end, 0.005, true, false);
            }
        } else if (end - start + 1 > 2) {
            int third = (end - start + 1) / 3;
            stableStooge(array, start + third, end);
            stableStooge(array, start, end - third);
            stableStooge(array, start + third, end);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        stableStooge(array, 0, currentLength - 1);
    }
}
