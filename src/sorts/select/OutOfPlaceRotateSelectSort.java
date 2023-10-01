package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class OutOfPlaceRotateSelectSort extends Sort {

    int[] ext;

    public OutOfPlaceRotateSelectSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Out-of-Place Rotate Selection");
        this.setRunAllSortsName("Out-of-Place Rotate Selection Sort");
        this.setRunSortName("Out-of-Place Rotate Selectsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void blockSwap(int[] array, int a, int b, int len) {
        for (int i = 0; i < len; i++) Writes.swap(array, a + i, b + i, 0.1, true, false);
    }

    protected void swapBlocksBackwards(int[] array, int a, int b, int len) {
        for (int i = 0; i < len; i++) Writes.swap(array, a + len - i - 1, b + len - i - 1, 0.1, true, false);
    }

    protected void shiftForwards(int[] array, int start, int length) {
        int temp = array[start];
        Highlights.clearMark(2);
        for (int i = 0; i < length; i++) Writes.write(array, start + i, array[start + i + 1], 0.1, true, false);
        Writes.write(array, start + length, temp, 1, true, false);
    }

    protected void shiftBackwards(int[] array, int start, int length) {
        int temp = array[start + length];
        Highlights.clearMark(2);
        for (int i = length; i > 0; i--) Writes.write(array, start + i, array[start + i - 1], 0.1, true, false);
        Writes.write(array, start, temp, 1, true, false);
    }

    protected void holyGriesMills(int[] array, int pos, int lenA, int lenB) {
        while (lenA > 1 && lenB > 1) {
            for (; lenA <= lenB; pos += lenA, lenB -= lenA) blockSwap(array, pos, pos + lenA, lenA);
            if (lenA <= 1 || lenB <= 1) break;
            for (; lenA >= lenB; lenA -= lenB) swapBlocksBackwards(array, pos + lenA - lenB, pos + lenA, lenB);
        }
        if (lenA == 1) shiftForwards(array, pos, lenB);
        else if (lenB == 1) shiftBackwards(array, pos, lenA);
    }

    protected void bridge(int[] array, int pos, int left, int right) {
        if (left < 1 || right < 1) return;
        int pta = pos;
        int ptb = pos + left;
        int ptc = pos + right;
        int ptd = ptb + right;
        if (left < right) {
            int bridge = right - left;
            if (bridge < left) {
                int loop = left;
                Writes.arraycopy(array, ptb, ext, 0, bridge, 0.1, true, true);
                while (loop-- > 0) {
                    Writes.write(array, --ptc, array[--ptd], 0.05, true, false);
                    Writes.write(array, ptd, array[--ptb], 0.05, true, false);
                }
                Writes.arraycopy(ext, 0, array, pta, bridge, 0.1, true, false);
            } else {
                Writes.arraycopy(array, pta, ext, 0, left, 0.1, true, true);
                Writes.arraycopy(array, ptb, array, pta, right, 0.1, true, false);
                Writes.arraycopy(ext, 0, array, ptc, left, 0.1, true, false);
            }
        } else {
            int bridge = left - right;
            if (bridge < right) {
                int loop = right;
                Writes.arraycopy(array, ptc, ext, 0, bridge, 0.1, true, true);
                while (loop-- > 0) {
                    Writes.write(array, ptc++, array[pta], 0.05, true, false);
                    Writes.write(array, pta++, array[ptb++], 0.05, true, false);
                }
                Writes.arraycopy(ext, 0, array, ptd - bridge, bridge, 0.1, true, false);
            } else {
                Writes.arraycopy(array, ptb, ext, 0, right, 0.1, true, true);
                while (left-- > 0) Writes.write(array, --ptd, array[--ptb], 0.1, true, false);
                Writes.arraycopy(ext, 0, array, pta, right, 0.1, true, false);
            }
        }
    }

    protected void rotate(int[] array, int pos, int lenA, int lenB) {
        if (lenA % lenB == 0 || lenB % lenA == 0) holyGriesMills(array, pos, lenA, lenB);
        else bridge(array, pos, lenA, lenB);
    }

    protected void idxRotate(int[] array, int a, int b, int c) {
        rotate(array, a, b - a, c - b);
    }

    protected void rotatesel(int[] array, int start, int end) {
        int min = start;
        for (int i = start + 1; i < end; i++) if (Reads.compareIndices(array, i, min, 0.1, true) < 0) min = i;
        if (min != start) {
            Highlights.clearAllMarks();
            idxRotate(array, start, min, end);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        ext = new int[currentLength];
        for (int i = 0; i < currentLength; i++) rotatesel(array, i, currentLength);
    }
}