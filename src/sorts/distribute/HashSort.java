package sorts.distribute;

import java.util.Arrays;

import main.ArrayVisualizer;
import sorts.templates.Sort;


final public class HashSort extends Sort {
	public HashSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
		this.setSortListName("Hash");
		this.setRunAllSortsName("Hashsort");
		this.setRunSortName("Hashsort");
		this.setCategory("Distribution Sorts");
		this.setComparisonBased(false);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}
	
	private int getVal(int[] array, int v) {
		int z = array[v];
		if(arrayVisualizer.doingStabilityCheck()) {
			z = arrayVisualizer.getStabilityValue(z);
		}
		return z;
	}
	
	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int min = getVal(array, 0), max = min;
		
		for(int i = 1; i < length; i++) {
			int v = getVal(array, i);
			if(v < min) min = v;
			if(v > max) max = v;
		}
		
		int zero = min - 1;
		
		int[] table = Writes.createExternalArray(length);
		
		Arrays.fill(table, zero); Writes.changeAuxWrites(length);
		
		boolean freeL, freeR, opdone;
		for(int i=0; i<length; i++) {
			int position = (int) (((getVal(array, i) - min + 1) / (double) (max - min + 1)) * length),
				posLeft, posRight;
			if(position > 0) { 
				position--;
			}
			Highlights.markArray(1, i);
			opdone = false;
			while(!opdone) {
				if(table[position] == zero) {
					Writes.write(table, position, array[i], 1, true, true);
					opdone = true;
				} else {
					if(Reads.compareValues(array[i], table[position]) < 0 || position == length - 1) {
						freeL = freeR = false;
						posLeft = posRight = position;
						while(!freeR) {
							if(posRight < length - 1)
								posRight++;
							else
								break;
							if(table[posRight] == zero)
								freeR = true;
						}
						while(!(freeL || freeR)) {
							if(posLeft > 0)
								posLeft--;
							else
								break;
							if(table[posLeft] == zero)
								freeL = true;
						}
						if(!freeL && freeR) {
							for(; posRight > position;) {
								Writes.write(table, posRight, table[--posRight], 0.125, true, true);
							}
						}
						if(freeL && !freeR) {
							while(Reads.compareValues(array[i], table[position]) < 0)
								position--;
							for(; posLeft < position;) {
								Writes.write(table, posLeft, table[++posLeft], 0.125, true, true);
							}
						}
						Writes.write(table, position, array[i], 1, true, true);
						opdone = true;
					} else {
						position++;
					}
				}
			}
		}
		Writes.arraycopy(table, 0, array, 0, length, 1, true, false);
		Writes.deleteExternalArray(table);
	}
}