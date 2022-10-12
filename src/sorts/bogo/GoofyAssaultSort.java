package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public final class GoofyAssaultSort extends BogoSorting {
    public GoofyAssaultSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Goofy Assault");
        this.setRunAllSortsName("Goofy Assault Sort");
        this.setRunSortName("Goofy Assaultsort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(6);
        this.setBogoSort(true);
    }
    private int b;

    private void clear(int[] bits) {
        for (int i = 0; i < bits.length; i++) {
            Writes.write(bits, i, 0, 0.001, true, true);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        b = 31-Integer.numberOfLeadingZeros(currentLength);
        int decCnt = 0;
        for (int i = 1; i < currentLength; i++) {
            decCnt += i;
        }
        int[] bits = Writes.createExternalArray((currentLength-1)/b+1), aux = Writes.createExternalArray(currentLength);
        int steps = 0;
        while (!isArraySorted(array, currentLength)) {
            Writes.arraycopy(array, 0, aux, 0, currentLength, 0.01, true, false);
            findindices:
            while (true) {
                for (int i = 0; i < currentLength; i++) {
                    Writes.write(array, i, currentLength - 1, 0.01, true, false);
                }
                for (int i = 0; i < decCnt; i++) {
                    int p;
                    do {
                        p = randInt(0, currentLength);
                    } while (Reads.compareOriginalValues(array[p], 0) == 0);
                    Writes.write(array, p, array[p] - 1, 0.01, true, false);
                }
                for (int i = 0; i < currentLength; i++) {
                    if ((bits[array[i]/b]&(1<<(array[i]%b))) > 0) {
                        clear(bits);
                        continue findindices;
                    }
                    Writes.write(bits, array[i]/b, bits[array[i]/b]|(1<<(array[i]%b)), 0.01, true, true);
                }
                break;
            }
            for (int i = 0; i < currentLength; i++) {
                Writes.write(array, i, aux[array[i]], 1, true, false);
            }
            clear(bits);
        }
        Writes.deleteExternalArrays(bits, aux);
    }
}