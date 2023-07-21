package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class JumpDownSort extends Sort {
    public JumpDownSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Jump Down");
        this.setRunAllSortsName("Jump Down Sort");
        this.setRunSortName("Jump Down Sort");
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
        int maxPoint = currentLength - 1;
        Highlights.markArray(1, maxPoint);
        for(int i = maxPoint; i > 0; i--) {
            for(int j = 0; j < i; j++) {
                Highlights.markArray(2, j);
                if (Reads.compareValues(array[i], array[j]) < 0) {
                    Writes.swap(array, i, j, 0.2, true, false);
                }
            }
            Highlights.markArray(1, i);
        }
    }
}
