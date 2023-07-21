package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ShellLowSort extends Sort {
    public ShellLowSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Shell (Low Prime)");
        this.setRunAllSortsName("Shell Sort (Low Prime)");
        this.setRunSortName("Shellsort (Low Prime)");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void shellPass(int[] array, int currentLength, int gap) {
        for (int h = gap, i = h; i < currentLength; i++) {
            int v = array[i];
            int j = i;
            boolean w = false;
            Highlights.markArray(1, j);
            Highlights.markArray(2, j - h);
            Delays.sleep(0.25);
            for (; j >= h && Reads.compareValues(array[j - h], v) == 1; j -= h) {
                Highlights.markArray(2, j - h);
                Writes.write(array, j, array[j - h], 0.25, w = true, false);
            }
            if (w) Writes.write(array, j, v, 0.25, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int primetesti = 2;
        int gap = currentLength;
        while (gap != 1) {
            double primetestrunning = gap;
            while (primetestrunning == gap) {
                boolean primetest = false;
                primetesti = 2;
                while (!primetest) {
                    if (Math.floor(primetestrunning / primetesti) == primetestrunning / primetesti) {
                        primetestrunning = primetestrunning / primetesti;
                        primetest = true;
                    } else primetesti++;
                }
            }
            gap /= primetesti;
            shellPass(array, currentLength, gap);
        }
    }
}