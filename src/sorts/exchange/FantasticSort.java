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

quoogesort

 */

public class FantasticSort extends Sort {
    public FantasticSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Fantastic");
        this.setRunAllSortsName("Fantastic Sort");
        this.setRunSortName("Fantastic Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void fantastic(int[] array, int a, int b, double delay, boolean mark, boolean aux, int d) {
        if (Reads.compareIndices(array, a, b, delay, mark) > 0) Writes.swap(array, a, b, delay, mark, aux);
        int f = (b-a+1)/4;
        if (b-a+1 == 3) {
            if (Reads.compareIndices(array, b-1, b, delay, mark) > 0) Writes.swap(array, b-1, b, delay, mark, aux);
            if (Reads.compareIndices(array, a, a+1, delay, mark) > 0) Writes.swap(array, a, a+1, delay, mark, aux);
            return;
        }
        if (b-a+1 >= 4) {
            Writes.recordDepth(d++);
            Writes.recursion(5);
            fantastic(array, a+f, b, delay, mark, aux, d);
            fantastic(array, a, b-f, delay, mark, aux, d);
            fantastic(array, a, b-2*f, delay, mark, aux, d);
            fantastic(array, a+2*f, b, delay, mark, aux, d);
            fantastic(array, a+f, b-f, delay, mark, aux, d);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        fantastic(array, 0, currentLength-1, 1, true, false, 0);
    }
}
