package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class CityscapeSortNS extends Sort {
    public CityscapeSortNS(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cityscape (No Shuffles)");
        this.setRunAllSortsName("Cityscape Sort (No Shuffles)");
        this.setRunSortName("Cityscape Sort (No Shuffles)");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void csdep(int[] array, int i, int j) {
        if (i == j) return;
        int a = Math.min(i, j);
        int b = Math.max(i, j);
        if (Reads.compareIndices(array, a, b, 0.1, true) > 0) {
            Writes.swap(array, a, b, 0.1, true, false);
        }
    }

    protected void shellPass(int[] array, int start, int end, int gap) {
        for (int h = gap, i = h + start; i < end; i++) {
            int v = array[i];
            int j = i;
            boolean w = false;
            Highlights.markArray(1, j);
            Highlights.markArray(2, j - h);
            Delays.sleep(0.25);
            while (j >= h && Reads.compareValues(array[j - h], v) == 1) {
                Highlights.markArray(1, j);
                Highlights.markArray(2, j - h);
                Delays.sleep(0.25);
                Writes.write(array, j, array[j - h], 0.25, true, false);
                j -= h;
                w = true;
            }
            if (w) {
                Writes.write(array, j, v, 0.25, true, false);
            }
        }
    }

    protected void shell(int[] array, int start, int end) {
        int gap = (int) ((end - start) / 2.25);
        while (gap >= 2) {
            shellPass(array, start, end, gap);
            gap /= 2.25;
        }
        shellPass(array, start, end, 1);
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

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i = currentLength;
        while (i > 0) {
            int h = i;
            int j = 0;
            for (; j < i; j++) {
                csdep(array, j, j + 1);
                for (h = i - 1; h > i - j - 2; h--) {
                    csdep(array, j, h);
                    if (h <= j) break;
                }
                if (h <= j) break;
            }
            shell(array, j, i);
            i = maxsorted(array, j, i);
        }
    }
}