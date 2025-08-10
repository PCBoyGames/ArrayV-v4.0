package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

 /*******************************************
  *          The Epsilon Committee          *
  * --------------------------------------- *
  * We've got 99 problems, and practicality *
  * ain't one                               *
  * ======================================= *
  * Author: Distray                         *
  *******************************************/

 public class LMRipbozoSort extends BogoSorting {
	   public LMRipbozoSort(ArrayVisualizer arrayVisualizer) {
		    super(arrayVisualizer);

		    this.setSortListName("Ripbozo (Linear)");
		    this.setRunAllSortsName("Ripbozo Sort (Linear Memory)");
		    this.setRunSortName("Linear Ripbozosort");
		    this.setCategory("Epsilon Committee Sorts");
		    this.setComparisonBased(true);
		    this.setBucketSort(false);
		    this.setRadixSort(false);
		    this.setUnreasonablySlow(true);
		    this.setUnreasonableLimit(5);
			this.setBogoSort(true);
	   }
	   public void runSort(int[] array, int length, int bucketCount) {
		   int[] swaps = Writes.createExternalArray(2 * length);

		   while(!isArraySorted(array, length)) {
			   while(true) {
				   for(int i=0; i<length; i++) {
					   Writes.write(swaps, 2*i, randInt(0, length), 0.05, true, true);
					   Writes.write(swaps, 2*i+1, randInt(0, length), 0.05, true, true);
					   Highlights.markArray(1, swaps[2*i]);
					   Highlights.markArray(1, swaps[2*i+1]);
				   }

				   boolean sorted = true;
				   for(int i=0; i<length-1; i++) {
					   for(int j=0; j<length; j++) {
						   int inda =     i == swaps[2*j] ? swaps[2*j+1] :     i == swaps[2*j+1] ? swaps[2*j] : i,
							   indb = (i+1) == swaps[2*j] ? swaps[2*j+1] : (i+1) == swaps[2*j+1] ? swaps[2*j] : (i+1);
						   sorted = sorted && Reads.compareIndices(array, inda, indb, 0.005, true) <= 0;
					   }
				   }
				   if(sorted) {
					   Writes.swap(array, randInt(0, length), randInt(0, length), 10, true, false);
					   break;
				   } else {
					   for(int j=0; j<2*length; j+=2) {
						   Writes.swap(array, swaps[j], swaps[j+1], 1, true, false);
					   }
				   }
			   }
		   }
		   Writes.deleteExternalArray(swaps);
	   }
 }