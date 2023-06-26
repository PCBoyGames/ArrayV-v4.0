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

 */

/**
 * A Median-of-Medians Dual-Pivot Block Partition Sort which stores recursions
 * in a priority queue.
 * <p>
 * To use this algorithm in another, use {@code quickSort()} from a reference
 * instance.
 *
 * @author Ayako-chan - implementation of the sort
 * @author aphitorite - key idea / concept of Priority Quicksort
 *
 */
public final class RitaSort extends Sort {

    public RitaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Rita");
        this.setRunAllSortsName("Rita Sort");
        this.setRunSortName("Ritasort");
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

    int[] medOfFiveSwaps = new int[] {
        0, 1, 3, 4, 2, 4, 2, 3, 1, 4, 0, 3, 0, 2, 1, 3, 1, 2
    };

    void netSwap(int[] array, int[] ext, int a, int b) {
        if (Reads.compareIndices(array, ext[a], ext[b], 0.5, true) > 0) {
            int t = ext[a];
            ext[a] = ext[b];
            ext[b] = t;
        }
    }

    protected int medOf3(int[] array, int i0, int i1, int i2) {
        int t;
        if (Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            t = i1;
            i1 = i0;
        } else t = i0;
        if (Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if (Reads.compareIndices(array, t, i2, 1, true) > 0) return t;
            return i2;
        }
        return i1;
    }

    int[] medOf5(int[] array, int[] indices) {
        if (indices.length < 5) return null;
        for (int i = 0; i < medOfFiveSwaps.length; i += 2)
            netSwap(array, indices, medOfFiveSwaps[i], medOfFiveSwaps[i+1]);
        return new int[] {indices[1], indices[3]};
    }

    int[] medOf15(int[] array, int start, int end) {
        int    length = end - start;
        int      half =  length / 2;
        int   quarter =    half / 2;
        int    eighth = quarter / 2;
        int sixteenth =  eighth / 2;

        // Provides good rounding without possibility of int overflow
        int[] samples0 = new int[] {start + sixteenth, start + quarter,
                start + quarter + eighth + sixteenth, start + half + eighth,
                start + half + quarter + sixteenth};
        int[] samples1 = new int[] {start + eighth, start + quarter + sixteenth, start + half,
                start + half + eighth + sixteenth, start + half + quarter + eighth};
        int[] samples2 = new int[] {start + sixteenth + eighth, start + quarter + eighth,
                start + half + sixteenth, start + half + quarter,
                start + half + quarter + eighth + sixteenth};

        int[] meds0 = medOf5(array, samples0);
        int[] meds1 = medOf5(array, samples1);
        int[] meds2 = medOf5(array, samples2);
        int[] meds = new int[2];
        meds[0] = medOf3(array, meds0[0], meds1[0], meds2[0]);
        meds[1] = medOf3(array, meds0[1], meds1[1], meds2[1]);
        return meds;
    }

    int[] medOf5Consec(int[] array, int i) {
        int[] samples = new int[] {i, i+1, i+2, i+3, i+4};
        return medOf5(array, samples);
    }

    int[] mOMHelper(int[] array, int start, int length) {
        if (length == 5) return medOf5Consec(array, start);

        int  third = length / 3;
        int[] meds0 = mOMHelper(array, start, third);
        int[] meds1 = mOMHelper(array, start + third, third);
        int[] meds2 = mOMHelper(array, start + 2 * third, third);

        int[] meds = new int[2];
        meds[0] = medOf3(array, meds0[0], meds1[0], meds2[0]);
        meds[1] = medOf3(array, meds0[1], meds1[1], meds2[1]);
        return meds;
    }

