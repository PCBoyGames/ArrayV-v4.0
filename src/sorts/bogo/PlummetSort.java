package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Flanlaina
in collaboration with PCBoy and Meme Man

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Flanlaina
 * @author Meme Man
 * @author PCBoy
 *
 */
public class PlummetSort extends BogoSorting {
    public PlummetSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Plummet");
        this.setRunAllSortsName("Plummet Sort");
        this.setRunSortName("Plummet Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < sortLength; i++) {
            if (array[i] < min) min = array[i];
            if (array[i] > max) max = array[i];
        }
        int size = max - min + 1;
        int[] holes = Writes.createExternalArray(size);
        for (int x = 0; x < sortLength; x++) {
            Highlights.markArray(2, x);
            Writes.write(holes, array[x] - min, holes[array[x] - min] + 1, 1, true, true);
        }
        Highlights.clearMark(2);
        for (int count = 0, j = 0; count < size; count++) {
            for (int i = 0; i < holes[count]; i++, j++) {
                Highlights.markArray(1, j);
                Delays.sleep(1);
                while (count + min != array[j]) {
                    int diff;
                    if (array[j] == min) diff = 1;
                    else if (array[j] == max) diff = -1;
                    else diff = randBoolean() ? 1 : -1;
                    Writes.write(array, j, array[j] + diff, 0.1, true, false);
                }
            }
        }
        Writes.deleteExternalArray(holes);
    }
}
