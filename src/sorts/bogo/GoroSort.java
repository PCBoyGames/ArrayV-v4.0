package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public final class GoroSort extends BogoSorting {
    public GoroSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Goro");
        this.setRunAllSortsName("Goro Sort");
        this.setRunSortName("Gorosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(16);
        this.setBogoSort(true);
    }

    protected void swap(int[] a, int l, int r, double d, boolean m, boolean x) {
        if (l != r) Writes.swap(a, l, r, d, m, x);
    }

    protected void initializeGoro(int[] array, int arrayLen) {
        int indexCount = -1;
        int[] indexes = new int[arrayLen];
        for (int init = 0; init < arrayLen; init++) indexes[init] = -1;
        for (int i = 0; i < arrayLen; i++) if (Math.random() > 0.5D) indexes[++indexCount] = i;
        if (indexCount <= 0) simpleSwap(array, arrayLen);
        else goroSwap(array, indexes, indexCount);
    }

    protected void simpleSwap(int[] array, int arrayLen) {
        int a = (int)(Math.random() * arrayLen);
        int b = (int)(Math.random() * arrayLen);
        if (a == b) {
            if (b + 1 == arrayLen) b = 0;
            else b++;
        }
        swap(array, a, b, delay, true, false);
    }

    protected void goroSwap(int[] array, int[] indexArr, int indexCount) {
        for (int i = 0; i <= indexCount; i++) swap(array, goroRandPosition(indexCount), indexArr[i], delay, true, false);
    }

    protected int goroRandPosition(int indexCount) {
        return (int) (Math.random() * (indexCount + 1));
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (!isArraySorted(array, currentLength)) initializeGoro(array, currentLength);
    }
}