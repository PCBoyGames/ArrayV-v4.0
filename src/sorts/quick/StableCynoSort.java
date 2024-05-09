package sorts.quick;

import java.util.PriorityQueue;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite, Gaming32 and Scandum

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 * @author Gaming32
 * @author Scandum
 *
 */
public final class StableCynoSort extends Sort {

    public StableCynoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stable Cyno");
        this.setRunAllSortsName("Stable Cyno Sort");
        this.setRunSortName("Stable Cynosort");
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
    
    protected int medOf3(int[] array, int i0, int i1, int i2) {
        int t;
        if(Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            t = i1;
            i1 = i0;
        } else t = i0;
        if(Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if(Reads.compareIndices(array, t, i2, 1, true) > 0) return t;
            return i2;
        }
        return i1;
    }
    
    int[] medOf5(int[] array, int[] indices) {
        for (int i = 0; i < 5; i++) {
            for(int j = i; j > 0; j--) {
                if (Reads.compareIndices(array, indices[j], indices[j - 1], 0.5, true) < 0) {
                    int t = indices[j];
                    indices[j] = indices[j - 1];
                    indices[j - 1] = t;
                } else break;
            }
        }
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
    
    protected int binSearch(int[] array, int a, int b, int val, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0))
                b = m;
            else
                a = m + 1;
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
        if (a != b) Writes.write(array, b, temp, 0.5, true, false);
    }

    protected void mergeFWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = m - a;
        Writes.arraycopy(array, a, tmp, 0, s, 1, true, true);
        int i = 0, j = m;
        while (i < s && j < b)
            if (Reads.compareValues(tmp[i], array[j]) <= 0)
                Writes.write(array, a++, tmp[i++], 1, true, false);
            else
                Writes.write(array, a++, array[j++], 1, true, false);
        while (i < s)
            Writes.write(array, a++, tmp[i++], 1, true, false);
    }

    protected void mergeBWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = b - m;
        Writes.arraycopy(array, m, tmp, 0, s, 1, true, true);
        int i = s - 1, j = m - 1;
        while (i >= 0 && j >= a)
            if (Reads.compareValues(tmp[i], array[j]) >= 0)
                Writes.write(array, --b, tmp[i--], 1, true, false);
            else
                Writes.write(array, --b, array[j--], 1, true, false);
        while (i >= 0)
            Writes.write(array, --b, tmp[i--], 1, true, false);
    }

    protected void merge(int[] array, int[] buf, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0) return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        Highlights.clearMark(2);
        if (m - a > b - m) mergeBWExt(array, buf, a, m, b);
        else mergeFWExt(array, buf, a, m, b);
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
        // alias for convenience
        findRun(array, a, b, b - a);
    }
    
    public void mergeSort(int[] array, int[] buf, int a, int b) {
        int len = b - a;
        if (len <= threshold) {
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
                merge(array, buf, runs[i], runs[i + 1], eIdx);
            }
            for (int i = 1, j = 2; i < rf; i++, j+=2, rf--)
                Writes.write(runs, i, runs[j], 0.5, false, true);

        }
        Writes.deleteExternalArray(runs);
    }
    
    int pivCmpSP(int v, int piv) {
        int c = Reads.compareValues(v, piv);
        if (c > 0)
            return 2;
        if (c < 0)
            return 0;
        return 1;
    }
    
    protected int[] partitionSP(int[] array, int[] buf, int a, int b, int piv) {
        Highlights.clearMark(2);
        int[] ptrs = new int[4];
        for (int i = a; i < b; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.5);
            Writes.write(buf, i - a, array[i], 0.5, false, true);
            int c = pivCmpSP(array[i], piv);
            ptrs[c]++;
        }
        for (int i = 1; i < ptrs.length; i++) ptrs[i] += ptrs[i-1];
        for (int i = b - a - 1; i >= 0; i--) {
            int c = pivCmpSP(buf[i], piv);
            Writes.write(array, a + --ptrs[c], buf[i], 1, true, false);
        }
        for (int i = 0; i < ptrs.length; i++) ptrs[i] += a;
        return new int[] {ptrs[1], ptrs[2]};
    }
    
    int pivCmpDP(int v, int piv1, int piv2) {
        int cmp = Reads.compareValues(v, piv1);
        if (cmp < 0) return 0; // v < piv1
        if (cmp == 0) return 1; // v == piv1
        // v > piv1
        cmp = Reads.compareValues(v, piv2);
        if (cmp < 0) return 2; // v < piv2
        if (cmp == 0) return 3; // v == piv2
        return 4; // v > piv2
    }
    
    protected int[] partitionDP(int[] array, int[] buf, int a, int b, int piv1, int piv2) {
        Highlights.clearMark(2);
        int[] ptrs = new int[6];
        for (int i = a; i < b; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.5);
            Writes.write(buf, i - a, array[i], 0.5, false, true);
            int c = pivCmpDP(array[i], piv1, piv2);
            ptrs[c]++;
        }
        for (int i = 1; i < ptrs.length; i++) ptrs[i] += ptrs[i-1];
        for (int i = b - a - 1; i >= 0; i--) {
            int c = pivCmpDP(buf[i], piv1, piv2);
            Writes.write(array, a + --ptrs[c], buf[i], 1, true, false);
        }
        for (int i = 0; i < ptrs.length; i++) ptrs[i] += a;
        return new int[] {ptrs[1], ptrs[2], ptrs[3], ptrs[4]};
    }
    
    void consumePartition(int[] array, PriorityQueue<Partition> queue, int a, int b, boolean bad) {
        if (b - a > threshold) queue.offer(new Partition(a, b, bad));
        else insertSort(array, a, b);
    }
    
    void innerSort(int[] array, int[] buf, int left, int right) {
        PriorityQueue<Partition> queue = new PriorityQueue<>((right - left - 1) / this.threshold + 1);
        queue.offer(new Partition(left, right, false));
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
            if (Reads.compareValues(piv1, piv2) == 0) {
                pr = partitionSP(array, buf, a, b, piv1);
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
                continue;
            }
            pr = partitionDP(array, buf, a, b, piv1, piv2);
            int lLen = pr[0] - a, mLen = pr[2] - pr[1], rLen = b - pr[3];
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
            consumePartition(array, queue, a, pr[0], bad);
            consumePartition(array, queue, pr[1], pr[2], bad);
            consumePartition(array, queue, pr[3], b, bad);
        }
    }
    
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
        int[] buf = Writes.createExternalArray(len);
        int sixth = len / 6;
        if (streaks > len / 20 || balance <= sixth || balance + eq >= len - sixth)
            mergeSort(array, buf, a, b);
        else innerSort(array, buf, a, b);
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
