package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

Coded for ArrayV by Kiriko-chan
in collaboration with aphitorite

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Kiriko-chan
 * @author aphitorite
 *
 */
public final class NaturalRotateMergeSort extends Sort {

    public NaturalRotateMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Natural Rotate Merge");
        this.setRunAllSortsName("Natural Rotate Merge Sort");
        this.setRunSortName("Natural Rotate Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void rotate(int[] array, int a, int m, int b) {
        IndexedRotations.cycleReverse(array, a, m, b, 1.0, true, false);
    }

    protected int binarySearch(int[] array, int a, int b, int value, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            boolean comp = left
                    ? Reads.compareValues(value, array[m]) <= 0
                    : Reads.compareValues(value, array[m]) < 0;
            if (comp)
                b = m;
            else
                a = m + 1;
        }
        return a;
    }

    public void merge(int[] array, int a, int m, int b) {
        if (m - a < 1 || b - m < 1)
            return;
        int m1, m2, m3;
        if (m - a >= b - m) {
            m1 = a + (m - a) / 2;
            m2 = binarySearch(array, m, b, array[m1], true);
            m3 = m1 + (m2 - m);
        } else {
            m2 = m + (b - m) / 2;
            m1 = binarySearch(array, a, m, array[m2], false);
            m3 = (m2++) - (m - m1);
        }
        rotate(array, m1, m, m2);
        merge(array, m3 + 1, m2, b);
        merge(array, a, m1, m3);
    }

    protected int findRun(int[] array, int a, int b) {
        int i = a + 1;
        boolean dir;
        if(i < b)
            dir = Reads.compareIndices(array, i - 1, i++, 0.5, true) <= 0;
        else
            dir = true;
        if(dir)
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0)
                i++;
        else {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0)
                i++;
            if(i - a < 4)
                Writes.swap(array, a, i - 1, 1.0, true, false);
            else
                Writes.reversal(array, a, i - 1, 1.0, true, false);
        }
        Highlights.clearMark(2);
        return i;
    }

    public void mergeSort(int[] array, int a, int b) {
        int i, j, k;
        while(true) {
            i = findRun(array, a, b);
            if(i >= b)
                return;
            j = findRun(array, i, b);
            merge(array, a, i, j);
            Highlights.clearMark(2);
            if(j >= b)
                return;
            k = j;
            while(true) {
                i = findRun(array, k, b);
                if(i >= b)
                    break;
                j = findRun(array, i, b);
                merge(array, k, i, j);
                if(j >= b)
                    break;
                k = j;
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
