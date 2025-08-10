package sorts.exchange;

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

public class FastCocktailShakerSort extends Sort {
    public FastCocktailShakerSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Fast Cocktail");
        this.setRunAllSortsName("Cocktail Shaker Sort");
        this.setRunSortName("Cocktail Shaker Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		Random r = new Random();

		for (int start = 0, end = length-1; start < end; ) {
            int consecSorted = 1;
			int w = 0;

			int rIdx = start + r.nextInt(Math.max(1, end-start-1));
			Highlights.markArray(1, rIdx);
			Highlights.markArray(2, rIdx+1);
			Delays.sleep(Math.max(0, (double)(end-start)/length));

            for (int i = start; i < end; i++) {
				consecSorted++;

                if (array[i] > array[i+1]) {
					int t = array[i]; array[i] = array[i+1]; array[i+1] = t;
                    consecSorted = 1; w++;
                }
            }
			//Reads.setComparisons(Reads.getComparisons() + end-start);
            Reads.addComparisons(end - start);
			//Writes.changeSwaps(w);
            Writes.swaps += w;
            Writes.writes += 2 * w;

			Highlights.markArray(1, end);
			Highlights.markArray(2, end-1);
			Delays.sleep(Math.max(0, (double)(end-start)/length));

			w = 0;
            end -= consecSorted;

            consecSorted = 1;

			rIdx = start + r.nextInt(Math.max(1, end-start-1));
			Highlights.markArray(1, rIdx);
			Highlights.markArray(2, rIdx+1);
			Delays.sleep(Math.max(0, (double)(end-start)/length));

            for (int i = end; i > start; i--) {
				consecSorted++;

                if (array[i-1] > array[i]) {
                    int t = array[i]; array[i] = array[i-1]; array[i-1] = t;
                    consecSorted = 1; w++;
                }
            }
			//Reads.setComparisons(Reads.getComparisons() + end-start);
            Reads.addComparisons(end - start);
			//Writes.changeSwaps(w);
            Writes.swaps += w;
            Writes.writes += 2 * w;

			Highlights.markArray(1, start);
			Highlights.markArray(2, start+1);
			Delays.sleep(Math.max(0, (double)(end-start)/length));

			w = 0;
            start += consecSorted;
        }
    }
}