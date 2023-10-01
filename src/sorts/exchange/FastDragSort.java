package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

import java.util.Random;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/



 */

public class FastDragSort extends Sort {
    public FastDragSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Fast Drag");
        this.setRunAllSortsName("Drag Sort");
        this.setRunSortName("Dragsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private boolean isSorted(int[] array, int a, int b, int idx) {
        int c = a, ce = c+1;
        for (int i = a; i < b; i++) {
            if (i == idx) continue;
            Reads.addComparison();
            c  += array[i] < array[idx] ? 1 : 0;
            ce += array[i] <= array[idx] ? 1 : 0;
        }
        return idx >= c && idx < ce;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        Random r = new Random();
        while (true) {
            int i = 0;
            while (i < currentLength && isSorted(array, 0, currentLength, i)) i++;
            if (i == currentLength) break;
            for (int j = i++; i < currentLength; i++) {
                if (!isSorted(array, 0, currentLength, i)) {
                    Highlights.markArray(1, i);
                    Highlights.markArray(2, r.nextInt(currentLength));
                    Writes.swap(array, j, i, 0.001, true, false);
                    j = i;
                }
            }
        }
    }
}
