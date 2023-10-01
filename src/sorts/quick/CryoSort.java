package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite and Scandum

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * A Median-of-Medians Stable Quicksort with O(sqrt(n)) External Space.
 * <p>
 * To use this algorithm in another, use {@code quickSort()} from a reference
 * instance.
 *
 * @author Ayako-chan - implementation of the sort
 * @author aphitorite - implementation of Rotate Mergesort
 * @author Scandum - the analyzer before sorting
 *
 */
public class CryoSort extends Sort {

    public CryoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cryo");
        this.setRunAllSortsName("Cryo Sort");
        this.setRunSortName("Cryosort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        setQuestion("Enter the number of items of the merging buffer (default: 64)", 64);
    }

    @Override
    public int validateAnswer(int answer) {
        return Math.max(answer, 0);
    }

    int equ(int a, int b) {
        return ((a - b) >> 31) + ((b - a) >> 31) + 1;
    }

    protected void stableSegmentReversal(int[] array, int start, int end) {
        if (end - start < 3) Writes.swap(array, start, end, 0.75, true, false);
        else Writes.reversal(array, start, end, 0.75, true, false);
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (i < end && Reads.compareIndices(array, i, i + 1, 0.5, true) == 0) i++;
            right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, 0.75, true, false);
                else Writes.reversal(array, left, right, 0.75, true, false);
            }
            i++;
        }
    }

    protected void rotateNoBuf(int[] array, int a, int m, int b) {
        int p0 = a, p1 = m - 1, p2 = m, p3 = b - 1;
        int tmp;
        while (p0 < p1 && p2 < p3) {
            tmp = array[p1];
            Writes.write(array, p1--, array[p0], 0.5, true, false);
            Writes.write(array, p0++, array[p2], 0.5, true, false);
            Writes.write(array, p2++, array[p3], 0.5, true, false);
            Writes.write(array, p3--, tmp, 0.5, true, false);
        }
        while (p0 < p1) {
            tmp = array[p1];
            Writes.write(array, p1--, array[p0], 0.5, true, false);
            Writes.write(array, p0++, array[p3], 0.5, true, false);
            Writes.write(array, p3--, tmp, 0.5, true, false);
        }
        while (p2 < p3) {
            tmp = array[p2];
            Writes.write(array, p2++, array[p3], 0.5, true, false);
            Writes.write(array, p3--, array[p0], 0.5, true, false);
            Writes.write(array, p0++, tmp, 0.5, true, false);
        }
        if (p0 < p3) { // don't count reversals that don't do anything
            if (p3 - p0 >= 3) Writes.reversal(array, p0, p3, 1, true, false);
            else Writes.swap(array, p0, p3, 1, true, false);
            Highlights.clearMark(2);
        }
    }

    protected void rotate(int[] array, int[] buf, int a, int m, int b) {
        Highlights.clearAllMarks();
        if (a >= m || m >= b) return;
        if (buf == null) {
            rotateNoBuf(array, a, m, b);
            return;
        }
        int pos = a, left = m - a, right = b - m;
        int pta = pos, ptb = pos + left, ptc = pos + right, ptd = ptb + right;
        if (left < right) {
            int bridge = right - left;
            if (bridge < left) {
                int loop = left;
                if (bridge > buf.length) {
                    rotateNoBuf(array, a, m, b);
                    return;
                }
                Writes.arraycopy(array, ptb, buf, 0, bridge, 1, true, true);
                while (loop-- > 0) {
                    Writes.write(array, --ptc, array[--ptd], 0.5, true, false);
                    Writes.write(array, ptd, array[--ptb], 0.5, true, false);
                }
                Writes.arraycopy(buf, 0, array, pta, bridge, 1, true, false);
            } else {
                if (left > buf.length) {
                    rotateNoBuf(array, a, m, b);
                    return;
                }
                Writes.arraycopy(array, pta, buf, 0, left, 1, true, true);
                Writes.arraycopy(array, ptb, array, pta, right, 1, true, false);
                Writes.arraycopy(buf, 0, array, ptc, left, 1, true, false);
            }
        } else if (right < left) {
            int bridge = left - right;
            if (bridge < right) {
                if (bridge > buf.length) {
                    rotateNoBuf(array, a, m, b);
                    return;
                }
                int loop = right;
                Writes.arraycopy(array, ptc, buf, 0, bridge, 1, true, true);
                while (loop-- > 0) {
                    Writes.write(array, ptc++, array[pta], 0.5, true, false);
                    Writes.write(array, pta++, array[ptb++], 0.5, true, false);
                }
                Writes.arraycopy(buf, 0, array, ptd - bridge, bridge, 1, true, false);
            } else {
                if (right > buf.length) {
                    rotateNoBuf(array, a, m, b);
                    return;
                }
                Writes.arraycopy(array, ptb, buf, 0, right, 1, true, true);
                while (left-- > 0) Writes.write(array, --ptd, array[--ptb], 1, true, false);
                Writes.arraycopy(buf, 0, array, pta, right, 1, true, false);
            }
        } else {
            while (left-- > 0) Writes.swap(array, pta++, ptb++, 1, true, false);
            Highlights.clearMark(2);
        }
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b)
            Writes.write(array, b, temp, 0.5, true, false);
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
        return binSearch(array, a + i / 2, Math.min(b, a - 1 + i), val, left);
    }

    protected int rightExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0) i *= 2;
        else while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        return binSearch(array, Math.max(a, b - i + 1), b - i / 2, val, left);
    }

    protected void mergeFWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = m - a;
        Writes.arraycopy(array, a, tmp, 0, s, 1, true, true);
        int i = 0, j = m;
        while (i < s && j < b) {
            if (Reads.compareValues(tmp[i], array[j]) <= 0)
                Writes.write(array, a++, tmp[i++], 1, true, false);
            else
                Writes.write(array, a++, array[j++], 1, true, false);
        }
        while (i < s) Writes.write(array, a++, tmp[i++], 1, true, false);
    }

    protected void mergeBWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = b - m;
        Writes.arraycopy(array, m, tmp, 0, s, 1, true, true);
        int i = s - 1, j = m - 1;
        while (i >= 0 && j >= a) {
            if (Reads.compareValues(tmp[i], array[j]) >= 0)
                Writes.write(array, --b, tmp[i--], 1, true, false);
            else
                Writes.write(array, --b, array[j--], 1, true, false);
        }
        while (i >= 0) Writes.write(array, --b, tmp[i--], 1, true, false);
    }

    protected void merge(int[] array, int[] buf, int a, int m, int b) {
        Highlights.clearMark(2);
        if (m - a > b - m)
            mergeBWExt(array, buf, a, m, b);
        else
            mergeFWExt(array, buf, a, m, b);
    }

    public void rotateMerge(int[] array, int[] buf, int a, int m, int b) {
        if (a >= m || m >= b) return;
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0) return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, buf, a, m, b);
            return;
        }
        int lenA = m - a, lenB = b - m;
        if (Math.min(m - a, b - m) <= buf.length) {
            merge(array, buf, a, m, b);
            return;
        }
        int c = lenA + (lenB - lenA) / 2;
        if (lenB < lenA) { // partitions c largest elements
            int r1 = 0, r2 = lenB;
            while (r1 < r2) {
                int ml = r1 + (r2 - r1) / 2;
                if (Reads.compareValues(array[m - (c - ml)], array[b - ml - 1]) > 0) r2 = ml;
                else r1 = ml + 1;
            }
            // [lenA-(c-r1)][c-r1][lenB-r1][r1]
            // [lenA-(c-r1)][lenB-r1][c-r1][r1]
            this.rotate(array, buf, m - (c - r1), m, b - r1);
            int m1 = b - c;
            this.rotateMerge(array, buf, m1, b - r1, b);
            this.rotateMerge(array, buf, a, m1 - (lenB - r1), m1);
        } else { // partitions c smallest elements
            int r1 = 0, r2 = lenA;
            while (r1 < r2) {
                int ml = r1 + (r2 - r1) / 2;
                if (Reads.compareValues(array[a + ml], array[m + (c - ml) - 1]) > 0) r2 = ml;
                else r1 = ml + 1;
            }
            // [r1][lenA-r1][c-r1][lenB-(c-r1)]
            // [r1][c-r1][lenA-r1][lenB-(c-r1)]
            this.rotate(array, buf, a + r1, m, m + (c - r1));
            int m1 = a + c;
            this.rotateMerge(array, buf, a, a + r1, m1);
            this.rotateMerge(array, buf, m1, m1 + (lenA - r1), b);
        }
    }

    protected boolean buildRuns(int[] array, int a, int b, int mRun) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 1, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) > 0) i++;
                if (i - j < 4) Writes.swap(array, j, i - 1, 1.0, true, false);
                else Writes.reversal(array, j, i - 1, 1.0, true, false);
            } else while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;
            if (i < b) {
                noSort = false;
                j = i - (i - j - 1) % mRun - 1;
            }
            while (i - j < mRun && i < b) {
                insertTo(array, i, rightExpSearch(array, j, i, array[i], false));
                i++;
            }
            j = i++;
        }
        return noSort;
    }

    protected void insertSort(int[] array, int a, int b) {
        buildRuns(array, a, b, b - a);
    }

    public void mergeSort(int[] array, int[] buf, int a, int b) {
        int j = b - a;
        while (j >= 32) j = (j - 1) / 2 + 1;
        if (buildRuns(array, a, b, j)) return;
        for (; j < b - a; j *= 2) {
            for (int i = a; i+j < b; i += 2*j)
                rotateMerge(array, buf, i, i + j, Math.min(i + 2 * j, b));
        }
    }

    protected int medOf3(int[] array, int i0, int i1, int i2) {
        int tmp;
        if (Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            tmp = i1;
            i1 = i0;
        } else tmp = i0;
        if (Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if (Reads.compareIndices(array, tmp, i2, 1, true) > 0) return tmp;
            return i2;
        }
        return i1;
    }

    public int medP3(int[] array, int a, int b, int d) {
        if (b - a == 3 || (b - a > 3 && d == 0))
            return medOf3(array, a, a + (b - a) / 2, b - 1);
        if (b - a < 3) return a + (b - a) / 2;
        int t = (b - a) / 3;
        int l = medP3(array, a, a + t, --d), c = medP3(array, a + t, b - t, d), r = medP3(array, b - t, b, d);
        // median
        return medOf3(array, l, c, r);
    }

    public int medOfMed(int[] array, int a, int b) {
        int log5 = 0, exp5 = 1, exp5_1 = 0;
        int[] indices = new int[5];
        int n = b - a;
        while (exp5 < n) {
            exp5_1 = exp5;
            log5++;
            exp5 *= 5;
        }
        if (log5 < 1) return a;
        // fill indexes, recursing if required
        if (log5 == 1) for (int i = a, j = 0; i < b; i++, j++) indices[j] = i;
        else {
            n = 0;
            for (int i = a; i < b; i += exp5_1) {
                indices[n] = medOfMed(array, i, Math.min(b, i + exp5_1));
                n++;
            }
        }
        // sort - insertion sort is good enough for 5 elements
        for (int i = 1; i < n; i++) {
            for (int j = i; j > 0; j--) {
                if (Reads.compareIndices(array, indices[j], indices[j - 1], 0.5, true) < 0) {
                    int t = indices[j];
                    indices[j] = indices[j - 1];
                    indices[j - 1] = t;
                } else break;
            }
        }
        // return median
        return indices[(n - 1) / 2];
    }

    protected int[] partitionEasy(int[] array, int[] buf, int a, int b, int piv) {
        Highlights.clearMark(2);
        int len = b - a;
        int p0 = a, p1 = 0, p2 = len;
        for (int i = a; i < b; i++) {
            int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
            if (cmp < 0) Writes.write(array, p0++, array[i], 0.5, true, false);
            else if (cmp == 0) Writes.write(buf, --p2, array[i], 0.5, false, true);
            else Writes.write(buf, p1++, array[i], 0.5, false, true);
        }
        int eqSize = len - p2, gtrSize = p1;
        if (eqSize < b - a) {
            for (int i = 0; i < eqSize; i++)
                Writes.write(array, p0 + i, buf[p2 + eqSize - 1 - i], 0.5, true, false);
            Writes.arraycopy(buf, 0, array, p0 + eqSize, gtrSize, 0.5, true, false);
        }
        return new int[] { p0, p0 + eqSize };
    }

    protected int[] partition(int[] array, int[] buf, int a, int b, int piv) {
        if (b - a < 2) {
            int[] court = new int[] { a, a };
            int cmp = Reads.compareValues(array[a], piv);
            if (cmp <= 0) {
                court[1]++;
                if (cmp < 0) court[0]++;
            }
            return court;
        }
        if (b - a <= buf.length) return partitionEasy(array, buf, a, b, piv);
        int m = a + (b - a) / 2;
        int[] l = partition(array, buf, a, m, piv);
        int[] r = partition(array, buf, m, b, piv);
        int r1 = r[0] - m, r2 = r[1] - r[0];
        rotate(array, buf, l[0], m, r[0]);
        l[0] += r1;
        l[1] += r1;
        rotate(array, buf, l[1], r[0], r[1]);
        return new int[] { l[0], l[1] + r2 };
    }

    protected void sortHelper(int[] array, int[] buf, int a, int b, boolean bad) {
        while (b - a > 32) {
            int pIdx;
            if (bad) pIdx = medOfMed(array, a, b);
            else pIdx = medP3(array, a, b, 1);
            int[] p = partition(array, buf, a, b, array[pIdx]);
            int lLen = p[0] - a, rLen = b - p[1], eqLen = p[1] - p[0];
            if (eqLen == b - a) return;
            if (rLen == 0) {
                bad = eqLen < lLen / 8;
                b = p[0];
                continue;
            }
            if (lLen == 0) {
                bad = eqLen < rLen / 8;
                a = p[1];
                continue;
            }
            bad = lLen < rLen / 8 || rLen < lLen / 8;
            if (rLen < lLen) {
                sortHelper(array, buf, p[1], b, bad);
                b = p[0];
            } else {
                sortHelper(array, buf, a, p[0], bad);
                a = p[1];
            }
        }
        insertSort(array, a, b);
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using a Median-of-Medians
     * Stable Quicksort with O(1) External Space.
     *
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     */
    public void quickSort(int[] array, int a, int b, int bLen) {
        int len = b - a;
        int balance = 0, eq = 0, streaks = 0, dist, eqdist, loop, cnt = len, pos = a;
        while (cnt > 16) {
            for (eqdist = dist = 0, loop = 0; loop < 16; loop++) {
                int cmp = Reads.compareIndices(array, pos, pos + 1, 0.5, true);
                dist += cmp > 0 ? 1 : 0;
                eqdist += cmp == 0 ? 1 : 0;
                pos++;
            }
            streaks += equ(dist, 0) | equ(dist + eqdist, 16);
            balance += dist;
            eq += eqdist;
            cnt -= 16;
        }
        while (--cnt > 0) {
            int cmp = Reads.compareIndices(array, pos, pos + 1, 0.5, true);
            balance += cmp > 0 ? 1 : 0;
            eq += cmp == 0 ? 1 : 0;
            pos++;
        }
        if (balance == 0) return;
        if (balance + eq == len - 1) {
            if (eq > 0) stableSegmentReversal(array, a, b - 1);
            else if (b - a < 4) Writes.swap(array, a, b - 1, 0.75, true, false);
            else Writes.reversal(array, a, b - 1, 0.75, true, false);
            return;
        }
        int[] buf = Writes.createExternalArray(bLen);
        int sixth = len / 6;
        if (streaks > len / 20 || balance <= sixth || balance + eq >= len - sixth)
            mergeSort(array, buf, a, b);
        else sortHelper(array, buf, a, b, false);
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength, bucketCount);

    }

}
