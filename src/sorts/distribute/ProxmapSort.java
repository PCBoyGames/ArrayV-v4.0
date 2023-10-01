package sorts.distribute;

import sorts.templates.Sort;
import main.ArrayVisualizer;

/*
 */

public class ProxmapSort extends Sort {
    public ProxmapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Proxmap");
        this.setRunAllSortsName("Proxmap Sort (By McDude_73)");
        this.setRunSortName("Proxmap Sort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	// Initializer
    	int start = 0, end = length;
    	int[] a2 = Writes.createExternalArray(length);
    	int[] mapKey = Writes.createExternalArray(length);
    	int[] hitCount = Writes.createExternalArray(length);

    	for (int x=start;x<end;x++) a2[x] = -1;

    	int min = array[start], max = array[start];
    	for (int i=start+1;i<end;i++) {
    		if (array[i] < min) min = array[i];
    		else if (array[i] > max) max = array[i];
    	}

    	// Save MapKey(s) [An optimization]
    	for (int j=start;j<end;j++) {
    		Writes.write(mapKey, j, (int) (Math.floor(((array[j]-min) / (max - min)) * (end - 1))),
    				1, true, true);
    		hitCount[mapKey[j]]++;
    		Writes.changeAuxWrites(1);
    	}

    	// Store Proxmaps in HitCount [An optimization]
    	hitCount[end-1] = end - hitCount[end-1];
    	for (int k=end-1;k>start;k--) {
    		Writes.write(hitCount, k-1, hitCount[k] - hitCount[k-1], 1, true, true);
    	}

    	// Finish it off
    	int insertIndex = 0, insertStart = 0;

    	for (int l=start;l<end;l++) {
    		insertIndex = hitCount[mapKey[l]];
    		insertStart = insertIndex;
    		while (a2[insertIndex] != -1) insertIndex++;
    		while (insertIndex > insertStart && array[l] < a2[insertIndex-1]) {
    			Writes.write(a2, insertIndex, a2[insertIndex-1], 0.01, true, true);
    			insertIndex--;
    		}
    		Writes.write(a2, insertIndex, array[l], 0.01, true, true);
    	}

    	for (int m=start;m<end;m++) Writes.write(array, m, a2[m], 1, true, false);

    	Writes.deleteExternalArray(a2);
    	Writes.deleteExternalArray(mapKey);
    	Writes.deleteExternalArray(hitCount);
    }
}