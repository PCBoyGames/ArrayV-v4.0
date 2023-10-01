package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class BackwardsCycleSort extends Sort {
	public BackwardsCycleSort(ArrayVisualizer arrayVisualizer)  {
		super(arrayVisualizer);

		this.setSortListName("Backwards Cycle");
		this.setRunAllSortsName("Backwards Cycle Sort");
		this.setRunSortName("Backward Cyclesort");
		this.setCategory("Selection Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(true);
		this.setUnreasonableLimit(2048);
		this.setBogoSort(false);
	}

	//Selects the kth smallest element in O(n log n) with no element moves
	private int kthSmallest(int[] array, int start, int end, int k) {
		int min = array[start];
		int max = array[start];
		for (int i = start; i <= end; i++) {
			if (Reads.compareValues(array[i], max) == 1) {
				max = array[i];
			} else if (Reads.compareValues(array[i], min) == -1) {
				min = array[i];
			}
		}

		int low = min;
		int high = max;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			int lessCount = 0;
			int equalCount = 0;
			for (int i = start; i <= end; i++) {
				int cmp = Reads.compareIndexValue(array, i, mid, 0.1, true);
				if (cmp == -1) {
					lessCount++;
				} else if (cmp == 0) {
					equalCount++;
				}
			}
			if (lessCount < k && lessCount + equalCount >= k) {
				//We have identified the kth smallest element. Now do a linear search to determine which one because there might be duplicate elements
				int count = 0;
				for (int i = start; i < end; i++) {
					if (Reads.compareValues(array[i], mid) == 0) {
						count++;
						if (count == k - lessCount) {
							return i;
						}
					}
				}
				return end;
			} else if (lessCount >= k) {
				high = mid - 1;
			} else {
				low = mid + 1;
			}
		}
		return high;
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		for (int i = 0; i < length; i++) {
			int j = i;
			Highlights.markArray(3, i);
			int pos = kthSmallest(array, i, length - 1, j - i + 1);
			while (pos != j) {
				Writes.swap(array, j, pos, 1, true, false);
				Highlights.clearMark(2);
				Highlights.markArray(4, j);
				j = pos;
				pos = kthSmallest(array, i + 1, length - 1, j - i);
			}
			Highlights.clearMark(4);
		}
	}
}