package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;


public class OreganoSort extends BogoSorting {
    public OreganoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Oregano");
        this.setRunAllSortsName("Oregano Sort");
        this.setRunSortName("Oregano Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    private void sprinkle(int[] array, int start, int end) {
        if (end >= arrayVisualizer.getCurrentLength() || start>end)
            return;
        if (start != end && Reads.compareValues(array[start], array[end]) == 1) {
            Writes.multiSwap(array, start, end, 1, true, false);
            this.sort(array,start+1,end-1);
        } else {
        }
    }

    private void sort(int[] array, int start, int end) {
        this.sprinkle(array, start, end);
        int q = (end-start+1)/4;
        if (q==0)
            return;
        Highlights.markArray(1, start);
        Highlights.markArray(2, end);
        Delays.sleep(0.05);
        this.sort(array, start, end-q);
        this.sort(array, end-2*q, end);
        this.sort(array, start+q, end);
        this.sort(array, start, end-2*q);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.sort(array, 0, currentLength);
    }
}