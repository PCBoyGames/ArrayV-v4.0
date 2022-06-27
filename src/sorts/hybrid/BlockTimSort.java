package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite and Distray

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 * @author Distray
 *
 */
public final class BlockTimSort extends Sort {

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

    protected void insertTo(int[] array, int a, int b, double sleep, boolean aux) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], sleep, true, aux);
        if (a != b)
            Writes.write(array, b, temp, sleep, true, aux);
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

    // Conjoined Gries-Mills rotations (by Distray)
    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        int lenA = m - a, lenB = b - m, pos = a;
        int end = pos + lenA + lenB;
        while (lenA > 0 && lenB > 0)
            if (lenA < lenB) {
                for (int i = 0; i < lenA; i++) {
                    int t = array[pos + i], j = pos + i + lenA;
                    for (; j < end; j += lenA)
                        Writes.write(array, j - lenA, array[j], 1, true, false);
                    Writes.write(array, j - lenA, t, 1, true, false);
                }
                pos += lenB;
                lenB %= lenA;
                lenA -= lenB;
            } else {
                for (int i = 0; i < lenB; i++) {
                    int t = array[pos + i + lenA], j = pos + i + lenA - lenB;
                    for (; j >= pos; j -= lenB)
                        Writes.write(array, j + lenB, array[j], 1, true, false);
                    Writes.write(array, j + lenB, t, 1, true, false);
                }
                end = pos + lenB;
                lenA %= lenB;
                lenB -= lenA;
            }
    }

    protected int leftBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) <= 0)
                b = m;
            else
                a = m + 1;
        }
        return a;
    }

    protected int rightBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m + 1;
        }
        return a;
    }

    protected int leftExpSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0)
            i *= 2;
        return leftBinSearch(array, a + i / 2, Math.min(b, a - 1 + i), val);
    }

    protected int rightExpSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
            i *= 2;
        return rightBinSearch(array, Math.max(a, b - i + 1), b - i / 2, val);
    }

    protected int leftBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
            i *= 2;
        return rightBinSearch(array, a + i / 2, Math.min(b, a - 1 + i), val);
    }

    protected int rightBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0)
            i *= 2;
        return leftBinSearch(array, Math.max(a, b - i + 1), b - i / 2, val);
    }

    protected boolean buildRuns(int[] array, int a, int b, int mRun) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 1, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) > 0) i++;
                if (i - j < 4)
                    Writes.swap(array, j, i - 1, 1.0, true, false);
                else
                    Writes.reversal(array, j, i - 1, 1.0, true, false);
            } else
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;
            if (i < b) {
                noSort = false;
                j = i - (i - j - 1) % mRun - 1;
            }
            while (i - j < mRun && i < b) {
                insertTo(array, i, rightExpSearch(array, j, i, array[i]), 0.5, false);
                i++;
            }
            j = i++;
        }
        return noSort;
    }

    protected void insertSort(int[] array, int a, int b) {
        int i = a + 1;
        if (i >= b)
            return;
        if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) i++;
            if (i - a < 4)
                Writes.swap(array, a, i - 1, 1.0, true, false);
            else
                Writes.reversal(array, a, i - 1, 1.0, true, false);
        } else
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        Highlights.clearMark(2);
        for (; i < b; i++)
            insertTo(array, i, rightExpSearch(array, a, i, array[i]), 0.5, false);
    }

    protected boolean checkReverseBounds(int[] array, int a, int m, int b) {
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return true;
        }
        return false;
    }

    protected boolean boundCheck(int[] array, int a, int m, int b) {
        return Reads.compareValues(array[m - 1], array[m]) <= 0
            || checkReverseBounds(array, a, m, b);
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
        int i = a, j = m, k;
        while (i < j && j < b)
            if (Reads.compareValues(array[i], array[j]) > 0) {
                k = leftExpSearch(array, j + 1, b, array[i]);
                rotate(array, i, j, k);
                i += k - j;
                j = k;
            } else
                i++;
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        int i = m - 1, j = b - 1, k;
        while (j > i && i >= a)
            if (Reads.compareValues(array[i], array[j]) > 0) {
                k = rightExpSearch(array, a, i, array[j]);
                rotate(array, k, i + 1, j + 1);
                j -= (i + 1) - k;
                i = k - 1;
            } else
                j--;
    }

    protected void merge(int[] array, int[] buf, int a, int m, int b) {
        Highlights.clearMark(2);
        if (buf == null || Math.min(m - a, b - m) <= 8) {
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
                if (k < bCnt && min == k - 1)
                    k++;
            }
            if (min == mKey)
                mKey = j;
        }
        return mKey;
    }

    protected int mergeBlocks(int[] array, int a, int m, int b, int p) {
        int i = a, j = m;
        while(i < m && j < b)
            if(Reads.compareIndices(array, i, j, 1.0, true) <= 0)
                Writes.write(array, p++, array[i++], 1.0, true, false);
            else
                Writes.write(array, p++, array[j++], 1.0, true, false);
        if(i > p)
            shiftFWExt(array, p, i, m);
        return j;
    }

    // same as mergeBlocks() except reverses equal items order
    protected int mergeBlocksRev(int[] array, int a, int m, int b, int p) {
        int i = a, j = m;
        while(i < m && j < b)
            if(Reads.compareIndices(array, i, j, 1.0, true) < 0)
                Writes.write(array, p++, array[i++], 1.0, true, false);
            else
                Writes.write(array, p++, array[j++], 1.0, true, false);
        if(i > p)
            shiftFWExt(array, p, i, m);
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
        for (int k = 0; k < bCnt; k++)
            Writes.write(tags, k, k, 0, true, true);
        insertTo(tags, 0, lCnt - 1, 0.5, true);
        int mKey = blockSelect(array, tags, i, 1, bLen - 1, lCnt, bCnt, bLen);
        boolean frag = true;
        while(l < lCnt && r < bCnt) {
            if(frag) {
                do {
                    j += bLen;
                    l++;
                    key++;
                } while (l < lCnt && getSubarray(tags, key, mKey));
                if(l == lCnt) {
                    i = mergeBlocks(array, i, j, b, i - bLen);
                    mergeFromBuf(array, buf, i - bLen, i, b, bLen);
                } else
                    i = mergeBlocks(array, i, j, j + bLen - 1, i - bLen);
            } else {
                do {
                    j += bLen;
                    r++;
                    key++;
                } while (r < bCnt && !getSubarray(tags, key, mKey));
                if(r == bCnt) {
                    shiftFWExt(array, i - bLen, i, b);
                    Writes.arraycopy(buf, 0, array, b - bLen, bLen, 1, true, false);
                } else
                    i = mergeBlocksRev(array, i, j, j + bLen - 1, i - bLen);
            }
            frag = !frag;
        }
    }

    protected void smartMerge(int[] array, int[] buf, int a, int m, int b) {
        if (boundCheck(array, a, m, b))
            return;
        a = leftBoundSearch(array, a, m, array[m]);
        b = rightBoundSearch(array, m, b, array[m - 1]);
        if (checkReverseBounds(array, a, m, b))
            return;
        merge(array, buf, a, m, b);
    }

    protected void smartBlockMerge(int[] array, int[] buf, int[] tags, int a, int m, int b, int bLen) {
        if (boundCheck(array, a, m, b))
            return;
        int s = leftBoundSearch(array, a, m, array[m]);
        b = rightBoundSearch(array, m, b, array[m - 1]);
        if (checkReverseBounds(array, s, m, b))
            return;
        if (Math.min(m - s, b - m) <= bLen)
            merge(array, buf, s, m, b);
        else {
            s -= (s - a) % bLen;
            blockMerge(array, buf, tags, s, m, b, bLen);
        }
    }

    public void blockMergeSort(int[] array, int a, int b) {
        int len = b - a;
        if (len < 128) {
            if (len < 32)
                insertSort(array, a, b);
            else { // adaptive bottom-up merge sort
                int j = getMinLevel(len);
                if (buildRuns(array, a, b, j))
                    return;
                int[] tmp = Writes.createExternalArray(len / 2);
                int i;
                for (; j < len; j *= 2) {
                    for (i = a; i + 2 * j <= b; i += 2 * j)
                        smartMerge(array, tmp, i, i + j, i + 2 * j);
                    if (i + j < b)
                        smartMerge(array, tmp, i, i + j, b);
                }
                Writes.deleteExternalArray(tmp);
            }
            return;
        }
        int j = getMinLevel(len);
        int bLen;
        for (bLen = j; bLen * bLen < len; bLen *= 2);
        int tLen = len / bLen - 2;
        if (buildRuns(array, a, b, j))
            return;
        int[] buf = Writes.createExternalArray(bLen);
        int i;
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
