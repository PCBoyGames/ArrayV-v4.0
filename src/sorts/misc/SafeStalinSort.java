package sorts.misc;

import java.util.Deque;
import java.util.LinkedList;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES
IN COLLABORATION WITH GAMING32

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

Help.

 - The Madhouse CEO

*/
final public class SafeStalinSort extends Sort {
    public SafeStalinSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Safe Stalin");
        this.setRunAllSortsName("Safe Stalin Sort");
        this.setRunSortName("Safe Stalinsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    protected void outsideAdd(Deque<Integer> outside, int x) {
        Writes.startLap();
        outside.add(x);
        Writes.stopLap();
        Writes.changeAuxWrites(1);
        Writes.changeAllocAmount(1);
    }

    protected int outsidePop(Deque<Integer> outside) {
        int value;
        Writes.startLap();
        value = outside.pop();
        Writes.stopLap();
        Writes.changeAuxWrites(1);
        Writes.changeAllocAmount(-1);
        return value;
    }

    protected void remove(int[] array, int i, int len) {
        Writes.arraycopy(array, i + 1, array, i, len - i - 1, 0, false, false);
        Writes.visualClear(array, len - 1);
        Delays.sleep(1);
    }

    protected void add(int[] array, int x, int len) {
        Writes.write(array, len, x, 1, true, false);
    }

    protected void insert(int[] array, int i, int x, int len) {
        Writes.arraycopy(array, i, array, i + 1, len - i, 0, false, false);
        Writes.write(array, i, x, 1, true, false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        Deque<Integer> outside = new LinkedList<>();
        boolean pass = false;
        int getlen = currentLength;
        int auxlen = 0;
        while (!pass) {
            int i = 0;
            while (!(i + 1 > currentLength)) {
                i++;
                while (!(i + 1 > getlen)) {
                    int compare = Reads.compareIndices(array, i - 1, i, 0.5, true);
                    if (compare > 0) {
                        outsideAdd(outside, array[i]);
                        remove(array, i, currentLength--);
                        getlen--;
                        auxlen++;
                    } else i++;
                }
                Highlights.clearMark(2);
                for (int j = 0; j < auxlen; j++) {
                    add(array, outsidePop(outside), currentLength++);
                    getlen++;
                }
                getlen = currentLength;
                auxlen = 0;
            }
            int verifyI = 1;
            pass = true;
            while (!(verifyI == currentLength || !pass)) {
                int compare = Reads.compareIndices(array, verifyI - 1, verifyI, 0.5, true);
                if (compare <= 0) verifyI++;
                else pass = false;
            }
            if (!pass) {
                i = verifyI;
                Highlights.clearMark(2);
                Highlights.markArray(1, i);
                while (!(i + 1 > currentLength)) {
                    outsideAdd(outside, array[i]);
                    remove(array, i, currentLength--);
                    auxlen++;
                }
                i = 1;
                for (int j = 0; j < auxlen; j++) {
                    insert(array, i - 1, outsidePop(outside), currentLength++);
                    i++;
                }
                auxlen = 0;
            }
        }
        Writes.clearAllocAmount();
    }
}
