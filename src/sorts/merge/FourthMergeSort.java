package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class FourthMergeSort extends Sort {
    public FourthMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Fourth Merge");
        this.setRunAllSortsName("Control and thatsOven's Fourth Merge Sort");
        this.setRunSortName("Control and thatsOven's Fourth Merge Sort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void multiSwap(int[] array, int a, int b, int len) {
		for (int i = 0; i < len; i++)
			Writes.swap(array, a+i, b+i, 1, true, false);
        Highlights.clearAllMarks();
	}

    private void multiSwapBW(int[] array, int a, int b, int len) {
		for (int i = 0; i < len; i++)
			Writes.swap(array, a+len-i-1, b+len-i-1, 1, true, false);
        Highlights.clearAllMarks();
	}

    private void insertTo(int[] array, int from, int to) {
		int temp = array[from];
		for (int i = from-1; i >= to; i--)
			Writes.write(array, i+1, array[i], 0.5, true, false);
		Writes.write(array, to, temp, 0.5, true, false);
        Highlights.clearAllMarks();
	}

    private void insertToBW(int[] array, int from, int to) {
		Highlights.clearMark(2);
		int temp = array[from];
		for (int i = from; i < to; i++)
			Writes.write(array, i, array[i+1], 0.5, true, false);
		Writes.write(array, to, temp, 0.5, true, false);
        Highlights.clearAllMarks();
	}

    private int binarySearch(int[] array, int start, int end, int value, boolean left) {
		int a = start, b = end, comparison;
		while (a < b) {
			int m = (a+b)/2;
			boolean comp;
            comparison = Reads.compareValues(value, array[m]);
			if (left) comp = comparison <= 0;
			else     comp = comparison < 0;
			if (comp) b = m;
			else     a = m+1;
		}
		return a;
	}

    private void rotate(int[] array, int a, int m, int b) {
        while (b - m > 1 && m - a > 1) {
            if (b - m < m - a) {
                this.multiSwap(array, a, m, b - m);
                a += b - m;
            } else {
                this.multiSwapBW(array, a, b - (m - a), m - a);
                b -= m - a;
            }
        }

        if     (b - m == 1) this.insertTo(array, m, a);
		else if (m - a == 1) this.insertToBW(array, a, b-1);
    }

    private void inPlaceMergeWithoutBuffer(int[] array, int a, int m, int b) {
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

    private void merge(int[] array, int a, int m, int b, int p) {
        int pLen = m-a;
    	int i = 0, j = m, k = a;

    	while (i < pLen && j < b) {
    		if (Reads.compareValues(array[p+i], array[j]) <= 0)
    			Writes.swap(array, k++, p+(i++), 1, true, false);
    		else
    			Writes.swap(array, k++, j++, 1, true, false);
    	}
        while (i < pLen) Writes.swap(array, k++, p+(i++), 1, true, false);
    }

    private void compNSwap(int[] array, int a, int b, int start) {
        if (Reads.compareIndices(array, start+a, start+b, 1, true) > 0) {
            Writes.swap(array, start+a, start+b, 1, true, false);
        }
    }

    public void mergeSort(int[] array, int a, int b) {
        if (b - a > 4) {
            int m = a + ((b - a) / 2);
            int q = (m - a) / 2;

            this.mergeSort(array, m, b);
            while (q > 16) {
                this.mergeSort(array, a, a+q);
                this.merge(array, a+q, m, b, a);
                m -= q;
                q /= 2;
            }
            this.mergeSort(array, a, m);
            this.inPlaceMergeWithoutBuffer(array, a, m, b);

        } else switch (b - a) {
            case 4: {
                this.compNSwap(array, 0, 1, a);
                this.compNSwap(array, 2, 3, a);
                this.compNSwap(array, 1, 3, a);
                this.compNSwap(array, 0, 2, a);
                this.compNSwap(array, 1, 2, a);
                break;
            }
            case 3: {
                this.compNSwap(array, 0, 1, a);
                this.compNSwap(array, 1, 2, a);
                this.compNSwap(array, 0, 1, a);
                break;
            }
            case 2: {
                this.compNSwap(array, 0, 1, a);
            }
            default: return;
        }
    }




    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.mergeSort(array, 0, currentLength);
    }
}