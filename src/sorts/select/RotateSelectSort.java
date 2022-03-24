package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class RotateSelectSort extends Sort {
    public RotateSelectSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Rotate Selection");
        this.setRunAllSortsName("Rotate Selection Sort");
        this.setRunSortName("Rotate Selectsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void rotatesel(int[] array, int start, int end) {
        int i = start + 1;
        int min = start;
        while (i < end) {
            if (Reads.compareIndices(array, i, min, 0.05, true) < 0) min = i;
            i++;
        }
        if (min != start) {
            Highlights.clearAllMarks();
            IndexedRotations.neon(array, start, min, end, 0.5, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 0; i < currentLength; i++) rotatesel(array, i, currentLength);
    }
}