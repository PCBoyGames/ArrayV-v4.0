package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.GrailSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
/**A Gapped Median-of-7 Out-of-Place Stable Quicksort.<p>
 * To use this algorithm in another, use {@code runSergioSort()} from a reference instance.
 * @version SerSan-1.0
 * @author PCBoy
*/
final public class SergioSort extends GrailSorting {

    int[] ext;

    public SergioSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Sergio");
        this.setRunAllSortsName("Sergio Quick Sort");
        this.setRunSortName("Sergio Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean sorted(int[] array, int start, int end) {
        for (int i = start; i + 1 <= end; i++) if (Reads.compareIndices(array, i, i + 1, 0, false) > 0) return false;
        return true;
    }

    protected void blockSwap(int[] array, int a, int b, int len) {
        for (int i = 0; i < len; i++) Writes.swap(array, a + i, b + i, 1, true, false);
    }

    protected void swapBlocksBackwards(int[] array, int a, int b, int len) {
        for (int i = 0; i < len; i++) Writes.swap(array, a + len - i - 1, b + len - i - 1, 1, true, false);
    }

    protected void shiftForwards(int[] array, int start, int length) {
        int temp = array[start];
        Highlights.clearMark(2);
        for (int i = 0; i < length; i++) Writes.write(array, start + i, array[start + i + 1], 1, true, false);
        Writes.write(array, start + length, temp, 1, true, false);
    }

    protected void shiftBackwards(int[] array, int start, int length) {
        int temp = array[start + length];
        Highlights.clearMark(2);
        for (int i = length; i > 0; i--) Writes.write(array, start + i, array[start + i - 1], 1, true, false);
        Writes.write(array, start, temp, 1, true, false);
    }

