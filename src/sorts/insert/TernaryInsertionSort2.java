package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Haruki
extending code by Meme Man

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Haruki
 * @author Meme Man
 *
 */
public class TernaryInsertionSort2 extends Sort {

    public TernaryInsertionSort2(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Ternary Insert 2");
        this.setRunAllSortsName("Ternary Insertion Sort");
        this.setRunSortName("Ternary Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public int ternarySearch(int[] array, int a, int b, int val, double sleep) {
        while (a < b) {
            int third = (b - a) / 3;
            int midA = a + third, midB = midA + third;
            Highlights.markArray(2, midA);
            Highlights.markArray(3, midB);
            Delays.sleep(sleep);
            if (Reads.compareValues(val, array[midA]) < 0)
                b = midA;
            else if (Reads.compareValues(val, array[midB]) >= 0)
                a = midB + 1;
            else {
                a = midA + 1;
                b = midB;
            }
        }
        Highlights.clearMark(2);
        Highlights.clearMark(3);
        return a;
    }

    public void insertionSort(int[] array, int a, int b, double rSleep, double wSleep) {
        for (int i = a + 1; i < b; i++) {
            int current = array[i];
            int dest = ternarySearch(array, a, i, current, rSleep);
            int pos = i;
            while (pos > dest) {
                Writes.write(array, pos, array[pos - 1], wSleep, true, false);
                pos--;
            }
            if (pos < i) Writes.write(array, pos, current, wSleep, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        insertionSort(array, 0, sortLength, 1, 0.05);

    }

}
