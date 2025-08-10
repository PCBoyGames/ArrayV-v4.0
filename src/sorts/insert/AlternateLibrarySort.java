package sorts.insert;

import sorts.templates.Sort;
import main.ArrayVisualizer;

import java.util.Arrays;

/*
 *
MIT License

Copyright (c) 2020-2021 aphitorite

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

public class AlternateLibrarySort extends Sort {
    public AlternateLibrarySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

		this.setSortListName("Alternate Library");
        this.setRunAllSortsName("Alternate Library Sort");
        this.setRunSortName("Alternate Library Sort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

	private boolean IS_EMPTY(int e) { return e < 0; }
	private int NONE = -1;

    private int searchFree(int e, int[] sorted, int last) {
        int first = 0;
        int middle;

        while(last >= 0 && IS_EMPTY(sorted[last])) last--;

        while(first <= last && IS_EMPTY(sorted[first])) first++;

        while(first <= last) {
            middle = (first+last)/2;

            if (IS_EMPTY(sorted[middle])) {
                int tmp = middle + 1;

                //Look to the right
                while(tmp < last && IS_EMPTY(sorted[tmp])) tmp++;

                if (Reads.compareValues(sorted[tmp], e) > 0) {
                    tmp = middle - 1;

                    while(middle > first && IS_EMPTY(sorted[middle])) middle--;

                    //Look to the left
                    if (Reads.compareValues(sorted[middle], e) < 0) //Found intermediate position
						return middle;

                    last = middle - 1;
                }
				else first = tmp + 1;
            }
			else if (Reads.compareValues(sorted[middle], e) < 0) {
                first = middle + 1;
            }
			else {
                last = middle - 1;
            }
        }
        //If no position was found return -1 or if a lower position was found, return that
        if (last >= 0 && IS_EMPTY(sorted[last])) last--;
        return last;
    }

    void libSort(int[] A, int N, int[] S, int EPSILON) {
        if (N == 0) return;

        int j, k, step;

        // ------ BASE CASE ------
        //Goal: We want 'goal' elements to be inserted into S, for now..
        int goal = 1;
        //How many elements have already been inserted, its 1 for efficiency
        int pos = 1;

		Highlights.markArray(1, 0);
		Writes.write(S, 0, A[0], 0.5, false, true); //We insert element 0 at position 0

        //Initial size of array S
        int sLen = Math.max((1+EPSILON), goal + 1);

        // ------ CONDITION -------
        //What has already been read must be less than the total array size
        while(pos < N) {
            // ------ ROUND ------
            //Each round i will end with goal=2^i sorted elements. i starts with 1
            for (j = 0; j < goal; j++) {
                //Search where to insert A[pos] (with binary search)
				Highlights.markArray(2, pos);
				Delays.sleep(1);

                int insPos = searchFree(A[pos], S, sLen-1);

                //Because our binary search returns us the location of an smaller item than the one we search...
                insPos++;

                if (!IS_EMPTY(S[insPos])) {//There is no place where we wanted to insert that element
                    int nextFree = insPos + 1;//Search a free space forward
                    while(!IS_EMPTY(S[nextFree])) nextFree++;

                    //At 'nextFree' there is a place, translate all elements one position to the right
                    if (nextFree >= sLen) {//Wait! nextFree is out of bounds
                        insPos--;

                        if (!IS_EMPTY(S[insPos])) {
                            //Search backward
                            nextFree = insPos - 1;
                            while(!IS_EMPTY(S[nextFree])) nextFree--;

                            //Now we translate all the elements to the left
                            while(nextFree < insPos) {
								Highlights.markArray(1, nextFree/(1+EPSILON));
								Writes.write(S, nextFree, S[nextFree+1], 0.5, false, true);
								nextFree++;
                            }
                        }
                    }
					else {
                        //Now we translate all the elements to the right
                        while(nextFree > insPos) {
							Highlights.markArray(1, nextFree/(1+EPSILON));
							Writes.write(S, nextFree, S[nextFree-1], 0.5, false, true);
                            nextFree--;
                        }
                    }
                    //Now nextFree is insPos; in other words, insPos is free
                }
				else if (insPos >= sLen) {//insPos is out of bounds
                    //Search a free space backwards
                    insPos--; //This place must be between the limits
                    int nextFree = insPos - 1;
                    while(!IS_EMPTY(S[nextFree])) nextFree--;

                    //Now we translate all the elements to the left
					while(nextFree < insPos) {
						Highlights.markArray(1, nextFree/(1+EPSILON));
						Writes.write(S, nextFree, S[nextFree+1], 0.5, false, true);
						nextFree++;
					}
                    //Now nextFree is insPos; in other words insPos is free
                }

				Highlights.markArray(1, insPos/(1+EPSILON));
				Writes.write(S, insPos, A[pos++], 0.5, false, true); //We insert the element and increment our counter

                if (pos >= N)
                    return;//That element was the last, return from the function
            }

            // ----- REBALANCE -----
            //It takes linear time. Tries to spread the elements as much as possible
            for (j = sLen-1, k = Math.min(goal*(2+2*EPSILON), (1+EPSILON)*N) - 1,
			    step = (k+1)/(j+1); j >= 0; j--, k -= step) {

				Highlights.markArray(1, k/(1+EPSILON));
				Writes.write(S, k, S[j], 0.5, false, true);
                S[j] = NONE;
            }

            //In each round insert the double of elements to the sorted array
            // because there will be the double of free spaces after the rebalance
            sLen = Math.min(goal*(2+2*EPSILON), N*(1+EPSILON));
            goal <<= 1;//We increment i
        }
    }

    @Override
    public void runSort(int[] A, int n, int bucketCount) {
        int epsilon = 1;
		int sLen = n * (1+epsilon);

		int[] S = Writes.createExternalArray(sLen);
		Arrays.fill(S, -1);

        int i, j;

		libSort(A, n, S, epsilon);
		Highlights.clearMark(2);

		for (i = 0, j = 0; i < sLen && j < n; i++)
            if (!IS_EMPTY(S[i]))
				Writes.write(A, j++, S[i], 0.5, true, false);

		Writes.deleteExternalArray(S);
    }
}