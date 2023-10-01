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



 */

public class BonusSort extends Sort {
    public BonusSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bonus");
        this.setRunAllSortsName("Bonus Sort");
        this.setRunSortName("Bonus Sort");
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
        int firstsel = 0;
        for (int i = 1; i < currentLength; i++) if (Reads.compareIndices(array, firstsel, i, 0.025, true) > 0) firstsel = i;
        for (; firstsel > 0; firstsel--) Writes.multiSwap(array, 0, currentLength - 1, 0.025, true, false);
        for (int i = 1; i < currentLength; i++) {
            int val = array[i - 1];
            int sweep = 0;
            while (Reads.compareValues(array[i], val) != 0) {
                Writes.multiSwap(array, i, currentLength - 1, 0.025, true, false);
                sweep++;
                if (sweep > currentLength - i - 1) {
                    val++;
                    sweep = 0;
                }
            }
        }
    }
}
