package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class StacklessStoogeSort extends Sort {
    public StacklessStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Stackless Stooge");
        this.setRunAllSortsName("Stackless Stooge Sort");
        this.setRunSortName("Stackless Stoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(false);
    }

    // equivalent to (ternary) (v & (1 << s)) >> s
    public int trit(int v, int sig) {
    	while(sig-- > 0) {
    		v /= 3;
    	}
    	return v % 3;
    }

    // equivalent to (ternary) v & ~(1 << s)
    public int cuttrit(int v, int s) {
    	int pow = 1, w = v;
    	while(s-- > 0) {
    		v /= 3;
    		pow *= 3;
    	}
    	return w - (pow * (v % 3));
    }
    
    // how many iterations it takes for a lerp to reach 0
    // (in this case, how much recursive depth it takes for stooge to hit 0-length)
    public int flerp(int v, int b) {
    	int i = 0;
    	while(v > 0) {
    		v = ((b-1)*v)/b;
    		i++;
    	}
    	return i;
    }
    
	public void stacklessStoogeSort(int[] A, int i, int j) {
		int cube = 3,
			maxDepth = flerp(j-i+1, 3)-1;
		for(int k=0; k<maxDepth; k++) cube*=3;
		for(int k=0; k<cube; k++) {
			int a = i, b = j;
			for(int third = (b-a+1)/3, 
					depthNow = maxDepth;;
					third=(b-a+1)/3) {
				Highlights.markArray(1, a);
				Highlights.markArray(2, b);
				Delays.sleep(0.0025);
				switch(trit(k, depthNow--)) {
					case 0: case 2:
						b -= third; break;
					case 1:
						a += third; break;
				}
				if((b - a == 1 && third == 1) || third < 1)
					break;
			}
	    	if(Reads.compareIndices(A, a, b, 0.05, true)>0)
	    		Writes.swap(A, a, b, 0.025, true, false);
		}
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.stacklessStoogeSort(array, 0, currentLength - 1);
    }
}