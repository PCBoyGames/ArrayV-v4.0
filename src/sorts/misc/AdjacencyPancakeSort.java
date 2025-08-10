package sorts.misc;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2024 aphitorite

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

public class AdjacencyPancakeSort extends Sort {
	public AdjacencyPancakeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Adjacency Pancake");
		this.setRunAllSortsName("Adjacency Pancake Sort");
		this.setRunSortName("Adjacency Pancake Sort");
		this.setCategory("Miscellaneous Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	// implementation of Bill Gate's improved pancake sort with at most 5/3 N + O(1) flips
	// uses this paper as a reference: https://www.sciencedirect.com/science/article/pii/S0304397508003575?via%3Dihub

	private void dualSwap(int[] array, int[] keys, int a, int b, double sleep) {
		Writes.swap(keys, a, b, 0, false, true);
		Writes.swap(array, a, b, sleep, true, false);
	}

	private void reversal(int[] array, int[] keys, int a, int b, double sleep) {
		if(b-a > 1) Writes.changeReversals(1);
		while(b-a > 1) this.dualSwap(array, keys, a++, --b, sleep);
	}

	private boolean isAdjacent(int[] keys, int a, int b, int N) {
		return (keys[a]+1)%N == keys[b] || (keys[b]+1)%N == keys[a];
	}

	private int findAdjacent(int[] keys, int e, int a, int N) {
		while(!isAdjacent(keys, a, e, N)) a++;
		return a;
	}

	@Override
	public void runSort(int[] array, int N, int bucketCount) {
		int a = 0, b = N;

		if(N == 2) {
			if(Reads.compareIndices(array, a, a+1, 0.5, true) > 0)
				Writes.reversal(array, a, a+1, 1, true, false);
			return;
		}

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

		sleep = Math.min(1d, 16*sleep);

		// begin flipping

		while(true) { // 9 different cases in total
			int i = a;
			while(i < b-1 && this.isAdjacent(keys, i, i+1, N)) i++;

			if(i == b-1) break; // n-1 adjacencies -> break

			if(i == a) { // singleton case (block of size 1)

				int j = this.findAdjacent(keys, a, a+2, N);

				if(!this.isAdjacent(keys, j-1, j, N)) // case 1, 2
					this.reversal(array, keys, a, j, sleep);

				else {
					int k = this.findAdjacent(keys, a, j+1, N);

					if(!this.isAdjacent(keys, k-1, k, N)) // case 1, 2
						this.reversal(array, keys, a, k, sleep);

					else { // case 3
						this.reversal(array, keys, a, j+1,   sleep);
						this.reversal(array, keys, a, j,     sleep);
						this.reversal(array, keys, a, k+1,   sleep);
						this.reversal(array, keys, a, a+k-j, sleep);
					}
				}
			}
			else { // block case

				int j = this.findAdjacent(keys, a, i+1, N);

				if(!this.isAdjacent(keys, j-1, j, N)) // case 4, 5
					this.reversal(array, keys, a, j, sleep);

				else {
					int k = this.findAdjacent(keys, i, i+2, N);

					if(k+1 < b && this.isAdjacent(keys, k+1, k, N)) { // case 6
						this.reversal(array, keys, a, i+1, sleep);
						this.reversal(array, keys, a, k+1, sleep);
					}
					else {
						this.reversal(array, keys, a, k+1,   sleep); // case 7
						this.reversal(array, keys, a, a+k-i, sleep);

						if(!this.isAdjacent(keys, k-1, k, N)) {
							if(j < k) { // case 8
								this.reversal(array, keys, a, k+1,     sleep);
								this.reversal(array, keys, a, i+k-j+1, sleep);
							}
							else { // case 9
								this.reversal(array, keys, a, j+1,   sleep);
								this.reversal(array, keys, a, a+j-k, sleep);
							}
						}
					}
				}
			}
		}

		// cleanup

		int i = a;
		while(keys[i] != 0 && keys[i] != N-1) i++;

		if(keys[i] == 0) {
			if(i == a) {
				Writes.deleteExternalArray(keys);
				return;
			}
			this.reversal(array, keys, a, b, sleep);
			i = b-2 - (i-a);
		}
		else if(i == a) {
			this.reversal(array, keys, a, b, sleep);
			Writes.deleteExternalArray(keys);
			return;
		}
		this.reversal(array, keys, a, ++i, sleep);
		this.reversal(array, keys, a, b,   sleep);
		this.reversal(array, keys, a, b-(i-a), sleep);

		Writes.deleteExternalArray(keys);
	}
}