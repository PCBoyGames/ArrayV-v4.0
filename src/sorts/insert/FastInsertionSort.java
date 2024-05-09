package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

import java.util.Random;

/*
 *
MIT License

Copyright (c) 2019 w0rthy

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

final public class FastInsertionSort extends Sort {
    public FastInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Fast Insertion");
        this.setRunAllSortsName("Insertion Sort");
        this.setRunSortName("Insertsort");
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

		for (int j = 1; j < n; j++) {
			Highlights.markArray(1, j);

			if (Reads.compareIndices(array, j-1, j, 0.5, false) > 0) {
				Highlights.clearMark(1);

				int i = j;
				int t = array[j];

				do array[i] = array[--i];
				while (i > 0 && array[i-1] > t);

				Reads.setComparisons(Reads.getComparisons().intValue() + j-i);
				Writes.changeWrites(j-i);

				Highlights.markArray(2, i+r.nextInt(j-i));
				Writes.write(array, i, t, (double)j/n, true, false);
			}
		}
    }
}