package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- THANKS FUNGAMER, VERY COOL -
------------------------------

*/
public class MyoSort extends Sort {

    int maxdepth;

    public MyoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Myo");
        this.setRunAllSortsName("Myo Sort");
        this.setRunSortName("Myosort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(7);
        this.setBogoSort(false);
    }

    protected void myoHelper(int[] array, int lo, int hi, int depth) {
        Writes.recordDepth(maxdepth - depth);
        Highlights.markArray(1, lo);
        Highlights.markArray(2, hi);
        Delays.sleep(0.001);
        if (hi - lo == 1) {if (Reads.compareIndices(array, lo, hi, 0.001, true) > 0) Writes.swap(array, lo, hi, 0.001, true, false);}
        else if (hi - lo > 1) {
            if (depth > 0) {
                for (int j = 0; j < depth; j++) {
                    for (int i = lo + 1; i < hi; i++) {
                        Writes.recursion();
                        myoHelper(array, i, hi, depth);
                    }
                    for (int i = lo + 1; i < hi; i++) {
                        Writes.recursion();
                        myoHelper(array, lo, i, depth);
                    }
                }
                Writes.recursion();
                myoHelper(array, lo, hi, depth - 1);
            } else {
                for (int i = lo + 1; i < hi; i++) {
                    Writes.recursion();
                    myoHelper(array, i, hi, 0);
                }
                for (int i = lo + 1; i < hi; i++) {
                    Writes.recursion();
                    myoHelper(array, lo, i, 0);
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        maxdepth = currentLength;
        myoHelper(array, 0, currentLength - 1, currentLength);
    }
}