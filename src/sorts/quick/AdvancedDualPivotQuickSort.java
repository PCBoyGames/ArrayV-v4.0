package sorts.quick;

import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;

import main.ArrayVisualizer;

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

final public class AdvancedDualPivotQuickSort extends Sort {
    public AdvancedDualPivotQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Advanced Dual-Pivot Quick");
        this.setRunAllSortsName("Advanced Dual-Pivot Quick Sort");
        this.setRunSortName("Advanced Dual-Pivot Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

	private final int MIN_LEVEL = 24;

	private void advDualPivQuick(int[] array, int a, int b, int d) {
		Highlights.clearMark(2);
		int n = b-a;

		if (n <= MIN_LEVEL) {
			BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
			smallSort.customBinaryInsert(array, a, b, 0.25);
			return;
		}

		int piv1, piv2, s = n/d;

		if (Reads.compareValues(array[a+s], array[b-1-s]) > 0) {
			piv1 = array[b-1-s];
			piv2 = array[a+s];
		}
		else {
			piv1 = array[a+s];
			piv2 = array[b-1-s];
		}

		int i1 = a, i = a, j = b, j1 = b;

		for (int k = i; k < j; k++) {
			int cmp1 = Reads.compareIndexValue(array, k, piv1, 0.5, true);

			if (cmp1 <= 0) {
				int t = array[k];

				Writes.write(array, k, array[i], 0.25, true, false);
				if (cmp1 == 0) {
					Writes.write(array, i, array[i1], 0.25, true, false);
					Writes.write(array, i1++, t, 0.25, true, false);
				}
				else Writes.write(array, i, t, 0.25, true, false);
				i++;
			}
			else {
				int cmp2 = Reads.compareIndexValue(array, k, piv2, 0.5, true);

				if (cmp2 >= 0) {
					while (--j > k) {
						int cmp = Reads.compareIndexValue(array, j, piv2, 0.5, true);
						if (cmp == 0) Writes.swap(array, --j1, j, 0.5, true, false);
						else if (cmp < 0) break;
					}
					Highlights.clearMark(2);

					int t = array[k];

					Writes.write(array, k, array[j], 0.25, true, false);
					if (cmp2 == 0) {
						Writes.write(array, j, array[--j1], 0.25, true, false);
						Writes.write(array, j1, t, 0.25, true, false);
					}
					else Writes.write(array, j, t, 0.25, true, false);

					cmp1 = Reads.compareIndexValue(array, k, piv1, 0.5, true);

					if (cmp1 <= 0) {
						t = array[k];

						Writes.write(array, k, array[i], 0.25, true, false);
						if (cmp1 == 0) {
							Writes.write(array, i, array[i1], 0.25, true, false);
							Writes.write(array, i1++, t, 0.25, true, false);
						}
						else Writes.write(array, i, t, 0.25, true, false);
						i++;
					}
				}
			}
		}
		if (i1 == b) return;

		if (Math.min(i-a, Math.min(j-i, b-j)) <= MIN_LEVEL) d++;
		this.advDualPivQuick(array, i, j, d);

		if (i1-a > i-i1) {
			int i2 = i;
			i = a;
			while (i1 < i2) Writes.swap(array, i++, i1++, 1, true, false);
		}
		else while (i1 > a) Writes.swap(array, --i, --i1, 1, true, false);

		this.advDualPivQuick(array, a, i, d);

		if (b-j1 > j1-j) {
			int j2 = j;
			j = b;
			while (j1 > j2) Writes.swap(array, --j, --j1, 1, true, false);
		}
		else while (j1 < b) Writes.swap(array, j++, j1++, 1, true, false);

		this.advDualPivQuick(array, j, b, d);
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.advDualPivQuick(array, 0, length, 3);
    }
}