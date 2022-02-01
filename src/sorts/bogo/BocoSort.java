 package sorts.bogo;

 import main.ArrayVisualizer;
 import sorts.templates.BogoSorting;
 // Bocosort compares random values, and swaps the range values if greater.
 public final class BocoSort extends BogoSorting {
   public BocoSort(ArrayVisualizer arrayVisualizer) {
     super(arrayVisualizer);

     this.setSortListName("Boco");
     this.setRunAllSortsName("Boco Sort");
     this.setRunSortName("Boco Sort");
     this.setCategory("Bogo Sorts");
     this.setComparisonBased(true);
     this.setBucketSort(false);
     this.setRadixSort(false);
     this.setUnreasonablySlow(true);
     this.setUnreasonableLimit(32);
     this.setBogoSort(true);
   }
   private void bocoExchange(int[] array, int a, int b) {
       if(a >= b)
           return;
       int q = randInt(a, b), r = randInt(q, b);
       Highlights.markArray(1, q);
       Highlights.markArray(2, r);
       Delays.sleep(0.025);
       if(Reads.compareValues(array[q], array[r]) == 1) {
           Writes.swap(array, a, b-1, 0.025, false, false);
           this.bocoExchange(array, a, b-1);
           this.bocoExchange(array, a+1, b);
       } else
           this.bocoExchange(array, q, r);
   }
   public void runSort(int[] array, int length, int bucketCount) {
       while(!isArraySorted(array, length))
           this.bocoExchange(array, 0, length);
   }
 }