package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Haruki
in collaboration with aphitorite
extending code by Gaming32

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Haruki
 * @author aphitorite
 * @author Gaming32
 *
 */
public class FifthMergeSort2 extends Sort {

    public FifthMergeSort2(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Fifth Merge 2");
        this.setRunAllSortsName("Fifth Merge Sort");
        this.setRunSortName("Fifth Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    static final int MIN_RUN = 8;

    protected void shiftFWExt(int[] array, int a, int m, int b) {
        while (m < b) {
            Highlights.markArray(2, m);
            Writes.write(array, a++, array[m++], 1, true, false);
        }
        Highlights.clearMark(2);
    }

    protected void shiftBWExt(int[] array, int a, int m, int b) {
        while (m > a) {
            Highlights.markArray(2, --m);
            Writes.write(array, --b, array[m], 1, true, false);
        }
        Highlights.clearMark(2);
    }

    protected void mergeWithBufFWExt(int[] array, int a, int m, int b, int p) {
        int i = m;
        while (a < m && i < b) {
            Highlights.markArray(2, i);
            if (Reads.compareValues(array[a], array[i]) <= 0)
                Writes.write(array, p++, array[a++], 1, true, false);
            else
                Writes.write(array, p++, array[i++], 1, true, false);
        }
        if (a > p) this.shiftFWExt(array, p, a, m);
        this.shiftFWExt(array, p, i, b);
    }

    protected void dualMergeBWExt(int[] array, int a, int m, int b, int p) {
        int i = m - 1; b--;
        while (p > b + 1 && b >= m) {
            Highlights.markArray(2, i);
            if (Reads.compareValues(array[b], array[i]) >= 0)
                Writes.write(array, --p, array[b--], 1, true, false);
            else
                Writes.write(array, --p, array[i--], 1, true, false);
        }
        if (b < m) this.shiftBWExt(array, a, i + 1, p);
        else {
            i++; b++; p = m - (i - a);
            while (a < i && m < b) {
                Highlights.markArray(2, m);
                if (Reads.compareValues(array[a], array[m]) <= 0)
                    Writes.write(array, p++, array[a++], 1, true, false);
                else
                    Writes.write(array, p++, array[m++], 1, true, false);
            }
            while (a < i) Writes.write(array, p++, array[a++], 1, true, false);
        }
    }

    protected int findRun(int[] array, int a, int b, double sleep, boolean auxwrite) {
        int i = a + 1;
        if (i >= b) return i;
        boolean dir = Reads.compareIndices(array, i - 1, i++, sleep, true) <= 0;
        while (i < b) {
            if (dir ^ Reads.compareIndices(array, i - 1, i, sleep, true) <= 0) break;
            i++;
        }
        if (!dir) {
            if (i - a < 4) Writes.swap(array, a, i - 1, sleep, true, auxwrite);
            else Writes.reversal(array, a, i - 1, sleep, true, auxwrite);
        }
        Highlights.clearMark(2);
        return i;
    }

    public void insertionSort(int[] array, int a, int b, double sleep, boolean auxwrite) {
        for (int i = findRun(array, a, b, sleep, auxwrite); i < b; i++) {
            if (Reads.compareIndices(array, i, i - 1, sleep, true) < 0) {
                Highlights.clearMark(2);
                int current = array[i];
                int pos = i;
                do {
                    Writes.write(array, pos, array[pos - 1], sleep, true, auxwrite);
                    pos--;
                } while (pos > a && Reads.compareValues(current, array[pos - 1]) < 0);
                Writes.write(array, pos, current, sleep, true, auxwrite);
            }
        }
    }

    protected void mergeTo(int[] from, int[] to, int a, int m, int b, int p, boolean aux) {
        int i = a, j = m;
        while (i < m && j < b) {
            Highlights.markArray(2, i);
            Highlights.markArray(3, j);
            if (Reads.compareValues(from[i], from[j]) <= 0)
                Writes.write(to, p++, from[i++], 1, true, aux);
            else
                Writes.write(to, p++, from[j++], 1, true, aux);
        }
        Highlights.clearMark(3);
        while (i < m) {
            Highlights.markArray(2, i);
            Writes.write(to, p++, from[i++], 1, true, aux);
        }
        while (j < b) {
            Highlights.markArray(2, j);
            Writes.write(to, p++, from[j++], 1, true, aux);
        }
        Highlights.clearMark(2);
    }

    public void mergePP(int[] array, int[] tmp, int a, int b, int o) {
        int j = MIN_RUN, len = b - a;
        for (int i = a; i < b; i += j)
            insertionSort(array, i, Math.min(i + j, b), 0.5, false);
        while (j < len) {
            int i;
            for (i = 0; i + j < len; i += 2 * j)
                mergeTo(array, tmp, a + i, a + i + j, a + Math.min(i + 2 * j, len), o + i, true);
            j *= 2;
            if (j >= len) {
                Writes.arraycopy(tmp, o, array, a, len, 1, true, false);
                break;
            }
            for (i = 0; i + j < len; i += 2 * j)
                mergeTo(tmp, array, o + i, o + i + j, o + Math.min(i + 2 * j, len), a + i, false);
            j *= 2;
        }
    }

    /**
     * Sorts the range {@code [left, right)} of {@code array} using Fifth Merge
     * Sort.
     * 
     * @param array the array
     * @param left  the start of the range, inclusive
     * @param right the end of the range, exclusive
     */
    public void fifthMergeSort(int[] array, int left, int right) {
        int currentLength = right - left;
        int fifthLen = currentLength / 5;
        int bufferLen = currentLength - fifthLen * 4;
        int[] buffer = Writes.createExternalArray(bufferLen);
        mergePP(array, buffer, left, left + bufferLen, 0);
        for (int i = 0, start = left + bufferLen; i < 4; i++, start += fifthLen) {
            mergePP(array, buffer, start, start + fifthLen, 0);
        }
        Writes.arraycopy(array, left, buffer, 0, bufferLen, 0.5, true, true);
        int twoFifths = 2 * fifthLen;
        for (int i = 0, start = left + bufferLen; i < 2; i++, start += twoFifths) {
            mergeWithBufFWExt(array, start, start + fifthLen, start + twoFifths, start - bufferLen);
        }
        dualMergeBWExt(array, left, left + twoFifths, left + 2 * twoFifths, right);
        int i = 0, j = left + bufferLen, k = left;
        while (i < bufferLen && j < right) {
            Highlights.markArray(2, j);
            if (Reads.compareValues(buffer[i], array[j]) <= 0)
                Writes.write(array, k++, buffer[i++], 1, true, false);
            else
                Writes.write(array, k++, array[j++], 1, true, false);
        }
        Highlights.clearMark(2);
        while (i < bufferLen) Writes.write(array, k++, buffer[i++], 1, true, false);
        Writes.deleteExternalArray(buffer);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        fifthMergeSort(array, 0, sortLength);

    }

}
