/**
 * 
 */
package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class TerrorismSort extends Sort {
	public TerrorismSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		setSortListName("Terrorism");
		setRunAllSortsName("Terrorism Sort");
		setRunSortName("Terrorismsort");
		setCategory("Esoteric Sorts");
		setComparisonBased(true);
		setBucketSort(false);
		setRadixSort(false);
		setUnreasonablySlow(true);
		setUnreasonableLimit(32);
		setBogoSort(false);

	}

	private void horror(int[] a, int i, int j, int k) {
		if(i >= k || j < 0 || i < 0)
			return;
		
		if (i != j && Reads.compareValues(a[i], a[j]) == -1) {
			Writes.swap(a, i, j, 0.025, true, false);
		}
		this.Delays.sleep(0.005D);

		this.Highlights.markArray(1, i);
		this.Highlights.markArray(2, j);
		this.horror(a, i+1, j, k);
		this.horror(a, i, j-1, k);
	}

	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) {
		horror(array, 0, sortLength-1, sortLength);
	}

}
