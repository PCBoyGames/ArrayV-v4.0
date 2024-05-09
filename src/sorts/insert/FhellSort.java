package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class FhellSort extends MadhouseTools {
    public FhellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Fhell");
        this.setRunAllSortsName("Fhell Sort");
        this.setRunSortName("Fhellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void shellPass(int[] array, int currentLength, int gap, boolean speed) {
        for (int h = gap, i = h; i < currentLength; i++) {
            int v = array[i];
            int j = i;
            boolean w = false;
            if (!speed) {
                Highlights.markArray(1, j);
                Highlights.markArray(2, j - h);
                Delays.sleep(0.25);
                for (; j >= h && Reads.compareValues(array[j - h], v) == 1; j -= h) {
                    Highlights.markArray(2, j - h);
                    Writes.write(array, j, array[j - h], 0.25, w = true, false);
                }
                if (w) Writes.write(array, j, v, 0.25, true, false);
            } else {
                int c = 0;
                for (; j >= h && Reads.compareValues(array[j - h], v) == 1; j -= h) {
                    array[j] = array[j - h];
                    c++;
                    w = true;
                }
                if (w) {
                    Writes.writes += c;
                    Writes.write(array, j, v, 0.25, true, false);
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (lowPrime(currentLength) == currentLength) {
            shellPass(array, currentLength, 1, true);
            return;
        }
        for (int i = currentLength / 2; i >= 1; i--) if (currentLength % i == 0) shellPass(array, currentLength, i, false);
    }
}