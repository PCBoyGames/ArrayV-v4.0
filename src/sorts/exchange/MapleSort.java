package sorts.exchange;

import sorts.templates.Sort;

import main.ArrayVisualizer;

/* _________________________
  /                         \
 | As Seen On PCBoy's Oh God |
 |       and Sort Grid       |
 |(AKA: MapleMagicShovySeas) |
  \_________________________/ */
final public class MapleSort extends Sort {
    public MapleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Maple");
        this.setRunAllSortsName("Maple Sort");
        this.setRunSortName("Maple Sort");
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
    	boolean pull = true;
    	for(; pull ;) {
    		pull = false;
    		int p=0;
    		for(int j=0; j<length; j+=2) {
    			if(j != length - 1 && Reads.compareIndices(array, j, j+1, 1, true) > 0) {
    				if(j+1!=p) {
    					pull=true;
    				}
    				Writes.multiSwap(array, j+1, p, 1, true, false);
    			} else {
    				if(j!=p) {
    					pull=true;
    				}
    				Writes.multiSwap(array, j, p++, 1, true, false);
    				if(p>=j) {
    					j--;
    					continue;
    				}
    			}
    			if(p <= j)
    				p++;
    		}
    	}
    }
}