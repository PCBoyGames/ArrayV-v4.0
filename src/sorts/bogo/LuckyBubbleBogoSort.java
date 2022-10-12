package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Ayako-chan
in collaboration with PCBoy and Meme Man

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author PCBoy
 * @author Meme Man
 *
 */
public final class LuckyBubbleBogoSort extends BogoSorting {

    public LuckyBubbleBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Bubble Bogo");
        this.setRunAllSortsName("Lucky Bubble Bogo Sort");
        this.setRunSortName("Lucky Bubble Bogosort");
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

    public void sort(int[] array, int a, int b, int luck) {
        while (!this.isRangeSorted(array, a, b, false, true)) {
            boolean noswap = true;
            while (noswap) {
                int i = BogoSorting.randInt(a, b - 1);
                if (Reads.compareIndices(array, i, i + 1, this.delay, true) > 0)
                    if (BogoSorting.randInt(1, 101) <= luck) {
                        Writes.swap(array, i, i + 1, this.delay, true, false);
                        noswap = false;
                    }
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int luck) {
        sort(array, 0, sortLength, luck);

    }

}
