package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.GrailSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

CHANGELOG:

SerSan-1.1 (05/20/2023)
 - Make IPU, IP, and OIP versions.
   + There was never a 1.0 for these, if you ask.
 - Assure better recursion behavior. All "main" loops now use a tail recursion method.
   + This required fixing the partition outputs of OOP Sergio to be more like the others in development.
 - Fix visual inconsistency with HGM rotations.
   + Before, rotating backwards equally would rotate forward on the last iteration.
   + This was escpecially noticeable in IP and OIP during development.

SerSan-1.0 (03/21/2023)
 - Initial version.
   + Only OOP Sergio was made and released with this version tag.

*/
/**A Gapped Median-of-7 In-Place Stable Quicksort.<p>
 * To use this algorithm in another, use {@code runSergioSort()} from a reference instance.
 * @version SerSan-1.1
 * @author PCBoy
*/
final public class InPlaceSergioSort extends GrailSorting {
    public InPlaceSergioSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("In-Place Sergio");
        this.setRunAllSortsName("In-Place Sergio Quick Sort");
        this.setRunSortName("In-Place Sergio Quicksort");
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

    protected int excludeBinSearch(int[] array, int a, int b, int v) {
        while (a < b) {
            int m = a + ((b - a) / 2);
            Highlights.markArray(1, a);
            Highlights.markArray(3, m);
            Highlights.markArray(2, b);
            Delays.sleep(1);
            int cmp = Reads.compareValues(v, array[m]);
            if (cmp == 0) return -1;
            else if (cmp < 0) b = m;
            else a = m + 1;
        }
        return a;
    }

