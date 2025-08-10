package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/**
 * @author Eisah-Jones on GitHub
 * @author Flanlaina
 */
public class SpinTheBottleSort extends BogoSorting {
    public SpinTheBottleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Spin The Bottle");
        this.setRunAllSortsName("Spin The Bottle Sort");
        this.setRunSortName("Spin The Bottle Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4096);
        this.setBogoSort(false);
    }

    public void shuffleArray(int[] array, int a, int b) {
        int len = b - a;
        if (len < 2) return;
        for (int i = a; i < b; i++) {
            /*
             * The following two lines of code chooses `j` uniformly and independently at
             * random from {a, a + 1, ..., i - 1, i + 1, ..., b - 1} in exactly O(1).
             */
            int j = randInt(a, b - 1);
            if (j >= i) j++;

            int cmp = Reads.compareIndices(array, i, j, delay, true);
            if ((i < j) ? (cmp > 0) : (cmp < 0))
                Writes.swap(array, i, j, delay, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        while (!isRangeSorted(array, 0, sortLength, false, true))
            shuffleArray(array, 0, sortLength);
    }
}
