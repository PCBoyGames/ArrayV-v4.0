package sorts.bogo;
import main.ArrayVisualizer;
import sorts.templates.BogoSorting;
// Bolosort randomly pushes numbers from one half to the other until it's partitioned, then recurses poorly until sorted.

public class Bolosort extends BogoSorting {
    public Bolosort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bolo");
        this.setRunAllSortsName("Bolo Sort");
        this.setRunSortName("Bolosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(true);
    }

    private boolean split(int[] array, int start, int split, int end) {
       if (start < split && split < end) {
          int k = start;

          for (int i = start + 1; i < split; ++i) {
             if (Reads.compareIndices(array, i, k, 0.025, true) > 0) {
                k = i;
             }
          }

          for (int i = end - 1; i >= split; --i) {
             if (Reads.compareIndices(array, i, k, 0.025, true) < 0) {
                return false;
             }
          }
       }
       return true;
    }
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
    	if (sortLength<2) return;
    	int z = sortLength/2;
        while(!isArraySorted(array, sortLength)) {
        	while(!split(array, 0, z, sortLength)) {
            	int a = randInt(0, z), b = randInt(z, sortLength);
            	if (Reads.compareIndices(array, a, b, 1, true) > 0) {
            		Writes.multiSwap(array, a, b, 0, false, false);
            	} else {
            		Writes.multiSwap(array, b, a, 0, false, false);
            	}
            }
        	runSort(array, z, bucketCount);
        	z = sortLength+(z-sortLength)/2;
        }
    }

}
