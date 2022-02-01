package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.Statistics;

public final class PullSelectionSortA extends Sort {
    public PullSelectionSortA(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pull Selection A");
        this.setRunAllSortsName("Pull Selection Sort (Type A)");
        this.setRunSortName("A-Pull Selection Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(32);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        Statistics.putStat("Pull");
        for(int i=0; i < length; i++) {
            boolean isLowest = false;
            while(!isLowest) {
                isLowest = true;
                for(int j=i+1; j<length; j++) {
                    if(Reads.compareValues(array[j], array[i]) == -1) {
                        isLowest = false;
                        Statistics.addStat("Pull");
                        Writes.multiSwap(array, i, j, 0.1, true, false);
                        break;
                    }
                }
            }
        }

    }
}
