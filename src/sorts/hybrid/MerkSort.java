package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class MerkSort extends Sort {
    public MerkSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Merk");
        this.setRunAllSortsName("Merk Sort");
        this.setRunSortName("Merksort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void circle(int[] array, int start, int end) {
        int i = start;
        int j = end;
        while (i < j) {
            if (Reads.compareIndices(array, i, j, 0.1, true) > 0) Writes.swap(array, i, j, 0.1, true, false);
            i++;
            j--;
        }
    }

    protected void shellPass(int[] array, int a, int b, int gap) {
        for (int i = a + gap; i < b; i++) {
            int key = array[i];
            int j = i - gap;
            boolean change = false;
            while (j >= a && Reads.compareValues(key, array[j]) < 0) {
                Writes.write(array, j + gap, array[j], 0.1, true, false);
                j -= gap;
                change = true;
            }
            if (change) Writes.write(array, j + gap, key, 0.1, true, false);
        }
        Highlights.clearAllMarks();
    }

    protected void shell(int[] array, int start, int end) {
        for (int gap = end - start; gap >= 1; gap /= 2) shellPass(array, start, end, gap);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int len = 2;
        for (; len <= currentLength; len *= 2);
        len /= 2;
        int l = len;
        for (; l >= 2; l /= 2) for (int i = 0; i + l <= len; i += l) circle(array, i, i + l - 1);
        l = 4;
        for (; l <= len; l *= 2) for (int i = 0; i + l <= len; i += l) shell(array, i, i + l);
        if (len != currentLength) shell(array, 0, currentLength);
    }
}