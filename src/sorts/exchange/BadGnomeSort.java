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

Made to study Invasort's worst case.

 */

public final class BadGnomeSort extends Sort {
    public BadGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bad Gnome");
        this.setRunAllSortsName("Bad Gnome Sort");
        this.setRunSortName("Bad Gnomesort");
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
        int s = 0;
        while (s < currentLength && Reads.compareIndices(array, s, s + 1, 0.1, true) <= 0) s++;
        for (int i = s; i < currentLength; i++) {
            Writes.multiSwap(array, currentLength-1, i, 0.1, true, false);
            int j = i - 1;
            while (j >= 0 && Reads.compareIndices(array, j, j + 1, 0.1, true) > 0) {
                Writes.swap(array, j, j + 1, 0.1, true, false);
                j--;
            }
        }
    }
}
