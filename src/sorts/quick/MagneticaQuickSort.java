package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- "QUICKSORT SAYS"  SANMAYCE -
------------------------------

*/
final public class MagneticaQuickSort extends Sort {
    public MagneticaQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Magnetica Quick");
        this.setRunAllSortsName("Magnetica Quick Sort");
        this.setRunSortName("Magnetica Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    protected void magnetica(int[] array, int left, int right) {
        int i, j, pl, pr;
        int[] stack = Writes.createExternalArray(right - left + 1);
        int stackptr = 0;
        int p;
        stackptr++; 
        Writes.write(stack, stackptr, left, 0, false, true);
        stackptr++; 
        Writes.write(stack, stackptr, right, 0, false, true);
        do {
            right = stack[stackptr];
            left = stack[stackptr - 1];
            stackptr = stackptr - 2;
            do {
                j = right;
                pl = left;
                pr = left;
                Writes.swap(array, (left + right) >> 1, pr, 2, true, false);
                p = array[pr];
                for (; pr < j; ) {
                    pr = pr + 1;
                    int cmp = Reads.compareValues(p, array[pr]);
                    if (cmp > 0) {
                        Highlights.markArray(3, j);
                        Writes.swap(array, pl, pr, 2, true, false);
                        pl = pl + 1;
                    } else if (cmp < 0) {
                        for (; Reads.compareValues(p, array[j]) < 0; ) j = j - 1;
                        Highlights.markArray(3, pl);
                        if (pr < j) Writes.swap(array, pr, j, 2, true, false);
                        j = j - 1;
                        pr = pr - 1;
                    }
                }
                j = pl - 1;
                i = pr + 1;
                if (i < right) {
                    stackptr = stackptr + 2;
                    Writes.write(stack, stackptr - 1, i, 0, false, true);
                    Writes.write(stack, stackptr, right, 0, false, true);;
                }
                right = j;
            } while (left < right);
        } while (stackptr != 0);   
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        magnetica(array, 0, currentLength - 1);
    }
}