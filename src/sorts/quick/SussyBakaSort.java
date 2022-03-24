package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;
import utils.IndexedRotations;

final public class SussyBakaSort extends BogoSorting {
    public SussyBakaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("SussyBaka");
        this.setRunAllSortsName("SussyBakasort");
        this.setRunSortName("Sussybakasort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private static final int sussyMode = 1;
    
    private int cmp(int[] array, int pos0, int pos1) {
    	int cmpv = Reads.compareIndices(array, pos1, pos0, 0.125, true);
    	return -(cmpv >> 31);
    }
    
    private int medof3(int[] array, int loc) {
    	int a = cmp(array, loc, loc+1),
    		b = cmp(array, loc+(a^1), loc+2);
    	b += (a^1)|b;
    	int c = cmp(array, loc+b, loc+a);
    	return loc + (((c - 1) & a) | (-c & b));
    }
    
    private int medof3(int[] array, int l, int l1, int l2) {
    	int[] spread = new int[] {l, l1, l2};
    	int a = cmp(array, l, l1),
    		b = cmp(array, spread[a^1], l2);
    	b += (a^1)|b;
    	int c = cmp(array, spread[b], spread[a]);
    	return spread[((c - 1) & a) | (-c & b)];
    }
    
    private int ninther(int[] array, int a, int b) {
    	if(b-a < 4) {
    		return a+(b-a)/2;
    	}
    	if(b-a < 8) {
    		return medof3(array, a+(b-a-1)/2);
    	}
    	int d = (b-a+1)/8,
    		m0 = medof3(array, a, a+d, a+2*d),
    		m1 = medof3(array, a+3*d, a+4*d, a+5*d),
    		m2 = medof3(array, a+6*d, a+7*d, b);
    	return medof3(array, m0, m1, m2);
    }
    
    private int pseudomo27(int[] array, int a, int b) {
    	if(b-a < 3*27) {
    		return ninther(array, a, b);
    	}
    	int d = (b-a+1)/8,
    		m0 = ninther(array, a,a+2*d),
    		m1 = ninther(array, a+3*d, a+5*d),
    		m2 = ninther(array, a+6*d, b);
    	return medof3(array, m0, m1, m2);
    }
    
    private int pseudomo81(int[] array, int a, int b) {
    	if(b-a < 4*81) {
    		return pseudomo27(array, a, b);
    	}
    	int d = (b-a+1)/24,
    		m0 = ninther(array, a, a+2*d),
    		m1 = ninther(array, a+3*d, a+5*d),
    		m2 = ninther(array, a+6*d, a+8*d),
    		m3 = ninther(array, a+9*d, a+11*d),
    		m4 = ninther(array, a+12*d, a+14*d),
    		m5 = ninther(array, a+15*d, a+17*d),
    		m6 = ninther(array, a+18*d, a+20*d),
    		m7 = ninther(array, a+19*d, a+21*d),
    		m8 = ninther(array, a+22*d, b);
    	return medof3(array,
    		medof3(array, m0, m1, m2),
    		medof3(array, m3, m4, m5),
    		medof3(array, m6, m7, m8)
    	);
    }
    
    private void ternarysplitOOP(int[] array, int[] tmp, int start, int end) {
    	if(start >= end)
    		return;
    	int p, pi;
    	switch(sussyMode) {
	    	case 0:
	    	default:
	    		int z = array[start] ^ array[end];
	    		if(z < (start ^ end)) { // based off of Sum-Seeded PDQ's pivot selection
		    		p = (z ^ (array[start] & (start ^ end))) % (end - start) + start;
		    	} else {
		    		p = (z ^ (array[end] & ~(start - end))) % (end - start) + start;
		    	}
	    		break;
	    	case 1:
	    		p = pseudomo81(array, start, end);
	    		break;
    	}
    	pi = array[p];
    	int mama = start, mia = end, mamamia_ = 0, mamamia = start, now = array[mama];
    	boolean allEq = true;
    	innerpartition:
    	while(mama <= mia) {
    		Highlights.markArray(1, mamamia);
    		Highlights.markArray(2, mamamia_+start);
    		Delays.sleep(1);
			Writes.visualClear(array, mama);
    		switch(Reads.compareValues(now, pi)) {
	    		case -1:
	    			Writes.write(array, mamamia++, now, 0, false, false);
	    			allEq = false;
	    			break;
	    		case 0:
	    			while(mama < mia && Reads.compareIndexValue(array, mia, pi, 0.5, true) == 0) {
	    				mia--;
	    			}
	    			int v = array[mia];
	    			Writes.write(array, mia--, now, 0, false, false);
	    			now = v;
	    			continue innerpartition;
	    		case 1:
	    			allEq = false;
	    			Writes.write(tmp, mamamia_++, now, 0.5, true, true);
	    			break;
    		}
    		now = array[++mama];
    	}
    	if(allEq)
    		return;
    	mia = mamamia + mamamia_ - 1;
    	Writes.arraycopy(tmp, 0, array, mamamia, mamamia_, 1, true, false);
    	ternarysplitOOP(array, tmp, mamamia, mia);
    	IndexedRotations.cycleReverse(array, mamamia, mia + 1, end + 1, 1, true, false);
    	ternarysplitOOP(array, tmp, start, mamamia - 1);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	int[] p = Writes.createExternalArray(currentLength);
    	ternarysplitOOP(array, p, 0, currentLength-1);
    	Writes.deleteExternalArray(p);
    }
}