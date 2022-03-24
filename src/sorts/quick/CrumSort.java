package sorts.quick;

import java.util.Random;

import main.ArrayVisualizer;
import sorts.merge.BlitQuadSort;
import sorts.templates.Sort;

// Crumsort: Coded by Scandum, ported* by Distray
// (*as best as possible from original program)

// This version was ported from the latest
// Crumsort repo, as of <YYYYMMDD> 2022/03/09.
final public class CrumSort extends Sort {
    public CrumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Crum");
        this.setRunAllSortsName("Crum Sort");
        this.setRunSortName("Crumsort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    final int crumOut = 28;
    private int crumAux = 32;
    private BlitQuadSort quadLatest;
    
    // Distray note: outputs 0 | 1 branchlessly,
    // allowing the same Flux optimizations to be made normally
    // without ternary ops
    private byte cmpOne(int[] array, int pos0, int pos1) {
    	int cmp = Reads.compareIndices(array, pos1, pos0, 0.125, true);
    	return (byte) (-(cmp >> 31));
    }
    
    private byte cmpOne_IV(int[] array, int pos, int value) {
    	int cmp = Reads.compareIndexValue(array, pos, value, 0.125, true);
    	return (byte) (-(cmp >> 31));
    }
    
    private byte cmpOne_VI(int[] array, int value, int pos) {
    	int cmp = Reads.compareValueIndex(array, value, pos, 0.125, true);
    	return (byte) (-(cmp >> 31));
    }
    
    // Distray note: Using this for more branchless shenanigans.
    private int equ(int a, int b) {
    	return ((a-b)>>31)+((b-a)>>31)+1;
    }
    
    private boolean crumAnalyze(int[] array, int[] swap, int offsMain, int offsSwap, int swapSize, int length) {
    	int balance = 0, streaks = 0,
    		dist, loop, pos = offsMain,
    		cnt = length;
    	while(cnt > 16) {
    		for(dist=0, loop=16; loop>0; --loop) {
    			dist += cmpOne(array, pos, pos+1);
        		pos++;
    		}
    		streaks += equ(dist, 0) | equ(dist, 16);
    		balance += dist;
    		cnt -= 16;
    	}
    	while(--cnt > 0) {
    		balance += cmpOne(array, pos, pos+1);
    		pos++;
    	}
    	if(balance == 0)
    		return true;
    	if(balance == length - 1) {
    		Writes.reversal(array, offsMain, offsMain+length-1, 1, true, false);
    		return true;
    	}
    	if(streaks > length / 24) {
    		quadLatest.quadSortSwap(array, swap, offsMain, offsSwap, swapSize, length);
    		return true;
    	}
    	return false;
    }
    
    private int medianOf3(int[] array, int pos0, int pos1, int pos2) {
    	byte[] tiers = new byte[2];
    	byte val;
    	val = cmpOne(array, pos0, pos1); tiers[0] = val; tiers[1] = (byte)(val ^ 1);
    	val = cmpOne(array, pos0, pos2); tiers[0] += val;
    	if(tiers[0] == 1) return pos0;
    	val = cmpOne(array, pos1, pos2); tiers[1] += val;
    	return tiers[1] == 1 ? pos1 : pos2;
    }
    
    private int medianOf5(int[] array, int pos0, int pos1, int pos2, int pos3, int pos4) {
    	byte[] tiers = new byte[4];
    	byte val;
    	val = cmpOne(array, pos0, pos1); tiers[0] = val; tiers[1] = (byte)(val ^ 1);
    	val = cmpOne(array, pos0, pos2); tiers[0] += val; tiers[2] = (byte)(val ^ 1);
    	val = cmpOne(array, pos0, pos3); tiers[0] += val; tiers[3] = (byte)(val ^ 1);
    	val = cmpOne(array, pos0, pos4); tiers[0] += val;
    	if(tiers[0] == 2) return pos0;
    	val = cmpOne(array, pos1, pos2); tiers[1] += val; tiers[2] += (byte)(val ^ 1);
    	val = cmpOne(array, pos1, pos3); tiers[1] += val; tiers[3] += (byte)(val ^ 1);
    	val = cmpOne(array, pos1, pos4); tiers[1] += val;
    	if(tiers[1] == 2) return pos1;
    	val = cmpOne(array, pos2, pos3); tiers[2] += val; tiers[3] += (byte)(val ^ 1);
    	val = cmpOne(array, pos2, pos4); tiers[2] += val;
    	if(tiers[2] == 2) return pos2;
    	val = cmpOne(array, pos3, pos4); tiers[3] += val;
    	return tiers[3] == 2 ? pos3 : pos4;
    }
    
    private int ninther(int[] array, int pos, int len) {
    	int div = len / 16,
	m0 = medianOf3(array, pos+2*div, pos+div, pos+5*div),
	m1 = medianOf3(array, pos+8*div, pos+6*div, pos+10*div),
	m2 = medianOf3(array, pos+14*div, pos+12*div, pos+15*div);
    	return array[medianOf3(array, m1, m0, m2)];
    }
    
    private int medianOf25(int[] array, int pos, int len) {
    	int div = len / 64,
	m0 = medianOf5(array, pos+4*div, pos+div, pos+2*div, pos+8*div, pos+10*div),
	m1 = medianOf5(array, pos+16*div, pos+12*div, pos+14*div, pos+18*div, pos+20*div),
	m2 = medianOf5(array, pos+32*div, pos+24*div, pos+30*div, pos+34*div, pos+38*div),
	m3 = medianOf5(array, pos+48*div, pos+42*div, pos+44*div, pos+50*div, pos+52*div),
	m4 = medianOf5(array, pos+60*div, pos+54*div, pos+56*div, pos+62*div, pos+63*div);
    	return array[medianOf5(array, m2, m0, m1, m3, m4)];
    }
    
    private int medianOfSqrt(int[] array, int[] swap, int offsMain, int offsSwap, int swapSz, int len) {
    	int sqrt = len > 262144 ? 256 : 128;
    	int div = len / sqrt;
    	int offset0 = (new Random().nextInt(sqrt)),
    		pta = offsMain + offset0,
    		pts = pta, ptx = pts;
    	for(int i=0; i<sqrt; i++) {
    		Writes.write(swap, offsSwap, array[pts], 0.5, true, true);
    		Writes.write(array, pts++, array[pta], 0.5, true, false);
    		Writes.write(array, pta, swap[offsSwap], 0.5, true, false);
    		pta += div;
    	}
    	quadLatest.quadSortSwap(array, swap, ptx, offsSwap, swapSz, sqrt);
    	return array[ptx+sqrt/2];
    }
    
    private int fulcrumReversePartition(int[] array, int[] swap, int bucket, int piv, int swapSize, int offsMain, int offsSwap, int nmemb) {
    	int m = 0, i, v;
		int[] p = bucket == 1 ? swap : array;
    	if(nmemb <= swapSize) {
    		int cnt = nmemb / 8, ptx = bucket == 1 ? offsSwap : offsMain;
    		do for(i=8; i>0; i--) {
    			v = cmpOne_VI(p, piv, ptx); Writes.write(swap, offsSwap++ - m, p[ptx], 1, true, swap!=array); Writes.write(array, offsMain + m, p[ptx++], 1, true, false); m += v;
    		} while(--cnt > 0);
    		for(cnt = nmemb & 7; cnt > 0; cnt--) {
    			v = cmpOne_VI(p, piv, ptx); Writes.write(swap, offsSwap++ - m, p[ptx], 1, true, swap!=array); Writes.write(array, offsMain + m, p[ptx++], 1, true, false); m += v;
    		}
    		Writes.arraycopy(swap, offsSwap-nmemb, array, offsMain+m, nmemb-m, 1, true, false);
    		return m;
    	}
    	Writes.arraycopy(array, offsMain, swap, offsSwap, 16, 1, true, array!=swap);
    	Writes.arraycopy(array, offsMain+nmemb-16, swap, offsSwap+16, 16, 1, true, array!=swap);
    	int ptl = offsMain, ptr = offsMain + nmemb - 1,
    		pta = ptl + 16, tpa = ptr - 16, cnt = nmemb / 16 - 2;
    	while(true) {
    		if(pta - ptl - m <= 16) {
    			if(cnt-- == 0) break;
    			for(i=16; i>0; i--) {
    				v = cmpOne_VI(array, piv, pta); Writes.write(array,ptl+m,array[pta],1,true,false); Writes.write(array,ptr+m,array[pta++],1,true,false); m+=v; ptr--;
    			}
    		}
    		if(pta - ptl - m > 16) {
    			if(cnt-- == 0) break;
    			for(i=16; i>0; i--) {
    				v = cmpOne_VI(array, piv, tpa); Writes.write(array,ptl+m,array[tpa],1,true,false); Writes.write(array,ptr+m,array[tpa--],1,true,false); m+=v; ptr--;
    			}
    		}
    	}
    	if(pta-ptl-m <= 16) {
			for(i=nmemb%16; i>0; i--) {
				v = cmpOne_VI(array, piv, pta); Writes.write(array,ptl+m,array[pta],1,true,false); Writes.write(array,ptr+m,array[pta++],1,true,false); m+=v; ptr--;
			}
    	} else {
			for(i=nmemb%16; i>0; i--) {
				v = cmpOne_VI(array, piv, tpa); Writes.write(array,ptl+m,array[tpa],1,true,false); Writes.write(array,ptr+m,array[tpa--],1,true,false); m+=v; ptr--;
			}
    	}
    	pta=offsSwap;
    	for(cnt=32; cnt>0; cnt--) {
			v = cmpOne_VI(swap, piv, pta); Writes.write(array,ptl+m,swap[pta],1,true,false); Writes.write(array,ptr+m,swap[pta++],1,true,false); m+=v; ptr--;
    	}
    	return m;
    }
    
    private int fulcrumDefaultPartition(int[] array, int[] swap, int bucket, int piv, int swapSize, int offsMain, int offsSwap, int nmemb) {
    	int m = 0, i, v;
		int[] p = bucket == 1 ? swap : array;
		if(nmemb <= swapSize) {
			int cnt = nmemb / 8, ptx = bucket == 1 ? offsSwap : offsMain;
			do for(i=8; i>0; i--) {
				v = cmpOne_IV(p, ptx, piv); Writes.write(swap, offsSwap++ - m, p[ptx], 1, true, swap!=array); Writes.write(array, offsMain + m, p[ptx++], 1, true, false); m += v;
			} while(--cnt > 0);
			for(cnt = nmemb & 7; cnt > 0; cnt--) {
				v = cmpOne_IV(p, ptx, piv); Writes.write(swap, offsSwap++ - m, p[ptx], 1, true, swap!=array); Writes.write(array, offsMain + m, p[ptx++], 1, true, false); m += v;
			}
			Writes.arraycopy(swap, offsSwap-nmemb, array, offsMain+m, nmemb-m, 1, true, false);
			return m;
		}
    	Writes.arraycopy(array, offsMain, swap, offsSwap, 16, 1, true, array!=swap);
    	Writes.arraycopy(array, offsMain+nmemb-16, swap, offsSwap+16, 16, 1, true, array!=swap);
    	int ptl = offsMain, ptr = offsMain + nmemb - 1,
    		pta = ptl + 16, tpa = ptr - 16, cnt = nmemb / 16 - 2;
    	while(true) {
    		if(pta - ptl - m <= 16) {
    			if(cnt-- == 0) break;
    			for(i=16; i>0; i--) {
    				v = cmpOne_IV(array, pta, piv); Writes.write(array,ptl+m,array[pta],1,true,false); Writes.write(array,ptr+m,array[pta++],1,true,false); m+=v; ptr--;
    			}
    		}
    		if(pta - ptl - m > 16) {
    			if(cnt-- == 0) break;
    			for(i=16; i>0; i--) {
    				v = cmpOne_IV(array, tpa, piv); Writes.write(array,ptl+m,array[tpa],1,true,false); Writes.write(array,ptr+m,array[tpa--],1,true,false); m+=v; ptr--;
    			}
    		}
    	}
    	if(pta-ptl-m <= 16) {
			for(i=nmemb%16; i>0; i--) {
				v = cmpOne_IV(array, pta, piv); Writes.write(array,ptl+m,array[pta],1,true,false); Writes.write(array,ptr+m,array[pta++],1,true,false); m+=v; ptr--;
			}
    	} else {
			for(i=nmemb%16; i>0; i--) {
				v = cmpOne_IV(array, tpa, piv); Writes.write(array,ptl+m,array[tpa],1,true,false); Writes.write(array,ptr+m,array[tpa--],1,true,false); m+=v; ptr--;
			}
    	}
    	pta=offsSwap;
    	for(cnt=32; cnt>0; cnt--) {
			v = cmpOne_IV(swap, pta, piv); Writes.write(array,ptl+m,swap[pta],1,true,false); Writes.write(array,ptr+m,swap[pta++],1,true,false); m+=v; ptr--;
    	}
    	return m;
    }
    
    private void fulcrumPartition(int[] array, int[] swap, int offsMain, int offsSwap, int swapSize, int nmemb) {
    	int aSize = 0, sSize, old = 0, piv;
    	while(true) {
    		if(nmemb <= 4096) {
    			piv = ninther(array, offsMain, nmemb);
    		} else if(nmemb <= 65536) {
    			piv = medianOf25(array, offsMain, nmemb);
    		} else {
    			piv = medianOfSqrt(array, swap, offsMain, offsSwap, swapSize, nmemb);
    		}
    		if(aSize > 0 && Reads.compareValues(old, piv) <= 0) {
    			aSize = fulcrumReversePartition(array, swap, 0, piv, swapSize, offsMain, offsSwap, nmemb);
    			sSize = nmemb - aSize;
    			if(sSize <= aSize / 16 || aSize <= crumOut) {
    				quadLatest.quadSortSwap(array, swap, offsMain, offsSwap, swapSize, aSize);
    			} else {
    				fulcrumPartition(array, swap, offsMain, offsSwap, swapSize, aSize);
    			}
				return;
    		}
    		aSize = fulcrumDefaultPartition(array, swap, 0, piv, swapSize, offsMain, offsSwap, nmemb);
			sSize = nmemb - aSize;
			if(aSize <= sSize / 16 || sSize <= crumOut) {
				if(sSize == 0) {
					aSize = fulcrumReversePartition(array, swap, 0, piv, swapSize, offsMain, offsSwap, aSize);
					sSize = nmemb - aSize;
	    			if(sSize <= aSize / 16 || aSize <= crumOut) {
	    				quadLatest.quadSortSwap(array, swap, offsMain, offsSwap, swapSize, aSize);
	    			} else {
	    				fulcrumPartition(array, swap, offsMain, offsSwap, swapSize, aSize);
	    			}
    				return;
				}
				quadLatest.quadSortSwap(array, swap, offsMain + aSize, offsSwap, swapSize, sSize);
			} else {
				fulcrumPartition(array, swap, offsMain + aSize, offsSwap, swapSize, sSize);
			}
			if(sSize <= aSize / 16 || aSize <= crumOut) {
				quadLatest.quadSortSwap(array, swap, offsMain, offsSwap, swapSize, aSize);
				return;
			}
			nmemb = aSize;
			old = piv;
    	}
    }
    public void crumSort(int[] array, int start, int nmemb) {
    	quadLatest = new BlitQuadSort(arrayVisualizer);
    	if(nmemb < 32) {
    		quadLatest.tailSwap(array, start, nmemb);
    		return;
    	}
    	int swapSize;
    	if(crumAux == -1) {
    		swapSize = 32;
    		while(swapSize * swapSize <= nmemb) {
    			swapSize *= 4;
    		}
    	} else
    		swapSize = crumAux;
    	
    	int[] swap = Writes.createExternalArray(swapSize);
    	
    	if(!crumAnalyze(array, swap, start, 0, swapSize, nmemb)) {
    		fulcrumPartition(array, swap, start, 0, swapSize, nmemb);
    	}
    	
    	Writes.deleteExternalArray(swap);
    }
    public void crumSortSwap(int[] array, int[] swap, int startMain, int startSwap, int swapSize, int nmemb) {
    	quadLatest = new BlitQuadSort(arrayVisualizer);
    	if(nmemb < 32) {
    		quadLatest.tailSwap(array, startMain, nmemb);
    		return;
    	}
    	if(!crumAnalyze(array, swap, startMain, startSwap, swapSize, nmemb)) {
    		fulcrumPartition(array, swap, startMain, startSwap, swapSize, nmemb);
    	}
    }
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	crumSort(array, 0, currentLength);
    }
}