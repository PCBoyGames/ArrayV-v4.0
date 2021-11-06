package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.HeapSorting;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- LANES V SCRATCH VISUALIZER -
------------------------------

*/
final public class ShepherdSort extends HeapSorting {
    public ShepherdSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Shepherd");
        this.setRunAllSortsName("Shepherd Sort");
        this.setRunSortName("Shepherd Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 0; i < currentLength; i++) {
            Highlights.markArray(3, i);
            heapify(array, i, currentLength, i == 0 ? 0.5 : 0.05, false);
        }
    }
}