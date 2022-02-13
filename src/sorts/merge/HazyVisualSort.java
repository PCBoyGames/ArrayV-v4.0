package sorts.merge;

import main.ArrayVisualizer;
import sorts.golf.NilSort;
import sorts.templates.Sort;

public class HazyVisualSort extends Sort {
    public HazyVisualSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Hazy Visual");
        this.setRunAllSortsName("Hazy Visualsort");
        this.setRunSortName("Hazy Visualsort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private NilSort fallback;

    private int msqrt(int v) { // approx sqrt
        int k = v;
        while(k/(v/=2)<v);
        return v;
    }

    private int mfcrt(int v) {
        int k = v;
        while(k/((v/=2)/msqrt(v))<v);
        return v;
    }

    private void bm(int[] array, int p0, int l0, int p1, int l1) {
        int j = p0 + l0 - 1, k = p1 + l1 - 1,
            l = p1 + l0 + l1;
        while(j >= p0 && k >= p1) {
            if(Reads.compareValues(array[j], array[k]) > 0) {
                Writes.swap(array, j--, --l, 1, true, false);
            } else {
                Writes.swap(array, k--, --l, 1, true, false);
            }
        }
        while(j >= p0)
            Writes.swap(array, j--, --l, 1, true, false);
        while(k >= p1)
            Writes.swap(array, k--, --l, 1, true, false);
    }

    private void am(int[] array, int p0, int l0, int p1, int l1) {
        for(int j=l0-1; j>=0; j--) {
            Writes.swap(array, p1+l1-l0+j, p0+j, 1, true, false);
        }
        int j = p1 + l1 - l0, k = p1, l = p0;
        while(j < p1 + l1 && k < p1 + l1 - l0) {
            if(Reads.compareValues(array[j], array[k]) <= 0)
                Writes.swap(array, j++, l++, 1, true, false);
            else
                Writes.swap(array, k++, l++, 1, true, false);
        }
        while(j < p1 + l1)
            Writes.swap(array, j++, l++, 1, true, false);
        while(k < p1 + l1 - l0)
            Writes.swap(array, k++, l++, 1, true, false);
        sort(array, p1 + l1 - l0, p1 + l1);
        fallback.grailMergeWithoutBuffer(array, p0, l1, l0);

    }

    private void abm(int[] array, int p0, int l0, int p1, int l1) {
        for(int j=l1-1; j>=0; j--) {
            Writes.swap(array, p1+j, p0+j, 1, true, false);
        }
        bm(array, p0, l1, p0+l1, l0-l1);
        sort(array, p0, p0 + l1);
        fallback.grailMergeWithoutBuffer(array, p0, l1, l0);
    }
    private void sort(int[] array, int start, int end) {
        if(end-start < 128) {
            fallback.runZero(array, start, end);
            return;
        }
        int l = end-start,
            v = mfcrt(l),
            lmod = l-(l%v);
        fallback.runZero(array, start, start+v);
        fallback.runZero(array, start+v, start+2*v);
        for(int i=v; i<l-2*v; i+=v) {
            bm(array, start, v, start+v, i);
            fallback.runZero(array, start, start+v);
        }
        am(array, start, v, start+v, lmod-v);
        if(l%v != 0) {
            sort(array, start+lmod, end);
            abm(array, start, lmod, start+lmod, end-lmod-start);
        }
    }
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        fallback = new NilSort(arrayVisualizer);
        sort(array, 0, sortLength);
    }
}