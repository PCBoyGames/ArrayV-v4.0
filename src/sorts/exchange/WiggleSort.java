package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author McDude_73
 * @author aphitorite
 * @author EilrahcF
 *
 */
public class WiggleSort extends Sort {

	/**
	 * @param arrayVisualizer
	 */
	public WiggleSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		setSortListName("Wiggle");
		setRunAllSortsName("Wiggle Sort");
		setRunSortName("Wigglesort");
		setCategory("Exchange Sorts");
		setComparisonBased(true);
		setBucketSort(false);
		setRadixSort(false);
		setUnreasonablySlow(false);
		setUnreasonableLimit(0);
		setBogoSort(false);

	}

	private void wiggleSort(int[] array, int start, int end) {
		if (end - start < 2) return;

		int midPoint = start + (end - start) / 2;

		boolean startLeft = true;
		int j = midPoint;

		for (int i = start; i < midPoint; i++) {
			for (int k = midPoint; k < end; k++) {
				if (Reads.compareIndices(array, i, j, 0.025, true) >= 0) {
					Writes.swap(array, i, j, 1.0D, true, false);
				}

				if (startLeft) {
					j++;
				} else {
					j--;
				}

			}
			if (startLeft) {
				j--;
				startLeft = false;
			} else {
				j++;
				startLeft = true;
			}
		}
		wiggleSort(array, start, midPoint);
		wiggleSort(array, midPoint, end);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		wiggleSort(array, 0, length);
	}
}
