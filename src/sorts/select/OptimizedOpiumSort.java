package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class OptimizedOpiumSort extends Sort {
    public OptimizedOpiumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Opium");
        this.setRunAllSortsName("Optimized Opium Sort");
        this.setRunSortName("Optimized Opium Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(false);
    }

    protected boolean isLeast(int[] array, int[] ext, int index, int extcollect, int length) {
        for (int i = index + 1; i < length; i++) if (Reads.compareIndices(array, index, i, 0.1, true) > 0) return false;
        Highlights.clearAllMarks();
        for (int i = 0; i < extcollect; i++) {
            Highlights.markArray(2, i);
            if (Reads.compareIndexValue(array, index, ext[i], 0.1, true) > 0) return false;
        }
        return true;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int[] collect = Writes.createExternalArray(currentLength);
        for (int j = 0; j < currentLength; j++) collect[j] = -1;
        for (int j = 0, h = 0, i = j, collected = 0; i < currentLength; j++) {
            if (j > currentLength - 1) {
                j = i;
                for (int k = i; k <= h; k++) {
                    Writes.write(array, k, collect[k - i], 0.1, true, false);
                    collect[k - i] = -1;
                }
                for (int k = 0; k < currentLength; k++) collect[k] = -1;
                collected = (h = 0);
            }
            if (isLeast(array, collect, j, collected, currentLength)) {
                Highlights.clearAllMarks();
                if (i != j) Writes.write(array, i, array[j], 0.1, true, false);
                i++;
                h = j;
            } else {
                Highlights.clearAllMarks();
                Writes.write(collect, collected++, array[j], 0.1, false, true);
            }
        }
        Writes.deleteExternalArray(collect);
    }
}
