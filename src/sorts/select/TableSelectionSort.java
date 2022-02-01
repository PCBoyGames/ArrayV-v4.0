package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Kiriko-chan
 *
 */
public final class TableSelectionSort extends Sort {

    public TableSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Table Selection");
        this.setRunAllSortsName("Table Selection Sort");
        this.setRunSortName("Table Selectionsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    boolean stableComp(int[] array, int[] table, int a, int b) {
        int comp = Reads.compareIndices(array, table[a], table[b], 0.125, true);

        return comp < 0 || (comp == 0 && Reads.compareOriginalIndices(table, a, b, 0, false) < 0);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] table = Writes.createExternalArray(length);
        for(int i = 0; i<length;i++)
            Writes.write(table, i, i, 0.5, true, true);
        for (int i = 0; i < length - 1; i++) {
            int minIdx = i;

            for (int j = i + 1; j < length; j++) {

                if (stableComp(array, table, j, minIdx)){
                    minIdx = j;
                    Highlights.markArray(3, minIdx);
                    Delays.sleep(0.125);
                }
            }
            if (minIdx != i)
                Writes.swap(table, i, minIdx, 0.25, true, true);
        }
        Highlights.clearMark(3);
        for(int i = 0; i < length; i++) {
            Highlights.markArray(2, i);

            if(Reads.compareOriginalValues(i, table[i]) != 0) {
                int t = array[i];
                int j = i, next = table[i];

                do {
                    Writes.write(array, j, array[next], 1, true, false);
                    Writes.write(table, j, j, 1, true, true);

                    j = next;
                    next = table[next];
                }
                while(Reads.compareOriginalValues(next, i) != 0);

                Writes.write(array, j, t, 1, true, false);
                Writes.write(table, j, j, 1, true, true);
            }
        }
        Writes.deleteExternalArray(table);
    }

}
