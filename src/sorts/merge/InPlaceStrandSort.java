package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class InPlaceStrandSort extends Sort {
    public InPlaceStrandSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("In-Place Strand");
        this.setRunAllSortsName("In-Place Strand Sort");
        this.setRunSortName("In-Place Strand Sort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int binarySearch(int[] array, int start, int end, int value, boolean left) {
		int a = start, b = end;
		while (a < b) {
			int m = (a+b)/2;
			boolean comp;
			if (left) comp = Reads.compareValues(value, array[m]) <= 0;
			else     comp = Reads.compareValues(value, array[m]) < 0;
			if (comp) b = m;
			else     a = m+1;
		}
		return a;
	}

    public void rotate(int[] array, int start, int split, int end) {
        int temp;
        while ((split < end) && (split > start)) {
            if ((end-split)<(split-start)) {
                for (int i = split; i < end; i++) {
                    Writes.swap(array, i-(end-split), i, 1, true, false);
                }
                temp = end;
                end = split;
                split = (split-(temp-split));
            }
            else{
                for (int i = start; i < split; i++) {
                    Writes.swap(array, i, i+(split-start), 1, true, false);
                }
                temp = start;
                start = split;
                split = (split+(split-temp));
            }
        }
    }

    private void mergeForward(int[] array, int a, int m, int b) {
		int i = a, j = m, k;
		while (i < j && j < b) {
			if (Reads.compareValues(array[i], array[j]) == 1) {
				k = this.binarySearch(array, j, b, array[i], true);
				this.rotate(array, i, j, k);
				i += k-j;
				j = k;
			}
			else i++;
		}
    }

    private void mergeBackward(int[] array, int a, int m, int b) {
        int i = m-1, j = b-1, k;
        while (j > i && i >= a) {
            if (Reads.compareValues(array[i], array[j]) > 0) {
                k = this.binarySearch(array, a, i, array[j], true);
                this.rotate(array, k, i+1, j+1);
                j -= (i+1)-k;
                i = k-1;
            } else j--;
        }
    }

    private void merge(int[] array, int leftStart, int rightStart, int end) {
        if (end - rightStart < rightStart - leftStart) {
            this.mergeBackward(array, leftStart, rightStart, end);
        } else {
            this.mergeForward(array, leftStart, rightStart, end);
        }
    }

    private void strandSort(int[] array, int start, int end) {
        int sortStart = start+1;
        int backSortStart = start+1;
        int iterations = 0;

        while (sortStart <= end) {
            for (int i = sortStart; i < end; i++) {
                if (Reads.compareIndices(array, i, sortStart-1, 1, true) > 0) {
                    Writes.swap(array, sortStart++, i, 2, true, false);
                }
            }
            if (iterations > 0) {
                this.merge(array, start, backSortStart, sortStart);
            }

            sortStart++;
            backSortStart = sortStart-1;
            iterations++;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.strandSort(array, 0, currentLength);
    }
}