package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Yuri-chan
 * @author aphitorite
 *
 */
public final class TableHeapSort extends Sort {

    public TableHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Table Heap");
        this.setRunAllSortsName("Table Heap Sort");
        this.setRunSortName("Table Heapsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void siftDown(int[] array, int[] keys, int r, int len, int a, int t) {
        int j = r;

        while(2*j + 1 < len) {
            j = 2*j + 1;

            if(j+1 < len) {
                int cmp = Reads.compareIndices(array, a+keys[j+1], a+keys[j], 0.25, true);

                if(cmp > 0 || (cmp == 0 && Reads.compareOriginalValues(keys[j+1], keys[j]) > 0))
                    j++;
            }
        }
        for(int cmp = Reads.compareIndices(array, a+t, a+keys[j], 0.25, true);

            cmp > 0 || (cmp == 0 && Reads.compareOriginalValues(t, keys[j]) > 0);

            j = (j-1)/2,
            cmp = Reads.compareIndices(array, a+t, a+keys[j], 0.25, true));

        for(int t2; j > r; j = (j-1)/2) {
            t2 = keys[j];
            Highlights.markArray(3, j);
            Writes.write(keys, j, t, 0.5, false, true);
            t = t2;
        }
        Highlights.markArray(3, r);
        Writes.write(keys, r, t, 0.5, false, true);
    }

    protected void tableSort(int[] array, int[] keys, int a, int b) {
        int len = b-a;

        for(int i = (len-1)/2; i >= 0; i--)
            this.siftDown(array, keys, i, len, a, keys[i]);

        for(int i = len-1; i > 0; i--) {
            int t = keys[i];
            Highlights.markArray(3, i);
            Writes.write(keys, i, keys[0], 0.5, false, true);
            this.siftDown(array, keys, 0, i, a, t);
        }
        Highlights.clearMark(3);

        for(int i = 0; i < len; i++) {
            Highlights.markArray(2, i);
            if(Reads.compareOriginalValues(i, keys[i]) != 0) {
                int t = array[a+i];
                int j = i, next = keys[i];

                do {
                    Writes.write(array, a+j, array[a+next], 1.0, true, false);
                    Writes.write(keys, j, j, 1.0, true, true);

                    j = next;
                    next = keys[next];
                }
                while(Reads.compareOriginalValues(next, i) != 0);

                Writes.write(array, a+j, t, 1.0, true, false);
                Writes.write(keys, j, j, 1.0, true, true);
            }
        }
        Highlights.clearMark(2);
    }

    public void customSort(int[] array, int start, int end) {
        int len = end - start;
        int[] keys = Writes.createExternalArray(len);
        for(int i = 0; i< len; i++) {
            Writes.write(keys, i, i, 0.5, true, true);
        }
        tableSort(array, keys, start, end);
        Writes.deleteExternalArray(keys);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        int[] keys = Writes.createExternalArray(sortLength);
        for(int i = 0; i < sortLength; i++) {
            Writes.write(keys, i, i, 0.5, true, true);
        }
        tableSort(array, keys, 0, sortLength);
        Writes.deleteExternalArray(keys);

    }

}
