package sorts.quick;

import java.util.PriorityQueue;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite and Scandum

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

Original name of this algorithm: Adaptive Ternary Rotate Priority Quicksort

 */

/**
 * A Median-of-Medians Stable Quicksort with O(sqrt(n)) External Space
 * which stores recursions in a priority queue.
 * <p>
 * To use this algorithm in another, use {@code quickSort()} from a reference
 * instance.
 *
 * @author Ayako-chan - implementation of the sort
 * @author aphitorite - key idea / concept of Priority Quicksort
 * @author Gaming32 - Binary Pattern-Defeating Mergesort
 * @author Scandum - the analyzer before sorting
 *
 */
public class OptimizedRotateMashuSort extends Sort {

    public OptimizedRotateMashuSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Rotate Mashu");
        this.setRunAllSortsName("Optimized Rotate Mashu Sort");
        this.setRunSortName("Optimized Rotate Mashusort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    class Partition implements Comparable<Partition> {
        public int a, b;
        public boolean bad;

        public Partition(int a, int b, boolean bad) {
            this.a = a;
            this.b = b;
            this.bad = bad;
        }

        public int length() {
            return this.b - this.a;
        }

        @Override
        public int compareTo(Partition y) {
            int len0 = this.length(), len1 = y.length();
            if (len0 < len1)
                return 1;
            if (len0 > len1)
                return -1;
            return 0;
        }
    }

    public static int log(int val, int base) {
        return (int) (Math.log(val) / Math.log(base));
    }

