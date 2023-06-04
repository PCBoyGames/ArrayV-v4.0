package sorts.hybrid;

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
 * A version of Gridsort which uses part of the array as a buffer.
 * <p>
 * To use this algorithm in another, use {@code quickGridSort()} from a reference
 * instance.
 * 
 * @author Ayako-chan - implementation of the sort
 * @author aphitorite - bucket splitting optimizations
 * @author Scandum - key idea / concept of Gridsort
 *
 */
public final class KleeSort extends Sort {

    public KleeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Klee");
        this.setRunAllSortsName("Klee Sort");
        this.setRunSortName("Kleesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b) Writes.swap(array, a, b, pause, mark, aux);
    }

    protected void multiSwap(int[] array, int a, int b, int len) {
        if (a != b)
            for (int i = 0; i < len; i++)
                Writes.swap(array, a + i, b + i, 1, true, false);
    }

    private void siftDown(int[] array, int val, int i, int p, int n) {
        while (4 * i + 1 < n) {
            int max = val;
            int next = i, child = 4 * i + 1;
            for (int j = child; j < Math.min(child + 4, n); j++) {
                if (Reads.compareValues(array[p + j], max) > 0) {
                    max = array[p + j];
                    next = j;
                }
            }
            if (next == i) break;
            Writes.write(array, p + i, max, 1, true, false);
            i = next;
        }
        Writes.write(array, p + i, val, 1, true, false);
    }

