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
public class OptimizedSafePandemicSort extends BogoSorting {

    public OptimizedSafePandemicSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Safe Pandemic");
        this.setRunAllSortsName("Optimized Safe Pandemic Sort");
        this.setRunSortName("Optimized Safe Pandemic Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void runPandemic(int[] array, int currentLength) {
        int random = array[randInt(0, currentLength)];
        boolean all = false;
        while (!all) {
            boolean change = false;
            all = true;
            for (int j = 0; j < currentLength && all; j++) if (Reads.compareValues(array[j], random) != 0) all = false;
            if (all) break;
            while (!change) {
                int i = randInt(0, currentLength);
                Highlights.markArray(1, i);
                Delays.sleep(0.01);
                if (Reads.compareValues(array[i], random) != 0) Writes.write(array, i, array[randInt(0, currentLength)], 0.1, change = true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        runPandemic(array, sortLength);

    }

}
