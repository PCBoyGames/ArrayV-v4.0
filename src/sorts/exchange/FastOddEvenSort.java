package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

import java.util.Random;

//MIT License

final public class FastOddEvenSort extends Sort {
    public FastOddEvenSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Fast Odd-Even");
        this.setRunAllSortsName("Odd-Even Sort");
        this.setRunSortName("Odd-Even Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int n, int bucketCount) {
		Random r = new Random();
		int c = 1;

        for (int j = 0; j < n && (c > 0 || j < 2); j++) {
			c = 0;

			int rIdx = r.nextInt(n-1);
			Highlights.markArray(1, rIdx);
			Highlights.markArray(2, rIdx+1);

			for (int i = j%2 + 1; i < n; i += 2) {
				if (array[i-1] > array[i])
					{ int t = array[i-1]; array[i-1] = array[i]; array[i] = t; c++; }
			}
			Reads.setComparisons(Reads.getComparisons().intValue() + (n-j%2)/2);
			Writes.changeWrites(2*c);
			Delays.sleep(1);
		}
    }
}