    protected void heapSort(int[] array, int a, int b) {
        int n = b - a;
        for (int i = (n - 1) / 4; i >= 0; i--)
            this.siftDown(array, array[a + i], i, a, n);
        for (int i = n - 1; i > 0; i--) {
            Highlights.markArray(2, a + i);
            int t = array[a + i];
            Writes.write(array, a + i, array[a], 1, false, false);
            this.siftDown(array, t, 0, a, i);
        }
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

    public int partition(int[] array, int a, int b, int pIdx) {
        int i = a, j = b;
        swap(array, a, pIdx, 1, true, false);
        Highlights.markArray(3, a);
        while (true) {
            do {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
            } while (i < j && Reads.compareValues(array[i], array[a]) < 0);
            do {
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
            } while (j >= i && Reads.compareValues(array[j], array[a]) > 0);
            if (i >= j) {
                Highlights.clearMark(3);
                swap(array, a, j, 1, true, false);
                return j;
            }
            Writes.swap(array, i, j, 1, false, false);
        }
    }

    // partition -> [a][E < piv][i][E == piv][j][E > piv][b]
    // returns -> i and j ^
    public int[] partTernary(int[] array, int a, int b, int piv) {
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
        int i1 = a, i = a - 1, j = b, j1 = b;
        while (true) {
            while (++i < j) {
                int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
                if (cmp == 0) swap(array, i1++, i, 1, true, false);
                else if (cmp > 0) break;
            }
            Highlights.clearMark(2);
            while (--j > i) {
                int cmp = Reads.compareIndexValue(array, j, piv, 0.5, true);
                if (cmp == 0) swap(array, --j1, j, 1, true, false);
                else if (cmp < 0) break;
            }
            Highlights.clearMark(2);
            if (i >= j) {
                if (i1 == b) return new int[] { a, b };
                if (j < i) j++;
                if (i1 - a > i - i1) {
                    int i2 = i;
                    i = a;
                    while (i1 < i2) swap(array, i++, i1++, 1, true, false);
                } else while (i1 > a) swap(array, --i, --i1, 1, true, false);
                if (b - j1 > j1 - j) {
                    int j2 = j;
                    j = b;
                    while (j1 > j2) swap(array, --j, --j1, 1, true, false);
                } else while (j1 < b) swap(array, j++, j1++, 1, true, false);
                return new int[] { i, j };
            }
            // The patch is not needed here, because it never swaps when i == j.
            Writes.swap(array, i, j, 1, true, false);
            Highlights.clearMark(2);
        }
    }

    void fancySplitBucket(int[] array, int a, int b, int p) {
        int n = b - a, m = a + n / 2;
        heapSort(array, m, b);
        int i = m - 1, j = b - 1;
        int c = n / 2;
        
        while (c-- > 0) {
            if (Reads.compareValues(array[i], array[j]) > 0)
                swap(array, --p, i--, 1, true, false);
            else
                swap(array, --p, j--, 1, true, false);
        }
        int m1 = m;
        
        while (i >= a && j >= m) {
            if (Reads.compareValues(array[i], array[j]) > 0)
                swap(array, --m1, i--, 1, true, false);
            else
                swap(array, --m1, j--, 1, true, false);
        }
        while (j >= m) swap(array, --m1, j--, 1, true, false);
    }

    protected void mergeTo(int[] array, int a, int m, int b, int p) {
        int i = a, j = m;
        
        while (i < m && j < b) {
            if (Reads.compareValues(array[i], array[j]) <= 0)
                Writes.swap(array, p++, i++, 1, true, false);
            else 
                Writes.swap(array, p++, j++, 1, true, false);
        }
        while (i < m) Writes.swap(array, p++, i++, 1, true, false);
        while (j < b) Writes.swap(array, p++, j++, 1, true, false);
    }
    
    int gridSearch(int[] array, int[] indices, int l, int r, int key, int gap, int ofs) {
        while (l < r) {
            int m = r - (r - l) / 2;
            if (Reads.compareIndices(array, ofs + indices[m]*gap, key, 0.25, true) <= 0)
                l = m;
            else r = m - 1;
        }
        Highlights.clearMark(2);
        return l;
    }
    
    void gridSort(int[] array, int[] sizes, int[] indices, int a, int b, int p, int bLen) {
        int bLen2X = 2 * bLen;
        if (b - a <= bLen2X) {
            heapSort(array, a, b);
            return;
        }
        // sort the first two blocks
        heapSort(array, a, a + bLen2X);
        // swap the first two blocks to buffer
        this.multiSwap(array, a, p, bLen);
        this.multiSwap(array, a+bLen, p+bLen2X, bLen);
        sizes[0] = sizes[1] = bLen;
        indices[1] = 1;
        int kSize = 2;
        for (int i = a+bLen2X; i < b; i++) {
            // search grid, find index, decode index
            int loc = gridSearch(array, indices, 0, kSize - 1, i, bLen2X, p);
            int idx = indices[loc];
            int bPos = p + idx * bLen2X;
            Writes.swap(array, bPos + sizes[idx], i, 1, true, false); // swap sorting element to bucket
            if (++sizes[idx] == bLen2X) { // if after inserting element and gap is full, split
                int bPosNew = p+kSize*bLen2X+bLen;
                fancySplitBucket(array, bPos, bPos + bLen2X, bPosNew); // sort bucket and merge with block
                // insert the new block's tail into the grid along with the new index
                sizes[idx] = bLen;
                sizes[kSize] = bLen;
                for (int j = kSize - 1; j > loc; j--) {
                    Writes.write(indices, j + 1, indices[j], 0, false, true);
                }
                Writes.write(indices, loc + 1, kSize++, 0, false, true);
            }
        }
        for (int i = 0, dst = a; i < kSize; i++) { // retrieve elements in sorted order
            int idx = indices[i];
            int bPos = p + idx * bLen2X;
            if (sizes[idx] > bLen) {
                heapSort(array, bPos + bLen, bPos + sizes[idx]);
                mergeTo(array, bPos, bPos + bLen, bPos + sizes[idx], dst);
            } else multiSwap(array, bPos, dst, sizes[idx]);
            dst += sizes[idx];
        }
    }

    void quickGridHelper(int[] array, int[] sizes, int[] indices, int a, int b, int p, int bLen, int pLen) {
        boolean bad = false;
        while (b - a > pLen / 2) {
            int pIdx;
            if (bad) pIdx = medOfMed(array, a, b);
            else pIdx = medP3(array, a, b, 1);
            int m = partition(array, a, b, pIdx);
            int left = m - a, right = b - m - 1;
            bad = Math.min(left, right) < Math.max(left, right) / 16;
            if (right < left) {
                gridSort(array, sizes, indices, m + 1, b, p, bLen);
                b = m;
            } else {
                gridSort(array, sizes, indices, a, m, p, bLen);
                a = m + 1;
            }
        }
        gridSort(array, sizes, indices, a, b, p, bLen);
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using a Quick Gridsort.
     * 
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     */
    public void quickGridSort(int[] array, int a, int b) {
        int z = 0, e = 0;
        for (int i = a; i < b - 1; i++) {
            int cmp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            z += cmp > 0 ? 1 : 0;
            e += cmp == 0 ? 1 : 0;
        }
        if (z == 0) return;
        if (z + e == b - a - 1) {
            if (b - a < 4) Writes.swap(array, a, b - 1, 0.75, true, false);
            else Writes.reversal(array, a, b - 1, 0.75, true, false);
            return;
        }
        int len = b - a;
        if (len <= 32) {
            insertSort(array, a, b);
            return;
        }
        int bLen = 1 << ((33 - Integer.numberOfLeadingZeros(len - 1)) / 2); // ceilPow2(sqrt(len))
        int[] sizes = Writes.createExternalArray(bLen);
        int[] indices = Writes.createExternalArray(bLen);
        int a1 = a, b1 = b;
        boolean bad = false;
        while (b1 - a1 > 8 * bLen) {
            int pIdx;
            if (bad) pIdx = medOfMed(array, a1, b1);
            else pIdx = medP3(array, a1, b1, 1);
            int[] pr = partTernary(array, a1, b1, array[pIdx]);
            if (pr[1] - pr[0] == b1 - a1) {
                a1 = b1;
                break;
            }
            int m1 = pr[0], m2 = pr[1];
            int lLen = m1 - a1, rLen = b1 - m2;
            bad = Math.min(lLen, rLen) < Math.max(lLen, rLen) / 16;
            if (rLen < lLen) { // sort the smaller partition using larger partition as space
                quickGridHelper(array, sizes, indices, m2, b1, a1, bLen, lLen);
                b1 = m1;
            } else {
                quickGridHelper(array, sizes, indices, a1, m1, m2, bLen, rLen);
                a1 = m2;
            }
        }
        if (b1 - a1 > 1) heapSort(array, a1, b1);
        Writes.deleteExternalArray(sizes);
        Writes.deleteExternalArray(indices);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickGridSort(array, 0, sortLength);

    }

}
