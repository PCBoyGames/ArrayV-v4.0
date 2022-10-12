package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

Coded for ArrayV by Ayako-chan
in collaboration with Distray

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author Distray
 *
 */
public final class GhastSort extends Sort {

    public GhastSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Ghast");
        this.setRunAllSortsName("Ghast Sort");
        this.setRunSortName("Ghast sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
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
            while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0)
                i *= 2;
        else
            while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
                i *= 2;
        int a1 = a + i / 2, b1 = Math.min(b, a - 1 + i);
        return binSearch(array, a1, b1, val, left);
    }

    protected int rightExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left)
            while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0)
                i *= 2;
        else
            while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
                i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        return binSearch(array, a1, b1, val, left);
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
                insertTo(array, i, rightExpSearch(array, j, i, array[i], false));
                i++;
            }
            j = i++;
        }
        return noSort;
    }
    
    public void mergeFW(int[] array, int start, int mid, int end) {
        if (start >= mid || mid >= end)
            return;
        int l = start, r = mid;
        while (r < end && l < mid) {
            while (l < mid && Reads.compareValues(array[l], array[r]) <= 0) {
                l++;
            }
            if (l >= mid)
                return;
            int z = l;
            while (l < mid && r < end && Reads.compareValues(array[l], array[r]) > 0) {
                l++;
                r++;
            }
            IndexedRotations.juggling(array, z, mid, r, 1, true, false);
            merge(array, z, l, l + (r - mid));
            mid = r;
        }
    }

    public void mergeBW(int[] array, int start, int mid, int end) {
        if (start >= mid || mid >= end)
            return;
        int l = mid - 1, r = end - 1;
        while (r >= mid && l >= start) {
            while (r >= mid && Reads.compareValues(array[l], array[r]) <= 0) {
                r--;
            }
            if (r < mid)
                return;
            int z = r;
            while (l >= start && r >= mid && Reads.compareValues(array[l], array[r]) >= 0) {
                l--;
                r--;
            }
            IndexedRotations.juggling(array, l + 1, mid, z + 1, 1, true, false);
            merge(array, r - (mid - l) + 1, r + 1, z + 1);
            mid = l + 1;
        }
    }

    public void merge(int[] array, int left, int mid, int right) {
        if (Reads.compareIndices(array, mid - 1, mid, 0.0, true) <= 0)
            return;
        left = leftExpSearch(array, left, mid, array[mid], false);
        right = rightExpSearch(array, mid, right, array[mid - 1], true);
        if (Reads.compareIndices(array, left, right - 1, 0.0, true) > 0) {
            IndexedRotations.juggling(array, left, mid, right, 1, true, false);
            return;
        }
        if (mid - left <= right - mid) {
            mergeFW(array, left, mid, right);
        } else {
            mergeBW(array, left, mid, right);
        }
    }
    
    public void mergeSort(int[] array, int a, int b) {
        int j = 16;
        if (buildRuns(array, a, b, j))
            return;
        for (; j < b - a; j *= 2)
            for (int i = a; i + j < b; i += 2 * j)
                merge(array, i, i + j, Math.min(i + 2 * j, b));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
