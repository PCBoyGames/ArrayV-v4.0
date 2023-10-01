package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class MoreOptimizedOpiumSort extends MadhouseTools {

    int where;

    public MoreOptimizedOpiumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("More Optimized Opium");
        this.setRunAllSortsName("More Optimized Opium Sort");
        this.setRunSortName("More Optimized Opium Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(16384);
        this.setBogoSort(false);
    }

    protected boolean isLeast(int[] array, int[] ext, int index, int extcollect, int length) {
        where = index + 1;
        for (int i = index + 1; i < length; i++) if (Reads.compareIndices(array, index, where = i, 0.1, true) > 0) return false;
        Highlights.clearAllMarks();
        for (int i = 0; i < extcollect; i++) {
            Highlights.markArray(2, i);
            if (Reads.compareIndexValue(array, index, ext[i], 0.1, true) > 0) return false;
        }
        where = index + 1;
        return true;
    }

    public void opium(int[] array, int start, int end, boolean domax, boolean minskip) {
        if (domax) end = maxSorted(array, start, end, 0.1, true);
        int[] collect = Writes.createExternalArray(end - start);
        for (int j = 0; j < end - start; j++) collect[j] = -1;
        for (int j = minSorted(array, start, end, 0.1, true), h = (where = 0), i = j, collected = 0; i < end; j = where) {
            if (j >= end) {
                for (int k = i; k <= h; k++) {
                    Writes.write(array, k, collect[k - i], 0.1, true, false);
                    collect[k - i] = -1;
                }
                for (int k = 0; k < end - start; k++) collect[k] = -1;
                if (minskip) {
                    int[] l = minSortedW(array, i, end, 0.1, true);
                    if (l[1] == -1) return;
                    i = l[0];
                    where = l[1];
                    j = where++;
                    collected = 0;
                    for (int k = i; k < j; k++) {
                        Highlights.markArray(1, k);
                        Writes.write(collect, collected++, array[k], 0.1, false, true);
                    }
                    if (i != j) Writes.write(array, i, array[j], 0.1, true, false);
                    h = j++;
                    i++;
                } else {
                    where = (j = i);
                    collected = (h = 0);
                }
            }
            if (j < end) {
                if (isLeast(array, collect, j, collected, end)) {
                    Highlights.clearAllMarks();
                    if (i != j) Writes.write(array, i, array[j], 0.1, true, false);
                    i++;
                    h = j;
                } else {
                    Highlights.clearAllMarks();
                    for (int k = j; k < Math.min(where, end); k++) {
                        Highlights.markArray(1, k);
                        Writes.write(collect, collected++, array[k], 0.1, false, true);
                    }
                }
            }
        }
        Writes.deleteExternalArray(collect);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        opium(array, 0, currentLength, true, false);
    }
}