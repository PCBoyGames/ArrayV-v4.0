package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public final class OmegaBojoSort extends BogoSorting {
    public OmegaBojoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Omega Bojo");
        this.setRunAllSortsName("Omega Bojo Sort");
        this.setRunSortName("Ω Bojosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(11);
        this.setBogoSort(true);
    }

    public void omegaReversal(int[] array, int a, int b, double sleep) {
        Writes.reversals++;
        Writes.swap(array, a, b, sleep, true, false);

        for(int i = a + 1; i < a + ((b - a + 1) / 2); i++) {
            Writes.swap(array, i, a + b - i, sleep, true, false);
            omegaReversal(array, i + 1, a + b - i - 1, sleep);
        }

    }

    private void omegaBojo(int[] array, int start, int end) {
        if(start < end) {
            int i = randInt(start, end);
            int j = randInt(start, end);
            //while(i == j) {
            //i = randInt(start, end);
            //j = randInt(start, end);
            //}
            omegaReversal(array, i, j, this.delay);
            if(j<i)
                omegaBojo(array, j, i);
            else
                omegaBojo(array, i, j);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while (!this.isArraySorted(array, length)) {
            omegaBojo(array, 0, length);
        }
    }
}
