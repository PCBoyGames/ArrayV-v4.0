package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

import java.util.ArrayList;

final public class CasinoSort extends Sort {
    public CasinoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Casino");
        this.setRunAllSortsName("Casino/\"Block Bingo\" Sort");
        this.setRunSortName("Casino Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    public void multiSwap(int[] array, int a, int b, int s) {
        while(s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }
    // Don't worry, you don't need to call me the "man that plays bingo",
    // I've done much more than just Bingo sorts.
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        ArrayList<Integer> bingoSizes = new ArrayList<>();
        for (int i = 0; i < length;) {
            int similar = 1;
            for(int j=i+1; j<length; j++) {
                if(Reads.compareValues(array[j], array[i]) == 0) {
                    Writes.multiSwap(array, j, i + similar++, 0.01, true, false);
                }
            }
            if(bingoSizes.size() >= length / 2) {
                LazyHeapSort lhs = new LazyHeapSort(arrayVisualizer);
                Writes.arrayListClear(bingoSizes);
                lhs.runSort(array, length, bucketCount);
                return;
            }
            Writes.arrayListAdd(bingoSizes, similar);
            i += similar;
        }
        int bingoNow = 0;
        while(bingoSizes.size() > 0) {
            int sizeNow = bingoSizes.remove(0);
            Writes.changeAllocAmount(-1);
            int min=-1, minIndex = bingoNow, testIndex = bingoNow + sizeNow;
            for(int i=0; i<bingoSizes.size(); i++) {
                if(Reads.compareValues(array[minIndex], array[testIndex]) > 0) {
                    minIndex = testIndex;
                    min = i;
                }
                testIndex += bingoSizes.get(i);
            }
            if(min >= 0) {
                if(sizeNow == bingoSizes.get(min)) {
                    multiSwap(array, minIndex, bingoNow, sizeNow);
                    bingoNow += sizeNow;
                } else {
                    IndexedRotations.griesMills(array, bingoNow, minIndex, minIndex + bingoSizes.get(min), 0.025, true, false);
                    bingoNow += bingoSizes.remove(min);
                    bingoSizes.add(0, sizeNow);
                }
            } else
                bingoNow += sizeNow;
        }
    }
}