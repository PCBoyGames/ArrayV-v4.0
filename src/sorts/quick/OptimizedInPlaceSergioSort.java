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
/**A Gapped Median-of-7 In-Place Stable Quicksort with O(1) External Space.<p>
 * To use this algorithm in another, use {@code runSergioSort()} from a reference instance.
 * @version SerSan-1.1
 * @author PCBoy
*/
public class OptimizedInPlaceSergioSort extends GrailSorting {

    int[] ext;
    int extlen = 16;
    int[][] network = {{0,6},{1,5},{1,2},{4,5},{0,1},{5,6},{1,5},{2,4},{1,2},{4,5},{3,4},{2,3},{3,4}};

    public OptimizedInPlaceSergioSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized In-Place Sergio");
        this.setRunAllSortsName("Optimized In-Place Sergio Quick Sort");
        this.setRunSortName("Optimized In-Place Sergio Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter amount of external space:\n(Default is 16)", 16);
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

    protected void trinity(int[] array, int pos, int lenA, int lenB) {
        if (lenA < 1 || lenB < 1) return;
        int a = pos, b = pos + lenA - 1, c = pos + lenA, d = pos + lenA + lenB - 1;
        int swap;
        while (a < b && c < d) {
            swap = array[b];
            Highlights.markArray(2, d);
            Writes.write(array, b--, array[a], 1, true, false);
            Highlights.markArray(2, b);
            Writes.write(array, a++, array[c], 1, true, false);
            Highlights.markArray(2, a);
            Writes.write(array, c++, array[d], 1, true, false);
            Highlights.markArray(2, c);
            Writes.write(array, d--, swap, 1, true, false);
        }
        while (a < b) {
            swap = array[b];
            Highlights.markArray(2, d);
            Writes.write(array, b--, array[a], 1, true, false);
            Highlights.markArray(2, b);
            Writes.write(array, a++, array[d], 1, true, false);
            Highlights.markArray(2, a);
            Writes.write(array, d--, swap, 1, true, false);
        }
        while (c < d) {
            swap = array[c];
            Highlights.markArray(2, a);
            Writes.write(array, c++, array[d], 1, true, false);
            Highlights.markArray(2, c);
            Writes.write(array, d--, array[a], 1, true, false);
            Highlights.markArray(2, d);
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
        else if (lenA <= extlen || lenB <= extlen) bridge(array, pos, lenA, lenB);
        else trinity(array, pos, lenA, lenB);
    }

    protected void rotateIndexed(int[] array, int a, int b, int c) {
        grailRotate(array, a, b - a, c - b);
    }

    protected void stableSegmentReversal(int[] array, int start, int end) {
        if (end - start < 3) Writes.swap(array, start, end, 0.075, true, false);
        else Writes.reversal(array, start, end, 0.075, true, false);
        for (int i = start; i < end; i++) {
            int left = i;
            while (Reads.compareIndices(array, i, i + 1, 0.25, true) == 0 && i < end) i++;
            int right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, 0.75, true, false);
                else Writes.reversal(array, left, right, 0.75, true, false);
            }
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

    protected void indiceshandle(int[] array, int[] ext, int a, int b) {
        if (Reads.compareIndices(array, ext[a], ext[b], 10, true) > 0) Writes.swap(ext, a, b, 0, false, true);
    }

    protected int extGMo7(int[] array, int start, int end) {
        for (int i = 0; i < 7; i++) Writes.write(ext, i, start + i * ((end - start) / 6), 0, false, true);
        for (int i = 0; i < network.length; i++) indiceshandle(array, ext, network[i][0], network[i][1]);
        return array[ext[3]];
    }

    protected int stableGMo7(int[] array, int start, int end) {
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
        int j;
        for (int i = blockfindRun(array, a, b); i < b; i = j) {
            j = blockfindRun(array, i, b);
            grailMergeWithoutBuffer(array, a, i - a, j - i);
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
        Writes.arraycopy(array, a, ext, 0, s, 0.5, true, true);
        int i = 0, j = m;
        while (i < s && j < b)
            if (Reads.compareValues(ext[i], array[j]) <= 0) Writes.write(array, a++, ext[i++], 0.5, true, false);
            else Writes.write(array, a++, array[j++], 0.5, true, false);
        while (i < s) Writes.write(array, a++, ext[i++], 0.5, true, false);
    }

    protected void mergeBWExt(int[] array, int a, int m, int b) {
        int s = b - m;
        Writes.arraycopy(array, m, ext, 0, s, 0.5, true, true);
        int i = s - 1, j = m - 1;
        while (i >= 0 && j >= a)
            if (Reads.compareValues(ext[i], array[j]) >= 0) Writes.write(array, --b, ext[i--], 0.5, true, false);
            else Writes.write(array, --b, array[j--], 0.5, true, false);
        while (i >= 0) Writes.write(array, --b, ext[i--], 0.5, true, false);
    }

    protected void merge(int[] array, int a, int m, int b, boolean bnd) {
        if (bnd) {
            if (a >= m || m >= b || Reads.compareValues(array[m - 1], array[m]) <= 0) return;
            a = leftExpSearch(array, a, m, array[m], false);
            b = rightExpSearch(array, m, b, array[m - 1], true);
            if (Reads.compareValues(array[a], array[b - 1]) > 0) {
                rotateIndexed(array, a, m, b);
                return;
            }
        }
        Highlights.clearMark(2);
        if (m - a > b - m) mergeBWExt(array, a, m, b);
        else mergeFWExt(array, a, m, b);
    }

    protected void rotateMerge(int[] array, int a, int m, int b, int d) {
        Writes.recordDepth(d);
        while (Math.min(m - a, b - m) > extlen) {
            if (a >= m || m >= b || Reads.compareValues(array[m - 1], array[m]) <= 0) return;
            a = leftExpSearch(array, a, m, array[m], false);
            b = rightExpSearch(array, m, b, array[m - 1], true);
            if (Reads.compareValues(array[a], array[b - 1]) > 0) {
                rotateIndexed(array, a, m, b);
                return;
            }
            if (Math.min(m - a, b - m) <= extlen) {
                merge(array, a, m, b, false);
                return;
            }
            int m1, m2, m3;
            if (m - a >= b - m) {
                m1 = a + (m - a) / 2;
                m2 = binSearch(array, m, b, array[m1], true);
                m3 = m1 + (m2 - m);
            }
            else {
                m2 = m + (b - m) / 2;
                m1 = binSearch(array, a, m, array[m2], false);
                m3 = (m2++) - (m - m1);
            }
            rotateIndexed(array, m1, m, m2);
            if (b - (m3 + 1) < m3 - a) {
                Writes.recursion();
                rotateMerge(array, m3 + 1, m2, b, d + 1);
                m = m1;
                b = m3;
            } else {
                Writes.recursion();
                rotateMerge(array, a, m1, m3, d + 1);
                m = m2;
                a = m3 + 1;
            }
        }
        merge(array, a, m, b, true);
    }

    protected void mergeSort(int[] array, int a, int b) {
        int i, j, k;
        while (true) {
            i = mergeFindRun(array, a, b);
            if (i >= b) break;
            j = mergeFindRun(array, i, b);
            rotateMerge(array, a, i, j, 0);
            if (j >= b) break;
            k = j;
            while (true) {
                i = mergeFindRun(array, k, b);
                if (i >= b) break;
                j = mergeFindRun(array, i, b);
                rotateMerge(array, k, i, j, 0);
                if (j >= b) break;
                k = j;
            }
        }
    }

    protected int[] partitionEasy(int[] array, int a, int b, int piv) {
        int len = b - a;
        int p0 = a, p1 = 0, p2 = len;
        boolean variety1 = false, variety2 = false, variety3 = false;
        for (int i = a; i < b; i++) {
            int cmp = Reads.compareIndexValue(array, i, piv, 0, false);
            Highlights.markArray(1, p0);
            Highlights.markArray(2, i);
            Delays.sleep(0.5);
            if (cmp < 0) {
                variety1 = true;
                if (p0 != i) Writes.write(array, p0++, array[i], 0.5, false, false);
                else p0++;
            }
            else if (cmp == 0) Writes.write(ext, --p2, array[i], 0.5, false, variety2 = true);
            else Writes.write(ext, p1++, array[i], 0.5, false, variety3 = true);
        }
        Highlights.clearAllMarks();
        int eqSize = len - p2, gtrSize = p1;
        if (eqSize < b - a) {
            if ((variety1 && variety3) || (variety2 && (variety1 || variety3))) {
                for (int i = 0; i < eqSize; i++) Writes.write(array, p0 + i, ext[p2 + eqSize - 1 - i], 0.5, true, false);
                Writes.arraycopy(ext, 0, array, p0 + eqSize, gtrSize, 0.5, true, false);
            }
        }
        return new int[] {p0, p0 + eqSize};
    }

    protected int[] partition(int[] array, int a, int b, int piv, int depth) {
        Writes.recordDepth(depth);
        if (b - a <= extlen) return partitionEasy(array, a, b, piv);
        int m = a + (b - a) / 2;
        Writes.recursion();
        int[] l = partition(array, a, m, piv, depth + 1);
        Writes.recursion();
        int[] r = partition(array, m, b, piv, depth + 1);
        int r1 = r[0] - m, r2 = r[1] - r[0];
        rotateIndexed(array, l[0], m, r[0]);
        l[0] += r1;
        l[1] += r1;
        rotateIndexed(array, l[1], r[0], r[1]);
        return new int[] {l[0], l[1] + r2};
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
                    int pivot = extlen >= 7 ? extGMo7(array, start, end) : stableGMo7(array, start, end);
                    part = partition(array, start, end + 1, pivot, 0);
                    Writes.recursion();
                    sergio(array, part[1], end, depth + 1, depthlimit);
                } else break;
            } else break;
        }
    }

    /**Runs a Gapped Median-of-7 In-Place Stable Quicksort with O(1) External Space on the input.*/
    public void runSergioSort(int[] array, int start, int length, int question) {
        ext = Writes.createExternalArray(extlen = question < length ? question : length);
        if (length <= 31) mergeSort(array, start, start + length);
        else if (pd(array, start, start + length) < start + length - 1) {
            if (!sorted(array, start, start + length - 1)) {
                if (extlen >= 7) sergio(array, start, start + length - 1, 0, (int) ((Math.E / 2) * (Math.log(length) / Math.log(2))));
                else {
                    if (canGrab7(array, start, start + length - 1)) {
                        sergio(array, start + 7, start + length - 1, 0, (int) ((Math.E / 2) * (Math.log(length) / Math.log(2))));
                        insertionSort(array, start, start + 7);
                        grailMergeWithoutBuffer(array, start, 7, length - 7);
                    } else mergeSort(array, start, start + length);
                }
            }
        }
        Writes.deleteExternalArray(ext);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1) return 1;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int question) {
        runSergioSort(array, 0, currentLength, question);
    }
}