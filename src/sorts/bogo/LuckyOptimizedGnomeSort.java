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
public final class LuckyOptimizedGnomeSort extends BogoSorting {

    public LuckyOptimizedGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Optimized Gnome");
        this.setRunAllSortsName("Lucky Optimized Gnome Sort");
        this.setRunSortName("Lucky Optimized Gnomesort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(true);
        setQuestion("Enter the luck for this sort:", 50);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 100) return 50;
        return answer;
    }

    public void sort(int[] array, int a, int b, int luck) {
        boolean done = false;
        while (!done) {
            done = true;
            for (int i = a + 1; i < b; i++) {
                int j = i;
                while (j > a && Reads.compareIndices(array, j, j - 1, delay, true) < 0) {
                    if (BogoSorting.randInt(1, 101) <= luck)
                        Writes.swap(array, j - 1, j, delay, true, false);
                    done = false;
                    j--;
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int luck) {
        sort(array, 0, sortLength, luck);

    }

}
