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

Finally.

 */

public class TernaryGnomeSort extends Sort {
    public TernaryGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Ternary Gnome");
        this.setRunAllSortsName("Optimized Gnome Sort + Ternary Search");
        this.setRunSortName("Optimized Gnomesort + Ternary Search");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void ternaryGnome(int[] array, int a, int b) {
        for (int i = a; i < b; i++) {
            int lo = a, hi = i;
            int num = array[i];
            while (lo < hi - 1) {
                int third = (hi - lo + 1) / 3,
                    midA = lo + third,
                    midB = hi - third;
                if (Reads.compareValues(array[midA], num) > 0) {
                    hi = midA;
                } else if (Reads.compareValues(array[midB], num) < 0) {
                    lo = midB;
                } else {
                    lo = midA;
                    // hi = midB;
                }
                Highlights.markArray(2, midA);
                Highlights.markArray(3, midB);
                Delays.sleep(1);
            }
            Highlights.clearMark(2);
            Highlights.clearMark(3);
            int place = Reads.compareValues(array[lo], num) > 0 ? lo : hi;

            Writes.multiSwap(array, i, place, 0.05, true, false);

            Highlights.clearAllMarks();
        }
    }


    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        ternaryGnome(array, 0, currentLength);
    }
}
