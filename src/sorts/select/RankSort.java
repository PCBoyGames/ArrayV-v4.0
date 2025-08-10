package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class RankSort extends Sort {
    public RankSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Rank");
        this.setRunAllSortsName("Rank Sort");
        this.setRunSortName("Ranksort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void rankSort(int[] array, int[] swap, int a, int b, double sleep) {
		for(int j = a; j < b; j++) {
			int c = 0;
			
			for(int i = a; i < b; i++) {
				if(i == j) continue;
				int cmp = Reads.compareIndices(array, i, j, sleep, true);
				if(cmp < 0 || (cmp == 0 && i < j)) c++;
			}
			Writes.write(swap, c, array[j], 0, false, true);
		}
        Writes.arraycopy(swap, 0, array, a, b - a, sleep, true, false);
    }

    public void rankSort(int[] array, int a, int b, double sleep) {
        int[] swap = Writes.createExternalArray(b - a);
        rankSort(array, swap, a, b, sleep);
        Writes.deleteExternalArray(swap);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        rankSort(array, 0, sortLength, 0.125);
    }
}

