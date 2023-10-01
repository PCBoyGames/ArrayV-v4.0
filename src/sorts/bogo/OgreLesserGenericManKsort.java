package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public class OgreLesserGenericManKsort extends BogoSorting {
    public OgreLesserGenericManKsort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Ogre Lesser Generic Man \"K\"");
        this.setRunAllSortsName("Ogre Lesser Generic Man K-Sort");
        this.setRunSortName("Ogre Lesser Generic Man K-Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLength, int buckets) {
    	if (currentLength < 2) return;
    	int[] sorted = Writes.copyOfArray(array, currentLength);
    	runSort(sorted, currentLength-1, 0);
    	for (int i = currentLength-1; i > 0 && Reads.compareIndices(sorted, i-1, i, 1, true) > 0; i--)
    		Writes.swap(sorted, i-1, i, 1, true, false);
    	boolean z=false, y=true;
    	int tr = 0;
    	for (;;) {
    		if (y||++tr>currentLength) {
        		z=true;
    			for (int i = 0; i < currentLength; i++) {
        			if (Reads.compareValues(array[i], sorted[i]) != 0) {z=false; break;}
        		}
        		if (z) break;
    		}
    		y=false;
	    	int p = randInt(0, currentLength);
	    	int l = randInt(0, 3);
    		int o = 1;
    		if (Reads.compareValues(array[p], sorted[p]) == 0) continue;
    		tr = 0;
	    	if ((l==0 && p == 0) || (l==1 && p == currentLength - 1)) continue;

	    	if (l==0) {
	    		while (o < p && Reads.compareValues(array[p-o], sorted[p-o]) == 0) o++;
	    		if (o == p) {
		    		Writes.write(array, p, array[p]+1, 0.1, true, false);
	    		} else Writes.write(array, p, array[p-o]+1, 0.1, true, false);
	    	} else if (l==1) {
	    		while (o < currentLength - p && Reads.compareValues(array[p+o], sorted[p+o]) == 0) o++;
	    		if (o == currentLength - p)
		    		Writes.write(array, p, array[p]-1, 0.1, true, false);
	    		else
	    			Writes.write(array, p, array[p+o]-1, 0.1, true, false);
	    	} else {
	    		int offs = randInt(0, 2)*2-1;
	    		if ((offs>0&&p==0)||(offs<0&&p==currentLength-1)) continue;
	    		Writes.write(array, p, array[p-offs], 0.1, true, false);
	    	}
	    	y=true;
    	}
    	Writes.deleteExternalArray(sorted);
    }
}