package sorts.merge;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
import sorts.insert.InsertionSort;
import sorts.templates.GrailSorting;
import utils.IndexedRotations;

public final class IotaSort extends GrailSorting {
    public IotaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Iota");
        this.setRunAllSortsName("Iota Sort");
        this.setRunSortName("Iotasort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private InsertionSort is;
    private int mergeBack(int[] array, int a, int p, int b, int q, int t) {
    	int l = a + p - 1, r = b + q - 1;
    	while(l >= a && r >= b) {
    		t--;
    		if(Reads.compareValues(array[l], array[r]) > 0) {
    			Writes.swap(array, t, l--, 1, true, false);
    		} else {
    			Writes.swap(array, t, r--, 1, true, false);
    		}
    	}
    	int s = b + l - a;
    	while(l >= a) {
			Writes.swap(array, --t, l--, 1, true, false);
    	}
    	while(r >= b) {
			Writes.swap(array, --t, r--, 1, true, false);
    	}
    	return s;
    }
    private void mergeBuf(int[] array, int a, int m, int b, int t) {
    	blockSwap(array, a, t, m - a);
    	int l = t, le = l + (m - a), r = m;
    	while(l < le && r < b) {
    		if(Reads.compareValues(array[l], array[r]) <= 0) {
    			Writes.swap(array, a++, l++, 1, true, false);
    		} else {
    			Writes.swap(array, a++, r++, 1, true, false);
    		}
    	}
    	while(l < le)
			Writes.swap(array, a++, l++, 1, true, false);
    }
	
	private void blockSwap(int[] array, int a, int b, int s) {
        while(s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }
	
	private int binSearchR(int[] array, int start, int end, int k) {
		while(start < end) {
			int mid = start + (end - start) / 2;
			if(Reads.compareValues(array[mid], k) > 0) {
				end = mid;
			} else {
				start = mid + 1;
			}
		}
		return start;
	}
    
    private boolean mp2(int[] array, int a, int b, int t) {
    	if(b-a < 8) {
    		is.customInsertSort(array, a, b, 0.5, false);
    		return false;
    	}
    	boolean r = false, n = false;
    	int z = 2;
    	for(; z<=b-a; z*=2) {
    		if(t > a && a + 2 * z > b) {
    			r = true;
    		}
    		mergeBack(array, n?t>a?t:a+z:a, z/2, a+z/2, Math.min(z/2, b-a-z/2), r?t+z:a+2*z);
    		if(z<b-a)
    			n = mp2(array, a, a+z, Math.max(t, a+2*z));
    	}
    	return r;
    }
    
    private void mergefrag(int[] array, int start,  int end) {
    	if(end-start < 32) {
    		is.customInsertSort(array, start, end, 1, false);
    		return;
    	}
    	int p = (31 - Integer.numberOfLeadingZeros(end-start)) / 2,
    		s = 1 << p, l = start + s, R = 0, t = start;
    	for(int i = l; i < end; i += s) {
    		int m = i;
    		for(int j = i + s; j < end; j += s) {
    			if(Reads.compareIndices(array, m, j, 0.1, true) > 0) {
    				m = j;
    			}
    		}
    		blockSwap(array, i, m, s);
    		if(++R > 1) {
    			int r = i, e = i + s;
    			while(l < i && r < e) {
    	    		if(Reads.compareValues(array[l], array[r]) <= 0) {
    	    			Writes.swap(array, t++, l++, 1, true, false);
    	    		} else {
    	    			Writes.swap(array, t++, r++, 1, true, false);
    	    		}
    			}
    			if(l < i) {
    				int n = i+s-(i-l);
    				blockSwap(array, l, n, i-l);
    				l = n;
    			} else {
    				l = r;
    			}
    		}
    	}
		blockSwap(array, t, l, end - l);
		t += end - l;
		sortrec(array, t, end);
		int pz = binSearchR(array, start, t, array[end-1]);
		int pb = binSearchR(array, start, t, array[t]) - start;
		pz = start + ((pz - start - 1) / s + 1) * s;
		pb = start + pb - (pb % s);
		IndexedRotations.cycleReverse(array, pz, t, end, 1, true, false);
		mergefrag(array, pb, pz+s);
    }
    
    private void sortrec(int[] array, int start, int end) {
    	int len = end - start, h = len / 2;
    	if(len <= 8) {
    		is.customInsertSort(array, start, start+len, 0.5, false);
    		return;
    	}
    	while(len > 8) {
    		mp2(array, start, start+len/2, start);
    		len /= 2;
    	}
		is.customInsertSort(array, start, start+len, 0.5, false);
    	for(; len < h; len *= 2) {
    		mergeBuf(array, start, start+len, start+2*len, start+h);
    	}
		sortrec(array, start+h, start+h+h/2);
		mergefrag(array, start, end);
    }
    
    private void sort(int[] array, int start, int end) {
    	if(start >= end)
    		return;
    	int len = end - start, d = 31 - Integer.numberOfLeadingZeros(len), c = d, f = 1 << c;
    	int s = start, l = -1, m = s;
		sortrec(array, start, start+=(1<<c));
    	while(--c >= 0) {
    		if((len & (1 << c)) != 0) {
    			if(l == -1) l = c;
    			sortrec(array, start, start+=(1<<c));
    		}
    	}
    	int L = 1 << l;
    	start = s + f;
    	while(--d >= 0) {
			int e = 1 << d;
    		if((len & e) != 0) {
    			blockSwap(array, s, start, e);
    			int z = mergeBack(array, s, e, s + L, f - L, start += e);
    			if(z > m)
    				m = z;
    			f += e;
    		}
    	}
    	sort(array, s, s + L);
    	if(l >= 0) {
    		int pz = binSearchR(array, s + L, end, array[s+L-1]);
    		int p = (31 - Integer.numberOfLeadingZeros(pz-s)) / 2, S = 1 << p;
    		pz = s + ((pz - s - 1) / S + 1) * S;
    		mergefrag(array, s, pz);
    	}
    }
    
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
    	is = new InsertionSort(arrayVisualizer);
    	sort(array, 0, sortLength);
    }
}