    protected void holyGriesMills(int[] array, int pos, int lenA, int lenB) {
        while (lenA > 1 && lenB > 1) {
            while (lenA <= lenB) {
                blockSwap(array, pos, pos + lenA, lenA);
                pos += lenA;
                lenB -= lenA;
            }
            if (lenA <= 1 || lenB <= 1) break;
            while (lenA > lenB) {
                swapBlocksBackwards(array, pos + lenA - lenB, pos + lenA, lenB);
                lenA -= lenB;
            }
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
                Writes.arraycopy(array, ptb, ext, 0, bridge, 1, true, true);
                while (loop-- > 0) {
                    Writes.write(array, --ptc, array[--ptd], 0.5, true, false);
                    Writes.write(array, ptd, array[--ptb], 0.5, true, false);
                }
                Writes.arraycopy(ext, 0, array, pta, bridge, 1, true, false);
            } else {
                Writes.arraycopy(array, pta, ext, 0, left, 1, true, true);
                Writes.arraycopy(array, ptb, array, pta, right, 1, true, false);
                Writes.arraycopy(ext, 0, array, ptc, left, 1, true, false);
            }
        } else {
            int bridge = left - right;
            if (bridge < right) {
                int loop = right;
                Writes.arraycopy(array, ptc, ext, 0, bridge, 1, true, true);
                while (loop-- > 0) {
                    Writes.write(array, ptc++, array[pta], 0.5, true, false);
                    Writes.write(array, pta++, array[ptb++], 0.5, true, false);
                }
                Writes.arraycopy(ext, 0, array, ptd - bridge, bridge, 1, true, false);
            } else {
                Writes.arraycopy(array, ptb, ext, 0, right, 1, true, true);
                while (left-- > 0) Writes.write(array, --ptd, array[--ptb], 1, true, false);
                Writes.arraycopy(ext, 0, array, pta, right, 1, true, false);
            }
        }
    }

    protected void grailRotate(int[] array, int pos, int lenA, int lenB) {
        if (lenA % lenB == 0 || lenB % lenA == 0) holyGriesMills(array, pos, lenA, lenB);
        else bridge(array, pos, lenA, lenB);
    }

    protected void stableSegmentReversal(int[] array, int start, int end) {
        if (end - start < 3) Writes.swap(array, start, end, 0.075, true, false);
        else Writes.reversal(array, start, end, 0.075, true, false);
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (Reads.compareIndices(array, i, i + 1, 0.25, true) == 0 && i < end) i++;
            right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, 0.75, true, false);
                else Writes.reversal(array, left, right, 0.75, true, false);
            }
            i++;
        }
    }

    protected int pd(int[] array, int start, int end) {
        int reverse = start;
        boolean lessunique = false;
        boolean different = false;
        int cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        while (cmp >= 0 && reverse + 1 < end) {
            if (cmp == 0) lessunique = true;
            else different = true;
            reverse++;
            if (reverse + 1 < end) cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        }
        if (reverse > start && different) {
            if (lessunique) stableSegmentReversal(array, start, reverse);
            else if (reverse < start + 3) Writes.swap(array, start, reverse, 0.75, true, false);
            else Writes.reversal(array, start, reverse, 0.75, true, false);
        }
        return reverse;
    }

    protected boolean indiceshandle(int[] array, int[] ext, int a, int b) {
        if (Reads.compareIndices(array, ext[a], ext[b], 10, true) > 0) {
            Writes.swap(ext, a, b, 0, false, true);
            return true;
        } else return false;
    }

    protected int extmo7(int[] array, int start, int end) {
        for (int i = 0; i < 7; i++) Writes.write(ext, i, start + i * ((end - start) / 6), 0, false, true);
        int[][] network = {{0,6},{1,5},{1,2},{4,5},{0,1},{5,6},{1,5},{2,4},{1,2},{4,5},{3,4},{2,3},{3,4}};
        for (int i = 0; i < network.length; i++) indiceshandle(array, ext, network[i][0], network[i][1]);
        return array[ext[3]];
    }

    protected int blockfindRun(int[] array, int a, int b) {
        int i = a + 1;
        if (i == b) return i;
        if (Reads.compareIndices(array, i - 1, i++, 1, true) > 0) {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) i++;
            if (i - a > 3) Writes.reversal(array, a, i - 1, 1, true, false);
            else Writes.swap(array, a, i - 1, 1, true, false);
        }
        else while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        Highlights.clearMark(2);
        return i;
    }

    protected void insertionSort(int[] array, int a, int b) {
        int i, j, len;
        i = blockfindRun(array, a, b);
        while (i < b) {
            j = blockfindRun(array, i, b);
            len = j - i;
            grailMergeWithoutBuffer(array, a, i - a, len);
            i = j;
        }
    }

    protected int mergeFindRun(int[] array, int a, int b) {
        int i = a + 1;
        if (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) i++;
                if (i - a < 4) Writes.swap(array, a, i - 1, 1, true, false);
                else Writes.reversal(array, a, i - 1, 1, true, false);
            } else while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        }
        Highlights.clearMark(2);
        if (i < a + 16 && i < b) {
            i = Math.min(b, a + 16);
            insertionSort(array, a, i);
        }
        return i;
    }

    protected int binSearch(int[] array, int a, int b, int val, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0)) b = m;
            else a = m + 1;
        }
        return a;
    }

    protected int leftExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0) i *= 2;
        else while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0) i *= 2;
        int a1 = a + i / 2, b1 = Math.min(b, a - 1 + i);
        return binSearch(array, a1, b1, val, left);
    }

    protected int rightExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0) i *= 2;
        else while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        return binSearch(array, a1, b1, val, left);
    }

    protected void mergeFWExt(int[] array, int a, int m, int b) {
        int s = m - a;
        Writes.arraycopy(array, a, ext, 0, s, 1, true, true);
        int i = 0, j = m;
        while (i < s && j < b) {
            if (Reads.compareValues(ext[i], array[j]) <= 0) Writes.write(array, a++, ext[i++], 1, true, false);
            else Writes.write(array, a++, array[j++], 1, true, false);
        }
        while (i < s) Writes.write(array, a++, ext[i++], 1, true, false);
    }

    protected void mergeBWExt(int[] array, int a, int m, int b) {
        int s = b - m;
        Writes.arraycopy(array, m, ext, 0, s, 1, true, true);
        int i = s - 1, j = m - 1;
        while (i >= 0 && j >= a) {
            if (Reads.compareValues(ext[i], array[j]) >= 0) Writes.write(array, --b, ext[i--], 1, true, false);
            else Writes.write(array, --b, array[j--], 1, true, false);
        }
        while (i >= 0) Writes.write(array, --b, ext[i--], 1, true, false);
    }

    protected void merge(int[] array, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0, true) <= 0) return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        Highlights.clearMark(2);
        if (m - a > b - m) mergeBWExt(array, a, m, b);
        else mergeFWExt(array, a, m, b);
    }

    protected void mergeSort(int[] array, int a, int b) {
        int i, j, k;
        while (true) {
            i = mergeFindRun(array, a, b);
            if (i >= b) break;
            j = mergeFindRun(array, i, b);
            merge(array, a, i, j);
            Highlights.clearMark(2);
            if (j >= b) break;
            k = j;
            while (true) {
                i = mergeFindRun(array, k, b);
                if (i >= b) break;
                j = mergeFindRun(array, i, b);
                merge(array, k, i, j);
                if (j >= b) break;
                k = j;
            }
        }
    }

    protected int[] partition(int[] array, int start, int end, int pivot, int lensave) {
        int collected = 0;
        int passed = 0;
        int tern = 0;
        int partedhere = 0;
        for (int i = start; i <= end; i++) {
            int comp = Reads.compareValues(pivot, array[i]);
            if (comp == 0) {
                Writes.write(ext, lensave - (tern + 1), array[i], 0, false, true);
                tern++;
            } else if (comp < 0) {
                Writes.write(ext, collected, array[i], 0, false, true);
                collected++;
            } else {
                if (i > start + passed) Writes.write(array, start + passed, array[i], 0, false, false);
                passed++;
            }
            Highlights.markArray(1, i);
            Highlights.markArray(2, start + passed);
            Delays.sleep(0.5);
        }
        Highlights.clearAllMarks();
        for (int i = 0; i < tern; i++) {
            if (partedhere == 0) if (Reads.compareOriginalValues(ext[lensave - (i + 1)], array[start + passed + i]) != 0) partedhere = 1;
            Writes.write(array, start + passed + i, ext[lensave - (i + 1)], 0.5, true, false);
        }
        for (int i = 0; i < collected; i++) {
            if (partedhere == 0) if (Reads.compareOriginalValues(ext[i], array[start + passed + tern + i]) != 0) partedhere = 1;
            Writes.write(array, start + passed + tern + i, ext[i], 0.5, true, false);
        }
        int[] ret = {passed, tern, partedhere};
        return ret;
    }

    protected void sergio(int[] array, int start, int end, int depth, int depthlimit, int lensave) {
        Writes.recordDepth(depth);
        if (end - start < 31 || depth > depthlimit) mergeSort(array, start, end + 1);
        else if (pd(array, start, end + 1) < end) {
            if (!sorted(array, start, end)) {
                int pivot = extmo7(array, start, end);
                int[] part = partition(array, start, end, pivot, lensave);
                if (part[2] == 1) {
                    Writes.recursion();
                    sergio(array, start + part[0] + part[1], end, depth + 1, depthlimit, lensave);
                    Writes.recursion();
                    sergio(array, start, start + part[0] - 1, depth + 1, depthlimit, lensave);
                } else mergeSort(array, start, end + 1);
            }
        }
    }

    /**Runs a Gapped Median-of-7 Out-of-Place Stable Quicksort on the input.*/
    public void runSergioSort(int[] array, int start, int length) {
        ext = Writes.createExternalArray(length);
        sergio(array, start, start + length - 1, 0, (int) ((Math.E / 2) * (Math.log(length) / Math.log(2))), length);
        Writes.deleteExternalArray(ext);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        runSergioSort(array, 0, currentLength);
    }
}