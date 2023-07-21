package sorts.distribute;

import main.ArrayVisualizer;
import sorts.select.SelectionSort;
import sorts.templates.Sort;

public final class MagicSort extends Sort {

    SelectionSort select;

    public MagicSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Magic");
        this.setRunAllSortsName("Magic Sort");
        this.setRunSortName("Magic Sort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        select = new SelectionSort(this.arrayVisualizer);
        int[] aux = Writes.createExternalArray(currentLength);
        Writes.arraycopy(array, 0, aux, 0, currentLength, 0.1, true, true);
        select.selection(aux, 0, currentLength, 0.01, true);
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int j = 0; j < currentLength; j++) {
                if (Reads.compareValues(array[j], aux[j]) > 0) {
                    Delays.sleep(0.05);
                    Writes.write(array, j, array[j]-1, 0.05, true, false);
                    sorted = false;
                } else if (Reads.compareValues(array[j], aux[j]) < 0) {
                    Delays.sleep(0.05);
                    Writes.write(array, j, array[j]+1, 0.05, true, false);
                    sorted = false;
                }
            }
        }
    }
}
