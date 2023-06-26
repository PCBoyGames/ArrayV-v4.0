package sorts.exchange;

import java.util.Random;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class CityscapeSort extends Sort {
    public CityscapeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cityscape");
        this.setRunAllSortsName("Cityscape Sort");
        this.setRunSortName("Cityscape Sort");
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
        if (Reads.compareIndices(array, a, b, 0.1, true) > 0) Writes.swap(array, a, b, 0.1, true, false);
    }

    protected void shellPass(int[] array, int start, int end, int gap) {
        for (int h = gap, i = h + start; i < end; i++) {
            int v = array[i];
            int j = i;
            boolean w = false;
            Highlights.markArray(1, j);
            Highlights.markArray(2, j - h);
            Delays.sleep(0.25);
            for (; j >= h && Reads.compareValues(array[j - h], v) == 1; j -= h) {
                Highlights.markArray(2, j - h);
                Writes.write(array, j, array[j - h], 0.25, w = true, false);
            }
            if (w) Writes.write(array, j, v, 0.25, true, false);
        }
    }

    protected void shell(int[] array, int start, int end) {
        for (int gap = (int) ((end - start) / 2.25); gap >= 2; gap /= 2.25) shellPass(array, start, end, gap);
        shellPass(array, start, end, 1);
    }

    protected int maxsorted(int[] array, int start, int end) {
        int a = end - 1;
        int b = end - 1;
        boolean segment = true;
        while (segment) {
            if (b - 1 < start) return start;
            if (Reads.compareIndices(array, b - 1, b, 0.1, true) > 0) segment = false;
            else b--;
        }
        int sel = b - 1;
        for (int s = b - 2; s >= start; s--) if (Reads.compareIndices(array, sel, s, 0.1, true) < 0) sel = s;
        while (Reads.compareIndices(array, sel, a, 0.1, true) <= 0) {
            a--;
            if (a < start) break;
        }
        return a + 1;
    }

    protected void shuffle(int[] array, int start, int end) {
        Random random = new Random(end);
        for (int i = start; i < end; i++) {
            int randomIndex = random.nextInt(end - i) + i;
            if (randomIndex != i) Writes.swap(array, i, randomIndex, 0.1, true, false);
        }
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
            i = maxsorted(array, 0, i);
            shuffle(array, 0, i);
        }
    }
}