    int[] medOfMed(int[] array, int start, int length) {
        if (length == 5) return medOf5Consec(array, start);

        length    /= 5; // because base case is 5, not 1
        int nearPower = (int) Math.pow(3, Math.round(Math.log(length)/Math.log(3)) - 1);
        if (nearPower == length) return mOMHelper(array, start, length * 5);
        length    *= 5;
        nearPower *= 5;

        // uncommon but can happen with numbers slightly smaller than 2*3^k, or here 10*3^k
        // (e.g., 17 < 18 and 47 < 54, or here 85 < 90 and 235 < 270)
        if (2*nearPower >= length) nearPower /= 3;

        int[] meds0 = mOMHelper(array, start, nearPower);
        int[] meds2 = mOMHelper(array, start + length - nearPower, nearPower);
        int[] meds1 = medOfMed(array, start + nearPower, length - 2 * nearPower);

        int[] meds = new int[2];
        meds[0] = medOf3(array, meds0[0], meds1[0], meds2[0]);
        meds[1] = medOf3(array, meds0[1], meds1[1], meds2[1]);
        return meds;
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

    protected int pivCmp(int v, int piv1, int piv2, boolean bias) {
        int cmp = Reads.compareValues(v, piv1);
        if (cmp < 0 || (bias && cmp == 0)) return 0;
        cmp = Reads.compareValues(v, piv2);
        if (cmp > 0 || (bias && cmp == 0)) return 2;
        return 1;
    }

    protected int[] partitionEasy(int[] array, int[] buf, int a, int b, int piv1, int piv2, boolean bias) {
        int[] ptrs = new int[4];
        for (int i = a; i < b; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.5);
            Writes.write(buf, i - a, array[i], 0.5, false, true);
            int c = pivCmp(array[i], piv1, piv2, bias);
            ptrs[c]++;
        }
        for (int i = 1; i < ptrs.length; i++) ptrs[i] += ptrs[i-1];
        for (int i = b - a - 1; i >= 0; i--) {
            int c = pivCmp(buf[i], piv1, piv2, bias);
            Writes.write(array, a + --ptrs[c], buf[i], 1, true, false);
        }
        for (int i = 0; i < ptrs.length; i++) ptrs[i] += a;
        return new int[] {ptrs[1], ptrs[2]};
    }

    protected int[] partition(int[] array, int[] buf, int[] tags, int a, int b, int bLen, int piv1, int piv2,
            boolean bias) {
        Highlights.clearMark(2);
        if (b - a <= 3 * bLen) return partitionEasy(array, buf, a, b, piv1, piv2, bias);
        int[] blks = new int[3];
        int[] cnts = new int[3];
        int t = a;
        int bCnt = 0;
        for (int i = a; i < b; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.25);
            int cmp = pivCmp(array[i], piv1, piv2, bias);
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

    void innerSort(int[] array, int[] buf, int[] tags, int l, int r, int bLen) {
        PriorityQueue<Partition> queue = new PriorityQueue<>((r - l - 1) / this.threshold + 1);
        queue.offer(new Partition(l, r, false));
        while (queue.size() > 0) {
            Partition part = queue.poll();
            int a = part.a, b = part.b;
            boolean bad = part.bad;
            int[] pr;
            if (bad) {
                int length = ((b - a) / 5) * 5; // round down to multiple of 5
                if ((length & 1) == 0) length -= 5; // even length bad
                pr = medOfMed(array, a, length);
            } else pr = medOf15(array, a, b);
            int piv1 = array[pr[0]], piv2 = array[pr[1]];
            boolean ternary = Reads.compareValues(piv1, piv2) == 0;
            pr = partition(array, buf, tags, a, b, bLen, piv1, piv2, !ternary);
            int m1 = pr[0], m2 = pr[1];
            int lLen = m1 - a, mLen = m2 - m1, rLen = b - m2;
            if (ternary) {
                if (mLen == b - a) continue;
                if (lLen == 0)
                    bad = mLen < rLen / 8;
                else if (rLen == 0)
                    bad = mLen < lLen / 8;
                else
                    bad = rLen / 8 > lLen || lLen / 8 > rLen;
            } else {
                if (lLen > mLen) {
                    if (lLen > rLen) // l > m, r
                        bad = lLen / 8 > mLen + rLen;
                    else // r >= l > m
                        bad = rLen / 8 > lLen + mLen;
                }
                else if (rLen > mLen) // r > m >= l
                    bad = rLen / 8 > lLen + mLen;
                else // m >= l, r
                    bad = mLen / 8 > lLen + rLen;
            }
            consumePartition(array, queue, a, m1, bad);
            if (!ternary) consumePartition(array, queue, m1, m2, bad);
            consumePartition(array, queue, m2, b, bad);
        }
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using a Median-of-Medians
     * Dual-Pivot Block Partition Sort.
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
            innerSort(array, buf, tags, a, b, bLen);
            Writes.deleteExternalArray(buf);
            Writes.deleteExternalArray(tags);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
