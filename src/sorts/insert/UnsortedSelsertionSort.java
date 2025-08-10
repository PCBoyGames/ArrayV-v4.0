package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class UnsortedSelsertionSort extends MadhouseTools {
    public UnsortedSelsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Unsorted Selsertion");
        this.setRunAllSortsName("Unsorted Selsertion Sort");
        this.setRunSortName("Unsorted Selsertion Sort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the limit for this sort:\n(Default: 16)", 16);
    }

    protected void insert(int[] array, int start, int mid, int end) {
        int bound = mid - 1;
        for (int i = end - 1; i > bound; i--) {
            int sel = i;
            for (int j = i - 1; j > bound; j--) if (Reads.compareIndices(array, j, sel, 0.5, true) > 0) sel = j;
            int dest = maxExponentialSearch(array, start, bound + 1, stableReturn(array[sel]), false, 0.5, true);
            while (bound >= dest) {
                Writes.swap(array, bound, i, 1, true, false);
                if (sel == i) sel = bound;
                bound--;
                i--;
            }
            if (sel != i) Writes.swap(array, i, sel, 1, true, false);
        }
    }

    public void selsert(int[] array, int a, int b, int size) {
        int i, j;
        int k = findRun(array, a, b, 0.5, true, false);
        i = a + Math.max(k - k % size, size);
        SelsertionSort selsert = new SelsertionSort(arrayVisualizer);
        if (i < b) selsert.selsert(array, a, i, false);
        while (i < b) {
            j = Math.min(i + size, b);
            insert(array, a, i, j);
            i = j;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int blockSize) {
        arrayVisualizer.setExtraHeading(" / bSize = " + blockSize);
        selsert(array, 0, currentLength, blockSize);
    }
}