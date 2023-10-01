package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Ayako-chan
in collaboration with PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author PCBoy
 *
 */
public class LuckyExchangeBomoSort extends BogoSorting {

    public LuckyExchangeBomoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Exchange Bomo");
        this.setRunAllSortsName("Lucky Exchange Bomo Sort");
        this.setRunSortName("Lucky Exchange Bomosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(true);
        setQuestion("Enter the luck for this sort:", 50);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 100)
            return 50;
        return answer;
    }

    public boolean pull(int[] array, int a, int b, int luck) {
        boolean anyswaps = false;
        if (a < b) {
            for (int i = a; i < b; i++) {
                if (Reads.compareIndices(array, i, i + 1, this.delay, true) > 0) {
                    if (BogoSorting.randInt(1, 101) <= luck) {
                        Writes.swap(array, i, i + 1, delay, true, false);
                        anyswaps = true;
                    }
                }
            }
        } else {
            for (int i = a; i > b; i--) {
                if (Reads.compareIndices(array, i, i - 1, this.delay, true) < 0) {
                    if (BogoSorting.randInt(1, 101) <= luck) {
                        Writes.swap(array, i, i - 1, delay, true, false);
                        anyswaps = true;
                    }
                }
            }
        }
        return anyswaps;
    }

    public void sort(int[] array, int a, int b, int luck) {
        while (!this.isRangeSorted(array, a, b, false, true)) {
            boolean noswap = true;
            while (noswap)
                if (pull(array, BogoSorting.randInt(a, b), BogoSorting.randInt(a, b), luck))
                    noswap = false;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int luck) {
        sort(array, 0, sortLength, luck);

    }

}
