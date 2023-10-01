package sorts.quick;

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
 * @author Ayako-chan
 * @author aphitorite
 *
 */
public class LazyLogSort extends Sort {

    public LazyLogSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lazy Log");
        this.setRunAllSortsName("Lazy Log Sort");
        this.setRunSortName("Lazy Logsort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    int productLog(int n) {
        int r = 1;
        while ((r << r) + r - 1 < n) r++;
        return r;
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

    protected void multiSwap(int[] array, int a, int b, int len, boolean fw) {
        if (a == b) return;
        if (fw)
            for (int i = 0; i < len; i++)
                Writes.swap(array, a + i, b + i, 1, true, false);
        else
            for (int i = len - 1; i >= 0; i--)
                Writes.swap(array, a + i, b + i, 1, true, false);
    }

    protected void insertTo(int[] array, int a, int b, double sleep) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], sleep, true, false);
        if (a != b)
            Writes.write(array, b, temp, sleep, true, false);
    }

    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        if (a >= m || m >= b) return;
        int l = m - a, r = b - m;
        if (l % r == 0 || r % l == 0) {
            while (l > 1 && r > 1) {
                if (r < l) {
                    this.multiSwap(array, m - r, m, r, false);
                    b -= r;
                    m -= r;
                    l -= r;
                } else {
                    this.multiSwap(array, a, m, l, true);
                    a += l;
                    m += l;
                    r -= l;
                }
            }
            if (r == 1) this.insertTo(array, m, a, 0.5);
            else if (l == 1) this.insertTo(array, a, b - 1, 0.5);
        } else {
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
    }

    protected int expSearch(int[] array, int a, int b, int val) {
        int i = 1;
        int a1, b1;
        if (Reads.compareIndexValue(array, a + (b - a) / 2, val, 0, false) <= 0) {
            while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
            a1 = Math.max(a, b - i + 1);
            b1 = b - i / 2;
        } else {
            while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0) i *= 2;
            a1 = a + i / 2;
            b1 = Math.min(b, a - 1 + i);
        }
        while (a1 < b1) {
            int m = a1 + (b1 - a1) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) < 0) b1 = m;
            else a1 = m + 1;
        }
        return a1;
    }

    public void insertSort(int[] array, int a, int b) {
        int i = a + 1;
        if (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) i++;
                if (i - a < 4) Writes.swap(array, a, i - 1, 1.0, true, false);
                else Writes.reversal(array, a, i - 1, 1.0, true, false);
            } else while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        }
        Highlights.clearMark(2);
        for (; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i]), 0.25);
    }

    boolean pivCmp(int v, int piv, boolean eqLower) {
        int c = Reads.compareValues(v, piv);
        return c < 0 || (eqLower && c == 0);
    }

    void pivBufXor(int[] array, int pa, int pb, int v, int wLen) {
        while (wLen-- > 0) {
            if (v % 2 == 1)
                Writes.swap(array, pa + wLen, pb + wLen, 1, true, false);
            v /= 2;
        }
    }

    // @param bit - < pivot means this bit
    int pivBufGet(int[] array, int pa, int piv, boolean bias, int wLen, int bit) {
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
                this.multiSwap(array, p + i * bLen, p + dest * bLen, bLen, true);
                dest = this.pivBufGet(array, p + i * bLen, piv, pCmp, wLen, bit);
            }
            this.pivBufXor(array, p + i * bLen, p1 + i * bLen, i, wLen);
        }
    }

    protected int[] partition(int[] array, int a, int b, int bLen, int piv, boolean bias) {
        boolean allEqual = true;
        if (b - a <= bLen) {
            int j = a;
            for (int i = a; i < b; i++) {
                int cmp = Reads.compareIndexValue(array, i, piv, 0.25, true);
                allEqual &= cmp == 0;
                if (cmp < 0 || bias && cmp == 0)
                    insertTo(array, i, j++, 0.25);
            }
            return new int[] {j, allEqual ? 1 : 0};
        }
        int p = a;
        int l = 0, r = 0;
        int lb = 0, rb = 0;
        for (int i = a; i < b; i++) {
            int cmp = Reads.compareIndexValue(array, i, piv, 0.25, true);
            allEqual &= cmp == 0;
            if (cmp < 0 || bias && cmp == 0) {
                insertTo(array, i, p + l++, 0.25);
                if (l == bLen) {
                    l = 0;
                    lb++;
                    p += bLen;
                }
            } else {
                r++;
                if (r == bLen) {
                    rotate(array, p, p + l, p + l + bLen);
                    r = 0;
                    rb++;
                    p += bLen;
                }
            }
        }
        int min = Math.min(lb, rb);
        int m = a + lb * bLen;
        if (min > 0) {
            int bCnt = lb + rb;
            int wLen = 32 - Integer.numberOfLeadingZeros(min - 1); // ceil(log2(min))
            for (int i = 0, j = 0, k = 0; i < min; i++) { // set bit buffers
                while (!this.pivCmp(array[a + j * bLen + wLen], piv, bias)) j++;
                while (this.pivCmp(array[a + k * bLen + wLen], piv, bias)) k++;
                this.pivBufXor(array, a + (j++) * bLen, a + (k++) * bLen, i, wLen);
            }
            if (lb < rb) {
                for (int i = bCnt - 1, j = 0; j < rb; i--) // swap right to left
                    if (!this.pivCmp(array[a + i * bLen + wLen], piv, bias))
                        this.multiSwap(array, a + i * bLen, a + (bCnt - (++j)) * bLen, bLen, false);
                this.blockCycle(array, a, lb, m, bLen, wLen, piv, bias, 0);
            } else {
                for (int i = 0, j = 0; j < lb; i++) // swap left to right
                    if (this.pivCmp(array[a + i * bLen + wLen], piv, bias))
                        this.multiSwap(array, a + i * bLen, a + (j++) * bLen, bLen, true);
                this.blockCycle(array, m, rb, a, bLen, wLen, piv, bias, 1);
            }
        }
        rotate(array, m, b - r - l, b - r);
        return new int[] {m + l, allEqual ? 1 : 0};
    }

    protected void sortHelper(int[] array, int a, int b, int bLen, boolean bad, boolean bias) {
        while (b - a > 32) {
            int pIdx;
            if (bad) pIdx = medOfMed(array, a, b);
            else pIdx = medP3(array, a, b, 1);
            int[] pr = partition(array, a, b, bLen, array[pIdx], bias);
            int m = pr[0];
            if (pr[1] != 0) return;
            if (m == a) {
                bias = !bias;
                pr = partition(array, a, b, bLen, array[pIdx], bias);
                m = pr[0];
                bad = (b - m) / 8 > m - a;
                a = m;
                continue;
            }
            if (m == b) {
                bias = !bias;
                pr = partition(array, a, b, bLen, array[pIdx], bias);
                m = pr[0];
                bad = (m - a) / 8 > b - m;
                b = m;
                continue;
            }
            int lLen = m - a, rLen = b - m;
            bad = rLen / 8 > lLen || lLen / 8 > rLen;
            if (lLen > rLen) {
                sortHelper(array, m, b, bLen, bad, bias);
                b = m;
            } else {
                sortHelper(array, a, m, bLen, bad, bias);
                a = m;
            }
        }
        insertSort(array, a, b);
    }

    public void quickSort(int[] array, int a, int b) {
        int z = 0, e = 0;
        for (int i = a; i < b - 1; i++) {
            int cmp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            z += cmp > 0 ? 1 : 0;
            e += cmp == 0 ? 1 : 0;
        }
        if (z == 0) return;
        if (z + e == b - a - 1) {
            if (e > 0) stableSegmentReversal(array, a, b - 1);
            else if (b - a < 4) Writes.swap(array, a, b - 1, 0.75, true, false);
            else Writes.reversal(array, a, b - 1, 0.75, true, false);
            return;
        }
        sortHelper(array, a, b, productLog(b - a), false, false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
