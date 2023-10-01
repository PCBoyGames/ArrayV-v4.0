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

you call for help

but nobody came

 */

public class HyperoperationSort extends Sort {
    public HyperoperationSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Hyperoperation");
        this.setRunAllSortsName("Hyperoperation Sort");
        this.setRunSortName("H-Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(3);
        this.setBogoSort(false);
    }

    private void compSwap(int[] array, int a, int b, double delay) {
        if (Reads.compareIndices(array, a, b, delay, true) > 0) Writes.swap(array, a, b, delay, true, false);
    }

    public long H(int[] array, long n, long a, long b, int d) {
        if (n == 0) {
            compSwap(array, (int) n, (int) n+1, 0.005);
            return b+1;
        } else if (n == 1 && b == 0) {
            long c = n % arrayVisualizer.getCurrentLength();
            if (c == 0) {
                compSwap(array, (int) c, (int) c+1, 0.005);
            } else {
                compSwap(array, (int) c-1, (int) c, 0.005);
            }
            return a;
        } else if (n == 2 && b == 0) {
            long c = n % arrayVisualizer.getCurrentLength();
            if (c == 0) {
                compSwap(array, (int) c, (int) c+1, 0.005);
            } else {
                compSwap(array, (int) c-1, (int) c, 0.005);
            }
            return 0;
        } else if (n >= 3 && b == 0) {
            long c = n % arrayVisualizer.getCurrentLength();
            if (c == 0) {
                compSwap(array, (int) c, (int) c+1, 0.005);
            } else {
                compSwap(array, (int) c-1, (int) c, 0.005);
            }
            return 1;
        } else {
            Writes.recordDepth(d++);
            Writes.recursion(2);
            return H(array, n-1, a, H(array, n, a, b-1, d), d);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        H(array, currentLength, currentLength, currentLength, 0);
    }
}
