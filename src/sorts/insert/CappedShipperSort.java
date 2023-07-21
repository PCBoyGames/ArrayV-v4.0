package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class CappedShipperSort extends MadhouseTools {

    boolean checks = true;
    int cap;
    int passgap;

    public CappedShipperSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Shipper (Capped)");
        this.setRunAllSortsName("Shipper Sort (Capped)");
        this.setRunSortName("Shippersort (Capped)");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void shipPass(int[] array, int a, int b) {
        checks = false;
        passgap = 1;
        for (int h = 1, i = h + a; i < b; i++) {
            int v = array[i];
            int j = i;
            boolean w = false;
            Highlights.markArray(1, j);
            Highlights.markArray(2, j - h);
            Delays.sleep(0.05);
            for (; j >= h && j - h >= a && Reads.compareValues(array[j - h], v) > 0; j -= h) {
                Highlights.markArray(2, j - h);
                Writes.write(array, j, array[j - h], 0.1, w = true, false);
            }
            if (w) {
                Writes.write(array, j, v, 0.1, true, false);
                if (h > 4) checks = true;
                if (h < cap) h++;
                passgap = h;
            }
        }
    }

    protected void shellPass(int[] array, int start, int end, int gap) {
        for (int h = gap, i = h + start; i < end; i++) {
            int v = array[i];
            int j = i;
            boolean w = false;
            Highlights.markArray(1, j);
            Highlights.markArray(2, j - h);
            Delays.sleep(0.5);
            for (; j >= h && j - h >= start && Reads.compareValues(array[j - h], v) == 1; j -= h) {
                Highlights.markArray(2, j - h);
                Writes.write(array, j, array[j - h], 0.5, w = true, false);
            }
            if (w) Writes.write(array, j, v, 0.5, true, false);
        }
        Highlights.clearAllMarks();
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        cap = (int) (currentLength - (Math.sqrt(currentLength) + Math.cbrt(currentLength)));
        int start = 0;
        while (checks && cap > 1) {
            shipPass(array, start = minSorted(array, start, currentLength, 0.01, true), currentLength);
            cap = (int) (passgap - (Math.sqrt(passgap) + Math.cbrt(passgap)));
        }
        shellPass(array, start, currentLength, 1);
    }
}