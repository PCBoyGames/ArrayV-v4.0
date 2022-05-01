package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class ShnexSort extends Sort {
    public ShnexSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Shnex");
        this.setRunAllSortsName("Shnex Sort");
        this.setRunSortName("Shnex Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(32);
        this.setBogoSort(false);
    }
    
    protected int stablereturn(int a) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) : a;
    }
    
    // Entirely cheating, like the par function, but this does what it needs to do in O(n) time.
    protected boolean noDupes(int[] array, int len) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < len; i++) {
            if (stablereturn(array[i]) < min) min = stablereturn(array[i]);
            if (stablereturn(array[i]) > max) max = stablereturn(array[i]);
        }
        if (min != 0 || max != len - 1) return false;
        int size = max - min + 1;
        int[] holes = new int[size];
        for (int x = 0; x < len; x++) {
            if (holes[stablereturn(array[x]) - min] == 1) {
                return false;
            } else holes[stablereturn(array[x]) - min] = 1;
        }
        return true;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (noDupes(array, currentLength)) {
            for (int i = 0; i < currentLength; i++) {
                Highlights.markArray(1, i);
                Delays.sleep(1);
                double times = 1;
                while (Reads.compareValues(stablereturn(array[i]), i) != 0) {
                    Writes.multiSwap(array, i, stablereturn(array[i]), 1 / times, true, false);
                    times += 0.1;
                }
                Highlights.clearAllMarks();
            }
        } else {
            arrayVisualizer.setExtraHeading(" / Data does not fit! Fallback!");
            double times = 1;
            for (int i = 0; i + 1 < currentLength; i++) {
                if (Reads.compareIndices(array, i, i + 1, 1 / times, true) > 0) {
                    Writes.multiSwap(array, i + 1, 0, 1 / times, true, false);
                    i = -1;
                    times += 0.1;
                }
            }
            arrayVisualizer.setExtraHeading("");
        }
    }
}