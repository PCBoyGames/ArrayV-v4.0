package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

Why do I keep doing this to myself?

 */

public class NitroSort extends Sort {
    public NitroSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Nitro");
        this.setRunAllSortsName("Nitro Sort");
        this.setRunSortName("Nitrosort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter shrink factor (input/100):", 130);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 110) return 130;
        return answer;
    }

    private void combSort(int[] array, int a, int b, double shrink) {
        boolean swapped = false;
        int gap = b;
        int incs[] = {48, 21, 7, 3, 1};
        while ((gap > 1) || swapped) {
            if (gap > 1) gap = (int) (gap / shrink);
            swapped = false;
            for (int i = 0; (gap + i) < b; i++) {
                if (gap <= Math.min(8, b * 0.03125)) {
                    gap = 0;
                    for (int k = 0; k < incs.length; k++) {
                        for (int h = incs[k], l = h + a; l < b; l++) {
                            int v = array[l];
                            int j = l;
                            boolean change = false;
                            while (j >= h && Reads.compareValues(array[j-h], v) > 0) {
                                Writes.write(array, j, array[j - h], 1, true, false);
                                change = true;
                                j -= h;
                            }
                            if (change) Writes.write(array, j, v, 0.5, true, false);
                        }
                    }
                    break;
                }
                if (Reads.compareValues(array[i], array[i + gap]) == 1) {
                    Writes.swap(array, i, i+gap, 0.75, true, false);
                    swapped = true;
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        combSort(array, 0, currentLength, bucketCount/100D);
    }
}
