package sorts.insert;

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

public class UntreeSort extends Sort {
    public UntreeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Untree");
        this.setRunAllSortsName("Untree Sort");
        this.setRunSortName("Untreesort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private boolean untree(int[] array, int a, int b) {
        boolean swapped = false;
        for (int i = a; i < b; i++)
            for (int j = i; j <= b; j++)
                if (j+1 > b || Reads.compareIndices(array, j, j+1, 1, true) <= 0) {
                    if (j != i) Writes.insert(array, j, i, 1, swapped = true, false);
                    break;
                }
        return swapped;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        boolean swapped;
        do {
            swapped = untree(array, 0, currentLength-1);
        } while (swapped);
    }
}
