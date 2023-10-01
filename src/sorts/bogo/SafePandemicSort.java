package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Ayako-chan
in collaboration with PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author PCBoy
 *
 */
public class SafePandemicSort extends BogoSorting {

    public SafePandemicSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Safe Pandemic");
        this.setRunAllSortsName("Safe Pandemic Sort");
        this.setRunSortName("Safe Pandemic Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(true);
    }

    protected void runPandemic(int[] array, int currentLength) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < currentLength; i++) {
            if (array[i] < min) min = array[i];
            if (array[i] > max) max = array[i];
        }
        int random = array[randInt(0, currentLength)];
        boolean all = false;
        while (!all) {
            all = true;
            for (int j = 0; j < currentLength && all; j++) if (Reads.compareValues(array[j], random) != 0) all = false;
            if (all) break;
            boolean change = false;
            while (!change) {
                int i = randInt(0, currentLength);
                Highlights.markArray(1, i);
                Delays.sleep(0.01);
                if (Reads.compareValues(array[i], random) != 0) Writes.write(array, i, randInt(min, max + 1), 0.1, change = true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        runPandemic(array, sortLength);

    }

}
