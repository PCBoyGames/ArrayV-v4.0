package sorts.bogo;

import java.util.ArrayList;
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

 public class QMRipbozoSort extends BogoSorting {
	   public QMRipbozoSort(ArrayVisualizer arrayVisualizer) {
		    super(arrayVisualizer);

		    this.setSortListName("Ripbozo (Quadratic)");
		    this.setRunAllSortsName("Ripbozo Sort (Quadratic Memory)");
		    this.setRunSortName("Quad Ripbozosort");
		    this.setCategory("Epsilon Committee Sorts");
		    this.setComparisonBased(true);
		    this.setBucketSort(false);
		    this.setRadixSort(false);
		    this.setUnreasonablySlow(true);
		    this.setUnreasonableLimit(5);
			this.setBogoSort(true);
	   }
	   public void runSort(int[] array, int length, int bucketCount) {
		   int n = length;

		   ArrayList<int[]> p = new ArrayList<>();

		   for(int i=n; i>0; i--) {
			   p.add(Writes.createExternalArray(length));
		   }

		   int[][] f = p.toArray(new int[0][]);
		   int[] swaps = Writes.createExternalArray(2 * length);

		   p.clear();

		   while(!isArraySorted(array, length)) {
			 packwatch:
			   while(true) {
				   for(int i=0; i<length; i++) {
					   Writes.arraycopy(array, 0, f[i], 0, length, 0.005, true, true);
					   Writes.write(swaps, 2*i, randInt(0, length), 0.05, true, true);
					   Writes.write(swaps, 2*i+1, randInt(0, length), 0.05, true, true);
					   Writes.swap(f[i], swaps[2*i], swaps[2*i+1], 0.05, true, true);
				   }

				   for(int i=0; i<length; i++) {
					   if(!isArraySorted(f[i], length)) {
						   for(int j=0; j<2*length; j+=2) {
							   Writes.swap(array, swaps[j], swaps[j+1], 0.05, true, false);
						   }
						   continue packwatch;
					   }
				   }
				   Writes.swap(array, randInt(0, length), randInt(0, length), 5, true, false);
				   break packwatch;
			   }
		   }
		   Writes.deleteExternalArrays(f);
		   Writes.deleteExternalArray(swaps);
	   }
 }