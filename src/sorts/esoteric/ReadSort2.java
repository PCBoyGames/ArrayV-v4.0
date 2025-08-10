package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.BogoSorting;

public class ReadSort2 extends BogoSorting {
    public ReadSort2(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Read (Insertion)");
        this.setRunAllSortsName("Read Sort (Insertion)");
        this.setRunSortName("Readsort (Insertion)");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }

    public void bogoSwapTSoS(int[] array, int start, int end, int[] aux, double sleep) {
        for (int k = 0; k < (end - start); k += 2) {
            if (Reads.compareValues(array[k], array[k + 1]) > 0) {
                Writes.write(aux, k, array[k + 1], sleep, true, true);
                Writes.write(aux, k + 1, array[k], sleep, true, true);
            } else {
                Writes.write(aux, k,     array[k],     sleep, true, true);
                Writes.write(aux, k + 1, array[k + 1], sleep, true, true);
            }
        }
        for (int k = 0; k < (end - start); k += 2) {
            if (aux[k] < aux[k + 1]) {
                Highlights.markArray(3, k);
                Highlights.markArray(4, k + 1);
                Writes.swap(array, aux[k], aux[k + 1], sleep*160, true, false);
            }
        }
 }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	int[] tSoS = Writes.createExternalArray(length);
    	double sleep = 0.1;
    	int okwecanstopnowwewentwaywaytoofar = 0;
        while(!this.isArraySorted(array, length) && okwecanstopnowwewentwaywaytoofar < length*length) {
            this.bogoSwapTSoS(array, 0, length, tSoS, sleep);
            okwecanstopnowwewentwaywaytoofar++;
            sleep /= 2;
        }
        Writes.deleteExternalArray(tSoS);

        new InsertionSort(arrayVisualizer).customInsertSort(array, 0, length, delay, false);
    }
}
