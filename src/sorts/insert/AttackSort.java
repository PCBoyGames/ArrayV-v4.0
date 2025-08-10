package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

public class AttackSort extends MadhouseTools {
	private SelsertionSort insertionSorter;

    public AttackSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Attack");
        this.setRunAllSortsName("Attack Sort");
        this.setRunSortName("Attack Sort");
        this.setCategory("Insertion Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

	public void attack(int[] array, int start, int end, int depth) {
		Writes.recordDepth(depth);
		while (true) {
			insertionSorter = new SelsertionSort(arrayVisualizer);
			if (end - start < 16) {
				insertionSorter.selsert(array, start, end, false);
				return;
			}
			int endp = end;
			int subLength = start + Math.min(end - start + 1, 16);
			insertionSorter.selsert(array, start, subLength, false);
			for (int i = start == end ? subLength : minSorted(array, start, end, 0.1, true); i < subLength; i = i == end ? subLength : minSorted(array, i, end, 0.1, true)) {
				int a = endp - 1;
				int b;
				Highlights.markArray(3, i);
				for (b = a; b > i; b--, a--) {
					Highlights.markArray(1, a);
					Highlights.markArray(2, b);
					if (Reads.compareValues(array[i], array[b]) <= 0) {if (a != b) Writes.swap(array, a, b, 0.5, true, false);}
					else a++;
					Delays.sleep(0.25);
				}
				Writes.swap(array, i, a, 0.5, true, false);
				if (a != b) {
					i += (a - b);
					subLength += (a - b);
					Writes.recursion();
					attack(array, b, a + 1, depth + 1);
				}
			}
			start = subLength;
		}
    }

	@Override
    public void runSort(int[] array, int length, int bucketCount) {
		attack(array, 0, length, 0);
    }
}
