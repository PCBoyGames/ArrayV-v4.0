package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES
IN COLLABORATION WITH CONTROL AND MG-2018

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class OriginalPCBoysParShellSort extends Sort {

    int lastgap;

    public OriginalPCBoysParShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("PCBoy's Par Shell (Original)");
        this.setRunAllSortsName("PCBoy's Par(X) Shell Sort (Original)");
        this.setRunSortName("PCBoy's Par(X) Shellsort (Original)");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the division constant for this sort:", 2);
    }

    protected int stablereturn(int a) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) : a;
    }

    protected int par(int[] array, int len) {
        boolean[] max = new boolean[len];
        int maximum = stablereturn(array[0]);
        for (int i = 1; i < len; i++) {
            if (stablereturn(array[i]) > maximum) {
                maximum = stablereturn(array[i]);
                max[i] = true;
            }
        }
        int p = 1;
        for (int i = len - 1, j = len - 1; j >= 0 && i >= p; j--) {
            while (!max[j] && j > 0) j--;
            maximum = stablereturn(array[j]);
            while (maximum <= stablereturn(array[i]) && i >= p) i--;
            if (stablereturn(array[j]) > stablereturn(array[i]) && p < i - j) p = i - j;
        }
        return p;
    }

    protected void shellPass(int[] array, int currentLength, int gap, int par) {
        if (gap >= lastgap) return;
        lastgap = gap;
        arrayVisualizer.setExtraHeading(" / Par(X): " + par + " / Gap: " + gap);
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
    public int validateAnswer(int answer) {
        if (answer < 1) return 1;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int constantdiv) throws Exception {
        int lastpar = currentLength;
        lastgap = currentLength;
        while (true) {
            int par = this.par(array, currentLength);
            int passpar = par;
            if (par >= lastpar) par = lastpar - constantdiv;
            lastpar = par;
            if (par / constantdiv <= 1) {
                shellPass(array, currentLength, 1, par);
                break;
            }
            shellPass(array, currentLength, (int) ((par / constantdiv) + par % constantdiv), passpar);
        }
        arrayVisualizer.setExtraHeading("");
    }
}