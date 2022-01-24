package sorts.merge;

import main.ArrayVisualizer;
import sorts.hybrid.QuadSort;
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
        int writeval = 0;
        for (int i = 0; i < base; i++) {
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
        int left = a;
        int right = b;
        while (left < right) {
            Highlights.markArray(1, left);
            Highlights.markArray(2, right);
            Delays.sleep(0.25);
            if (Reads.compareValues(array[left], array[right]) > 0) Writes.swap(array, left, right, 0.25, true, false);
            left++;
            right--;
        }
    }
    
    protected void circlepass(int[] array, int start, int len) {
        int gap = len;
        while (gap > 1) {
            int offset = 0;
            while (offset + (gap - 1) < len) {
                circle(array, start + offset, start + offset + (gap - 1));
                offset += gap;
            }
            gap /= 2;
        }
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
        int index = 0;
        while (len < currentLength) {
            index = 0;
            while (index + len - 1 < currentLength) {
                if (len == 2) {
                    Highlights.markArray(1, index);
                    Highlights.markArray(2, index + 1);
                    Delays.sleep(0.25);
                    if (Reads.compareValues(array[index], array[index + 1]) > 0) Writes.swap(array, index, index + 1, 0.25, true, false);
                }
                else method(array, index, len);
                Highlights.clearAllMarks();
                index += len;
            }
            len *= 2;
        }
        if (len == currentLength) method(array, 0, currentLength);
        else quad.runSort(array, currentLength, 0);
    }
}