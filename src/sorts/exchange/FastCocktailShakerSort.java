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
		boolean fw = true;

		for (int j = 0, k = length; k-j > 1; fw = !fw)  {
			if (fw) {
				int t = array[j];

				for (int i = j+1; i < k; i++) {
					if (t > array[i]) array[i-1] = array[i];
					else { array[i-1] = t; t = array[i]; }
				}
				Reads.setComparisons(Reads.getComparisons().longValue() + k-j-1);
				Writes.changeWrites(k-j-1);

				Writes.write(array, k-1, t, (double)(k-j)/length, true, false);
				Highlights.markArray(1, j+r.nextInt(k-j));
				k--;
			}
			else {
				int t = array[k-1];

				for (int i = k-2; i >= j; i--) {
					if (t < array[i]) array[i+1] = array[i];
					else { array[i+1] = t; t = array[i]; }
				}
				Reads.setComparisons(Reads.getComparisons().longValue() + k-j-1);
				Writes.changeWrites(k-j-1);

				Writes.write(array, j, t, (double)(k-j)/length, true, false);
				Highlights.markArray(1, j+r.nextInt(k-j));
				j++;
			}
		}
    }
}