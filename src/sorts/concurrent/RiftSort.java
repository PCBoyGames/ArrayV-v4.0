package sorts.concurrent;

import sorts.templates.Sort;
import main.ArrayVisualizer;

/*
 *
MIT License

Copyright (c) 2020 aphitorite

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

public class RiftSort extends Sort {
    public RiftSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Rift");
        this.setRunAllSortsName("Riftsort");
        this.setRunSortName("Riftsort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

	private void compSwap(int[] array, int a, int b) {
		if(Reads.compareIndices(array, a, b, 0.5, true) == 1)
			Writes.swap(array, a, b, 0.5, true, false);
	}

	private void riftMerge(int[] array, int a, int b) {
		int m = (a+b)/2;
		int h = (b-a+1)/2;

		for(int i = h; i > 0; i--)
			for(int j = 0; j < i; j++)
				if(m-i+j >= a) this.compSwap(array, m-i+j, m+j);
	}

	private void riftSortRec(int[] array, int a, int b) {
		if(b-a < 2) return;

		int m = (a+b)/2;

		this.riftSortRec(array, a, m);
		this.riftSortRec(array, m, b);
		this.riftMerge(array, a, b);
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.riftSortRec(array, 0, length);
    }
}