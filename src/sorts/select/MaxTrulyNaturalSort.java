package sorts.select;

import java.util.BitSet;

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
 * @author Flanlaina
 * 
 */
public class MaxTrulyNaturalSort extends Sort {
    public MaxTrulyNaturalSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Truly Natural (Max)");
        this.setRunAllSortsName("Max Truly Natural Sort");
        this.setRunSortName("Max Truly Natural Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void sort(int[] array, int a, int b) {
        int len = b - a;
        if (len < 2) return;
        int[] buf = Writes.createExternalArray(len);
        Writes.arraycopy(array, a, buf, 0, len, 0.125, true, true);
        BitSet excl = new BitSet(len);
        for (int i = b - 1; i >= a; i--) {
            int sel = 0;
            Highlights.markArray(2, sel);
            Delays.sleep(0.125);
            while (excl.get(sel)) {
                sel++;
                Highlights.markArray(2, sel);
                Delays.sleep(0.125);
            }
            for (int j = sel + 1; j < len; j++) {
                Highlights.markArray(2, j);
                Delays.sleep(0.125);
                if (!excl.get(j))
                    if (Reads.compareIndices(buf, j, sel, 0, false) >= 0) sel = j;
            }
            Writes.write(array, i, buf[sel], 0.125, true, false);
            excl.set(sel);
            Writes.visualClear(buf, sel);
        }
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);
    }
}
