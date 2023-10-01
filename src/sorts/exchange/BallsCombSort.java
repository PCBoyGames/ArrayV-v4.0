package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

comb sortb ut ballsk

 */

public class BallsCombSort extends Sort {
    public BallsCombSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Balls Comb");
        this.setRunAllSortsName("Balls Comb Sort");
        this.setRunSortName("Balls Combsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(25);
        this.setBogoSort(false);
    }

    public void ballsComb(int[] array, int a, int b) {
        for (int i = a+1; i < b; i++) {
            boolean swapped = false;
            int gap = i+1;
            while ((gap > 1) || swapped) {
                if (gap > 1) gap--;
                swapped = false;
                for (int j = a; (gap + j) <= i; j++) {
                    if (Reads.compareValues(array[j], array[j + gap]) > 0) {
                        Writes.swap(array, j, j+gap, 0.75, swapped = true, false);
                        i = -1;
                    }
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        ballsComb(array, 0, currentLength);
    }
}
