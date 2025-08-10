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

public final class OptimizedBadGnomeSort extends Sort {
    public OptimizedBadGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Bad Gnome");
        this.setRunAllSortsName("Optimized Bad Gnome Sort");
        this.setRunSortName("Optimized Bad Gnomesort");
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
        for (; s+1 < currentLength && Reads.compareIndices(array, s, s+1, 0.25, true) <= 0; s++);
        for (int i = s; i < currentLength; i++) {
            if (i != currentLength-1) Writes.swap(array, currentLength-1, i, 0.5, true, false);
            for (int j = i-1; j >= 0 && Reads.compareIndices(array, j, j+1, 0.25, true) > 0; j--)
                Writes.swap(array, j, j+1, 0.25, true, false);
        }
    }
}
