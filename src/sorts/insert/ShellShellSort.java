package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class ShellShellSort extends Sort {
    public ShellShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Shell Shell");
        this.setRunAllSortsName("Shell Shell Sort");
        this.setRunSortName("Shell Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the base for this sort:\n(Default: 3)", 3);
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
        for (; gap <= currentLength; gap *= set);
        for (; gap >= init; gap /= set) {
            if (set == 3) combPass(array, currentLength, gap);
            else shellPass(array, currentLength, gap);
        }
    }

    public int validateAnswer(int answer) {
        if (answer < 2) answer = 2;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        int gap = base;
        for (; gap <= currentLength; gap *= base);
        gap /= base;
        while (gap != 1) {
            gap /= base;
            if (gap < 1) gap = 1;
            shellShellPass(array, currentLength, gap, base);
        }
    }
}