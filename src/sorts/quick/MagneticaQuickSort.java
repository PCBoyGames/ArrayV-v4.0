package sorts.quick;

import java.util.Random;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- "QUICKSORT SAYS"  SANMAYCE -
------------------------------

*/
final public class MagneticaQuickSort extends Sort {
    
    InsertionSort insert = new InsertionSort(arrayVisualizer);
    Random random = new Random();

    boolean randompivot = false;
    boolean medianpivot = false;
    
    boolean standalone = false;
    boolean insertion = false;
    
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
        this.setQuestion("Enter variant:\n1: Mid Pivot Standalone\n2: Mid Pivot + Insertion\n3: Mo3/7 Pivot Standalone\n4: Mo3/7 Pivot + Insertion\n5: Random Pivot Standalone\n6: Random Pivot + Insertion\n(Default is 4)", 4);
    }
    
    protected void magnetica(int[] array, int left, int right) {
        int threshold = insertion ? 17 : 0;
        int i, j, pl, pr;
        int entries = right - left + 1;
        int[] stack = new int[entries];
        int stackptr = 2;
        int p;
        int lback = left;
        int rback = right;
        int midmid;
        int gear = 0;
        int cmp;
        Writes.write(stack, 1, left, 0, false, true);
        Writes.write(stack, 2, right, 0, false, true);
        do{
            right = stack[stackptr];
            left = stack[stackptr - 1];
            stackptr -= 2;
            for (; left + threshold < right; ) {
                j = right;
                pl = left;
                pr = left;
                // Sorry, Sanmayce, I think I found a better way to do this.
                // It should also save me from having to write too much code.
                if (medianpivot || (medianpivot && insertion && right - left > 31)) {
                    midmid = left + ((right - left) >> 2);
                    if (gear == 0) {
                        insert.customInsertSort(array, midmid, midmid + 3, 0.5, false);
                        Writes.swap(array, midmid + 1, pr, 2, true, false);
                    } else {
                        insert.customInsertSort(array, midmid, midmid + 7, 0.5, false);
                        Writes.swap(array, midmid + 3, pr, 2, true, false);
                    }
                } else if (randompivot) Writes.swap(array, random.nextInt(right - left) + left, pr, 2, true, false);
                else Writes.swap(array, (left + right) >> 1, pr, 2, true, false);
                p = array[pr];
                for (; pr < j; ) {
                    pr++;
                    cmp = Reads.compareValues(p, array[pr]);
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
                if (insertion) {
                    gear = (Math.max(right - i, j - left) > (Math.min(right - i, j - left) << 6) ? 1 : 0);
                }
                if (i + threshold < right) {
                    stackptr += 2;
                    Writes.write(stack, stackptr - 1, i, 0, false, true);
                    Writes.write(stack, stackptr, right, 0, false, true);
                    if (insertion) {
                        stackptr *= (stackptr + 2 <= entries - 1 ? 1 : 0);
                        right    *= (stackptr + 2 <= entries - 1 ? 1 : 0); 
                    }
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
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 6) return 4;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int variant) {
        if (variant == 3 || variant == 4) medianpivot = true;
        if (variant == 5 || variant == 6) randompivot = true;
        if (variant == 1 || variant == 3 || variant == 5) standalone = true;
        else insertion = true; 
        magnetica(array, 0, currentLength - 1);
    }
}