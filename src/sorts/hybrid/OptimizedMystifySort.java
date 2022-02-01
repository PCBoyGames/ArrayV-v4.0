package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedMystifySort extends Sort {
    public OptimizedMystifySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Mystify");
        this.setRunAllSortsName("Optimized Mystify Sort");
        this.setRunSortName("Optimized Mystifysort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the base for this sort:", 2);
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
    public int validateAnswer(int answer) {
        if (answer < 2) return 2;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        int effectivelen = currentLength;
        effectivelen -= effectivelen % base;
        int size = 1;
        while (size <= effectivelen) size *= base;
        size /= base;
        int initsize = size;
        boolean verify = false;
        while (size > base && !verify) {
            size /= base;
            int gap = initsize;
            while (gap > size) shellPass(array, effectivelen, gap /= base);
            int verifyi = size - 1;
            verify = true;
            while (verifyi < currentLength && verify) {
                if (Reads.compareIndices(array, verifyi, verifyi + 1, 0.25, true) <= 0) verifyi++;
                else verify = false;
            }
            if (size != 1 && !verify) {
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
        }
        if (!verify) {
            int gap = initsize;
            while (gap > 1) shellPass(array, currentLength, gap /= base);
        }
    }
}