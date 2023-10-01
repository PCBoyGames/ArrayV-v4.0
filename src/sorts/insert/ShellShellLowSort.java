package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class ShellShellLowSort extends MadhouseTools {
    public ShellShellLowSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Shell Shell (Low Prime)");
        this.setRunAllSortsName("Shell Shell Sort (Low Prime)");
        this.setRunSortName("Shell Shellsort (Low Prime)");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void combPass(int[] array, int currentLength, int gap) {
        for (int i = 0; i + gap < currentLength; i++) if (Reads.compareIndices(array, i, i + gap, 0.25, true) > 0) Writes.swap(array, i, i + gap, 0.25, true, false);
    }

    protected void shellPass(int[] array, int currentLength, int gap) {
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

    protected void shellShellPass(int[] array, int currentLength, int gap, int set) {
        int init = gap;
        if (set == 2) set++;
        else set--;
        if (init > 1) {
            int[] tree = factorTree(init);
            String treeString = "[";
            for (int i = 0; i < tree.length; i++) {
                treeString += (tree[i] + ", ");
            }
            treeString = treeString.substring(0, treeString.length() - 2) + "]";
            arrayVisualizer.setExtraHeading(" / " + init + " = " + treeString + " / Smooth: " + set);
        } else {
            arrayVisualizer.setExtraHeading(" / 1 = [] / Smooth: " + set);
        }
        for (; gap <= currentLength; gap *= set);
        for (; gap >= init; gap /= set) {
            if (set == 3) combPass(array, currentLength, gap);
            else shellPass(array, currentLength, gap);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int gap = currentLength;
        int[] tree = factorTree(gap);
        String treeString = "[";
        for (int i = 0; i < tree.length; i++) {
            treeString += (tree[i] + ", ");
        }
        treeString = treeString.substring(0, treeString.length() - 2) + "]";
        arrayVisualizer.setExtraHeading(" / " + gap + " = " + treeString);
        for (int i = 0; i < currentLength; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.25);
        }
        while (gap != 1) {
            int s = lowPrime(gap);
            gap /= s;
            shellShellPass(array, currentLength, gap, s);
        }
    }
}