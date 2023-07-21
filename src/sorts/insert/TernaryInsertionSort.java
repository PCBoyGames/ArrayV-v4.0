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

Finally

 */

public final class TernaryInsertionSort extends Sort {
    public TernaryInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Ternary Insert");
        this.setRunAllSortsName("Ternary Insertion Sort");
        this.setRunSortName("Ternary Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public int ternary(int[] array, int start, int end, int key, double sleep) {
        while (start < end-1) {
            int third = (end - start + 1) / 3,
                midA = start + third,
                midB = end - third;
            if (Reads.compareValues(array[midA], key) == 1) {
                end = midA;
            } else if (Reads.compareValues(array[midB], key) == -1) {
                start = midB;
            } else {
                start = midA;
                end = midB;
            }
            Highlights.markArray(2, midA);
            Highlights.markArray(3, midB);
            Delays.sleep(sleep);
        }
        Highlights.clearMark(2);
        Highlights.clearMark(3);
        return Reads.compareValues(array[start], key) == 1 ? start : end;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 1; i < currentLength; i++) {
            Writes.insert(array, i, ternary(array, 0, i, array[i], 1), 0.05, true, false);
        }
    }
}
