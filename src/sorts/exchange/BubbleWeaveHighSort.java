package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class BubbleWeaveHighSort extends Sort {
    public BubbleWeaveHighSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bubble Weave (High Prime)");
        this.setRunAllSortsName("Bubble Weave Sort (High Prime)");
        this.setRunSortName("Bubble Weave (High Prime)");
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
        int gap = currentLength;
        while (true) {
            int end = currentLength - 1;
            int swap = currentLength - 1;
            int start = 0;
            while (end > 0) {
                swap = 0;
                boolean startfound = false;
                for (int i = start - gap > 0 ? start - gap : 0; i + gap <= end; i++) {
                    if (Reads.compareIndices(array, i, i + gap, 0.25, true) > 0) {
                        if (!startfound) start = i;
                        Writes.swap(array, i, swap = i + gap, 0.25, startfound = true, false);
                    }
                }
                end = swap - gap;
            }
            double primetestrunning = gap;
            int primetesti = 2;
            while (primetestrunning != 1) {
                boolean primetest = false;
                primetesti = 2;
                while (!primetest) {
                    if (Math.floor(primetestrunning / primetesti) == primetestrunning / primetesti) {
                        primetestrunning = primetestrunning / primetesti;
                        primetest = true;
                    } else primetesti++;
                }
            }
            gap /= primetesti;
            if (gap == 1) break;
        }
        MoreOptimizedBubbleSort mob = new MoreOptimizedBubbleSort(arrayVisualizer);
        mob.runSort(array, currentLength, 0);
    }
}
