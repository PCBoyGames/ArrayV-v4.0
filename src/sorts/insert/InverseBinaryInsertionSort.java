package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class InverseBinaryInsertionSort extends Sort {

    public InverseBinaryInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Inverse Binary Insertion");
        this.setRunAllSortsName("Inverse Binary Insertion Sort");
        this.setRunSortName("Inverse Binary Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    protected void insertTo(int[] array, int a, int b, double sleep) {
        Highlights.clearMark(2);
        if (a != b) {
            int temp = array[a];
            int d = (a > b) ? -1 : 1;
            for (int i = a; i != b; i += d)
                Writes.write(array, i, array[i + d], sleep, true, false);
            Writes.write(array, b, temp, sleep, true, false);
        }
    }
    
    protected int binSearch(int[] array, int a, int b, int val, double sleep) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(sleep);
            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m + 1;
        }
        return a;
    }
    
    public void insertSort(int[] array, int a, int b, double sleep) {
        for (int i = a + 1; i < b; i++)
            insertTo(array, b - 1, binSearch(array, a, i, array[b - 1], sleep), sleep / 8);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        insertSort(array, 0, sortLength, 1);

    }

}
