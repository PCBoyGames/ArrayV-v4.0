package sorts.quick;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- "QUICKSORT SAYS"  SANMAYCE -
------------------------------

*/
final public class MagneticaQuickSort110 extends Sort {
    
    InsertionSort insert = new InsertionSort(arrayVisualizer);
    
    public MagneticaQuickSort110(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Magnetica Quick (1.10)");
        this.setRunAllSortsName("Magnetica Quick Sort (1.10)");
        this.setRunSortName("Magnetica Quicksort (1.10)");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the insertion threshold for this sort:\n(Default is 13)", 13);
    }
    
    @Override
    public int validateAnswer(int answer) {
        if (answer < 0) return 0;
        return answer;
    }
    
    protected void magnetica(int[] array, int left, int right, int threshold) {
        boolean insertion = threshold > 0;
        int i, j, pl, pr;
        int[] stack = Writes.createExternalArray(right - left + 1);
        int stackptr = 2;
        int p;
        int lback = left;
        int rback = right;
        int x0, x1, x2, x3, x4;
        int o0, o1, o2, o3, o4;
        int midmid;
        Writes.write(stack, 1, left, 0, false, true);
        Writes.write(stack, 2, right, 0, false, true);
        do {
            right = stack[stackptr];
            left = stack[stackptr - 1];
            stackptr -= 2;
            for (; left + threshold < right; ) {
                Highlights.clearAllMarks();
                j = right;
                pl = left;
                pr = left;
                if (insertion) {
                    midmid = left + ((right - left) >> 2);
                    x0 = array[midmid];
                    x1 = array[midmid + 1];
                    x2 = array[midmid + 2];
                    x3 = array[midmid + 3];
                    x4 = array[midmid + 4];
                    o0 = (x0 >  x1 ? 1 : 0)
                       + (x0 >  x2 ? 1 : 0)
                       + (x0 >  x3 ? 1 : 0)
                       + (x0 >  x4 ? 1 : 0);
                    o1 = (x1 >= x0 ? 1 : 0)
                       + (x1 >  x2 ? 1 : 0)
                       + (x1 >  x3 ? 1 : 0)
                       + (x1 >  x4 ? 1 : 0);
                    o2 = (x2 >= x0 ? 1 : 0)
                       + (x2 >= x1 ? 1 : 0)
                       + (x2 >  x3 ? 1 : 0)
                       + (x2 >  x4 ? 1 : 0);
                    o3 = (x3 >= x0 ? 1 : 0)
                       + (x3 >= x1 ? 1 : 0)
                       + (x3 >= x2 ? 1 : 0)
                       + (x3 >  x4 ? 1 : 0);
                    o4 = 10 - (o0 + o1 + o2 + o3);
                    Writes.write(array, midmid + o0, x0, 2, true, false);
                    Writes.write(array, midmid + o1, x1, 2, true, false);
                    Writes.write(array, midmid + o2, x2, 2, true, false);
                    Writes.write(array, midmid + o3, x3, 2, true, false);
                    Writes.write(array, midmid + o4, x4, 2, true, false);
                    Writes.swap(array, midmid + 2, pr, 2, true, false);
                } else Writes.swap(array, (left + right) >> 1, pr, 2, true, false);
                p = array[pr];
                for (; pr < j; ) {
                    pr++;
                    int cmp = Reads.compareValues(p, array[pr]);
                    if (cmp > 0) {
                        Highlights.markArray(3, j);
                        Writes.swap(array, pl, pr, 2, true, false);
                        pl++;
                    } else if (cmp < 0) {
                        for (; Reads.compareValues(p, array[j]) < 0; ) j--;
                        Highlights.markArray(3, pl);
                        if (pr < j) Writes.swap(array, pr, j, 2, true, false);
                        j--;
                        pr--;
                    }
                }
                j = pl - 1;
                i = pr + 1;
                if (i + threshold < right) {
                    stackptr += 2;
                    Writes.write(stack, stackptr - 1, i, 0, false, true);
                    Writes.write(stack, stackptr, right, 0, false, true);
                }
                right = j;
            }
        } while (stackptr != 0);
        Highlights.clearAllMarks();
        if (insertion) {
            insert.customInsertSort(array, lback, rback + 1, 0.5, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int threshold) {
        magnetica(array, 0, currentLength - 1, threshold);
    }
}