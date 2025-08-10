package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Haruki
in collaboration with aphitorite

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * An adaptive stable merge sort with O(sqrt(n)) dynamic external buffer.
 * <p>
 * To use this algorithm in another, use {@code blockMergeSort()} from a
 * reference instance.
 * 
 * @author Haruki (a.k.a. Ayako-chan)
 * @author aphitorite
 *
 */
public class BlockTimSort extends Sort {

    public BlockTimSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Block Tim");
        setRunAllSortsName("Block Tim Sort");
        setRunSortName("Block Timsort");
        setCategory("Hybrid Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    // adaptive stable merge sort with O(sqrt(n)) dynamic external buffer

    public static int getMinLevel(int n) {
        while (n >= 32)
            n = (n + 1) / 2;
        return n;
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b) Writes.write(array, b, temp, 0.5, true, false);
    }

    protected void insertToBWExt(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        for (int i = a; i < b; i++)
            Writes.write(array, i, array[i + 1], 0.5, false, true);
        if (a != b)
            Writes.write(array, b, temp, 0.5, false, true);
    }

    protected void multiSwap(int[] array, int a, int b, int len) {
        for (int i = 0; i < len; i++)
            Writes.swap(array, a + i, b + i, 1, true, false);
    }

    protected void shiftFWExt(int[] array, int a, int m, int b) {
        Highlights.clearMark(2);
        while (m < b)
            Writes.write(array, a++, array[m++], 1, true, false);
    }

    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        int l = m - a, r = b - m;
        while (l > 1 && r > 1)
            if (r < l) {
                this.multiSwap(array, m - r, m, r);
                b -= r;
                m -= r;
                l -= r;
            } else {
                this.multiSwap(array, a, m, l);
                a += l;
                m += l;
                r -= l;
            }
        if (r == 1) this.insertTo(array, m, a);
        else if (l == 1) this.insertTo(array, a, b - 1);
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

