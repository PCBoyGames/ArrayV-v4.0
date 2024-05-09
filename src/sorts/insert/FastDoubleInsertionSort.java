package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

import java.util.Random;

/*
 *
MIT License

Copyright (c) 2020-2021 Gaming32 and Morewenn

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

final public class FastDoubleInsertionSort extends Sort {
    public FastDoubleInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Fast Double Insertion");
        this.setRunAllSortsName("Double Insertion Sort");
        this.setRunSortName("Double Insertsort");
        this.setCategory("Insertion Sorts");
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
		int k = n/2, j = k-1+n%2;

		if (n%2 == 0 && Reads.compareIndices(array, j, k, 0.5, true) > 0)
			Writes.swap(array, j, k, 1, false, false);

		for (j--, k++; k < n; j--, k++) {
			int i;
			int t;

			if (Reads.compareIndices(array, j, k, 0.5, true) > 0) {
				t = array[k];
				Writes.write(array, k, array[j], 0, false, false);

				for (i = j; array[i+1] <= t; i++) array[i] = array[i+1];

				Reads.setComparisons(Reads.getComparisons().intValue() + i-j+1);
				Writes.changeWrites(i-j);

				if (i-j > 0) Highlights.markArray(2, j+r.nextInt(i-j));
				else        Highlights.clearMark(2);
				Writes.write(array, i, t, 2d*k/n-1, true, false);

				t = array[k];

				for (i = k; array[i-1] >= t; i--) array[i] = array[i-1];

				Reads.setComparisons(Reads.getComparisons().intValue() + k-i+1);
				Writes.changeWrites(k-i);

				if (k-i > 0) Highlights.markArray(2, k-r.nextInt(k-i));
				else        Highlights.clearMark(2);
				Writes.write(array, i, t, 2d*k/n-1, true, false);
			}
			else {
				t = array[j];

				for (i = j; array[i+1] < t; i++) array[i] = array[i+1];

				Reads.setComparisons(Reads.getComparisons().intValue() + i-j+1);
				Writes.changeWrites(i-j);

				if (i-j > 0) Highlights.markArray(2, j+r.nextInt(i-j));
				else        Highlights.clearMark(2);
				Writes.write(array, i, t, 2d*k/n-1, true, false);

				t = array[k];

				for (i = k; array[i-1] > t; i--) array[i] = array[i-1];

				Reads.setComparisons(Reads.getComparisons().intValue() + k-i+1);
				Writes.changeWrites(k-i);

				if (k-i > 0) Highlights.markArray(2, k-r.nextInt(k-i));
				else        Highlights.clearMark(2);
				Writes.write(array, i, t, 2d*k/n-1, true, false);
			}
		}
    }
}