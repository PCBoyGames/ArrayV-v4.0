package sorts.quick;

import java.util.PriorityQueue;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

Original name of this algorithm: Ternary Aeos Priority Quicksort

 */

/**
 * A Median-of-Medians Ternary Block Partition Sort which stores recursions in a
 * priority queue.
 * <p>
 * To use this algorithm in another, use {@code quickSort()} from a reference
 * instance.
 *
 * @author Ayako-chan - implementation of the sort
 * @author aphitorite - key idea / concept of Priority Quicksort
 *
 */
public final class ReimuSort extends Sort {

    public ReimuSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Reimu");
        this.setRunAllSortsName("Reimu Sort");
        this.setRunSortName("Reimusort");
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

    int threshold = 32;

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
        else if (b - a < 3) return a + (b - a) / 2;
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

    protected void multiSwap(int[] array, int a, int b, int len) {
        if (a != b)
            for (int i = 0; i < len; i++)
                Writes.swap(array, a + i, b + i, 1, true, false);
    }

    // not at all a true rotation method, but the concept is similar
    protected void rotate(int[] array, int[] buf, int start, int[] cnts, int[] blks, int bLen) {
        int i = start + blks[1] * bLen;
        int j = i + cnts[0] + cnts[1];
        if (i != j) Writes.arraycopy(array, i, array, j, blks[2] * bLen, 1, true, false);
        j -= cnts[1];
        Writes.arraycopy(buf, bLen, array, j, cnts[1], 1, true, false);
        i -= blks[1] * bLen;
        j -= blks[1] * bLen;
        if (i != j) Writes.arraycopy(array, i, array, j, blks[1] * bLen, 1, true, false);
        j -= cnts[0];
        Writes.arraycopy(buf, 0, array, j, cnts[0], 1, true, false);
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        if (a != b) {
            int temp = array[a];
            int d = (a > b) ? -1 : 1;
            for (int i = a; i != b; i += d)
                Writes.write(array, i, array[i + d], 0.5, true, false);
            Writes.write(array, b, temp, 0.5, true, false);
        }
    }

    protected int expSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        while (a1 < b1) {
            int m = a1 + (b1 - a1) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) < 0) b1 = m;
            else a1 = m + 1;
        }
        return a1;
    }

    protected void insertSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i]));
    }

    int pivCmp(int v, int piv) {
        int c = Reads.compareValues(v, piv);
        if (c > 0) return 2;
        if (c < 0) return 0;
        return 1;
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

    protected int[] partition(int[] array, int[] buf, int[] tags, int a, int b, int bLen, int piv) {
        Highlights.clearMark(2);
        // determines which elements do not need to be moved
        for (; a < b; a++) {
            Highlights.markArray(1, a);
            Delays.sleep(0.25);
            if (Reads.compareValues(array[a], piv) >= 0) break;
        }
        for (; b > a; b--) {
            Highlights.markArray(1, b - 1);
            Delays.sleep(0.25);
            if (Reads.compareValues(array[b - 1], piv) <= 0) break;
        }
        // partitions the list stably
        if (b - a <= 3 * bLen) return partitionEasy(array, buf, a, b, piv);
        int[] blks = new int[3];
        int[] cnts = new int[3];
        int t = a;
        int bCnt = 0;
        for (int i = a; i < b; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.25);
            int cmp = pivCmp(array[i], piv);
            Writes.write(buf, cmp * bLen + cnts[cmp]++, array[i], 0.5, false, true);
            if (cnts[cmp] >= bLen) {
                Writes.arraycopy(buf, cmp * bLen, array, t, bLen, 1.0, true, false);
                cnts[cmp] = 0;
                blks[cmp]++;
                t += bLen;
                Writes.write(tags, bCnt++, cmp * bLen, 0, false, true);
            }
        }
        // Figure out each block's sorted position
        int[] tPos = new int[] {0, blks[0], bCnt - blks[2]};
        for (int i = 0; i < bCnt; i++) {
            int type = tags[i] / bLen;
            Writes.write(tags, i, tPos[type]++ * bLen, 0, false, true);
        }
        for (int i = 0; i < bCnt; i++) {
            int now = tags[i];
            boolean change = false;
            while (Reads.compareOriginalValues(i, now / bLen) != 0) {
                multiSwap(array, a + i * bLen, a + now, bLen);
                int tmp = tags[now / bLen];
                Writes.write(tags, now / bLen, now, 0, false, true);
                now = tmp;
                change = true;
            }
            if (change) Writes.write(tags, i, now, 0, false, true);
        }
        Highlights.clearMark(2);
        Writes.arraycopy(buf, 2 * bLen, array, b - cnts[2], cnts[2], 1.0, true, false);
        int[] ptrs = {a + blks[0] * bLen, a + (blks[0] + blks[1]) * bLen};
        rotate(array, buf, ptrs[0], cnts, blks, bLen);
        ptrs[0] += cnts[0];
        ptrs[1] += cnts[0] + cnts[1];
        return ptrs;
    }

    void consumePartition(int[] array, PriorityQueue<Partition> queue, int a, int b, boolean bad) {
        if (b - a > threshold) queue.offer(new Partition(a, b, bad));
        else insertSort(array, a, b);
    }

    protected void sortHelper(int[] array, int[] buf, int[] tags, int l, int r, int bLen) {
        PriorityQueue<Partition> queue = new PriorityQueue<>((r - l - 1) / this.threshold + 1);
        queue.offer(new Partition(l, r, false));
        while (queue.size() > 0) {
            Partition part = queue.poll();
            int a = part.a, b = part.b;
            boolean bad = part.bad;
            int pIdx;
            if (bad) pIdx = medOfMed(array, a, b);
            else pIdx = medP3(array, a, b, 1);
            int[] pr = partition(array, buf, tags, a, b, bLen, array[pIdx]);
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
     * Ternary Block Partition Sort.
     *
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     */
    public void quickSort(int[] array, int a, int b) {
        int z = 0, e = 0;
        for (int i = a; i < b - 1; i++) {
            int cmp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            z += cmp > 0 ? 1 : 0;
            e += cmp == 0 ? 1 : 0;
        }
        if (z == 0) return;
        if (z + e == b - a -1) {
            if (e > 0) stableSegmentReversal(array, a, b - 1);
            else if (b - a < 4) Writes.swap(array, a, b - 1, 0.75, true, false);
            else Writes.reversal(array, a, b - 1, 0.75, true, false);
            return;
        }
        int len = b - a;
        if (len <= threshold) insertSort(array, a, b);
        else {
            int bLen = 1;
            while (bLen * bLen < len) bLen *= 2;
            int[] buf = Writes.createExternalArray(3 * bLen);
            int[] tags = Writes.createExternalArray(len / bLen);
            sortHelper(array, buf, tags, a, b, bLen);
            Writes.deleteExternalArray(buf);
            Writes.deleteExternalArray(tags);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