    protected boolean checkReverseBounds(int[] array, int a, int m, int b) {
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return true;
        }
        return false;
    }

    protected void mergeFromBuf(int[] array, int[] buf, int a, int m, int b, int bufLen) {
        int i = 0;
        while(i < bufLen && m < b)
            if(Reads.compareValues(buf[i], array[m]) <= 0)
                Writes.write(array, a++, buf[i++], 1, true, false);
            else
                Writes.write(array, a++, array[m++], 1, true, false);
        while(i < bufLen)
            Writes.write(array, a++, buf[i++], 1, true, false);
    }

    protected void mergeTo(int[] from, int[] to, int a, int m, int b, int p, boolean aux) {
        int i = a, j = m;
        while(i < m && j < b) {
            Highlights.markArray(2, i);
            Highlights.markArray(3, j);
            if(Reads.compareValues(from[i], from[j]) <= 0)
                Writes.write(to, p++, from[i++], 1, true, aux);
            else
                Writes.write(to, p++, from[j++], 1, true, aux);
        }
        Highlights.clearMark(3);
        while(i < m) {
            Highlights.markArray(2, i);
            Writes.write(to, p++, from[i++], 1, true, aux);
        }
        while(j < b) {
            Highlights.markArray(2, j);
            Writes.write(to, p++, from[j++], 1, true, aux);
        }
        Highlights.clearMark(2);
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

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = leftExpSearch(array, m, b, array[a], true);
            rotate(array, a, m, i);
            int t = i - m;
            m = i;
            a += t + 1;
            if (m >= b)
                break;
            a = leftExpSearch(array, a, m, array[m], false);
        }
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        while (b > m && m > a) {
            int i = rightExpSearch(array, a, m, array[b - 1], false);
            rotate(array, i, m, b);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a)
                break;
            b = rightExpSearch(array, m, b, array[m - 1], true);
        }
    }

    protected void merge(int[] array, int[] buf, int a, int m, int b) {
        Highlights.clearMark(2);
        if (Math.min(m - a, b - m) <= 8) {
            if (m - a > b - m)
                inPlaceMergeBW(array, a, m, b);
            else
                inPlaceMergeFW(array, a, m, b);
        } else if (m - a > b - m)
            mergeBWExt(array, buf, a, m, b);
        else
            mergeFWExt(array, buf, a, m, b);
    }

    protected boolean getSubarray(int[] tags, int t, int mKey) {
        return Reads.compareOriginalValues(tags[t], tags[mKey]) < 0;
    }

    // returns mKey final position
    protected int blockSelect(int[] array, int[] tags, int p, int r, int d, int lCnt, int bCnt, int bLen) {
        int mKey = lCnt;
        for (int j = 0, k = lCnt + 1; j < k - 1; j++) {
            int min = j;
            for (int i = Math.max(lCnt - r, j + 1); i < k; i++) {
                int cmp = Reads.compareIndices(array, p + d + i * bLen, p + d + min * bLen, 0, false);
                if (cmp < 0 || (cmp == 0 && Reads.compareOriginalValues(tags[i], tags[min]) < 0))
                    min = i;
            }
            if (min != j) {
                multiSwap(array, p + j * bLen, p + min * bLen, bLen);
                Writes.swap(tags, j, min, 1, false, true);
                if (k < bCnt && min == k - 1) k++;
            }
            if (min == mKey) mKey = j;
        }
        return mKey;
    }
    
    protected int mergeBlocks(int[] array, int a, int m, int b, int p, boolean fwEq) {
        int i = a, j = m;
        while (i < m && j < b) {
            int cmp = Reads.compareIndices(array, i, j, 0.0, true);
            if (cmp < 0 || (fwEq && cmp == 0))
                Writes.write(array, p++, array[i++], 1.0, true, false);
            else
                Writes.write(array, p++, array[j++], 1.0, true, false);
        }
        if(i > p) shiftFWExt(array, p, i, m);
        return j;
    }

    // is never called if m-a || b-m <= bLen
    // should never be called if (m-a)%bLen != 0
    protected void blockMerge(int[] array, int[] buf, int[] tags, int a, int m, int b, int bLen) {
        int b1 = b - (b - m - 1) % bLen - 1,
                i = a + bLen, j = a, key = -1,
                lCnt = (m - i) / bLen, bCnt = (b1 - i) / bLen, l = -1, r = lCnt - 1;
        for (int k = 0; k < bLen; k++) {
            Writes.write(buf, k, array[m - bLen + k], 0.5, true, false);
            Writes.write(array, m - bLen + k, array[a + k], 0.5, true, false);
        }
        for (int k = 0; k < bCnt; k++) Writes.write(tags, k, k, 0, true, true);
        insertToBWExt(tags, 0, lCnt - 1);
        int mKey = blockSelect(array, tags, i, 1, bLen - 1, lCnt, bCnt, bLen);
        boolean frag = true;
        while(l < lCnt && r < bCnt) {
            if(frag) {
                do {
                    j += bLen;
                    l++;
                    key++;
                } while (l < lCnt && getSubarray(tags, key, mKey));
                if (l == lCnt) {
                    i = mergeBlocks(array, i, j, b, i - bLen, true);
                    mergeFromBuf(array, buf, i - bLen, i, b, bLen);
                } else i = mergeBlocks(array, i, j, j + bLen - 1, i - bLen, true);
            } else {
                do {
                    j += bLen;
                    r++;
                    key++;
                } while (r < bCnt && !getSubarray(tags, key, mKey));
                if (r == bCnt) {
                    shiftFWExt(array, i - bLen, i, b);
                    Writes.arraycopy(buf, 0, array, b - bLen, bLen, 1, true, false);
                } else i = mergeBlocks(array, i, j, j + bLen - 1, i - bLen, false);
            }
            frag = !frag;
        }
    }

    protected void smartMerge(int[] array, int[] buf, int a, int m, int b) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (checkReverseBounds(array, a, m, b))
            return;
        merge(array, buf, a, m, b);
    }

    protected void pingPongMerge(int[] array, int[] buf, int a, int m1, int m2, int m3, int b) {
        int p = 0, p1 = p + m2-a, pEnd = p + b-a;
        if(Reads.compareIndices(array, m1-1, m1, 1, true) > 0
        || (m3 < b && Reads.compareIndices(array, m3-1, m3, 1, true) > 0)) {
            mergeTo(array, buf, a, m1, m2, p, true);
            mergeTo(array, buf, m2, m3, b, p1, true);
            mergeTo(buf, array, p, p1, pEnd, a, false);
        }
        else 
            smartMerge(array, buf, a, m2, b);
    }

    protected void smartBlockMerge(int[] array, int[] buf, int[] tags, int a, int m, int b, int bLen) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        int s = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (checkReverseBounds(array, s, m, b))
            return;
        if (Math.min(m - s, b - m) <= bLen)
            merge(array, buf, s, m, b);
        else {
            s -= (s - a) % bLen;
            blockMerge(array, buf, tags, s, m, b, bLen);
        }
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using a block merge sort.
     *
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     */
    public void blockMergeSort(int[] array, int a, int b) {
        int len = b - a;
        if (len < 128) { // adaptive bottom-up merge sort
            int j = 16;
            if (buildRuns(array, a, b, j)) return;
            int[] tmp = Writes.createExternalArray(len / 2);
            int i;
            for (; j < len; j *= 2) {
                for (i = a; i + 2 * j <= b; i += 2 * j)
                    smartMerge(array, tmp, i, i + j, i + 2 * j);
                if (i + j < b)
                    smartMerge(array, tmp, i, i + j, b);
            }
            Writes.deleteExternalArray(tmp);
            return;
        }
        int j = 16;
        int bLen;
        for (bLen = j; bLen * bLen < len; bLen *= 2);
        int tLen = len / bLen;
        if (buildRuns(array, a, b, j)) return;
        int[] buf = Writes.createExternalArray(bLen);
        int i;
        for (; 4 * j <= bLen; j *= 4) {
            for(i = a; i+2*j < b; i += 4*j)
                pingPongMerge(array, buf, i, i+j, i+2*j, Math.min(i+3*j, b), Math.min(i+4*j, b));
            if (i + j < b)
                smartMerge(array, buf, i, i + j, b);
        }
        for (; j <= bLen; j *= 2) {
            for (i = a; i + 2 * j <= b; i += 2 * j)
                smartMerge(array, buf, i, i + j, i + 2 * j);
            if (i + j < b)
                smartMerge(array, buf, i, i + j, b);
        }
        int[] tags = Writes.createExternalArray(tLen);
        for (; j < len; j *= 2) {
            for (i = a; i + 2 * j <= b; i += 2 * j)
                smartBlockMerge(array, buf, tags, i, i + j, i + 2 * j, bLen);
            if (i + j < b)
                smartBlockMerge(array, buf, tags, i, i + j, b, bLen);
        }
        Writes.deleteExternalArray(tags);
        Writes.deleteExternalArray(buf);
    }


    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        blockMergeSort(array, 0, sortLength);

    }

}
