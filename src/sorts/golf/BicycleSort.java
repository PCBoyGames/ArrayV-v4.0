package sorts.golf;

import java.math.BigInteger;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.select.PreAphiCycleSort;
import sorts.templates.GrailSorting;

// cheater does the cheaty
public class BicycleSort extends GrailSorting {
    public BicycleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bicycle");
        this.setRunAllSortsName("Bicycle Sort");
        this.setRunSortName("Bicyclesort");
        this.setCategory("Golf Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private int getRank(int[] array, int start, int end, int index, int bound, boolean low) {
        BigInteger bits = BigInteger.ZERO;
        int excess = 0;
        for(int i=start; i<end; i++) {
            if(bits.testBit(array[i])) {
                if(((array[i] > bound) ^ low) && i != index && Reads.compareValues(array[i], array[index]) < 0) {
                    excess++;
                }
            } else {
                bits = bits.setBit(array[i]);
            }
        }
        for(int i=!low ? bound : array[index]; i<(!low ? array[index] : bound); i++) {
            if(bits.testBit(i)) {
                excess++;
            }
        }
        return excess;
    }
    private int getRank_stuck(int[] array, int start, int end, int index, int bound, boolean low) {
        int rank = 0;
        for(int j=start; j<end; j++) {
            if((low ^ (array[j] >= bound)) && j != index && Reads.compareValues(array[j], array[index]) < 0) {
                rank++;
            }
        }
        return rank;
    }
    private void tailCycle(int[] array, int start, int end, int to, int pivDisqualify) {
        boolean seekHi = false;
        if(end > to) {
            seekHi = true;
        }
        int length = end-start;
        for(int i=start; i<end; i++) {
            int rank, attempts = 0;
            do {
                rank= attempts >= length ? getRank_stuck(array, i, end, i, pivDisqualify, seekHi) +
                      (seekHi ? getRank_stuck(array, to, to+length, i, pivDisqualify, true) :
                                  getRank_stuck(array, to-length, to, i, pivDisqualify, false)) :
                      !seekHi ? getRank(array, start, end, i, pivDisqualify, false) + getRank(array, to-length, to, i, pivDisqualify, false) :
                                getRank(array, start, end, i, pivDisqualify, true) + getRank(array, to, to+length, i, pivDisqualify, true);
                if(rank == 0)
                    break;
                int now = seekHi ? to+(length-rank)-2 : to-length+rank+1;
                while(array[now] == array[i]) {
                    now++;
                }
                Writes.swap(array, i, now, 1, true, false);
                attempts++;
            } while(rank > 0);
        }
    }
    private void cycle(int[] array, int start, int end, int recursionsUntilFail) {
        PreAphiCycleSort c = new PreAphiCycleSort(arrayVisualizer);
        if(recursionsUntilFail == 0) {
            c.cycleSort(array, start, end+1, 0);
            return;
        }
        if(end == start)
            return;
        int piv = start + (end-start+1) / 2, x = array[piv];
        int i = start, j = end;
        while(i <= j) {
            while(Reads.compareValues(array[i], x) > 0) {
                i++;
                Highlights.markArray(1, i);
            }
            while(Reads.compareValues(array[j], x) < 0) {
                j--;
                Highlights.markArray(2, j);
            }
            if(i <= j)
                Writes.swap(array, i++, j--, 1, true, false);
        }
        if(i > piv) {
            this.tailCycle(array, j, end+1, start, x);
            int w = start + (end - j) - 1;
            if(w > start)
                this.cycle(array, w, end, --recursionsUntilFail);
            else
                c.cycleSort(array, start, end+1, 0);
        } else {
            this.tailCycle(array, start, i, end, x);
            int w = end - (i - start) + 1;
            if(w < end)
                this.cycle(array, start, w, --recursionsUntilFail);
            else
                c.cycleSort(array, start, end+1, 0);
        }
    }
    @Override
    public void runSort(int[] array, int len, int buck) {
        int p = 1, l = 0;
        while(p <= len/2) {
            p *= 2;
            l++;
        }
        this.cycle(array, 0, len-1, l+(l>>1));

        InsertionSort i = new InsertionSort(arrayVisualizer);
        i.customInsertSort(array, 0, len, 1, false);
    }
}