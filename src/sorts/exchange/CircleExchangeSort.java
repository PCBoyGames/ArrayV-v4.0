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

Circle + Exchange

 */

public class CircleExchangeSort extends Sort {
    public CircleExchangeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Circle-Exchange");
        this.setRunAllSortsName("Circle-Exchange Sort");
        this.setRunSortName("Circle-Exchange Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int circleExchange(int[] array, int a, int b, int swaps, double delay) {
        if (a == b) return swaps;
        int lo = a, hi = b, m = (b - a) / 2;
        while (true) {
            if (a >= b) break;
            for (int i = a; i < b; i++) {
                if (Reads.compareIndices(array, a, i, delay, true) > 0) {
                    Writes.swap(array, a, i, delay, true, false);

                    swaps++;
                }
            }
            a++;
            b--;
        }
        swaps = circleExchange(array, lo, lo+m, swaps, delay);
        swaps = circleExchange(array, hi-m, hi, swaps, delay);
        return swaps;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int swaps = 0;
        do {
            swaps = circleExchange(array, 0, currentLength, 0, 0.005);
        } while (swaps != 0);
    }
}
