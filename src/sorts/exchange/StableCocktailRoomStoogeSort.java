package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Harumi
extending code by thatsOven

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Harumi
 * @author thatsOven
 *
 */
public class StableCocktailRoomStoogeSort extends Sort {

    public StableCocktailRoomStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stable Cocktail Room Stooge");
        this.setRunAllSortsName("Stable Cocktail Room Stooge Sort");
        this.setRunSortName("Stable Cocktail Room Stoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public boolean isSorted(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++)
            if (Reads.compareIndices(array, i, i - 1, 0, true) < 0) return false;
        return true;
    }
    
    private void stoogeBubble(int[] array, int start, int end, boolean dir) {
        if (end - start + 1 == 2) {
            if (Reads.compareIndices(array, start, end, 0.0025, true) > 0) {
                Writes.swap(array, start, end, 0.005, true, false);
            }
        } else if (end - start + 1 > 2) {
            int third = (end - start + 1) / 3;
            if (dir) {
                stoogeBubble(array, start, end - third, dir);
                stoogeBubble(array, start + third, end, dir);
            } else {
                stoogeBubble(array, start + third, end, dir);
                stoogeBubble(array, start, end - third, dir);
            }
        }
    }
    
    public void sort(int[] array, int a, int b) {
        boolean dir = false;
        while (!isSorted(array, a, b)) stoogeBubble(array, a, b - 1, dir = !dir);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