    protected boolean canGrab7(int[] array, int a, int b) {
        int l = 1;
        for (int i = a + 1; i <= b; i++) {
            Highlights.markArray(4, i);
            int bin = excludeBinSearch(array, a, a + l, array[i]);
            Highlights.clearAllMarks();
            if (bin != -1) {
                Writes.insert(array, i, bin, 1, true, false);
                l++;
                if (l == 7) return true;
            }
        }
        return false;
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
            while (lenA >= lenB) {
                swapBlocksBackwards(array, pos + lenA - lenB, pos + lenA, lenB);
                lenA -= lenB;
            }
        }
        if (lenA == 1) shiftForwards(array, pos, lenB);
        else if (lenB == 1) shiftBackwards(array, pos, lenA);
    }

    protected void trinity(int[] array, int pos, int lenA, int lenB) {
        if (lenA < 1 || lenB < 1) return;
        int a = pos, b = pos + lenA - 1, c = pos + lenA, d = pos + lenA + lenB - 1;
        int swap;
        while (a < b && c < d) {
            swap = array[b];
            Writes.write(array, b--, array[a], 1, true, false);
            Writes.write(array, a++, array[c], 1, true, false);
            Writes.write(array, c++, array[d], 1, true, false);
            Writes.write(array, d--, swap, 1, true, false);
        }
        while (a < b) {
            swap = array[b];
            Writes.write(array, b--, array[a], 1, true, false);
            Writes.write(array, a++, array[d], 1, true, false);
            Writes.write(array, d--, swap, 1, true, false);
        }
        while (c < d) {
            swap = array[c];
            Writes.write(array, c++, array[d], 1, true, false);
            Writes.write(array, d--, array[a], 1, true, false);
            Writes.write(array, a++, swap, 1, true, false);
        }
        if (a < d) {
            if (d - a > 2) Writes.reversal(array, a, d, 1, true, false);
            else Writes.swap(array, a, d, 1, true, false);
            Highlights.clearMark(2);
        }
    }

    protected void grailRotate(int[] array, int pos, int lenA, int lenB) {
        if (lenA == 0 || lenB == 0) return;
        Highlights.clearAllMarks();
        if (lenA % lenB == 0 || lenB % lenA == 0) holyGriesMills(array, pos, lenA, lenB);
        else trinity(array, pos, lenA, lenB);
    }

    protected void rotateIndexed(int[] array, int a, int b, int c) {
        grailRotate(array, a, b - a, c - b);
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

    protected int stableGMo7(int[] array, int start, int end) {
        int[][] network = {{0,6},{1,5},{1,2},{4,5},{0,1},{5,6},{1,5},{2,4},{1,2},{4,5},{3,4},{2,3},{3,4}};
        insertionSort(array, 0, 7);
        for (int n = 0; n < network.length; n++) {
            int posA = network[n][0];
            int posB = network[n][1];
            int relA;
            if (n == 0 || n == 1 || n == 3 || n == 10) relA = posA;
            else {
                relA = 0;
                for (int i = 0; i < 7; i++) if (i != posA) if (Reads.compareIndices(array, i, posA, 0, false) < 0) relA++;
            }
            int relB;
            if (n <= 2) relB = posB;
            else {
                relB = 0;
                for (int i = 0; i < 7; i++) if (i != posB) if (Reads.compareIndices(array, i, posB, 0, false) < 0) relB++;
            }
            int a = start + relA * ((end - start) / 6);
            int b = start + relB * ((end - start) / 6);
            if (Reads.compareIndices(array, a, b, 10, true) > 0) Writes.swap(array, posA, posB, 0, false, false);
        }
        int pos = 0;
        for (int i = 0; i < 7; i++) if (i != 3) if (Reads.compareIndices(array, i, 3, 0, false) < 0) pos++;
        return array[start + pos * ((end - start) / 6)];
    }

    protected int blockfindRun(int[] array, int a, int b) {
        int i = a + 1;
        if (i == b) return i;
        if (Reads.compareIndices(array, i - 1, i++, 1, true) > 0) {
            while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) > 0) i++;
            if (i - a > 3) Writes.reversal(array, a, i - 1, 1, true, false);
            else Writes.swap(array, a, i - 1, 1, true, false);
        }
        else while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;
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
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) > 0) i++;
                if (i - a < 4) Writes.swap(array, a, i - 1, 1, true, false);
                else Writes.reversal(array, a, i - 1, 1, true, false);
            } else while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;
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
            Delays.sleep(1);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0)) b = m;
            else a = m + 1;
        }
        return a;
    }

    protected int expSearch(int[] array, int a, int b, int val, boolean dir, boolean left) {
        int i = 1;
        int a1, b1;
        if (dir) {
            if (left) while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0) i *= 2;
            else while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0) i *= 2;
            a1 = a + i / 2;
            b1 = Math.min(b, a - 1 + i);
        } else {
            if (left) while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0) i *= 2;
            else while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
            a1 = Math.max(a, b - i + 1);
            b1 = b - i / 2;
        }
        return binSearch(array, a1, b1, val, left);
    }

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = expSearch(array, m, b, array[a], true, true);
            rotateIndexed(array, a, m, i);
            int t = i - m;
            m = i;
            a += t + 1;
            if (a >= m) break;
            a = expSearch(array, a, m, array[m], true, false);
        }
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        while (b > m && m > a) {
            int i = expSearch(array, a, m, array[b - 1], false, false);
            rotateIndexed(array, i, m, b);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a) break;
            b = expSearch(array, m, b, array[m - 1], false, true);
        }
    }

    protected void merge(int[] array, int a, int m, int b, int d) {
        Writes.recordDepth(d);
        int lenA = m - a, lenB = b - m;
        if (lenA <= 16 || lenB <= 16) {
            if (m - a > b - m) inPlaceMergeBW(array, a, m, b);
            else inPlaceMergeFW(array, a, m, b);
            return;
        }
        int c = lenA + (lenB - lenA) / 2;
        if (lenB < lenA) {
            int r1 = 0, r2 = lenB;
            while (r1 < r2) {
                int ml = r1 + (r2 - r1) / 2;
                if (Reads.compareValues(array[m - (c - ml)], array[b - ml - 1]) > 0) r2 = ml;
                else r1 = ml + 1;
            }
            rotateIndexed(array, m - (c - r1), m, b - r1);
            int m1 = b - c;
            Writes.recursion();
            merge(array, m1, b - r1, b, d + 1);
            Writes.recursion();
            merge(array, a, m1 - (lenB - r1), m1, d + 1);
        } else {
            int r1 = 0, r2 = lenA;
            while (r1 < r2) {
                int ml = r1 + (r2 - r1) / 2;
                if (Reads.compareValues(array[a + ml], array[m + (c - ml) - 1]) > 0) r2 = ml;
                else r1 = ml + 1;
            }
            rotateIndexed(array, a + r1, m, m + (c - r1));
            int m1 = a + c;
            Writes.recursion();
            merge(array, a, a + r1, m1, d + 1);
            Writes.recursion();
            merge(array, m1, m1 + (lenA - r1), b, d + 1);
        }
    }

    protected void mergeSort(int[] array, int a, int b) {
        int i, j, k;
        while (true) {
            i = mergeFindRun(array, a, b);
            if (i >= b) break;
            j = mergeFindRun(array, i, b);
            merge(array, a, i, j, 0);
            Highlights.clearMark(2);
            if (j >= b) break;
            k = j;
            while (true) {
                i = mergeFindRun(array, k, b);
                if (i >= b) break;
                j = mergeFindRun(array, i, b);
                merge(array, k, i, j, 0);
                if (j >= b) break;
                k = j;
            }
        }
    }

    protected int[] partition(int[] array, int a, int b, int piv, int depth) {
        Writes.recordDepth(depth);
        if (b - a < 2) {
            int[] court = new int[] {a, a};
            int cmp = Reads.compareValues(array[a], piv);
            if (cmp < 0) {
                court[0]++;
                court[1]++;
            } else if (cmp == 0) court[1]++;
            return court;
        }
        int m = a + (b - a) / 2;
        Writes.recursion();
        int[] l = partition(array, a, m, piv, depth + 1);
        Writes.recursion();
        int[] r = partition(array, m, b, piv, depth + 1);
        int l1 = l[0] - a, l2 = l[1] - l[0];
        int r1 = r[0] - m, r2 = r[1] - r[0];
        rotateIndexed(array, a + l1, m, m + r1);
        rotateIndexed(array, a + l1 + l2 + r1, m + r1, m + r1 + r2);
        return new int[] {a + l1 + r1, a + l1 + r1 + l2 + r2};
    }

    protected void sergio(int[] array, int start, int end, int depth, int depthlimit) {
        Writes.recordDepth(depth);
        int[] part = {end + 1, 0};
        while (true) {
            end = part[0] - 1;
            if (end - start < 31 || depth > depthlimit) {
                mergeSort(array, start, end + 1);
                break;
            } else if (pd(array, start, end + 1) < end) {
                if (!sorted(array, start, end)) {
                    int pivot = stableGMo7(array, start, end);
                    part = partition(array, start, end + 1, pivot, 0);
                    Writes.recursion();
                    sergio(array, part[1], end, depth + 1, depthlimit);
                } else break;
            } else break;
        }
    }

    /**Runs a Gapped Median-of-7 In-Place Stable Quicksort on the input.*/
    public void runSergioSort(int[] array, int start, int length) {
        if (length <= 31) mergeSort(array, start, start + length);
        else if (pd(array, start, start + length) < start + length - 1) {
            if (!sorted(array, start, start + length - 1)) {
                if (canGrab7(array, start, start + length - 1)) {
                    sergio(array, start + 7, start + length - 1, 0, (int) ((Math.E / 2) * (Math.log(length) / Math.log(2))));
                    insertionSort(array, start, start + 7);
                    grailMergeWithoutBuffer(array, start, 7, length - 7);
                } else mergeSort(array, start, start + length);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        runSergioSort(array, 0, currentLength);
    }
}