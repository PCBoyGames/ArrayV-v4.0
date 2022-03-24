package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class SiftSort extends Sort {

	public SiftSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Sift");
		this.setRunAllSortsName("Sift Sort");
		this.setRunSortName("Siftsort");
		this.setCategory("Exchange Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	private void sift(int[] l1, int left, int right, int sum) {
		if (right - left < 2)
			return;
		int meanleft = 0, meanright = 0;
		int count = 0;

		int i = left, j = right;
		while (i < j) {
			if (Reads.compareValues(l1[j], sum) <= 0) {
				while (Reads.compareValues(l1[i], sum) < 0) {
					i++;
				}
				int hold = l1[i];
				meanleft += (l1[i] = l1[j]);

				meanright += (l1[j--] = hold);
				count++;
			} else
				meanright += l1[j--];
		}

		if (count > 0) {
			if (i != 0) sift(l1, left, left + i, meanleft / i);
			if (j != 0) sift(l1, left + i, right, meanright / j);
		}
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		int mean = 0;
		for (int i : array)
			mean += i;
		sift(array, 0, currentLength, mean / currentLength);
	}
}
