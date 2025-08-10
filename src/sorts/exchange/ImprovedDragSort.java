package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2024 aphitorite & Meme Man

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

public class ImprovedDragSort extends Sort {
	public ImprovedDragSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Improved Drag");
		this.setRunAllSortsName("Improved Dragsort");
		this.setRunSortName("Improved Dragsort");
		this.setCategory("Exchange Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	private void dualSwap(int[] array, int[] keys, int a, int b, double sleep) {
		Writes.swap(keys, a, b, 0, false, true);
		Writes.swap(array, a, b, sleep, true, false);
	}

	private boolean isSorted(int[] array, int[] keys, int a, int i, double sleep) {
		Highlights.markArray(1, i);
		Delays.sleep(sleep);
		return i-a == keys[i-a];
	}

	@Override
	public void runSort(int[] array, int N, int bucketCount) {
		int a = 0, b = N;

		int[] keys = Writes.createExternalArray(N);

		double sleep = 2d / N;

		// find sorted indices

		for(int j = a; j < b; j++) {
			int c = 0;

			for(int i = a; i < b; i++) {
				if(i == j) continue;
				int cmp = Reads.compareIndices(array, i, j, sleep, true);
				if(cmp < 0 || (cmp == 0 && i < j)) c++;
			}
			Writes.write(keys, j-a, c, 0, false, true);
		}

		sleep = Math.min(1d, 8*sleep);

		// begin dragging

		while(true) {
			int i = a;

			while(i < b && this.isSorted(array, keys, a, i, sleep)) i++;

			if(i == b) break;

			for(int j = i++; i < b; i++) {
				if(!this.isSorted(array, keys, a, i, sleep)) {
					this.dualSwap(array, keys, j, i, sleep);
					j = i;
				}
			}
			Highlights.clearMark(2);
		}
		Writes.deleteExternalArray(keys);
	}
}