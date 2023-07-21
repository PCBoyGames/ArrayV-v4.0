package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class CiuraCappedShipperSort extends MadhouseTools {

    boolean checks = true;
    int cap;
    int passgap;

    public CiuraCappedShipperSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Shipper (Capped, Ciura)");
        this.setRunAllSortsName("Shipper Sort (Capped, Ciura)");
        this.setRunSortName("Shippersort (Capped, Ciura)");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int[] gaps = {1, 4, 10, 23, 57, 132, 301, 701, 1750, 3938, 8861};

    protected int ciura(int n) {
        if (n <= gaps.length) {
            return gaps[n - 1];
        }
        return (int) Math.pow(2.25, n);
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
                if (h < ciura(cap)) h++;
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
        cap = 0;
        while (ciura(cap + 1) < currentLength) cap++;
        int start = 0;
        while (checks && cap > 0) {
            shipPass(array, start = minSorted(array, start, currentLength, 0.01, true), currentLength);
            while (ciura(cap) >= passgap) cap--;
        }
        shellPass(array, start, currentLength, 1);
    }
}