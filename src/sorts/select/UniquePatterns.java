package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class UniquePatterns extends Sort {
    public UniquePatterns(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("AAA");
        this.setRunAllSortsName(" ");
        this.setRunSortName(" ");
        this.setCategory(" ");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        /*int left = 1;
        int right = 2;
        int highestlow = 0;
        int item = 0;
        int pull = 0;
        int stacked = 0;
        for (int j = currentLength - 1; j >= left; j--) {
            if (Reads.compareValues(array[j - 1], array[j]) < 0) {
                Writes.swap(array, j - 1, j, 0.1, true, false);
            }
        }
        while (left <= currentLength) {
            right = left + 1 + stacked;
            highestlow = 0;
            while (right <= currentLength) {
                Highlights.markArray(1, left - 1);
                Highlights.markArray(2, right - 1);
                Delays.sleep(0.01);
                if (Reads.compareValues(array[left - 1], array[right - 1]) > 0) {
                    if (highestlow == 0) {
                        highestlow = right;
                    } else {
                        Highlights.markArray(1, highestlow - 1);
                        Highlights.markArray(2, right - 1);
                        Delays.sleep(0.01);
                        if (Reads.compareValues(array[highestlow - 1], array[right - 1]) < 0) {
                            highestlow = right;
                        }
                    }
                }
                right++;
            }
            Highlights.clearMark(2);
            if (highestlow == 0) {
                left += stacked + 1;
                stacked = 0;
                for (int j = currentLength - 1; j >= left; j--) {
                    if (Reads.compareValues(array[j - 1], array[j]) < 0) {
                        Writes.swap(array, j - 1, j, 0.1, true, false);
                    }
                }
            } else {
                item = array[highestlow - 1];
                pull = highestlow;
                while (pull > left) {
                    Writes.write(array, pull - 1, array[pull - 2], 0.01, true, false);
                    pull--;
                }
                Writes.write(array, left - 1, item, 0.01, true, false);
                stacked++;
            }
        }*/
    }
}