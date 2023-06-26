package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class SafeAssSort extends BogoSorting {

    boolean changes = false;
    boolean changesthis = false;

    public SafeAssSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Safe Ass");
        this.setRunAllSortsName("Safe Ass Sort");
        this.setRunSortName("Safe Ass Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int stablereturn(int a) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) : a;
    }

    protected int par(int[] array, int len) {
        boolean[] max = new boolean[len];
        int maximum = stablereturn(array[0]);
        for (int i = 1; i < len; i++) {
            if (stablereturn(array[i]) > maximum) {
                maximum = stablereturn(array[i]);
                max[i] = true;
            }
        }
        int p = 1;
        for (int i = len - 1, j = len - 1; j >= 0 && i >= p; j--) {
            while (!max[j] && j > 0) j--;
            maximum = stablereturn(array[j]);
            while (maximum <= stablereturn(array[i]) && i >= p) i--;
            if (stablereturn(array[j]) > stablereturn(array[i]) && p < i - j) p = i - j;
        }
        return p;
    }

    protected void networksort(int[] array, int[] indexnetwork, int start, int length) {
        for (int i = 1; i < length; i += 2) {
            Highlights.markArray(3, start + i - 1);
            Highlights.markArray(4, start + i);
            pairsort(array, start + indexnetwork[i - 1], start + indexnetwork[i]);
        }
    }

    protected void pairsort(int[] array, int i, int j) {
        if (i > j) {
            int temp = i;
            i = j;
            j = temp;
        }
        if (Reads.compareIndices(array, i, j, 0.025, true) > 0) {
            Writes.swap(array, i, j, 0.025, true, false);
            changes = true;
            changesthis = true;
        }
    }

    protected void initializeCurve(int[] array, int currentLen) {
        int floorLog2 = (int) (Math.log(currentLen) / Math.log(2));
        for (int i = 0; i < currentLen; i++) {
            int value = (int) (currentLen * curveSum(floorLog2, (double) i / currentLen));
            Writes.write(array, i, value, 0.1, true, true);
        }
    }

    protected double curveSum(int n, double x) {
        double sum = 0;
        while (n >= 0) sum += curve(n--, x);
        return sum;
    }

    protected double curve(int n, double x) {
        return triangleWave((1 << n) * x) / (1 << n);
    }

    protected double triangleWave(double x) {
        return Math.abs(x - (int) (x + 0.5));
    }

    protected void linearInvert(int[] array, int currentLen) {
        int[] tmp = new int[currentLen];
        tableinvert(array, tmp, currentLen);
        Highlights.clearAllMarks();
        Writes.arraycopy(tmp, 0, array, 0, currentLen, 0.1, true, true);
    }

    protected void siftDown(int[] array, int[] keys, int r, int len, int a, int t) {
        int j = r;
        while (2*j + 1 < len) {
            j = 2*j + 1;
            if (j+1 < len) {
                int cmp = Reads.compareOriginalIndices(array, a+keys[j+1], a+keys[j], 0, true);
                if (cmp > 0 || (cmp == 0 && Reads.compareOriginalValues(keys[j+1], keys[j]) > 0)) j++;
            }
        }
        for (int cmp = Reads.compareOriginalIndices(array, a+t, a+keys[j], 0, true);
            cmp > 0 || (cmp == 0 && Reads.compareOriginalValues(t, keys[j]) > 0);
            j = (j-1)/2,
            cmp = Reads.compareOriginalIndices(array, a+t, a+keys[j], 0, true));
        for (int t2; j > r; j = (j-1)/2) {
            t2 = keys[j];
            Highlights.markArray(3, j);
            Writes.write(keys, j, t, 0, false, true);
            t = t2;
        }
        Highlights.markArray(3, r);
        Writes.write(keys, r, t, 0, false, true);
    }

    protected void tableSort(int[] array, int[] keys, int a, int b) {
        int len = b-a;
        for (int i = (len-1)/2; i >= 0; i--) this.siftDown(array, keys, i, len, a, keys[i]);
        for (int i = len-1; i > 0; i--) {
            int t = keys[i];
            Highlights.markArray(3, i);
            Writes.write(keys, i, keys[0], 0, false, true);
            this.siftDown(array, keys, 0, i, a, t);
        }
        Highlights.clearAllMarks();
    }

    protected void tableinvert(int[] array, int[] table, int currentLength) {
        for (int i = 0; i < currentLength; i++) Writes.write(table, i, i, 0, false, true);
        tableSort(array, table, 0, currentLength);
    }

    protected void prepareIndexes(int[] array, int length) {
        initializeCurve(array, length);
        linearInvert(array, length);
        linearInvert(array, length);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int indexeslen = par(array, currentLength);
        int lastlen = currentLength;
        int[] indexes = Writes.createExternalArray(indexeslen);
        boolean lenchange = true;
        int firstpos = 0;
        int nextlast = currentLength;
        int lastpos = currentLength;
        boolean firstfound = false;
        while (lastlen > 1) {
            changes = false;
            Highlights.clearAllMarks();
            if (lenchange) prepareIndexes(indexes, indexeslen);
            lenchange = false;
            firstfound = false;
            Highlights.clearAllMarks();
            for (int i = firstpos > 0 ? firstpos - 1 : 0; i + indexeslen <= (lastpos + 1 < currentLength ? lastpos + 1 : currentLength); i++) {
                changesthis = false;
                networksort(array, indexes, i, indexeslen);
                if (changes && !firstfound) {
                    firstpos = i;
                    firstfound = true;
                }
                if (changesthis) nextlast = i + indexeslen;
            }
            Highlights.clearAllMarks();
            lastpos = nextlast;
            if (!changes) {
                if (!isArraySorted(array, currentLength)) {
                    firstpos = 0;
                    lastpos = currentLength;
                    lastlen = indexeslen;
                    indexeslen = par(array, currentLength);
                    if (indexeslen == lastlen && indexeslen > 2) indexeslen--;
                    lenchange = true;
                    Writes.deleteExternalArray(indexes);
                    indexes = Writes.createExternalArray(indexeslen);
                } else break;
            }
        }
        Highlights.clearAllMarks();
        InsertionSort clean = new InsertionSort(arrayVisualizer);
        clean.customInsertSort(array, 0, currentLength, 10, false);
    }
}