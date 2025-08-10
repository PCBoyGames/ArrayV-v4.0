package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

+---------------------------+
| SORTING ALGORITHM SCARLET |
+---------------------------+
|    A sorting algorithm    |
|    studio by Flanlaina    |
|    (a.k.a Ayako-chan)     |
+---------------------------+

 */

/**
 * @author Flanlaina
 * @author fungamer2
 *
 */
public class FibonacciGnomeSort extends Sort {

    public FibonacciGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Fibonacci Gnome");
        this.setRunAllSortsName("Optimized Gnome Sort + Fibonacci Search");
        this.setRunSortName("Optimized Gnomesort + Fibonacci Search");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public int fibonacciSearch(int[] array, int start, int end, int item) {
        int fibM2 = 0;
        int fibM1 = 1;
        int fibM = 1;
        while (fibM <= end - start) {
            fibM2 = fibM1;
            fibM1 = fibM;
            fibM = fibM2 + fibM1;
        }
        int offset = start - 1;
        while (fibM > 1) {
            int i = Math.min(offset + fibM2, end);
            Highlights.markArray(1, offset + 1);
            Highlights.markArray(2, i);
            if (Reads.compareValues(array[i], item) <= 0) {
                fibM = fibM1;
                fibM1 = fibM2;
                fibM2 = fibM - fibM1;
                offset = i;
            } else {
                fibM = fibM2;
                fibM1 -= fibM2;
                fibM2 = fibM - fibM1;
            }
            Delays.sleep(0.6);
        }
        int position = ++offset;
        if (Reads.compareValues(array[position], item) <= 0) {
            position++;
        }
        return position;
    }

    public void fibonacciGnomeSort(int[] array, int start, int end) {
        for (int i = start + 1; i < end; i++) {
            int position = this.fibonacciSearch(array, start, i - 1, array[i]);
            int j = i;
            while (j > position) {
                Writes.swap(array, j, j - 1, 0.15, true, false);
                j--;
            }

        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        fibonacciGnomeSort(array, 0, sortLength);

    }

}
