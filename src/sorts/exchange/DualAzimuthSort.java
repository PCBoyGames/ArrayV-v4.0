package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;
import utils.Rotations;

final public class DualAzimuthSort extends BogoSorting {
    public DualAzimuthSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Dual Azimuth");
        this.setRunAllSortsName("Dual Azimuth Sort");
        this.setRunSortName("Dual Azimuth Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int azi = 0;
        while(!isArraySorted(array, currentLength)) {
            for(int i=azi; i<currentLength; i++) {
                int p = -1, s = 1;
                for(int j=i+1; j<currentLength; j++) {
                    if(Reads.compareValues(array[i], array[j]) == 1 && p < 0) {
                        p = j;
                    }
                }
                if(p >= 0) {
                    Rotations.holyGriesMills(array, azi, s, p-azi-s, 1, true, false);
                    i += s - 1;
                } else Writes.swap(array, i, azi++, 1, true, false);
            }
            if(azi >= currentLength)
                azi = 0;
        }
    }
}