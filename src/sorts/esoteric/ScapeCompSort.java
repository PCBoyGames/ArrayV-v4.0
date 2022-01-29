package sorts.esoteric;
import main.ArrayVisualizer;
import sorts.templates.Sort;

public class ScapeCompSort extends Sort {
	public ScapeCompSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
        this.setSortListName("Scape Comparison");
        this.setRunAllSortsName("Scape Comparison Sort");
        this.setRunSortName("Scapecomp Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(12);
        this.setBogoSort(false);
	}
	
	private void scapeComp(int[] array, int start, int end, int minElement, boolean direction) {
		if(start >= end || start < 0 || end < 0 || end > arrayVisualizer.getCurrentLength() || start > arrayVisualizer.getCurrentLength())
			return;
		Highlights.markArray(4, start);
		Highlights.markArray(5, end);
		Delays.sleep(0.0001);
		int min = start, mid = (start+end)/2;
		if(direction) {
			for(int i = start+1; i < end; i++) {
				if(Reads.compareValues(array[min], array[i]) == 1 && Reads.compareValues(array[i], minElement) == 1) {
					min = i;
				}
			}
			if(Reads.compareValues(array[start], array[min]) == 1) {
				Writes.swap(array, start, min, 1, true, false);
			}
		} else {
			min = end-1;
			for(int i = start; i < end-1; i++) {
				if(Reads.compareValues(array[min], array[i]) == -1 && Reads.compareValues(array[i], minElement) == -1) {
					min = i;
				}
			}
			if(Reads.compareValues(array[end-1], array[min]) == -1) {
				Writes.swap(array, end-1, min, 1, true, false);
			}
		}
		int minEl = array[min];
		if(min > mid) { // A recipe for trouble.
			this.scapeComp(array, start, min, minEl, direction);
			this.scapeComp(array, min+1, end, minEl, !direction);
			this.scapeComp(array, start+1, end, minEl, direction);
			this.scapeComp(array, start, end-1, minEl, !direction);
			this.scapeComp(array, start+1, end, minEl, !direction);
		} else {
			this.scapeComp(array, min+1, end, minEl, direction);
			this.scapeComp(array, start, min, minEl, !direction);
			this.scapeComp(array, start, end-1, minEl, direction);
			this.scapeComp(array, start+1, end, minEl, !direction);
			//this.scapeComp(array, start, end-1, minEl, !direction);
		}
	}
	
	@Override
	public void runSort(int[] array, int sortLength, int bucketLength) {
		this.scapeComp(array, 0, sortLength, Integer.MIN_VALUE, true);
	}
}