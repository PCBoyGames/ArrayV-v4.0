package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.PDBinaryInsertionSort;
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
    PDBinaryInsertionSort binsert = new PDBinaryInsertionSort(arrayVisualizer);

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

    protected void binsert(int[] array, int start, int currentLength) {
        binsert.pdbinsert(array, start, currentLength, 0.25, false);
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