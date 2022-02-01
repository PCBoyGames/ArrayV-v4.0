package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class TableClamberSort extends Sort {

    public TableClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Table Clamber");
        this.setRunAllSortsName("Table Clamber Sort");
        this.setRunSortName("Table Clambersort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean stableComp(int[] array, int[] table, int a, int b) {
        int comp = Reads.compareIndices(array, table[a], table[b], 0.125, true);
        return comp < 0 || (comp == 0 && Reads.compareOriginalIndices(table, a, b, 0, false) < 0);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int[] table = Writes.createExternalArray(currentLength);
        for (int i = 0; i < currentLength; i++) Writes.write(table, i, i, 0.5, true, true);
        for (int i = 0; i < currentLength; i++)
            for (int j = 0; j < i; j++) {
                Highlights.markArray(3, j);
                if (stableComp(array, table, i, j)) Writes.swap(table, i, j, 0.125, true, true);
            }
        Highlights.clearMark(3);
        for(int i = 0; i < currentLength; i++) {
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