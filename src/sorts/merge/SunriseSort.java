package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;


public final class SunriseSort extends BogoSorting {
    public SunriseSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Sunrise");
        this.setRunAllSortsName("Sunrise Sort");
        this.setRunSortName("Sunrisesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private int insert(int[] temp, int item, int a, int b) {
        if(a != -1 && Reads.compareValues(temp[a], item) == 0) {
            int r = randInt(a, b+1);
            Writes.write(temp, b, temp[r], 0.5, true, true);
            Writes.write(temp, r, item, 0.5, true, true);
            return a;
        } else {
            Writes.write(temp, b, item, 0.5, true, true);
            return b;
        }
    }
    private void sunriseMerge(int[] array, int[] temp, int a, int b, int c) {
        int mid = b-1, to = a, len = c-a+1,
            to2 = -1;
        while(a <= mid && b <= c) {
            Highlights.markArray(1, a);
            Highlights.markArray(2, b);
            if(Reads.compareValues(array[a], array[b]) == -1) {
                to2 = this.insert(temp, array[a++], to2, to++);
            } else {
                to2 = this.insert(temp, array[b++], to2, to++);
            }
        }
        while(a <= mid) {
            to2 = this.insert(temp, array[a++], to2, to++);
        }
        while(b <= c) {
            to2 = this.insert(temp, array[b++], to2, to++);
        }
        for(int i=0; i<len; i++, c--) {
            Writes.write(array, c, temp[c], 1, true, false);
        }
    }
    private void sunrise(int[] array, int[] temp, int a, int b) {
        int mid = a + (b - a) / 2;
        if(a < b) {
            this.sunrise(array, temp, a, mid);
            this.sunrise(array, temp, mid+1, b);
            this.sunriseMerge(array, temp, a, mid+1, b);
        }
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] tmp = Writes.createExternalArray(length);
        this.sunrise(array, tmp, 0, length-1);
        Writes.deleteExternalArray(tmp);
    }
}