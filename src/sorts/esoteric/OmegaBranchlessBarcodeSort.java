package sorts.esoteric;
import java.util.Comparator;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public class OmegaBranchlessBarcodeSort extends BogoSorting {
    public OmegaBranchlessBarcodeSort(ArrayVisualizer aV) {
        super(aV);
        this.setSortListName("Omega Branchless Barcode");
        this.setRunAllSortsName("Omega Branchless Barcode Sort");
        this.setRunSortName("Omega Branchless Barcodesort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private void omegaBarcode(int[] array, int start, int end, int order) {
        if (order < 1 || start >= end)
            return;
        int j = randInt(start, end), g = randInt(start, end);
        for (int i=start; i<end; i++) {
            for (int h=start; h<g; h++) {
                int k = Math.min(i, j),
                    l = Math.max(i, j),
                    cmp = Reads.compareIndices(array, l, k, 0.005, true) >> 31;
                for (int m=0; m<order && m<l-k; m++) {
                    Writes.reversal(array, (m+k)&cmp, l&cmp, 1, true, false);
                    omegaBarcode(array, m, l-k-1, (order-1)&cmp);
                }
                for (int m=k; m<l; m++) {
                    omegaBarcode(array, m+1, l, order-1);
                }
                if (i < end)
                    j = i + randInt(0, end-i);
            }
            g = randInt(start, end);
        }
    }

    private int sign(int v) {
        return (v>>31) | -(-v>>31);
    }

    private int binsearch(int[] array, int start, int end, int key, double slp, Comparator<Integer> bcmp) {
        while (end-start > 0) {
            int l = end - start,
                c = bcmp.compare(key, array[start+l/2]); // branchless compare here
            Highlights.markArray(1, start+l/2);
            Delays.sleep(slp);
            binaryInsertSort(array, start, start + (l / 2), slp, 0);
            binaryInsertSort(array, end - (l / 2), end, slp, 0);
            start += ((l / 2) + 1) * sign(c+1);
            end -= ((l + 1) / 2) * -sign(c-1);
        }
        Highlights.clearAllMarks();
        return start;
    }

    // Binary Dead Insertion ("Bitrot Sort")
    public void binaryInsertSort(int[] array, int start, int end, double compSleep, double writeSleep) {
        for (int i = start + 1; i < end; i++) {
            int num = array[i];
            int src = binsearch(array, start, i, num, compSleep, Reads::compareValues);

            int j = i - 1;

            while (j >= src)
            {
                Writes.write(array, j + 1, array[j], writeSleep, true, false);
                binaryInsertSort(array, start, j, compSleep, writeSleep);
                j--;
            }
            if (src < i)
                Writes.write(array, src, num, writeSleep, true, false);
            while (j > start)
                binaryInsertSort(array, start, j--, compSleep, writeSleep);

            Highlights.clearAllMarks();
        }
    }
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        omegaBarcode(array, 0, sortLength, sortLength);
        binaryInsertSort(array, 0, sortLength, 0.005, 0.005);
    }
}