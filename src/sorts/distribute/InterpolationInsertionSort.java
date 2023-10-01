package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2021 aphitorite

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

public class InterpolationInsertionSort extends Sort {
    public InterpolationInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Interpolation Insert");
        this.setRunAllSortsName("Interpolation Insertion Sort");
        this.setRunSortName("Interpolation Insertsort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

	private void insertTo(int[] array, int a, int b) {
		int temp = array[a];
		while (a > b) Writes.write(array, a, array[--a], 0.05, true, false);
		Writes.write(array, b, temp, 0.05, true, false);
		Highlights.clearMark(1);
	}

	private int interpSearch(int[] array, int a, int b, int val) {
		while (a < b) {
			int min = array[a], max = array[b-1];
			if (min == max) {
				Highlights.markArray(2, a);
				Delays.sleep(1);
				if (Reads.compareValues(val, min) < 0) b = a;
				else a = b;
			} else {
				int m = a + (int) ((b - a - 1) * ((double) Math.max(0, Math.min(val, max) - min) / (max - min)));
				Highlights.markArray(2, m);
				Delays.sleep(1);
				if (Reads.compareValues(val, array[m]) < 0) b = m;
				else a = m+1;
			}
		}
		return a;
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
		for (int i = 1; i < currentLength; i++) this.insertTo(array, i, this.interpSearch(array, 0, i, array[i]));
    }
}