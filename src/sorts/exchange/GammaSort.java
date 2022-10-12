package sorts.exchange;

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

 public final class GammaSort extends BogoSorting {
   public GammaSort(ArrayVisualizer arrayVisualizer) {
     super(arrayVisualizer);

     this.setSortListName("Gamma");
     this.setRunAllSortsName("Gamma Sort");
     this.setRunSortName("Gamma Sort");
     this.setCategory("Impractical Sorts");
     this.setComparisonBased(true);
     this.setBucketSort(false);
     this.setRadixSort(false);
     this.setUnreasonablySlow(true);
     this.setUnreasonableLimit(5);
     this.setBogoSort(true);
   }

   private void getpermutations(int[] array, int depth, int length, ArrayList<int[]> p, boolean a) {
       if (depth >= length-1) {
           p.add(Writes.copyOfArray(array, length));
           return;
       }

       for (int i = length-1; i > depth; --i) {
           getpermutations(array, depth+1, length, p, a);

           if ((length-depth)%2 == 0)
               Writes.swap(array, depth, i, this.delay, true, a);
           else
               Writes.swap(array, depth, length-1, this.delay, true, a);
       }
       getpermutations(array, depth+1, length, p, a);
   }
   private void wtfBogo(int[] array, int a, int b, boolean aux) {
       if (a < b) {
           this.wtfBogo(array, a, b-1, aux);
           this.bogoSwap(array, a, b, aux);
           this.wtfBogo(array, a+1, b, aux);
       }
   }
   private boolean allMatch(int[][] arrays, int length) {
       boolean f = true;
       for (int i=1; i<arrays.length; i++)
           for (int j=0; j<length; j++) {
               if (Reads.compareValues(arrays[i-1][j], arrays[i][j]) != 0) {
                   this.wtfBogo(arrays[i-1], 0, length, true);
                   this.runSort(arrays[i], length-1, 1);
                   f = false;
               }
           }
       return f;
   }
   private boolean isBogoBogoSorted(int[] array, int length, boolean aux) {
       this.wtfBogo(array, 0, length, aux);
       runSort(array, length-1, aux?1:0);
       return Reads.compareIndices(array, length-2, length-1, 1, true) <= 0;
   }
   public void runSort(int[] array, int length, int a) {
       if (length < 2) return; // catch bad eggs
       ArrayList<int[]> p = new ArrayList<>();
       getpermutations(array, 0, length, p, a==1);
       int[][] perms = p.toArray(new int[0][]);
       while (!isBogoBogoSorted(array, length, a==1)) {
           do {
               for (int[] i : perms)
                   this.wtfBogo(i, 0, length, true);
           } while (!allMatch(perms, length));
           Writes.arraycopy(perms[0], 0, array, 0, length, 1, true, a==1);
       }
       Writes.deleteExternalArrays(perms);
   }
 }