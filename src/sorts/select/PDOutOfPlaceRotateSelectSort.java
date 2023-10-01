package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class PDOutOfPlaceRotateSelectSort extends MadhouseTools {
    public PDOutOfPlaceRotateSelectSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating Out-of-Place Rotate Selection");
        this.setRunAllSortsName("Pattern-Defeating Out-of-Place Rotate Selection Sort");
        this.setRunSortName("Pattern-Defeating Out-of-Place Rotate Selectsort");
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
        if (patternDefeat(array, 0, currentLength, false, 0.1, true, false)) return;
        int[] ext = Writes.createExternalArray(currentLength);
        int[] min = minSortedW(array, 0, currentLength, 0.01, true);
        for (int i = min[0]; i < currentLength && min[1] != -1; i = min[0]) {
            Highlights.clearAllMarks();
            rotateAuxIndexed(array, ext, i, min[1], currentLength, 0.1, true, false);
            min = minSortedW(array, i + 1, currentLength, 0.1, true);
        }
        Writes.deleteExternalArray(ext);
    }
}