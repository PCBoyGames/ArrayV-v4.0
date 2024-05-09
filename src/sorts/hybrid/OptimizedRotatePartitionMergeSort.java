package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class OptimizedRotatePartitionMergeSort extends Sort {

    public OptimizedRotatePartitionMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Rotate Partition Merge");
        this.setRunAllSortsName("Optimized Rotate Partition Merge Sort");
        this.setRunSortName("Optimized Rotate Partition Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the external buffer size (0 for in-place):", 64);
    }

    @Override
    public int validateAnswer(int answer) {
        return Math.max(answer, 0);
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

    protected void rotateNoBuf(int[] array, int a, int m, int b) {
        if (a >= m || m >= b) return;
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

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b)
            Writes.write(array, b, temp, 0.5, true, false);
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

    protected int minExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0) i *= 2;
        else while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0) i *= 2;
        return binSearch(array, a + i / 2, Math.min(b, a - 1 + i), val, left);
    }

    protected int maxExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0) i *= 2;
        else while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        return binSearch(array, Math.max(a, b - i + 1), b - i / 2, val, left);
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

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = minExpSearch(array, m, b, array[a], true);
            rotateNoBuf(array, a, m, i);
            int t = i - m;
            m = i;
            a += t + 1;
            if (m >= b) break;
            a = minExpSearch(array, a, m, array[m], false);
        }
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        while (b > m && m > a) {
            int i = maxExpSearch(array, a, m, array[b - 1], false);
            rotateNoBuf(array, i, m, b);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a) break;
            b = maxExpSearch(array, m, b, array[m - 1], true);
        }
    }

    protected void merge(int[] array, int[] buf, int a, int m, int b) {
        Highlights.clearMark(2);
        if (m - a > b - m)
            mergeBWExt(array, buf, a, m, b);
        else
            mergeFWExt(array, buf, a, m, b);
    }

    protected void inPlaceMerge(int[] array, int a, int m, int b) {
        Highlights.clearMark(2);
        if (b - m < m - a)
            inPlaceMergeBW(array, a, m, b);
        else
            inPlaceMergeFW(array, a, m, b);
    }

    public void rotateMerge(int[] array, int[] buf, int a, int m, int b) {
        if (a >= m || m >= b || (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0)) return;
        a = minExpSearch(array, a, m, array[m], false);
        b = maxExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, buf, a, m, b);
            return;
        }
        int lenA = m - a, lenB = b - m;
        if (Math.min(lenA, lenB) <= 8) {
            inPlaceMerge(array, a, m, b);
            return;
        }
        if (buf != null && Math.min(lenA, lenB) <= buf.length) {
            merge(array, buf, a, m, b);
            return;
        }
        int c = (lenA + lenB) / 2;
        if (lenB < lenA) { // partitions c largest elements
            int r1 = 0, r2 = lenB;
            while (r1 < r2) {
                int ml = r1 + (r2 - r1) / 2;
                if (Reads.compareValues(array[m - (c - ml)], array[b - ml - 1]) > 0) r2 = ml;
                else r1 = ml + 1;
            }
            // [lenA-(c-r1)][c-r1][lenB-r1][r1]
            // [lenA-(c-r1)][lenB-r1][c-r1][r1]
            this.rotate(array, buf, m - (c - r1), m, b - r1);
            int m1 = b - c;
            this.rotateMerge(array, buf, m1, b - r1, b);
            this.rotateMerge(array, buf, a, m1 - (lenB - r1), m1);
        } else { // partitions c smallest elements
            int r1 = 0, r2 = lenA;
            while (r1 < r2) {
                int ml = r1 + (r2 - r1) / 2;
                if (Reads.compareValues(array[a + ml], array[m + (c - ml) - 1]) > 0) r2 = ml;
                else r1 = ml + 1;
            }
            // [r1][lenA-r1][c-r1][lenB-(c-r1)]
            // [r1][c-r1][lenA-r1][lenB-(c-r1)]
            this.rotate(array, buf, a + r1, m, m + (c - r1));
            int m1 = a + c;
            this.rotateMerge(array, buf, a, a + r1, m1);
            this.rotateMerge(array, buf, m1, m1 + (lenA - r1), b);
        }
    }

    public int findRun(int[] array, int start, int end) {
        int i = start;
        if (i + 1 >= end) return i + 1;
        boolean lessunique = false;
        boolean different = false;
        int cmp = Reads.compareIndices(array, i, i + 1, 0.5, true);
        while (cmp == 0 && i + 1 < end) {
            lessunique = true;
            i++;
            if (i + 1 < end) cmp = Reads.compareIndices(array, i, i + 1, 0.5, true);
        }
        if (cmp > 0) {
            while (cmp >= 0 && i + 1 < end) {
                if (cmp == 0) lessunique = true;
                else different = true;
                i++;
                if (i + 1 < end) cmp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            }
            if (i > start && different) {
                if (lessunique) stableSegmentReversal(array, start, i);
                else if (i < start + 3) Writes.swap(array, start, i, 0.75, true, false);
                else Writes.reversal(array, start, i, 0.75, true, false);
            }
        } else {
            while (cmp <= 0 && i < end) {
                i++;
                if (i + 1 < end) cmp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            }
        }
        return i + 1;
    }

    protected boolean buildRuns(int[] array, int a, int b, int mRun) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            i = findRun(array, j, b);
            if (i < b) {
                noSort = false;
                j = i - (i - j - 1) % mRun - 1;
            }
            while (i - j < mRun && i < b) {
                insertTo(array, i, maxExpSearch(array, j, i, array[i], false));
                i++;
            }
            j = i++;
        }
        return noSort;
    }

    public void mergeSortWithBuf(int[] array, int[] buf, int a, int b) {
        int j = b - a;
        while (j >= 32) j = (j - 1) / 2 + 1;
        if (buildRuns(array, a, b, j)) return;
        for (; j < b - a; j *= 2) {
            for (int i = a; i+j < b; i += 2*j)
                rotateMerge(array, buf, i, i + j, Math.min(i + 2 * j, b));
        }
    }

    public void mergeSort(int[] array, int a, int b, int auxSize) {
        int[] buf = (auxSize > 0) ? Writes.createExternalArray(auxSize) : null;
        mergeSortWithBuf(array, buf, a, b);
        if (buf != null) Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int auxSize) {
        mergeSort(array, 0, sortLength, auxSize);

    }

}
