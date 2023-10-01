package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- WHAT? WHY? IT'S NOT SHELL! -
------------------------------

*/
public class MatthijsSort extends Sort {
    public MatthijsSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Matthijs");
        this.setRunAllSortsName("Matthijs Sort");
        this.setRunSortName("Matthijs Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int gap = currentLength;
        while (gap > 1) {
            boolean done = false;
            gap /= 2;
            do {
                done = true;
                for (int i = 0; i < currentLength - gap; i++) if (Reads.compareIndices(array, i, i + gap, 0.25, true) > 0) Writes.swap(array, i, i + gap, 0.25, true, done = false);
            } while (!done);
        }
    }
}