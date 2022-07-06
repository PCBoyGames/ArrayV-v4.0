package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

final public class OnlineStacklessRotateMergeSort extends Sort {
    public OnlineStacklessRotateMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Online Stackless Rotate Merge");
        this.setRunAllSortsName("Online Stackless Rotate Merge Sort");
        this.setRunSortName("Online Stackless Rotate Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private int binSearch(int[] array, int l, int r, int k, int b) {
        while (l < r) {
            int m = l + (r - l) / 2;
            if (Reads.compareIndices(array, k, m, 1, true) < b)
                r = m;
            else
                l = m + 1;
        }
        return l;
    }
    private void merge(int[] array, int a, int z, int b) {
        if (Reads.compareIndices(array, z-1, z, 5, true) <= 0)
            return;
        int j, k, l, s, q = b - a, r, c;
        do {
            s = r = 0;
            j = k = a;
            c = z == a ? 1 : 0;
            for (int i = z - (1 - c); i < b - 1; i++) {
                if (c == 0 || Reads.compareIndices(array, i, i+1, 1, true) > 0) {
                    k = binSearch(array, l=i+1, Math.min(l+q, b), i, 1);
                    j = binSearch(array, j, i, l, 0);
                    int m = l-j, n = k-l, o, p, t = Math.max(m, n);
                    if (r < t)
                        r = t;
                    int h = Math.min(m, n);
                    if (h > s) s = h;
                    if (m > n ^ h == 1) {
                        o = j+(l-j)/2;
                        p = binSearch(array, l, k, o, 1);
                    } else {
                        p = l+(k-l)/2;
                        o = binSearch(array, j, l, p++, 0);
                    }
                    IndexedRotations.cycleReverse(array, o, l, p, 1, true, false);
                    j = i = k;
                    if (c++ == 0) z = o;
                }
            }
            q = r;
        } while (s > 1);
    }
    public void sort(int[] array, int a, int b) {
        int zl = 32 - Integer.numberOfLeadingZeros(b-a-1), z = 1;
        while (zl-- > 0) z *= 3;
        stack:
        for (int i=2; i<z;) {
            int l = a, r = b;
            for (int x=z/3; x>0; x/=3) {
                switch((i/x)%3) {
                    case 0: r = l+(r-l)/2; break;
                    case 1: l += (r-l)/2; break;
                    case 2: merge(array, l, l+(r-l)/2, r); i += x; continue stack;
                }
            }
            i+=2;
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        sort(array, 0, length);
    }
}