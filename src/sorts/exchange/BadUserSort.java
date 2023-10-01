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

for @itzwindows

 */

public class BadUserSort extends Sort {
    public BadUserSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bad User (DDG Variant)");
        this.setRunAllSortsName("Bad User Sort (DDG Variant)");
        this.setRunSortName("Bad User Sort (DDG Variant)");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public int badUser(int[] array, int a, int b, int d) {
        if (a >= b) return a;
        Writes.recordDepth(d++);
        int sth = a;
        Writes.recursion();
        int i = badUser(array, a, b-1, d) % arrayVisualizer.getCurrentLength();
        Writes.recursion();
        int j = badUser(array, a+1, b, d) % arrayVisualizer.getCurrentLength();
        while (i < j) {
            if (Reads.compareValues(array[i], array[j]) > 0) {
                Writes.swap(array, i, j, 0.001, true, false);
                sth++;
            }
            i++; j--;
        }
        Writes.recursion();
        badUser(array, a+1, b-1, d);
        return sth;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        badUser(array, 0, currentLength-1, 0);
    }
}
