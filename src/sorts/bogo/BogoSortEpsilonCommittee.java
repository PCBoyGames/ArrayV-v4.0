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
  * Authors: naoan1201, Californium-252,    *
  * Distray                                 *
  *******************************************/

 public final class BogoSortEpsilonCommittee extends BogoSorting {
   public BogoSortEpsilonCommittee(ArrayVisualizer arrayVisualizer) {
     super(arrayVisualizer);

     this.setSortListName("The Epsilon Committee's Bogo");
     this.setRunAllSortsName("The Epsilon Committee's Bogo Sort");
     this.setRunSortName("The Epsilon Committee's Bogo Sort");
     this.setCategory("Bogo Sorts");
     this.setComparisonBased(true);
     this.setBucketSort(false);
     this.setRadixSort(false);
     this.setUnreasonablySlow(true);
     this.setUnreasonableLimit(5);
     this.setBogoSort(true);
   }

   private void getpermutations(int[] array, int depth, int length, ArrayList<int[]> p) {
       Writes.recordDepth(depth);
       if (depth >= length-1) {
           p.add(Writes.copyOfArray(array, length));
           return;
       }

       for (int i = length-1; i > depth; --i) {
         Writes.recursion();
         getpermutations(array, depth+1, length, p);

           if ((length-depth)%2 == 0)
               Writes.swap(array, depth, i, this.delay, true, false);
           else
               Writes.swap(array, depth, length-1, this.delay, true, false);
       }
       Writes.recursion();
       getpermutations(array, depth+1, length, p);
   }
   private boolean allMatch(int[][] arrays, int length) {
     for (int i=1; i<arrays.length; i++)
       for (int j=0; j<length; j++)
         if (Reads.compareValues(arrays[i-1][j], arrays[i][j]) != 0)
           return false;
     return true;
   }
   public void runSort(int[] array, int length, int bucketCount) {
     ArrayList<int[]> p = new ArrayList<>();
     getpermutations(array, 0, length, p);
     int[][] perms = p.toArray(new int[0][]);
     while (!isArraySorted(array, length)) {
       do {
         for (int[] i : perms)
           this.bogoSwap(i, 0, length, true);
       } while (!allMatch(perms, length));
       Writes.arraycopy(perms[0], 0, array, 0, length, 1, true, false);
     }
     Writes.deleteExternalArrays(perms);
   }
 }