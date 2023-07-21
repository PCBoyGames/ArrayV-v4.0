package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class TransposeModuloWeaveMergeSort extends Sort {

    QuadSort quad = new QuadSort(arrayVisualizer);

    public TransposeModuloWeaveMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Transpose by Modulo Weave Merge");
        this.setRunAllSortsName("Transpose by Modulo Weave Merge Sort");
        this.setRunSortName("Transpose by Modulo Weave Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void weave(int[] array, int[] pieces, int start, int len, int base) {
        for (int i = 0, writeval = 0; i < base; i++) {
            for (int j = i; j < len; j += base) {
                Highlights.markArray(1, start + writeval);
                Highlights.markArray(2, start + j);
                Writes.write(pieces, writeval, array[start + j], 0.25, false, true);
                writeval++;
            }
        }
        Highlights.clearAllMarks();
        Writes.arraycopy(pieces, 0, array, start, len, 0.25, true, false);
    }

    protected void circle(int[] array, int a, int b) {
        for (; a < b; a++, b--) if (Reads.compareIndices(array, a, b, 0.25, true) > 0) Writes.swap(array, a, b, 0.25, true, false);
    }

    protected void circlepass(int[] array, int start, int len) {
        for (int gap = len; gap > 1; gap /= 2) for (int offset = 0; offset + (gap - 1) < len; offset += gap) circle(array, start + offset, start + offset + (gap - 1));
    }

    protected void method(int[] array, int start, int len) {
        int[] pieces = Writes.createExternalArray(len);
        if (Reads.compareIndices(array, start + (len / 2) - 1, start + (len / 2), 0.25, true) > 0) {
            weave(array, pieces, start, len, len / 2);
            circlepass(array, start, len);
        }
        Writes.deleteExternalArray(pieces);
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        int len = 2;
        for (; len < currentLength; len *= 2) {
            for (int index = 0; index + len - 1 < currentLength; index += len) {
                if (len == 2) {if (Reads.compareIndices(array, index, index + 1, 0.25, true) > 0) Writes.swap(array, index, index + 1, 0.25, true, false);}
                else method(array, index, len);
                Highlights.clearAllMarks();
            }
        }
        if (len == currentLength) method(array, 0, currentLength);
        else quad.runSort(array, currentLength, 0);
    }
}