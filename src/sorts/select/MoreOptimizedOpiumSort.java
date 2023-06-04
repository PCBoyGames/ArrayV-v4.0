package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class MoreOptimizedOpiumSort extends Sort {

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

    protected int maxsorted(int[] array, int e, int i) {
        int a = i - 1;
        int b = e;
        boolean segment = true;
        while (segment) {
            if (b - 1 < 0) return 0;
            if (Reads.compareIndices(array, b - 1, b, 0.1, true) > 0) segment = false;
            else b--;
        }
        int sel = b - 1;
        for (int s = b - 2; s >= 0; s--) if (Reads.compareIndices(array, sel, s, 0.1, true) < 0) sel = s;
        while (Reads.compareIndices(array, sel, a, 0.1, true) <= 0) a--;
        return a + 1;
    }

    protected int minsorted(int[] array, int a, int b) {
        int i = a;
        boolean segment = true;
        while (segment) {
            if (i + 1 > b) return b;
            if (Reads.compareIndices(array, i, i + 1, 0.1, true) > 0) segment = false;
            else i++;
        }
        int sel = i + 1;
        for (int s = i + 2; s < b; s++) if (Reads.compareIndices(array, sel, s, 0.1, true) > 0) sel = s;
        while (Reads.compareIndices(array, sel, a, 0.1, true) >= 0) a++;
        return a;
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

    @Override
    public void runSort(int[] array, int currentLength, int stable) {
        if (stable != 574873) currentLength = maxsorted(array, currentLength, currentLength);
        int[] collect = Writes.createExternalArray(currentLength);
        for (int j = 0; j < currentLength; j++) collect[j] = -1;
        int collected = 0;
        int j = minsorted(array, 0, currentLength);
        int h = (where = 0);
        for (int i = j; i < currentLength; j = where) {
            if (j > currentLength - 1) {
                for (int k = i; k <= h; k++) {
                    Writes.write(array, k, collect[k - i], 0.1, true, false);
                    collect[k - i] = -1;
                }
                for (int k = 0; k < currentLength; k++) collect[k] = -1;
                where = (j = i);
                collected = (h = 0);
            }
            if (isLeast(array, collect, j, collected, currentLength)) {
                Highlights.clearAllMarks();
                if (i != j) Writes.write(array, i, array[j], 0.1, true, false);
                i++;
                h = j;
            } else {
                Highlights.clearAllMarks();
                for (int k = j; k < Math.min(where, currentLength); k++) {
                    Highlights.markArray(1, k);
                    Writes.write(collect, collected++, array[k], 0.1, false, true);
                }
            }
        }
        Writes.deleteExternalArray(collect);
    }
}
