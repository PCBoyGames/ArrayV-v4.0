package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.Sort;

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

public class FastLSDRadixSort extends Sort {
    public FastLSDRadixSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Fast LSD Radix");
        this.setRunAllSortsName("Least Significant Digit Radix Sort, Base 4");
        this.setRunSortName("Least Significant Digit Radixsort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(true);
        this.setRadixSort(true);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.setRunAllSortsName("Least Significant Digit Radix Sort, Base " + bucketCount);

        int highestpower = Reads.analyzeMaxLog(array, sortLength, bucketCount, 0.5, true);

		int[] pos = Writes.createExternalArray(bucketCount+1);
		int[] tmp = Writes.createExternalArray(sortLength);

        for (int p = 0; p <= highestpower; p++) {
            for (int i = 0; i < sortLength; i++) {
                Highlights.markArray(1, i);

                int digit = Reads.getDigit(array[i], p, bucketCount);

				Writes.write(pos, digit+1, pos[digit+1]+1, 0, false, true);
				Writes.write(tmp, i, array[i], 0.5, true, true);
            }
			for (int i = 2; i < bucketCount; i++)
				Writes.write(pos, i, pos[i]+pos[i-1], 0, false, true);

			for (int i = 0; i < bucketCount; i++)
				if (pos[i] < sortLength) Highlights.markArray(i, pos[i]);

			for (int i = 0; i < sortLength; i++) {
				int digit = Reads.getDigit(tmp[i], p, bucketCount);

				Writes.write(array, pos[digit], tmp[i], 0, false, false);
				Writes.write(pos, digit, pos[digit]+1, 0.5, false, true);

				if (pos[digit] < sortLength) Highlights.markArray(digit, pos[digit]);
				else                        Highlights.clearMark(digit);
			}
			Highlights.clearAllMarks();

			for (int i = 0; i < bucketCount; i++)
				Writes.write(pos, i, 0, 0, false, true);
        }
        Writes.deleteExternalArray(pos);
        Writes.deleteExternalArray(tmp);
    }
}