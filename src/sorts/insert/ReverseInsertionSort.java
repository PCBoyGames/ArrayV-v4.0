/**
 *
 */
package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author McDude73
 *
 */
public class ReverseInsertionSort extends Sort {

    /**
     * @param arrayVisualizer
     */
    public ReverseInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Reverse Insertion");
        this.setRunAllSortsName("Reverse Insertion Sort");
        this.setRunSortName("Reverse Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) throws Exception {
        for (int i = length - 1; i >= 0; i--) {
            int current = array[i];
            int pos = i + 1;

            while (pos <= length - 1 && Reads.compareValues(array[pos], current) < 0) {
                Writes.write(array, pos - 1, array[pos], 0.015D, true, false);
                pos++;
            }
            Writes.write(array, pos - 1, current, 0.015D, true, false);
        }
    }

}