    int threshold = 32;

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
        if (left)
            while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0) i *= 2;
        else
            while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0) i *= 2;
        int a1 = a + i / 2, b1 = Math.min(b, a - 1 + i);
        return binSearch(array, a1, b1, val, left);
    }

    protected int rightExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left)
            while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0) i *= 2;
        else
            while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        return binSearch(array, a1, b1, val, left);
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

    protected void merge(int[] array, int[] buf, int a, int m, int b, boolean bnd) {
        if (bnd) {
            if (a >= m || m >= b || Reads.compareValues(array[m - 1], array[m]) <= 0) return;
            a = leftExpSearch(array, a, m, array[m], false);
            b = rightExpSearch(array, m, b, array[m - 1], true);
            if (Reads.compareValues(array[a], array[b - 1]) > 0) {
                rotate(array, buf, a, m, b);
                return;
            }
        }
        Highlights.clearMark(2);
        if (m - a > b - m)
            mergeBWExt(array, buf, a, m, b);
        else
            mergeFWExt(array, buf, a, m, b);
    }

    public void rotateMerge(int[] array, int[] buf, int a, int m, int b) {
        while (Math.min(m - a, b - m) > buf.length) {
            if (a >= m || m >= b || Reads.compareValues(array[m - 1], array[m]) <= 0) return;
            a = leftExpSearch(array, a, m, array[m], false);
            b = rightExpSearch(array, m, b, array[m - 1], true);
            if (Reads.compareValues(array[a], array[b - 1]) > 0) {
                rotate(array, buf, a, m, b);
                return;
            }
            if (Math.min(m - a, b - m) <= buf.length) {
                merge(array, buf, a, m, b, false);
                return;
            }
            int m1, m2, m3;
            if (m-a >= b-m) {
                m1 = a+(m-a)/2;
                m2 = binSearch(array, m, b, array[m1], true);
                m3 = m1+(m2-m);
            } else {
                m2 = m+(b-m)/2;
                m1 = binSearch(array, a, m, array[m2], false);
                m3 = (m2++)-(m-m1);
            }
            rotate(array, buf, m1, m, m2);
            if (b - (m3 + 1) < m3 - a) {
                rotateMerge(array, buf, m3 + 1, m2, b);
                m = m1;
                b = m3;
            } else {
                rotateMerge(array, buf, a, m1, m3);
                m = m2;
                a = m3 + 1;
            }
        }
        merge(array, buf, a, m, b, true);
    }

    protected int findRun(int[] array, int a, int b, int mRun) {
        int i = a + 1;
        if (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) i++;
                if (i - a < 4) Writes.swap(array, a, i - 1, 1.0, true, false);
                else Writes.reversal(array, a, i - 1, 1.0, true, false);
            } else
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        }
        Highlights.clearMark(2);
        while (i - a < mRun && i < b) {
            insertTo(array, i, rightExpSearch(array, a, i, array[i], false));
            i++;
        }
        return i;
    }

    public void insertSort(int[] array, int a, int b) {
        // technically an insertion sort
        findRun(array, a, b, b - a);
    }

    public void mergeSort(int[] array, int[] buf, int a, int b) {
        int len = b - a;
        if (len <= 32) {
            insertSort(array, a, b);
            return;
        }
        int mRun = 16;
        int[] runs = Writes.createExternalArray((len - 1) / mRun + 2);
        int r = a, rf = 0;
        while (r < b) {
            Writes.write(runs, rf++, r, 0.5, false, true);
            r = findRun(array, r, b, mRun);
        }
        while (rf > 1) {
            for (int i = 0; i < rf - 1; i += 2) {
                int eIdx;
                if (i + 2 >= rf) eIdx = b;
                else eIdx = runs[i + 2];
                rotateMerge(array, buf, runs[i], runs[i + 1], eIdx);
            }
            for (int i = 1, j = 2; i < rf; i++, j+=2, rf--) {
                Writes.write(runs, i, runs[j], 0.5, false, true);
            }
        }
        Writes.deleteExternalArray(runs);
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
        if (b - a <= 6) return a + (b - a) / 2;
        int p = 1;
        while (6 * p < b - a) p *= 3;
        int l = medP3(array, a, a + p, -1), c = medOfMed(array, a + p, b - p), r = medP3(array, b - p, b, -1);
        // median
        return medOf3(array, l, c, r);
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

    void consumePartition(int[] array, PriorityQueue<Partition> queue, int a, int b, boolean bad) {
        if (b - a > threshold) queue.offer(new Partition(a, b, bad));
        else insertSort(array, a, b);
    }

    protected void sortHelper(int[] array, int[] buf, int left, int right) {
        PriorityQueue<Partition> queue = new PriorityQueue<>((right - left - 1) / this.threshold + 1);
        queue.offer(new Partition(left, right, false));
        while (queue.size() > 0) {
            Partition part = queue.poll();
            int a = part.a, b = part.b;
            boolean bad = part.bad;
            int pIdx;
            if (bad) pIdx = medOfMed(array, a, b);
            else pIdx = medP3(array, a, b, 1);
            int[] pr = partition(array, buf, a, b, array[pIdx]);
            int lLen = pr[0] - a, rLen = b - pr[1], eqLen = pr[1] - pr[0];
            if (eqLen == b - a) continue;
            if (rLen == 0) {
                consumePartition(array, queue, a, pr[0], eqLen < lLen / 8);
                continue;
            }
            if (lLen == 0) {
                consumePartition(array, queue, pr[1], b, eqLen < rLen / 8);
                continue;
            }
            bad = rLen / 8 > lLen || lLen / 8 > rLen;
            consumePartition(array, queue, a, pr[0], bad);
            consumePartition(array, queue, pr[1], b, bad);
        }
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using a Median-of-Medians
     * Stable Quicksort with O(sqrt(n)) External Space.
     *
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     */
    public void quickSort(int[] array, int a, int b) {
        int len = b - a;
        if (len <= threshold) {
            insertSort(array, a, b);
            return;
        }
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
        int bLen = 1;
        while (bLen * bLen < b - a) bLen *= 2;
        int[] buf = Writes.createExternalArray(bLen);
        int sixth = len / 6;
        if (streaks > len / 20 || balance <= sixth || balance + eq >= len - sixth)
            mergeSort(array, buf, a, b);
        else sortHelper(array, buf, a, b);
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
