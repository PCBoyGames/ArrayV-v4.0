package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

Coded for ArrayV by Kiriko-chan

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Kiriko-chan
 *
 */
public final class LazierQuickSort extends Sort {

    public LazierQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lazier Stable Quick");
        this.setRunAllSortsName("Lazier Stable Quick Sort");
        this.setRunSortName("Lazier Quicksort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    class PivotPair {
        public int l, r;

        public PivotPair(int l, int r) {
            this.l = l;
            this.r = r;
        }
    }

    public static int floorLog(int n) {
        int log = 0;
        while ((n >>= 1) != 0) ++log;
        return log;
    }

    protected int medianOf3(int[] array, int v0, int v1, int v2) {
        int[] t = new int[2];
        int val;
        val = (Reads.compareIndices(array, v0, v1, 1, true) > 0)? 1 : 0;
        t[0]  = val; t[1] = val^1;
        val = (Reads.compareIndices(array, v0, v2, 1, true) > 0)? 1 : 0;
        t[0] += val;
        if (t[0] == 1)
            return v0;
        val = (Reads.compareIndices(array, v1, v2, 1, true) > 0)? 1 : 0;
        t[1] += val;
        return t[1] == 1 ? v1 : v2;
    }

    protected int medianOf9(int[] array, int a, int b) {
        int v0, v1, v2, div = (b - a)/9;

        v0 = this.medianOf3(array, a,        a+div*1,  a+div*2);
        v1 = this.medianOf3(array, a+div*3,  a+div*4,  a+div*5);
        v2 = this.medianOf3(array, a+div*6,  a+div*7,  a+div*8);

        return this.medianOf3(array, v0, v1, v2);
    }

    protected int leftBinSearch(int[] array, int a, int b, int val) {
        while(a < b) {
            int m = a+(b-a)/2;

            if(Reads.compareValues(val, array[m]) <= 0)
                b = m;
            else
                a = m+1;
        }
        return a;
    }
    protected int rightBinSearch(int[] array, int a, int b, int val) {
        while(a < b) {
            int m = a+(b-a)/2;

            if(Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m+1;
        }
        return a;
    }

    protected int rightExpSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while(b-i >= a && Reads.compareValues(val, array[b-i]) < 0) i *= 2;

        return this.rightBinSearch(array, Math.max(a, b-i+1), b-i/2, val);
    }

    protected int leftBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
            i *= 2;

        return this.rightBinSearch(array, a + i / 2, Math.min(b, a - 1 + i), val);
    }

    protected int rightBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0)
            i *= 2;

        return this.leftBinSearch(array, Math.max(a, b - i + 1), b - i / 2, val);
    }

    protected void insertionSort(int[] array, int a, int b) {
        int i = a + 1;
        if(Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
            while(i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) i++;
            Writes.reversal(array, a, i - 1, 1.0, true, false);
        }
        else while(i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        Highlights.clearMark(2);
        for(; i < b; i++) {
            insertTo(array, i, rightExpSearch(array, a, i, array[i]));
        }
    }

    protected void rotate(int[] array, int a, int m, int b) {
        IndexedRotations.holyGriesMills(array, a, m, b, 1.0, true, false);
    }

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        int i = a, j = m, k;

        while(i < j && j < b){
            if(Reads.compareValues(array[i], array[j]) == 1) {
                k = this.leftBinSearch(array, j+1, b, array[i]);
                this.rotate(array, i, j, k);

                i += k-j;
                j = k;
            }
            else
                i++;
        }
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        int i = m-1, j = b-1, k;

        while(j > i && i >= a){
            if(Reads.compareValues(array[i], array[j]) > 0) {
                k = this.rightBinSearch(array, a, i, array[j]);
                this.rotate(array, k, i+1, j+1);

                j -= (i+1)-k;
                i = k-1;
            }
            else
                j--;
        }
    }

    public void smartInPlaceMerge(int[] array, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0)
            return;
        a = this.leftBoundSearch(array, a, m, array[m]);
        b = this.rightBoundSearch(array, m, b, array[m - 1]);
        if (Reads.compareIndices(array, a, b - 1, 0.0, true) > 0)
            rotate(array, a, m, b);
        else if (b - m < m - a)
            inPlaceMergeBW(array, a, m, b);
        else
            inPlaceMergeFW(array, a, m, b);
    }

    protected void buildRuns(int[] array, int a, int b, int mRun) {
        int i = a + 1, j = a;
        while (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 1, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) > 0) i++;
                Writes.reversal(array, j, i - 1, 1, true, false);
            } else
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;

            if (i < b)
                j = i - (i - j - 1) % mRun - 1;

            while (i - j < mRun && i < b) {
                this.insertTo(array, i, this.rightBinSearch(array, j, i, array[i]));
                i++;
            }
            j = i++;
        }
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        boolean change = false;
        while (a > b) {
            Writes.write(array, a, array[--a], 0.125, true, false);
            change = true;
        }
        if (change) Writes.write(array, b, temp, 0.125, true, false);
    }

    public void lazyStableSort(int[] array, int start, int end) {
        int mRun = end - start;
        for (; mRun >= 32; mRun = (mRun + 1) / 2);
        buildRuns(array, start, end, mRun);
        for (int i, j = mRun; j < (end - start); j *= 2) {
            for (i = start; i + 2 * j <= end; i += 2 * j)
                smartInPlaceMerge(array, i, i + j, i + 2 * j);
            if (i + j < end)
                smartInPlaceMerge(array, i, i + j, end);
        }

    }

    protected PivotPair partition(int[] array, int a, int b, int piv) {
        int l = a, r = a;
        for(int i = a; i < b; i++) {
            int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
            if(cmp < 0) {
                insertTo(array, i, l++);
                r++;
            } else if(cmp == 0)
                insertTo(array, i, r++);
        }
        return new PivotPair(l, r);
    }

    protected void quickSort(int[] array, int a, int b, int depth) {
        while(b - a > 16) {
            if(depth == 0) {
                lazyStableSort(array, a, b);
                return;
            }
            int p = medianOf3(array, a, a + (b - a) / 2, b - 1);
            PivotPair m = partition(array, a, b, array[p]);
            int l = m.l - a, r = b - m.r, eqCnt = m.r - m.l;
            if(eqCnt == b - a)
                return;
            if((l == 0 || r == 0) || (l / r >= 16 || r / l >= 16)) {
                p = medianOf9(array, a, b);
                m = partition(array, a, b, array[p]);
                l = m.l - a;
                r = b - m.r;
                eqCnt = m.r - m.l;
                if(eqCnt == b - a)
                    return;
            }
            if(l > r) {
                quickSort(array, m.r, b, --depth);
                b = m.l;
            } else {
                quickSort(array, a, m.l, --depth);
                a = m.r;
            }
        }
        insertionSort(array, a, b);
    }

    public void customSort(int[] array, int a, int b) {
        quickSort(array, a, b, 2 * floorLog(b - a));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength, 2 * floorLog(sortLength));

    }

}
