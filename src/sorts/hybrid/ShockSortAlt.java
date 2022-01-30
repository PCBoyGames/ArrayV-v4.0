package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.merge.QuadSort;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ShockSortAlt extends Sort {
    
    QuadSort quad = new QuadSort(arrayVisualizer);
    
    public ShockSortAlt(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Shock (Quad)");
        this.setRunAllSortsName("Shock Sort (Quad)");
        this.setRunSortName("Shocksort (Quad)");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    protected int pow2lte(int value) {
        int val;
        for (val = 1; val <= value; val <<= 1);
        return val >> 1;
    }
    
    protected int binarySearch(int[] array, int a, int b, int value) {
        while (a < b) {
            int m = a + ((b - a) / 2);
            Highlights.markArray(1, a);
            Highlights.markArray(3, m);
            Highlights.markArray(2, b);
            Delays.sleep(1);
            if (Reads.compareValues(value, array[m]) < 0) b = m;
            else a = m + 1;
        }
        Highlights.clearMark(3);
        return a;
    }
    
    protected void binsert(int[] array, int start, int currentLength) {
        for (int i = start + 1; i < currentLength; i++) {
            if (Reads.compareValues(array[i - 1], array[i]) > 0) {
                int item = array[i];
                int left = binarySearch(array, start, i - 1, item);
                Highlights.clearAllMarks();
                Highlights.markArray(2, left);
                for (int right = i; right > left; right--) Writes.write(array, right, array[right - 1], 0.05, true, false);
                Writes.write(array, left, item, 0.05, true, false);
                Highlights.clearAllMarks();
            } else {
                Highlights.markArray(1, i);
                Delays.sleep(0.25);
            }
        }
    }
    
    private void shellPass(int[] array, int currentLength, int gap) {
        for (int h = gap, i = h; i < currentLength; i++) {
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

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int effectivelen = currentLength;
        int size = pow2lte((int) Math.sqrt(effectivelen));
        effectivelen -= effectivelen % size;
        for (int i = 0; i < effectivelen; i += size) binsert(array, i, i + size);
        if (effectivelen != currentLength) binsert(array, effectivelen, currentLength);
        int gap = effectivelen;
        while (gap > size) shellPass(array, currentLength, gap /= 2);
        int verifyi = size - 1;
        boolean verify = true;
        while (verifyi < currentLength && verify) {
            if (Reads.compareIndices(array, verifyi, verifyi + 1, 0.25, true) <= 0) verifyi++;
            else verify = false;
        }
        if (!verify) {
            if (array[verifyi] - array[verifyi + 1] > currentLength / 2) {
                int[] pieces = Writes.createExternalArray(effectivelen);
                int writeval = 0;
                for (int i = 0; i < size; i++) {
                    for (int j = i; j < effectivelen; j += size) {
                        Highlights.markArray(2, j);
                        Writes.write(pieces, writeval, array[j], 0.25, true, true);
                        writeval++;
                    }
                }
                Highlights.clearMark(2);
                Writes.arraycopy(pieces, 0, array, 0, effectivelen, 0.25, true, false);
                Writes.deleteExternalArray(pieces);
            }
            quad.runSort(array, currentLength, 0);
        }
    }
}