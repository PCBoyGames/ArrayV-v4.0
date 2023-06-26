package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 *
 */
public final class InverseSelectionSort extends Sort {

    public InverseSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Inverse Selection");
        this.setRunAllSortsName("Inverse Selection Sort");
        this.setRunSortName("Inverse Selection Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.0625, true, false);
        if (a != b)
            Writes.write(array, b, temp, 0.05, true, false);
    }
    
    public void selectionSort(int[] array, int a, int b) {
        for (int i = a; i < b; i++) {
            int maxIdx = i;
            for (int j = i + 1; j < b; j++) {
                if (Reads.compareIndices(array, maxIdx, j, 0.0625, true) <= 0) {
                    maxIdx = j;
                    Highlights.markArray(3, j);
                }
            }
            Highlights.clearMark(3);
            insertTo(array, maxIdx, a);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        selectionSort(array, 0, sortLength);

    }

}
