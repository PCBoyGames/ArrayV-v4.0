package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.Timer;

final public class BranchlessBubbleSort extends Sort {
    private Timer Timer;
    public BranchlessBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.Timer = arrayVisualizer.getTimer();
        this.setSortListName("Branchless Bubble");
        this.setRunAllSortsName("Branchless Bubble Sort");
        this.setRunSortName("Branchless Bubblesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private boolean compSwap(int[] a, int l, int r) {
        int c;

        Reads.addComparison();
        Timer.startLap("Compare");
        c = (a[r]-a[l]) >> 31;
        Timer.stopLap();

        Writes.write(a, l, a[l] ^ (c & a[r]), 0.025, true, false);
        Writes.write(a, r, a[r] ^ (c & a[l]), 0.025, true, false);
        Writes.write(a, l, a[l] ^ (c & a[r]), 0.025, true, false);

        return c < 0;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        boolean sorted = false;
        for(int i = length - 1; i > 0 && !sorted; i--) {
            sorted = true;
            for(int j = 0; j < i; j++) {
                sorted = !compSwap(array, j, j+1) && sorted;
            }
        }
    }
}