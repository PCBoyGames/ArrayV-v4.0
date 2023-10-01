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
 * @author Ayako-chan
 * @author aphitorite
 * @author Scandum
 *
 */
public class AyakoSort extends Sort {

    public AyakoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Ayako");
        this.setRunAllSortsName("Ayako Sort");
        this.setRunSortName("Ayakosort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    int threshold = 32;

    int equ(int a, int b) {
        return ((a - b) >> 31) + ((b - a) >> 31) + 1;
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

    void multiSwap(int[] array, int a, int b, int s) {
        if (a != b) while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }

    void multiSwapBW(int[] array, int a, int b, int s) {
        if (a != b) while (s-- > 0) Writes.swap(array, a + s, b + s, 1, true, false);
    }

    void rotate(int[] array, int a, int m, int b) {
        int l = m - a, r = b - m;
        while (l > 1 && r > 1) {
            if (r < l) {
                this.multiSwapBW(array, m - r, m, r);
                b -= r;
                m -= r;
                l -= r;
            } else {
                this.multiSwap(array, a, m, l);
                a += l;
                m += l;
                r -= l;
            }
        }
        if (r == 1) this.insertTo(array, m, a);
        else if (l == 1) this.insertTo(array, a, b - 1);
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

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b) Writes.write(array, b, temp, 0.5, true, false);
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

    public void insertSort(int[] array, int a, int b) {
        buildRuns(array, a, b, b - a);
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
        if (m - a > b - m) mergeBWExt(array, buf, a, m, b);
        else mergeFWExt(array, buf, a, m, b);
    }

    protected void blockCycle(int[] array, int[] buf, int[] keys, int a, int bLen, int bCnt) {
        for (int i = 0; i < bCnt; i++)
            if (Reads.compareOriginalValues(i, keys[i]) != 0) {
                Writes.arraycopy(array, a + i * bLen, buf, 0, bLen, 1, true, true);
                int j = i, next = keys[i];
                do {
                    Writes.arraycopy(array, a + next * bLen, array, a + j * bLen, bLen, 1, true, false);
                    Writes.write(keys, j, j, 1, true, true);
                    j = next;
                    next = keys[next];
                } while (Reads.compareOriginalValues(next, i) != 0);
                Writes.arraycopy(buf, 0, array, a + j * bLen, bLen, 1, true, false);
                Writes.write(keys, j, j, 1, true, true);
            }
    }

    protected void blockMerge(int[] array, int[] buf, int[] tags, int a, int m, int b, int bLen) {
        int c = 0, t = 2;
        int i = a, j = m, k = 0;
        int l = 0, r = 0;
        while (c++ < 2 * bLen) { // merge 2 blocks into buffer to create 2 buffers
            Highlights.markArray(2, i);
            Highlights.markArray(3, j);
            if (Reads.compareValues(array[i], array[j]) <= 0) {
                Writes.write(buf, k++, array[i++], 1, true, true);
                l++;
            } else {
                Writes.write(buf, k++, array[j++], 1, true, true);
                r++;
            }
        }
        boolean left = l >= r;
        k = left ? i - l : j - r;
        c = 0;
        do {
            if (i < m) Highlights.markArray(2, i);
            else Highlights.clearMark(2);
            if (j < b) Highlights.markArray(3, j);
            else Highlights.clearMark(3);
            if (i < m && (j == b || Reads.compareValues(array[i], array[j]) <= 0)) {
                Writes.write(array, k++, array[i++], 1, true, false);
                l++;
            } else {
                Writes.write(array, k++, array[j++], 1, true, false);
                r++;
            }
            if (++c == bLen) { // change buffer after every block
                Writes.write(tags, t++, (k - a) / bLen - 1, 0, false, true);
                if (left) l -= bLen;
                else r -= bLen;
                left = l >= r;
                k = left ? i - l : j - r;
                c = 0;
            }
        } while (i < m || j < b);
        Highlights.clearAllMarks();
        int b1 = b - c;
        Writes.arraycopy(array, k - c, array, b1, c, 1, true, false); // copy remainder to end (r buffer)
        r -= c;
        // l and r buffers are divisible by bLen
        t = 0;
        k = 0;
        while (l > 0) {
            Writes.arraycopy(buf, k, array, m - l, bLen, 1, true, false);
            Writes.write(tags, t++, (m - a - l) / bLen, 0, false, true);
            k += bLen;
            l -= bLen;
        }
        while (r > 0) {
            Writes.arraycopy(buf, k, array, b1 - r, bLen, 1, true, false);
            Writes.write(tags, t++, (b1 - a - r) / bLen, 0, false, true);
            k += bLen;
            r -= bLen;
        }
        blockCycle(array, buf, tags, a, bLen, (b - a) / bLen);
    }

    protected void smartMerge(int[] array, int[] buf, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0) return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        Highlights.clearMark(2);
        merge(array, buf, a, m, b);
    }

    protected void pingPongMerge(int[] array, int[] buf, int a, int m1, int m2, int m3, int b) {
        int p = 0, p1 = p + m2-a, pEnd = p + b-a;
        if (Reads.compareIndices(array, m1-1, m1, 1, true) > 0
        || (m3 < b && Reads.compareIndices(array, m3-1, m3, 1, true) > 0)) {
            mergeTo(array, buf, a, m1, m2, p, true);
            mergeTo(array, buf, m2, m3, b, p1, true);
            mergeTo(buf, array, p, p1, pEnd, a, false);
        }
        else smartMerge(array, buf, a, m2, b);
    }

    protected void smartBlockMerge(int[] array, int[] buf, int[] tags, int a, int m, int b, int bLen) {
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0) return;
        int s = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[s], array[b - 1]) > 0) {
            rotate(array, s, m, b);
            return;
        }
        if (Math.min(m - s, b - m) <= 2 * bLen) merge(array, buf, s, m, b);
        else {
            s -= (s - a) % bLen;
            blockMerge(array, buf, tags, s, m, b, bLen);
        }
    }

    public void blockMergeSort(int[] array, int[] buf, int[] tags, int a, int b, int bLen) {
        int len = b - a;
        int j = 16;
        int bufLen = 2 * bLen;
        if (buildRuns(array, a, b, j)) return;
        int i;
        for (; 4 * j <= bufLen; j *= 4) {
            for (i = a; i+2*j < b; i += 4*j)
                pingPongMerge(array, buf, i, i+j, i+2*j, Math.min(i+3*j, b), Math.min(i+4*j, b));
            if (i + j < b)
                smartMerge(array, buf, i, i + j, b);
        }
        for (; j <= bufLen; j *= 2)
            for (i = a; i + j < b; i += 2 * j)
                smartMerge(array, buf, i, i + j, Math.min(i + 2 * j, b));
        for (; j < len; j *= 2)
            for (i = a; i + j < b; i += 2 * j)
                smartBlockMerge(array, buf, tags, i, i + j, Math.min(i + 2 * j, b), bLen);
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
            int c = pivCmp(array[i], piv);
            Writes.write(buf, c * bLen + cnts[c]++, array[i], 0.5, false, true);
            if (cnts[c] >= bLen) {
                Writes.arraycopy(buf, c * bLen, array, t, bLen, 1.0, true, false);
                cnts[c] = 0;
                blks[c]++;
                t += bLen;
                Writes.write(tags, bCnt++, c * bLen, 0, false, true);
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

    void sortHelper(int[] array, int[] buf, int[] tags, int a, int b, int bLen, boolean bad) {
        while (b - a > threshold) {
            int pIdx;
            if (bad) pIdx = medOfMed(array, a, b);
            else pIdx = medP3(array, a, b, 2);
            int[] pr = partition(array, buf, tags, a, b, bLen, array[pIdx]);
            if (pr[1] - pr[0] == b - a) return;
            int lLen = pr[0] - a, rLen = b - pr[1], eqLen = pr[1] - pr[0];
            if (rLen == 0) {
                bad = eqLen < lLen / 8;
                b = pr[0];
                continue;
            }
            if (lLen == 0) {
                bad = eqLen < rLen / 8;
                a = pr[1];
                continue;
            }
            bad = lLen < rLen / 8 || rLen < lLen / 8;
            if (rLen < lLen) {
                sortHelper(array, buf, tags, pr[1], b, bLen, bad);
                b = pr[0];
            } else {
                sortHelper(array, buf, tags, a, pr[0], bLen, bad);
                a = pr[1];
            }
        }
        insertSort(array, a, b);
    }

    public void quickSort(int[] array, int a, int b) {
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
        int bLen = 1;
        while (bLen * bLen < len) bLen *= 2;
        int[] buf = Writes.createExternalArray(3 * bLen);
        int[] tags = Writes.createExternalArray(len / bLen);
        int sixth = len / 6;
        if (streaks > len / 20 || balance <= sixth || balance + eq >= len - sixth)
            blockMergeSort(array, buf, tags, a, b, bLen);
        else sortHelper(array, buf, tags, a, b, bLen, false);
        Writes.deleteExternalArray(buf);
        Writes.deleteExternalArray(tags);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
