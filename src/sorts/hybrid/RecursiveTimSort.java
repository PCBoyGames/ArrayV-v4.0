package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author aphitorite
 * @author Kiriko-chan
 *
 */
public class RecursiveTimSort extends Sort {

    public RecursiveTimSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Recursive Tim");
        this.setRunAllSortsName("Recursive Tim Sort");
        this.setRunSortName("Recursive Timsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int M = 7;

    private int highlight = 0;

    private int leftBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a+(b-a)/2;
            Highlights.markArray(2, this.highlight+m);
            Delays.sleep(0.5);

            if (Reads.compareValues(val, array[m]) <= 0)
                b = m;
            else
                a = m+1;
        }

        return a;
    }

    private int rightBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a+(b-a)/2;
            Highlights.markArray(2, this.highlight+m);
            Delays.sleep(0.5);

            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m+1;
        }

        return a;
    }

    private int leftExpSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (a-1+i < b && Reads.compareValues(val, array[a-1+i]) > 0) i *= 2;

        return this.leftBinSearch(array, a+i/2, Math.min(b, a-1+i), val);
    }

    private int rightExpSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b-i >= a && Reads.compareValues(val, array[b-i]) < 0) i *= 2;

        return this.rightBinSearch(array, Math.max(a, b-i+1), b-i/2, val);
    }

    private int leftBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (a-1+i < b && Reads.compareValues(val, array[a-1+i]) >= 0) i *= 2;

        return this.rightBinSearch(array, a+i/2, Math.min(b, a-1+i), val);
    }

    private int rightBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b-i >= a && Reads.compareValues(val, array[b-i]) <= 0) i *= 2;

        return this.leftBinSearch(array, Math.max(a, b-i+1), b-i/2, val);
    }

    private void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);

        if (a > b) {
            int temp = array[a];

            do Writes.write(array, a, array[--a], 0.25, true, false);
            while (a > b);

            Writes.write(array, b, temp, 0.25, true, false);
        }
    }

    protected void insertion(int[] array, int a, int b) {
        int i = a + 1;
        if (i >= b)
            return;
        if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) i++;
            if (i - a >= 4)
                Writes.reversal(array, a, i - 1, 1, true, false);
            else
                Writes.swap(array, a, i - 1, 1, true, false);
        }
        else while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        Highlights.clearMark(2);
        for (; i < b; i++)
            insertTo(array, i, rightExpSearch(array, a, i, array[i]));
    }

    //galloping mode code refactored from TimSorting.java
    private void mergeFW(int[] array, int[] tmp, int a, int m, int b) {
        int len1 = m-a, t = a;
        Highlights.clearMark(2);
        Writes.arraycopy(array, a, tmp, 0, len1, 1, true, true);
        int i = 0, mGallop = M, l = 0, r = 0;
        while (true) {
            do {
                if (Reads.compareValues(tmp[i], array[m]) <= 0) {
                    Writes.write(array, a++, tmp[i++], 1, true, false);
                    l++;
                    r = 0;
                    if (i == len1) return;
                }
                else {
                    Highlights.markArray(2, m);
                    Writes.write(array, a++, array[m++], 1, true, false);
                    r++;
                    l = 0;
                    if (m == b) {
                        while (i < len1) Writes.write(array, a++, tmp[i++], 1, true, false);
                        return;
                    }
                }
            }
            while ((l | r) < mGallop);
            do {
                l = this.leftExpSearch(array, m, b, tmp[i])-m;
                for (int j = 0; j < l; j++)
                    Writes.write(array, a++, array[m++], 1, true, false);
                Writes.write(array, a++, tmp[i++], 1, true, false);
                if (i == len1) return;
                else if (m == b) {
                    while (i < len1) Writes.write(array, a++, tmp[i++], 1, true, false);
                    return;
                }
                this.highlight = t;
                r = this.leftBoundSearch(tmp, i, len1, array[m])-i;
                this.highlight = 0;
                for (int j = 0; j < r; j++)
                    Writes.write(array, a++, tmp[i++], 1, true, false);
                Writes.write(array, a++, array[m++], 1, true, false);
                if (i == len1) return;
                else if (m == b) {
                    while (i < len1) Writes.write(array, a++, tmp[i++], 1, true, false);
                    return;
                }
                mGallop--;
            }
            while ((l | r) >= M);
            if (mGallop < 0) mGallop = 0;
            mGallop += 2;
        }
    }
    private void mergeBW(int[] array, int[] tmp, int a, int m, int b) {
        int len2 = b-m, t = a;
        Highlights.clearMark(2);
        Writes.arraycopy(array, m, tmp, 0, len2, 1, true, true);
        int i = len2-1, mGallop = M, l = 0, r = 0;
        m--;
        while (true) {
            do {
                if (Reads.compareValues(tmp[i], array[m]) >= 0) {
                    Writes.write(array, --b, tmp[i--], 1, true, false);
                    l++;
                    r = 0;
                    if (i < 0) return;
                }
                else {
                    Highlights.markArray(2, m);
                    Writes.write(array, --b, array[m--], 1, true, false);
                    r++;
                    l = 0;
                    if (m < a) {
                        while (i >= 0) Writes.write(array, --b, tmp[i--], 1, true, false);
                        return;
                    }
                }
            }
            while ((l | r) < mGallop);
            do {
                l = (m+1)-this.rightExpSearch(array, a, m+1, tmp[i]);
                for (int j = 0; j < l; j++)
                    Writes.write(array, --b, array[m--], 1, true, false);
                Writes.write(array, --b, tmp[i--], 1, true, false);
                if (i < 0) return;
                else if (m < a) {
                    while (i >= 0) Writes.write(array, --b, tmp[i--], 1, true, false);
                    return;
                }
                this.highlight = t;
                r = (i+1)-this.rightBoundSearch(tmp, 0, i+1, array[m]);
                this.highlight = 0;
                for (int j = 0; j < r; j++)
                    Writes.write(array, --b, tmp[i--], 1, true, false);
                Writes.write(array, --b, array[m--], 1, true, false);
                if (i < 0) return;
                else if (m < a) {
                    while (i >= 0) Writes.write(array, --b, tmp[i--], 1, true, false);
                    return;
                }
            }
            while ((l | r) >= M);
            if (mGallop < 0) mGallop = 0;
            mGallop += 2;
        }
    }

    public void smartMerge(int[] array, int[] tmp, int a, int m, int b) {
        if (Reads.compareValues(array[m-1], array[m]) <= 0) return;
        a = this.leftBoundSearch(array, a, m, array[m]);
        b = this.rightBoundSearch(array, m, b, array[m-1]);
        if (b-m < m-a) this.mergeBW(array, tmp, a, m, b);
        else          this.mergeFW(array, tmp, a, m, b);
    }

    public void sort(int[] array, int[] temp, int a, int b) {
        if (b - a < 32) {
            insertion(array, a, b);
            return;
        }
        int m = a + (b - a) / 2;
        sort(array, temp, a, m);
        sort(array, temp, m, b);
        smartMerge(array, temp, a, m, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        int[] temp = Writes.createExternalArray(sortLength / 2);
        sort(array, temp, 0, sortLength);
        Writes.deleteExternalArray(temp);

    }

}
