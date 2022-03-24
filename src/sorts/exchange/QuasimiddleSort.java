package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class QuasimiddleSort extends Sort {
    public QuasimiddleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Quasimiddle");
        this.setRunAllSortsName("Quasimiddle Sort");
        this.setRunSortName("Quasimiddle Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    protected boolean cs(int[] array, int a, int b) {
        if (Reads.compareIndices(array, a, b, 0.05, true) > 0) {
            Writes.swap(array, a, b, 0.05, true, false);
            return true;
        }
        return false;
    }
    
    protected boolean middle(int[] array, int start, int end) {
        int middle = start + (end - start) / 2;
        boolean swaps = false;
        boolean toggle = false;
        for (int i = 1; middle - i >= start && middle + i <= end; i++) {
            toggle = cs(array, middle - i, middle);
            if (!swaps) if (toggle) swaps = true;
            toggle = cs(array, middle, middle + i);
            if (!swaps) if (toggle) swaps = true;
        }
        return swaps;
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        boolean swaps = true;
        while (swaps) {
            swaps = false;
            boolean toggle = false;
            for (int i = 1; i < currentLength; i++) {
                toggle = middle(array, 0, i);
                if (!swaps) if (toggle) swaps = true;
                toggle = middle(array, i, currentLength - 1);
                if (!swaps) if (toggle) swaps = true;
            }
        }
    }
}