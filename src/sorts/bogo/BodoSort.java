 package sorts.bogo;

 import main.ArrayVisualizer;
 import sorts.templates.BogoSorting;

 public final class BodoSort extends BogoSorting {
   public BodoSort(ArrayVisualizer arrayVisualizer) {
     super(arrayVisualizer);

     this.setSortListName("Bodo");
     this.setRunAllSortsName("Bodo Sort (Buffed Boko Sort)");
     this.setRunSortName("Bodo Sort");
     this.setCategory("Bogo Sorts");
     this.setComparisonBased(true);
     this.setBucketSort(false);
     this.setRadixSort(false);
     this.setUnreasonablySlow(true);
     this.setUnreasonableLimit(32);
     this.setBogoSort(true);
   }


   public void runSort(int[] array, int length, int bucketCount) {
     while (!isRangeSorted(array, 0, length, false, true)) {
       int index = BogoSorting.randInt(0, length - 1),
         index2 = BogoSorting.randInt(0, length - 1);
       while(index < length - 1 && Reads.compareValues(array[index], array[index2]) == 1) {
         Writes.swap(array, index, ++index, 0.075, true, false);
         index2 = BogoSorting.randInt(0, length - 1);
       }
     }
   }
 }