package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class CrinkleSort extends MadhouseTools {
    public CrinkleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Crinkle");
        this.setRunAllSortsName("Crinkle Sort");
        this.setRunSortName("Crinkle Sort");
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
        for (int i = minSorted(array, 0, currentLength, 0.01, true), j = maxSorted(array, i, currentLength, 0.01, true); i < j; i = i < j ? minSorted(array, i + 1, j, 0.01, true) : i) {
            int grabbed = 0;
            int k;
            for (k = j - 1; k > i; k--) {
                if (Reads.compareIndices(array, i + grabbed, k + grabbed, 0.01, true) < 0) {
                    //Writes.insert(array, i, k, 0.01, true, false);
                    if (grabbed > 1) {
                        Writes.reversal(array, i + grabbed, k + grabbed, 0.05, true, false);
                        Writes.reversal(array, i, k + grabbed, 0.05, true, false);
                    } else if (grabbed == 1) Writes.insert(array, i, k + grabbed, 0.05, true, false);
                    if (k + grabbed == j - 1) j = maxSorted(array, i, j, 0.01, true);
                    grabbed = 0;
                } else grabbed++;
            }
            if (grabbed > 1) {
                Writes.reversal(array, i + grabbed, k + grabbed, 0.05, true, false);
                Writes.reversal(array, i, k + grabbed, 0.05, true, false);
            } else if (grabbed == 1) Writes.insert(array, i, k + grabbed, 0.05, true, false);
        }
    }
}