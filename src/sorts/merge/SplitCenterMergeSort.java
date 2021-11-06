package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SplitCenterMergeSort extends Sort {
    public SplitCenterMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Split Center Merge");
        this.setRunAllSortsName("Split Center Merge Sort");
        this.setRunSortName("Split Center Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the base for this sort:", 2);;
    }
    
    protected void method(int[] array, int start, int len) {
        int way = 1;
        int i = start;
        int swapless = 0;
        boolean anyswaps = false;
        while (swapless < 2) {
            anyswaps = false;
            i = (int) Math.floor(len / 2) + start;
            i -= way;
            while (i < start + len - 1 && i >= start) {
                if (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) {
                    Writes.swap(array, i, i + 1, 0.01, true, false);
                    anyswaps = true;
                }
                i += way;
            }
            way *= -1;
            if (!anyswaps) {
                swapless++;
            } else {
                swapless = 0;
            }
        }
        if (len <= 4) {
            for (i = start; i + 1 < start + len; i++) {
                if (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) {
                    Writes.swap(array, i, i + 1, 0.01, true, false);
                }
            }
        }
    }
    
    @Override
    public int validateAnswer(int answer) {
        if (answer < 2)
            return 2;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        int len = base;
        int index = 0;
        while (len < currentLength) {
            index = 0;
            while (index + len - 1 < currentLength) {
                method(array, index, len);
                index += len;
            }
            len *= base;
        }
        method(array, 0, currentLength);
    }
}