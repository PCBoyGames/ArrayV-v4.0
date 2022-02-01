package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import java.util.Random;

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

final public class BogoCycleSort extends Sort {
	public BogoCycleSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Bogo Cycle");
		this.setRunAllSortsName("Bogo Cycle Sort");
		this.setRunSortName("Bogo Cyclesort");
		this.setCategory("Bogo Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(true);
		this.setUnreasonableLimit(768);
		this.setBogoSort(false);
	}

	private boolean correctPos(int[] array, int idx, int b) {
		int lower = 0, upper = 0;
		Highlights.markArray(2, idx);

		for(int i = 0; i < b; i++) {
			if(i == idx) continue;

			Highlights.markArray(1, i);
			int cmp = Reads.compareValues(array[i], array[idx]);
			lower += (1-cmp)/2;
			upper += (2-cmp)/2;
		}
		return idx >= lower && idx <= upper;
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int[] idx = Writes.createExternalArray(length);

		for(int i = 0; i < length; i++)
			Writes.write(idx, i, i, 0, false, true);

		int size = length;
		Random r = new Random();

		do {
			int c = 0;

			for(int i = 0; i < size; i++)
				if(!correctPos(array, idx[i], length))
					Writes.write(idx, c++, idx[i], 0, false, true);

			size = c;

			for(int i = 0; i < size; i++) {
				int rand = i+r.nextInt(size-i);
				Writes.swap(array, idx[i], idx[rand], 0, true, false);
				Writes.swap(idx, i, rand, 0.01, false, true);
			}
		}
		while(size > 1);

		Writes.deleteExternalArray(idx);
	}
}