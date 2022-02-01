package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ParFurtherRandomShellSort extends BogoSorting {
    public ParFurtherRandomShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Par(X) Further Randomized Shell");
        this.setRunAllSortsName("Par(X) Further Randomized Shell Sort");
        this.setRunSortName("Par(X) Further Random Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void shellPass(int[] array, int currentLength, int par, int bounding) {
        if (bounding == 1) arrayVisualizer.setExtraHeading(" / Insertion Pass");
        else arrayVisualizer.setExtraHeading(" / Par(X): " + par + " / Bound: " + bounding);
        for (int h = randInt(1, bounding + 1), i = h; i < currentLength; i++) {
            int v = array[i];
            int j = i;
            boolean w = false;
            Highlights.markArray(1, j);
            Highlights.markArray(2, j - h);
            Delays.sleep(0.25);
            while (j >= h && Reads.compareValues(array[j - h], v) == 1) {
                Highlights.markArray(1, j);
                Highlights.markArray(2, j - h);
                Delays.sleep(0.25);
                Writes.write(array, j, array[j - h], 0.25, true, false);
                j -= h;
                w = true;
            }
            if (w) {
                Writes.write(array, j, v, 0.25, true, false);
            }
            h = randInt(1, i < bounding ? i + 1 : bounding + 1);
        }
    }

    protected int stablereturn(int a) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) : a;
    }

    private int nextBound(int[] array, int currentLength, int bounding) {
        boolean[] max = new boolean[currentLength];
        int maximum = stablereturn(array[0]);
        for (int i = 1; i < currentLength; i++) {
            if (stablereturn(array[i]) > maximum) {
                maximum = stablereturn(array[i]);
                max[i] = true;
            }
        }
        int i = currentLength - 1;
        int p = 1;
        int j = currentLength - 1;
        while (j >= 0 && i >= p) {
            while(!max[j] && j > 0) j--;
            maximum = stablereturn(array[j]);
            while (maximum <= stablereturn(array[i]) && i >= p) i--;
            if (stablereturn(array[j]) > stablereturn(array[i]) && p < i - j) p = i - j;
            j--;
        }
        return p;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int bounding = currentLength - 1;
        while (bounding > Math.cbrt(currentLength)) {
            shellPass(array, currentLength, bounding, (int) ((bounding / 2) + bounding % 2));
            Highlights.clearAllMarks();
            bounding = nextBound(array, currentLength, bounding);
        }
        shellPass(array, currentLength, bounding, 1);
        arrayVisualizer.setExtraHeading("");
    }
}