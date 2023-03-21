package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Ported to ArrayV by Kiriko-chan
in collaboration with aphitorite

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author aphitorite
 *
 */
public final class SimpleNaturalMergeSort extends Sort {

    public SimpleNaturalMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Simple Natural Merge");
        this.setRunAllSortsName("Simple Natural Merge Sort");
        this.setRunSortName("Simple Natural Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
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
    
    public void merge(int[] array, int[] buf, int a, int m, int b) {
        if(b - m < m - a)
            mergeBWExt(array, buf, a, m, b);
        else
            mergeFWExt(array, buf, a, m, b);
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
    
    public void mergeSort(int[] array, int[] buf, int a, int b) {
        int i, j, k;
        while(true) {
            i = findRun(array, a, b);
            if(i >= b)
                return;
            j = findRun(array, i, b);
            merge(array, buf, a, i, j);
            Highlights.clearMark(2);
            if(j >= b)
                return;
            k = j;
            while(true) {
                i = findRun(array, k, b);
                if(i >= b)
                    break;
                j = findRun(array, i, b);
                merge(array, buf, k, i, j);
                if(j >= b)
                    break;
                k = j;
            }
        }
    }
    
    public void customSort(int[] array, int a, int b) {
        int[] buf = Writes.createExternalArray((b - a) / 2);
        mergeSort(array, buf, a, b);
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        customSort(array, 0, sortLength);

    }

}
