package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite and Distray

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

Original name of this algorithm: Pattern-Improved Equal-Optimized Logsort

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 * @author Distray
 *
 */
public final class GuujiSort extends Sort {

    public GuujiSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Guuji");
        this.setRunAllSortsName("Guuji Sort");
        this.setRunSortName("Guujisort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Set block size (default: calculates minimum block length for current length)", 1);
    }

    private int productLog(int n) {
        int r = 1;
        while ((r << r) + r - 1 < n)
            r++;
        return r;
    }

    private int equ(int a, int b) {
        return ((a - b) >> 31) + ((b - a) >> 31) + 1;
    }

    protected void stableSegmentReversal(int[] array, int start, int end) {
        if (end - start < 3)
            Writes.swap(array, start, end, 0.75, true, false);
        else
            Writes.reversal(array, start, end, 0.75, true, false);
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (i < end && Reads.compareIndices(array, i, i + 1, 0.5, true) == 0)
                i++;
            right = i;
            if (left != right) {
                if (right - left < 3)
                    Writes.swap(array, left, right, 0.75, true, false);
                else
                    Writes.reversal(array, left, right, 0.75, true, false);
            }
            i++;
        }
    }

    // median of 3
    protected int medOf3(int[] array, int l0, int l1, int l2) {
        int t;
        if (Reads.compareIndices(array, l0, l1, 1, true) > 0) {
            t = l0;
            l0 = l1;
            l1 = t;
        }
        if (Reads.compareIndices(array, l1, l2, 1, true) > 0) {
            t = l1;
            l1 = l2;
            l2 = t;
            if (Reads.compareIndices(array, l0, l1, 1, true) > 0) {
                return l0;
            }
        }
        return l1;
    }

    // median of medians with customizable depth
    protected int medOfMed(int[] array, int start, int end, int depth) {
        if (end - start < 9 || depth <= 0) {
            return medOf3(array, start, start + (end - start) / 2, end);
        }
        int div = (end - start) / 8;
        int m0 = medOfMed(array, start, start + 2 * div, --depth);
        int m1 = medOfMed(array, start + 3 * div, start + 5 * div, depth);
        int m2 = medOfMed(array, start + 6 * div, end, depth);
        return medOf3(array, m0, m1, m2);
    }

    protected void multiSwap(int[] array, int a, int b, int len) {
        if (a != b)
            for (int i = 0; i < len; i++)
                Writes.swap(array, a + i, b + i, 1, true, false);
    }

    // swap across auxiliary arrays
    protected void swapCopy(int[] src, int srcstart, int[] dst, int dststart, int srclen, int dstlen, double sleep,
            boolean mark, boolean toAux) {
        int i = 0, tmp;
        for (; i < Math.min(srclen, dstlen); i++) {
            tmp = src[srcstart + i];
            Writes.write(src, srcstart + i, dst[dststart + i], sleep / 2d, mark, toAux);
            Writes.write(dst, dststart + i, tmp, sleep / 2d, !mark, !toAux);
        }
        for (; i < srclen; i++) {
            Writes.write(dst, dststart + i, src[srcstart + i], sleep, !mark, !toAux);
        }
        for (; i < dstlen; i++) {
            Writes.write(src, srcstart + i, dst[dststart + i], sleep, mark, toAux);
        }
    }

    protected int expSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
            i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        while (a1 < b1) {
            int m = a1 + (b1 - a1) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) < 0)
                b1 = m;
            else
                a1 = m + 1;
        }
        return a1;
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

    public void insertSort(int[] array, int a, int b) {
        int i = a + 1;
        if (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0)
                    i++;
                if (i - a < 4)
                    Writes.swap(array, a, i - 1, 1.0, true, false);
                else
                    Writes.reversal(array, a, i - 1, 1.0, true, false);
            } else
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0)
                    i++;
        }
        Highlights.clearMark(2);
        for (; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i]));
    }

    // @param bias - false for < piv, true for <= piv
    protected boolean pivCmp(int v, int piv, boolean bias) {
        int c = Reads.compareValues(v, piv);
        return c < 0 || (bias && c == 0);
    }

    protected void pivBufXor(int[] array, int pa, int pb, int v, int wLen) {
        while (wLen-- > 0) {
            if (v % 2 == 1)
                Writes.swap(array, pa + wLen, pb + wLen, 1, true, false);
            v /= 2;
        }
    }

    // @param bit - < pivot means this bit
    protected int pivBufGet(int[] array, int pa, int piv, boolean bias, int wLen, int bit) {
        int r = 0;
        while (wLen-- > 0) {
            r *= 2;
            r |= (this.pivCmp(array[pa++], piv, bias) ? 0 : 1) ^ bit;
        }
        return r;
    }

    protected void blockCycle(int[] array, int p, int n, int p1, int bLen, int wLen, int piv, boolean pCmp, int bit) {
        for (int i = 0; i < n; i++) {
            int dest = this.pivBufGet(array, p + i * bLen, piv, pCmp, wLen, bit);
            while (dest != i) {
                this.multiSwap(array, p + i * bLen, p + dest * bLen, bLen);
                dest = this.pivBufGet(array, p + i * bLen, piv, pCmp, wLen, bit);
            }
            this.pivBufXor(array, p + i * bLen, p1 + i * bLen, i, wLen);
        }
    }

    protected int[] partition(int[] array, int[] aux, int a, int b, int bLen, int piv, boolean pCmp,
            boolean badImbalance) {
        boolean allEq = true, invert = false, opposing = true;
        int t = a, l = 0, r = 0, lb = 0, rb = 0;
        int lStreaks = 0, rStreaks = 0, maxStreaks = 0;
        for (int i = a; i < b; i++) {
            int cmp = Reads.compareIndexValue(array, i, piv, 0.25, true);
            allEq = allEq && cmp == 0;
            boolean hiPart = !(cmp < 0 || pCmp && cmp == 0);
            opposing = opposing && (!(!pCmp ^ !hiPart) || cmp == 0);
            int trStreaks = hiPart ? rStreaks : lStreaks, tlStreaks = hiPart ? lStreaks : rStreaks, tr = hiPart ? r : l;
            if (hiPart ^ invert) {
                Writes.write(aux, tr++, array[i], 1, false, true);
                if (tr >= bLen) {
                    trStreaks++;
                    if (maxStreaks < trStreaks)
                        maxStreaks = trStreaks;
                    tlStreaks = 0;
                    int tl = hiPart ? l : r;
                    if (badImbalance)
                        swapCopy(array, t, aux, 0, tl, bLen, 1, true, false);
                    else {
                        Writes.arraycopy(array, t, array, t + bLen, tl, 1, true, false);
                        Writes.arraycopy(aux, 0, array, t, bLen, 1, true, false);
                    }
                    tr = 0;
                    if (hiPart)
                        rb++;
                    else
                        lb++;
                    t += bLen;
                    if (badImbalance)
                        invert = !invert;
                }
            } else {
                if (t + tr != i)
                    Writes.write(array, t + tr, array[i], 1, true, false);
                if (++tr >= bLen) {
                    trStreaks++;
                    if (maxStreaks < trStreaks)
                        maxStreaks = trStreaks;
                    tlStreaks = 0;
                    tr = 0;
                    if (hiPart)
                        rb++;
                    else
                        lb++;
                    t += bLen;
                }
            }
            if (hiPart) {
                rStreaks = trStreaks;
                lStreaks = tlStreaks;
                r = tr;
            } else {
                lStreaks = trStreaks;
                rStreaks = tlStreaks;
                l = tr;
            }
        }
        int min = Math.min(lb, rb);
        int m = a + lb * bLen;
        if (min > 0) {
            int bCnt = lb + rb;
            int wLen = 32 - Integer.numberOfLeadingZeros(min - 1); // ceil(log2(min))
            for (int i = 0, j = 0, k = 0; i < min; i++) { // set bit buffers
                while (!this.pivCmp(array[a + j * bLen + wLen], piv, pCmp))
                    j++;
                while (this.pivCmp(array[a + k * bLen + wLen], piv, pCmp))
                    k++;
                this.pivBufXor(array, a + (j++) * bLen, a + (k++) * bLen, i, wLen);
            }
            if (lb < rb) {
                for (int i = bCnt - 1, j = 0; j < rb; i--) // swap right to left
                    if (!this.pivCmp(array[a + i * bLen + wLen], piv, pCmp))
                        this.multiSwap(array, a + i * bLen, a + (bCnt - (++j)) * bLen, bLen);
                this.blockCycle(array, a, lb, m, bLen, wLen, piv, pCmp, 0);
            } else {
                for (int i = 0, j = 0; j < lb; i++) // swap left to right
                    if (this.pivCmp(array[a + i * bLen + wLen], piv, pCmp))
                        this.multiSwap(array, a + i * bLen, a + (j++) * bLen, bLen);
                this.blockCycle(array, m, rb, a, bLen, wLen, piv, pCmp, 1);
            }
        }
        if (invert) {
            if (l != 0) {
                Writes.arraycopy(array, m, array, m + l, rb * bLen + r, 1, true, false);
                Writes.arraycopy(aux, 0, array, m, l, 1, true, false);
            }
        } else {
            Writes.arraycopy(aux, 0, array, b - r, r, 1, true, false);
            if (l > 0) {
                Highlights.clearMark(2);
                Writes.arraycopy(array, b - r - l, aux, 0, l, 1, false, true);
                Writes.arraycopy(array, m, array, m + l, rb * bLen, 1, true, false);
                Writes.arraycopy(aux, 0, array, m, l, 1, true, false);
            }
        }
        boolean badStreak = maxStreaks > (lb + rb) / 4 + 1;
        return new int[] { m + l, (allEq ? 1 : 0) | (opposing ? 2 : 0) | (badStreak ? 4 : 0) };
    }

    // same as arraycopy, but reverses the elements being copied at the same time
    protected void copyReverse(int[] src, int srcPos, int[] dest, int destPos, int len, boolean aux) {
        for (int i = 0; i < len; i++)
            Writes.write(dest, destPos + i, src[srcPos + len - 1 - i], 1, !aux, aux);
    }

    protected int[] partitionEasy(int[] array, int[] buf, int a, int b, int piv) {
        Highlights.clearMark(2);
        int len = b - a;
        int p0 = a, p1 = 0, p2 = len;
        for (int i = a; i < b; i++) {
            int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
            if (cmp < 0)
                Writes.write(array, p0++, array[i], 1, true, false);
            else if (cmp == 0)
                Writes.write(buf, --p2, array[i], 1, false, true);
            else
                Writes.write(buf, p1++, array[i], 1, false, true);
        }
        int eqSize = len - p2, gtrSize = p1;
        copyReverse(buf, p2, array, p0, eqSize, false);
        Writes.arraycopy(buf, 0, array, p0 + eqSize, gtrSize, 1, true, false);
        return new int[] { p0, p0 + eqSize };
    }

    protected void easyStable(int[] array, int[] buf, int a, int b) {
        while (b - a > 16) {
            int pIdx = medOfMed(array, a, b - 1, (int) (Math.log(b - a) / Math.log(9)));
            int[] p = partitionEasy(array, buf, a, b, array[pIdx]);
            int lLen = p[0] - a, rLen = b - p[1], eqLen = p[1] - p[0];
            if (eqLen == b - a)
                return;
            if (rLen == 0) {
                b = p[0];
                continue;
            }
            if (lLen == 0) {
                a = p[1];
                continue;
            }
            if (rLen < lLen) {
                easyStable(array, buf, p[1], b);
                b = p[0];
            } else {
                easyStable(array, buf, a, p[0]);
                a = p[1];
            }
        }
        insertSort(array, a, b);
    }

    protected void sortHelper(int[] array, int[] buf, int a, int b, int bLen, boolean bad) {
        while (b - a > Math.max(bLen, 16)) {
            int pIdx = medOfMed(array, a, b - 1, (int) (Math.log(b - a) / Math.log(9)));
            int[] pr = partition(array, buf, a, b, bLen, array[pIdx], true, bad);
            int m = pr[0];
            if ((pr[1] & 0x1) != 0)
                return;
            if ((pr[1] & 0x2) != 0) {
                a = m;
                continue;
            }
            if (m == b) {
                pr = partition(array, buf, a, b, bLen, array[pIdx], false, bad);
                b = pr[0];
                continue;
            }
            int lLen = m - a, rLen = b - m;
            bad = rLen / 8 > lLen || lLen / 8 > rLen || (pr[1] & 0x4) != 0;
            if (lLen > rLen) {
                sortHelper(array, buf, m, b, bLen, bad);
                b = m;
            } else {
                sortHelper(array, buf, a, m, bLen, bad);
                a = m;
            }
        }
        easyStable(array, buf, a, b);
    }

    protected void analyze(int[] array, int[] buf, int a, int b, int bLen) {
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
        if (balance == 0)
            return;
        if (balance + eq == len - 1) {
            if (eq > 0)
                stableSegmentReversal(array, a, b - 1);
            else if (b - a < 4) {
                Writes.swap(array, a, b - 1, 0.75, true, false);
            } else
                Writes.reversal(array, a, b - 1, 0.75, true, false);
            return;
        }
        int sixth = len / 6;
        sortHelper(array, buf, a, b, bLen, streaks > len / 20 || balance <= sixth || balance + eq >= len - sixth);
    }

    public void quickSort(int[] array, int a, int b, int bLen) {
        int len = b - a;
        if (len <= 16)
            insertSort(array, a, b);
        else {
            bLen = Math.max(productLog(len), Math.min(bLen, len));
            int[] buf = Writes.createExternalArray(bLen);
            analyze(array, buf, a, b, bLen);
            Writes.deleteExternalArray(buf);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength, bucketCount);

    }

}
