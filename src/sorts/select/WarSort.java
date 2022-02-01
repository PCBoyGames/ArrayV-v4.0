package sorts.select;

import main.ArrayVisualizer;
import sorts.insert.PCBoysParShellSort;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class WarSort extends Sort {
    public WarSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("War");
        this.setRunAllSortsName("War Sort");
        this.setRunSortName("Warsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean changesinitems(int a, int b) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) != arrayVisualizer.getStabilityValue(b) : a != b;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) throws Exception {
        PCBoysParShellSort parshell = new PCBoysParShellSort(arrayVisualizer);
        int verifyi = 0;
        boolean verifypass = false;
        boolean changes = true;
        while (!verifypass && changes) {
            int i = verifyi;
            int[] extA = Writes.createExternalArray((int) Math.ceil(currentLength / 2));
            int extAlen = 0;
            int[] extB = Writes.createExternalArray((int) Math.ceil(currentLength / 2));
            int extBlen = 0;
            for (int j = 0; j < Math.ceil(currentLength / 2); j++) {
                Writes.visualClear(extA, j);
                Writes.visualClear(extB, j);
            }
            arrayVisualizer.setExtraHeading(" / Splitting");
            while (i < currentLength) {
                Highlights.markArray(1, i);
                if (i % 2 == 0) {
                    Writes.write(extA, extAlen, array[i], 0, false, true);
                    extAlen++;
                } else {
                    Writes.write(extB, extBlen, array[i], 0, false, true);
                    extBlen++;
                }
                Delays.sleep(0.25);
                i++;
            }
            i = currentLength - 1;
            changes = false;
            arrayVisualizer.setExtraHeading(" / Comparing");
            while (i >= verifyi) {
                if (extAlen > 0 && extBlen > 0) {
                    int cmp = Reads.compareValues(extA[extAlen - 1], extB[extBlen - 1]);
                    if (cmp >= 0) {
                        if (changesinitems(array[i], extA[extAlen - 1])) changes = true;
                        Writes.write(array, i, extA[extAlen - 1], 0.25, true, false);
                        Writes.visualClear(extA, extAlen - 1);
                        extAlen--;
                    }
                    if (cmp == 0) i--;
                    if (cmp <= 0) {
                        if (changesinitems(array[i], extB[extBlen - 1])) changes = true;
                        Writes.write(array, i, extB[extBlen - 1], 0.25, true, false);
                        Writes.visualClear(extB, extBlen - 1);
                        extBlen--;
                    }
                } else {
                    if (extAlen > 0) {
                        if (changesinitems(array[i], extA[extAlen - 1])) changes = true;
                        Writes.write(array, i, extA[extAlen - 1], 0.25, true, false);
                        Writes.visualClear(extA, extAlen - 1);
                        extAlen--;
                    }
                    if (extBlen > 0) {
                        if (changesinitems(array[i], extB[extBlen - 1])) changes = true;
                        Writes.write(array, i, extB[extBlen - 1], 0.25, true, false);
                        Writes.visualClear(extB, extBlen - 1);
                        extBlen--;
                    }
                }
                i--;
            }
            if (verifyi > 0) verifyi--;
            verifypass = true;
            while (verifyi + 1 < currentLength && verifypass) {
                if (Reads.compareIndices(array, verifyi, verifyi + 1, 0.25, true) <= 0) verifyi++;
                else verifypass = false;
            }
            Writes.deleteExternalArray(extA);
            Writes.deleteExternalArray(extB);
            Highlights.clearAllMarks();
        }
        if (!verifypass) {
            arrayVisualizer.setExtraHeading(" / Resorting to Par(X) Shell");
            Thread.sleep(1000);
            parshell.runSort(array, currentLength, 2);
        }
        arrayVisualizer.setExtraHeading("");
    }
}
