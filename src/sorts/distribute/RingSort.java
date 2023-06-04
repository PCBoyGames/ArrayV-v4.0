package sorts.distribute;

import java.util.Arrays;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class RingSort extends Sort {
	public RingSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		this.setSortListName("Ring");
		this.setRunAllSortsName("Ring Sort");
		this.setRunSortName("Ring Sort");
		this.setCategory("Distribution Sorts");
		this.setComparisonBased(false);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}
	
	int[] getMinMax(int[] array, int len) {
		int[] minmax = new int[2];
		Arrays.fill(minmax, array[0]);
		Highlights.toggleAnalysis(true);
		Highlights.markArray(1, 0);
		Delays.sleep(1);
		for(int i=1; i<len; i++) {
			if(minmax[0] > array[i]) {
				minmax[0] = array[i];
			}
			
			if(minmax[1] < array[i]) {
				minmax[1] = array[i];
			}
			
			Highlights.markArray(1, i);
			Delays.sleep(1);
		}
		
		Highlights.toggleAnalysis(false);
		return minmax;
	}
	
	void sort(int[] array, int len) {
		int[] minmax = this.getMinMax(array, len);
		int min = minmax[0], max = minmax[1];
		int mem = 0, idx = 0;
		int[] count = new int[max-min+1];
		for(int i=0; i<len; i++) {
			count[array[i]-min]++;
		}
		Writes.changeAuxWrites(len);
		
		while(mem < len) {
			while(count[idx] == 0) {
				idx++;
			}
			
			int write = idx+min;
			for(int i=mem; i<len; i++) {
				if(count[idx] > 0) {
					count[idx]--;
					Highlights.markArray(2, mem);
					mem++;
				}
				Writes.write(array, i, write, 1, true, false);
			}
		}
	}
	
	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
		this.sort(array, sortLength);
	}
}
