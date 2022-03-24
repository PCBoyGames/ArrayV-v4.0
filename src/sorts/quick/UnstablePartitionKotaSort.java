package sorts.quick;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import main.ArrayVisualizer;
import sorts.golf.CaiSortMkII;
import sorts.templates.Sort;
import utils.IndexedRotations;

final public class UnstablePartitionKotaSort extends Sort {
    public UnstablePartitionKotaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Unstable Partition Kota");
        this.setRunAllSortsName("Unstable Partition Kota Sort");
        this.setRunSortName("Unstable Partition Kotasort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private int cmp(int[] array, int pos0, int pos1) {
    	int cmp = Reads.compareIndices(array, pos1, pos0, 0.125, true);
    	return -(cmp >> 31);
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
    
    private int pseudomo243(int[] array, int a, int b) {
    	if(b-a < 4*243) {
    		return pseudomo81(array, a, b);
    	}
    	int d = (b-a+1)/24,
    		m0 = pseudomo27(array, a, a+2*d),
    		m1 = pseudomo27(array, a+3*d, a+5*d),
    		m2 = pseudomo27(array, a+6*d, a+8*d),
    		m3 = pseudomo27(array, a+9*d, a+11*d),
    		m4 = pseudomo27(array, a+12*d, a+14*d),
    		m5 = pseudomo27(array, a+15*d, a+17*d),
    		m6 = pseudomo27(array, a+18*d, a+20*d),
    		m7 = pseudomo27(array, a+19*d, a+21*d),
    		m8 = pseudomo27(array, a+22*d, b);
    	return medof3(array,
    		medof3(array, m0, m1, m2),
    		medof3(array, m3, m4, m5),
    		medof3(array, m6, m7, m8)
    	);
    }
    
    private class Subarr implements Comparable<Subarr> {
    	public int start, mid, end;
    	public Subarr(int start, int mid, int end) {
    		this.start = start;
    		this.mid = mid;
    		this.end = end;
    	}
    	public Subarr(int start, int end) {
    		this.start = start;
    		this.mid = this.end = end;
    	}
    	public int length() {
    		return end - start;
    	}
    	public String toString() {
    		return String.format("block <%d, %d, %d>", start, mid, end);
    	}
    	public int compareTo(Subarr other) {
    		return (int) Math.signum(start - other.start);
    	}
    }
    
    private void gappedMerge(int[] array, int[] table, int[] tmp, int start, int end) {
    	int mid = start + (end - start) / 2;
    	if(start == mid)
    		return;
    	gappedMerge(array, table, tmp, start, mid);
    	gappedMerge(array, table, tmp, mid, end);
    	int m2 = mid, to = start, ctr = 0;
    	while(start < m2 && mid < end) {
    		if(Reads.compareIndices(array, table[start], table[mid], 1, true) <= 0) {
    			Writes.write(tmp, to+ctr, array[table[start++]], 0, false, true);
    		} else {
    			Writes.write(tmp, to+ctr, array[table[mid++]], 0, false, true);
    		}
    		ctr++;
    	}
    	while(start < m2) {
			Writes.write(tmp, to+ctr++, array[table[start++]], 1, true, true);
    	}
    	while(--ctr >= 0) {
    		Writes.write(array, table[to+ctr], tmp[to+ctr], 1, true, false);
    	}
    }
    
    private Subarr generateSubarray(int[] array, int[] cache, int[] tmp, int pivot, int start, int end) {
    	int large = 0, i = start;
    	while(large < cache.length && i < end) {
    		Highlights.markArray(1, i);
    		Delays.sleep(2.5);
    		if(Reads.compareValues(array[i], pivot) > 0) {
    			Writes.write(cache, large++, i, 0, false, true);
    		}
    		i++;
    	}
    	gappedMerge(array, cache, tmp, 0, large);
    	int k=i;
    	for(int j=large; j>0;) {
    		Writes.swap(array, --k, cache[--j], 1, true, false);
    	}
    	return new Subarr(start, i-large, i);
    }
    
    private <T> boolean some(T[] array, Function<? super T, Boolean> func) {
    	for(T i : array) {
    		if(func.apply(i)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    private Subarr min(int[] array, Subarr[] all) {
    	Subarr n = all[0];
    	for(Subarr i : all) {
    		if(i != n && i.mid < i.end && (n.mid >= n.end || Reads.compareIndices(array, i.mid, n.mid, 0.1, true) < 0)) {
    			n = i;
    		}
    	}
    	if(n.mid >= n.end) {
    		return null;
    	}
    	return n;
    }
    
    private void multiSwapBW(int[] array, int locA, int locB, int size) {
    	for(int i=size-1; i>=0; i--) {
    		Writes.swap(array, locA+i, locB+i, 1, true, false);
    	}
    }
    
    private void multiSwap(int[] array, int locA, int locB, int size) {
    	for(int i=0; i<size; i++) {
    		Writes.swap(array, locA+i, locB+i, 1, true, false);
    	}
    }
    
    private void OH_GOD_NO(int[] array, int block, Subarr... ptrs) {
    	ArrayList<Subarr> blocks = new ArrayList<>();
    	if(ptrs.length == 1) {
    		return;
    	}
    	do {
        	Subarr max = ptrs[0];
        	for(Subarr i : ptrs) {
        		if(i.mid-i.start > max.mid-max.start) {
        			max=i;
        		}
        	}
    		int interval = block, ogstart = max.start;
    		Subarr k;
    		do {
    			k = min(array, ptrs);
    			if(k != null)
    				Writes.swap(array, max.start++, k.mid++, 1, true, false);
    			interval--;
    		} while(k != null && interval > 0);
    		blocks.add(new Subarr(ogstart, max.start));
    		Writes.changeAllocAmount(1);
    		Writes.changeAuxWrites(1);
    	} while(some(ptrs, t -> t.mid < t.end));
    	int end = ptrs[ptrs.length-1].end;
    	int ble = blocks.size();
    	Subarr fragment = null;
    	if(blocks.get(ble-1).length() != block) {
    		ble--;
    		fragment = blocks.remove(ble);
    	}
    	Collections.sort(blocks);
    	for(int i=0; i<ble-1; i++) {
    		int m = i;
    		for(int j=i+1; j<ble; j++) {
    			if(Reads.compareIndices(array, blocks.get(m).start, blocks.get(j).start, 0.05, true) > 0) {
    				m = j;
    			}
    		}
    		multiSwap(array, blocks.get(m).start, blocks.get(i).start, block);
    	}
    	if(fragment != null) {
    		blocks.add(fragment);
    	}
    	for(int i=blocks.size()-1; i>=0; i--) {
    		Subarr b=blocks.get(i);
    		int l=b.length();
    		Writes.changeAllocAmount(-1);
    		if(i > 0 && end-l < blocks.get(i-1).end) {
    			IndexedRotations.radon(array, b.start, b.end, end, 0.125, true, false);
    			for(Subarr j : blocks) {
    				if(j.end > b.end) {
    					j.start -= l;
    					j.mid -= l;
    					j.end -= l;
    				}
    			}
    		} else
    			multiSwapBW(array, end-l, blocks.get(i).start, l);
    		end-=l;
    	}
    }
    
    private void OH_GOD_NO(int[] array, int block, Collection<Subarr> ptrs) {
    	OH_GOD_NO(array, block, ptrs.toArray(new Subarr[0]));
    }
    
    private void partition(int[] array, int[] cache, int[] tmp, int start, int end) {
    	if(end-start < cache.length) {
    		for(int i=0; i<end-start; i++) {
    			Writes.write(cache, i, start+i, 1, true, true);
    		}
    		gappedMerge(array, cache, tmp, 0, end-start);
    		return;
    	}
    	int pivot = pseudomo243(array, start, end), ap = array[pivot];
    	ArrayList<Subarr> stack = new ArrayList<>();
    	Subarr now = new Subarr(start, start, start);
    	int minBlock = cache.length / 2;
    	do {
    		now = generateSubarray(array, cache, tmp, ap, now.end, end);
    		stack.add(now);
    		if((now.mid-now.start)/2 < minBlock) {
    			minBlock = (now.mid - now.start) / 2;
    		}
    	} while(now.end < end);
		int buffersize = 0;
    	if(minBlock < 2) {
    		CaiSortMkII lazy = new CaiSortMkII(arrayVisualizer);
    		for(int i=0; i<stack.size(); i++) {
    			Subarr s = stack.get(i);
    			IndexedRotations.radon(array, start + buffersize, s.start, s.mid, 0.1, true, false);
    			for(int j=0; j<i; j++) {
    				Subarr t = stack.get(j);
    				t.start+=s.mid-s.start;
    				t.mid+=s.mid-s.start;
    				t.end+=s.mid-s.start;
    			}
    			buffersize += s.mid - s.start;
    			s.start = s.mid;
    		}
    		int[] ptrs = new int[stack.size()+1];
    		for(int i=0; i<stack.size(); i++) {
    			ptrs[i] = stack.get(i).mid;
    		}
    		ptrs[stack.size()] = end;
    		lazy.buf = start;
    		lazy.bufsz = buffersize;
    		lazy.caiMerge(array, ptrs);
    		IndexedRotations.radon(array, start, lazy.buf, lazy.buf+lazy.bufsz, 1, true, false);
    	} else {
    		for(int i=0; i<stack.size(); i++) {
    			Subarr s = stack.get(i);
    			buffersize += s.mid - s.start;
    		}
    		OH_GOD_NO(array, minBlock, stack);
    	}
		partition(array, cache, tmp, start, start + buffersize);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	int l = 0;
    	while(1<<(2*++l) < currentLength);
    	int size = 1<<l;
    	int[] c = Writes.createExternalArray(size),
    		  t = Writes.createExternalArray(size);
    	partition(array, c, t, 0, currentLength);
    }
}