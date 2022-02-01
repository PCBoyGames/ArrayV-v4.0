package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Kiriko-chan
in collaboration with PCBoy and mariam-ts

---------------------------------
- The mariam-ts Sort-O-Matic    -
- and Sorting Algorithm Scarlet -
---------------------------------

 */

/**
 * @author Kiriko-chan
 * @author PCBoy
 * @author mariam-ts
 *
 */
public final class WeirdBubblePopSort extends Sort {

    public WeirdBubblePopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Weird Bubble Pop");
        this.setRunAllSortsName("Weird Bubble Pop Sort");
        this.setRunSortName("Weird Bubble Popsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void weirdBubble(int[] array, int a, int b, boolean dir) {
        int cmp = dir ? 1 : -1;
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            for (int i = a + 1; i < b; i++) {
                int j = i;
                while (j > a && Reads.compareIndices(array, j - 1, j, 0.05, true) * cmp > 0) {
                    Writes.swap(array, i, j - 1, 0.05, true, false);
                    j--;
                    sorted = false;
                }
            }
        }
    }

    public void popSort(int[] array, int a, int b) {
        int quarter = (b - a) / 4, half = (b - a) / 2;
        weirdBubble(array, a, a + quarter, false);
        weirdBubble(array, a + quarter, a + half, true);
        weirdBubble(array, a + half, b - quarter, false);
        weirdBubble(array, b - quarter, b, true);
        weirdBubble(array, a, a + half, false);
        weirdBubble(array, a + half, b, true);
        weirdBubble(array, a, b, true);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        popSort(array, 0, sortLength);

    }

}
