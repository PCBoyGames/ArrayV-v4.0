package sorts.golf;

import java.math.BigInteger;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
import sorts.insert.InsertionSort;
import sorts.select.CycleSort;
import sorts.templates.GrailSorting;
import utils.IndexedRotations;
import utils.Searches;

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
	private void tailCycle(int[] array, int start, int end, int to, int pivDisqualify) {
		boolean seekHi = false;
		if(end > to) {
			seekHi = true;
		}
		int length = end-start;
		for(int i=start; i<end; i++) {
			int rank;
			do {
				rank = !seekHi ? getRank(array, start, end, i, pivDisqualify, false) + getRank(array, to-length, to, i, pivDisqualify, false) :
					            getRank(array, start, end, i, pivDisqualify, true) + getRank(array, to, to+length, i, pivDisqualify, true);
				if(rank == 0)
					break;
				Writes.swap(array, i, seekHi ? to+(length-rank)-2 : to-length+rank+1, 1, true, false);
			} while(rank > 0);
		}
	}
	private void cycle(int[] array, int start, int end) {
		if(end == start)
			return;
		int piv = start + (end-start+1) / 2, x = array[piv];
		int i = start, j = end;
		CycleSort c = new CycleSort(arrayVisualizer);
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
				this.cycle(array, w, end);
			else
				c.cycleSort(array, start, end+1, 0);
		} else {
			this.tailCycle(array, start, i, end, x);
			int w = end - (i - start) + 1;
			if(w < end)
				this.cycle(array, start, w);
			else
				c.cycleSort(array, start, end+1, 0);
		}
	}
	@Override
	public void runSort(int[] array, int len, int buck) {
		this.cycle(array, 0, len-1);
	}
}