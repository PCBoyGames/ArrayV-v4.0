package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;
import utils.Timer;

/*
Copyright (C) 2014-2021 Igor van den Hoven ivdhoven@gmail.com
*/

/*
Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:
The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


// *Ported by Distray, version in wolfsort repo as of 2022/03/08*

final public class UpdatedQuadSort extends Sort {
	private Timer Timer;
    public UpdatedQuadSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Quad (Updated)");
        this.setRunAllSortsName("Quad Sort (Updated)");
        this.setRunSortName("Updated Quadsort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        
        this.Timer = arrayVisualizer.getTimer();
    }
    
    private static int swapAuxiliary = 32;
    int[] main;
    
    int cmp(int v, int w) {
    	int x;
    	Reads.addComparison();
    	Timer.startLap("Compare");
    	x = -(((arrayVisualizer.reversedComparator()?1:-1)*(v-w))>>31);
    	Timer.stopLap();
    	return x;
    }
    
    boolean cmpB(int v, int w) {
    	boolean x;
    	Reads.addComparison();
    	Timer.startLap("Compare");
    	x = arrayVisualizer.reversedComparator() ? v < w : v > w;
    	Timer.stopLap();
    	return x;
    }
    
    boolean conditionalPair(int[] array, int pair) {
    	int x = cmp(array[pair],array[pair+1]);
    	int y = array[pair+1-x];
    	Writes.write(array, pair, array[pair+x], 0.5, true, false);
    	Writes.write(array, pair+1, y, 0.5, true, false);
    	return x == 1;
    }
    
    void parityMerge2(int[] array, int[] swap, int offs0, int offs1) {
    	int x, y, ptl = offs0, ptr = offs0+2, pts = offs1;
    	y = cmp(array[ptl],array[ptr]); x = y ^ 1; Writes.write(swap, pts+x, array[ptr], 0.25, true, swap!=main); ptr += y;
    	Writes.write(swap, pts+y, array[ptl], 0.25, true, false); ptl += x; pts++;
    	Writes.write(swap, pts, cmp(array[ptl], array[ptr]) == 0 ? array[ptl] : array[ptr], 0.25, true, swap!=main);
    	
    	ptl = offs0 + 1; ptr = offs0 + 3; pts = offs1 + 3;
    	y = cmp(array[ptl],array[ptr]); x = y ^ 1; Writes.write(swap, --pts+x, array[ptr], 0.25, true, swap!=main); ptr -= x;
    	Writes.write(swap, pts+y, array[ptl], 0.25, true, false); ptl -= y;
    	Writes.write(swap, pts, cmp(array[ptl], array[ptr]) == 1 ? array[ptl] : array[ptr], 0.25, true, swap!=main);
    }
    
    void parityMerge4(int[] array, int[] swap, int offs0, int offs1) {
    	int x, y, ptl = offs0, ptr = offs0+4, pts = offs1;
    	y = cmp(array[ptl],array[ptr]); x = y ^ 1; Writes.write(swap, pts+x, array[ptr], 0.25, true, swap!=main); ptr += y;
    	Writes.write(swap, pts+y, array[ptl], 0.25, true, false); ptl += x; pts++;
    	y = cmp(array[ptl],array[ptr]); x = y ^ 1; Writes.write(swap, pts+x, array[ptr], 0.25, true, swap!=main); ptr += y;
    	Writes.write(swap, pts+y, array[ptl], 0.25, true, false); ptl += x; pts++;
    	y = cmp(array[ptl],array[ptr]); x = y ^ 1; Writes.write(swap, pts+x, array[ptr], 0.25, true, swap!=main); ptr += y;
    	Writes.write(swap, pts+y, array[ptl], 0.25, true, false); ptl += x; pts++;
    	Writes.write(swap, pts, cmp(array[ptl], array[ptr]) == 0 ? array[ptl] : array[ptr], 0.25, true, swap!=main);
    	
    	ptl = offs0 + 3; ptr = offs0 + 7; pts = offs1 + 7;
    	y = cmp(array[ptl],array[ptr]); x = y ^ 1; Writes.write(swap, --pts+x, array[ptr], 0.25, true, swap!=main); ptr -= x;
    	Writes.write(swap, pts+y, array[ptl], 0.25, true, false); ptl -= y;
    	y = cmp(array[ptl],array[ptr]); x = y ^ 1; Writes.write(swap, --pts+x, array[ptr], 0.25, true, swap!=main); ptr -= x;
    	Writes.write(swap, pts+y, array[ptl], 0.25, true, false); ptl -= y;
    	y = cmp(array[ptl],array[ptr]); x = y ^ 1; Writes.write(swap, --pts+x, array[ptr], 0.25, true, swap!=main); ptr -= x;
    	Writes.write(swap, pts+y, array[ptl], 0.25, true, false); ptl -= y;
    	Writes.write(swap, pts, cmp(array[ptl], array[ptr]) == 1 ? array[ptl] : array[ptr], 0.25, true, swap!=main);
    }
    
    public void unguardedInsert(int[] array, int offs0, int offs1, int nmemb) {
    	int pta, end, key, top;
    	for(int i=offs1; i<nmemb; i++) {
    		pta = end = offs0 + i;
    		if(Reads.compareValues(array[--pta], array[end]) <= 0)
    			continue;
    		key = array[end];
    		if(Reads.compareValues(array[offs0+1], key) > 0) {
    			top = i - 1;
    			do {
    				Writes.write(array, end--, array[pta--], 1, true, false);
    			} while(--top != 0);
				Writes.write(array, end--, key, 1, true, false);
    		} else {
    			do {
    				Writes.write(array, end--, array[pta--], 1, true, false);
    				Writes.write(array, end--, array[pta--], 1, true, false);
    			} while(Reads.compareValues(array[pta], key) > 0);
				Writes.write(array, end, key, 1, true, false);
    		}
    		conditionalPair(array, end);
    	}
    }
    
    void bubbleSort(int[] array, int offs0, int nmemb) {
    	if(nmemb > 1) {
    		if(nmemb > 2) {
    			conditionalPair(array, offs0++);
    			conditionalPair(array, offs0--);
    		}
			conditionalPair(array, offs0);
    	}
    }
    
    void quadSwap4(int[] array, int offs0) {
    	conditionalPair(array, offs0); offs0 += 2;
    	conditionalPair(array, offs0); offs0--;
    	if(cmpB(array[offs0], array[offs0+1])) {
    		Writes.swap(array, offs0, offs0+1, 1, true, array!=main); offs0--;
        	conditionalPair(array, offs0); offs0 += 2;
        	conditionalPair(array, offs0); offs0--;
        	conditionalPair(array, offs0);
    	}
    }
    
    void paritySwap8(int[] array, int offs0) {
    	int ptl = offs0;
    	conditionalPair(array, ptl); ptl += 2;
    	conditionalPair(array, ptl); ptl += 2;
    	conditionalPair(array, ptl); ptl += 2;
    	conditionalPair(array, ptl);
    	if(!cmpB(array[offs0+1], array[offs0+2]) &&
		   !cmpB(array[offs0+3], array[offs0+4]) &&
		   !cmpB(array[offs0+5], array[offs0+6]))
    		return;
    	int[] swap = Writes.createExternalArray(8);
		parityMerge2(array, swap, offs0, 0);
    	parityMerge2(array, swap, offs0+4, 4);
    	parityMerge4(swap, array, 0, offs0);
    	Writes.deleteExternalArray(swap);
    }
    
    void parityMerge(int[] dest, int[] from, int offs1, int offs0, int block, int nmemb) {
    	int ptl = offs0, ptr = offs0 + block, ptd = offs1, tpl = offs0 + block - 1, tpr = offs0 + nmemb - 1, tpd = offs1 + nmemb - 1, x, y;
    	for(block--; block>0; block--) {
    		y=cmp(from[ptl],from[ptr]);x=y^1;Writes.write(dest,ptd+x,from[ptr],0.25,true,dest!=main);ptr+=y;Writes.write(dest,ptd+y,from[ptl],0.25,true,dest!=main);ptl+=x;ptd++;
    		y=cmp(from[tpl],from[tpr]);x=y^1;Writes.write(dest,--tpd+x,from[tpr],0.25,true,dest!=main);tpr-=x;Writes.write(dest,tpd+y,from[tpl],0.25,true,dest!=main);tpl-=y;
    	}
    	Writes.write(dest,ptd,cmp(from[ptl],from[ptr])==0?from[ptl]:from[ptr],0.25,true,dest!=main);
    	Writes.write(dest,tpd,cmp(from[tpl],from[tpr])==1?from[tpl]:from[tpr],0.25,true,dest!=main);
    }
    
    void paritySwap16(int[] array, int offs0) {
    	quadSwap4(array, offs0);
    	quadSwap4(array, offs0+4);
    	quadSwap4(array, offs0+8);
    	quadSwap4(array, offs0+12);
    	if(!cmpB(array[offs0+3], array[offs0+4]) &&
		   !cmpB(array[offs0+7], array[offs0+8]) &&
		   !cmpB(array[offs0+11], array[offs0+12]))
    		return;
    	int[] swap = Writes.createExternalArray(16);
		parityMerge4(array, swap, offs0, 0);
    	parityMerge4(array, swap, offs0+8, 8);
    	parityMerge(array, swap, offs0, 0, 8, 16);
    	Writes.deleteExternalArray(swap);
    }
    
    public void tailSwap(int[] array, int offs0, int nmemb) {
    	if(nmemb < 4) {
    		bubbleSort(array, offs0, nmemb);
    		return;
    	}
    	if(nmemb < 8) {
    		quadSwap4(array, offs0);
    		unguardedInsert(array, offs0, 4, nmemb);
    		return;
    	}
    	if(nmemb < 16) {
    		paritySwap8(array, offs0);
    		unguardedInsert(array, offs0, 8, nmemb);
    		return;
    	}
		paritySwap16(array, offs0);
		unguardedInsert(array, offs0, 16, nmemb);
    }
    
    void parityTailSwap8(int[] array, int offs0) {
    	conditionalPair(array, offs0+4);
    	conditionalPair(array, offs0+6);
    	if(!cmpB(array[offs0+3], array[offs0+4]) && !cmpB(array[offs0+5], array[offs0+6])) {
    		return;
    	}
    	int[] swap = Writes.createExternalArray(8);
    	Writes.arraycopy(array, offs0, swap, 0, 4, 1, true, false);
    	parityMerge2(array, swap, offs0+4, 4);
    	parityMerge4(swap, array, 0, offs0);
    	Writes.deleteExternalArray(swap);
    }
    
    void parityTailFlip8(int[] array, int offs0) {
    	if(!cmpB(array[offs0+3], array[offs0+4])) {
    		return;
    	}
    	int[] swap = Writes.createExternalArray(8);
    	Writes.arraycopy(array, offs0, swap, 0, 8, 1, true, false);
    	parityMerge4(swap, array, 0, offs0);
    	Writes.deleteExternalArray(swap);
    }
    
    int quadSwap(int[] array, int offs0, int nmemb) {
    	int[] swap = Writes.createExternalArray(32);
    	int count = nmemb/8*2, reverse, pta = offs0, pts = pta, pte;
    	boolean swapper = false;
    	while(count != 0 || swapper) {
    		if(!swapper) {
        		count--;
	    		int z = (cmp(array[pta], array[pta+1])) |
	    				(cmp(array[pta+1], array[pta+2]) * 2) |
	    				(cmp(array[pta+2], array[pta+3]) * 4);
	    		switch(z) {
	    			case 0: break;
	    			case 1:
	    				Writes.swap(array, pta, pta+1, 1, true, false);
	    				conditionalPair(array, pta+1);
	    				conditionalPair(array, pta+2);
	    				break;
	    			case 2:
	    				Writes.swap(array, pta+1, pta+2, 1, true, false);
						conditionalPair(array, pta);
						conditionalPair(array, pta+2);
						conditionalPair(array, pta+1);
						break;
	    			case 3:
	    				Writes.swap(array, pta, pta+2, 1, true, false);
						conditionalPair(array, pta+2);
						conditionalPair(array, pta+1);
						break;
	    			case 4:
	    				Writes.swap(array, pta+2, pta+3, 1, true, false);
						conditionalPair(array, pta+1);
						conditionalPair(array, pta);
						break;
	    			case 5:
	    				Writes.swap(array, pta, pta+1, 1, true, false);
	    				Writes.swap(array, pta+2, pta+3, 1, true, false);
						conditionalPair(array, pta+1);
						conditionalPair(array, pta+2);
						conditionalPair(array, pta);
						break;
	    			case 6:
	    				Writes.swap(array, pta+1, pta+3, 1, true, false);
						conditionalPair(array, pta);
						conditionalPair(array, pta+1);
						break;
	    			case 7:
	    				pts = pta;
	    				swapper = true;
						break;
	    		}
    		}
    		if(!swapper) {
    			count--;
    			parityTailSwap8(array, pta);
    			pta += 8;
    			continue;
    		}
    		pta += 4;
    		swapper = false;
    		if(count-- != 0) {
    			if(cmpB(array[pta], array[pta+1])) {
        			if(cmpB(array[pta+2], array[pta+3])) {
            			if(cmpB(array[pta+1], array[pta+2])) {
                			if(cmpB(array[pta-1], array[pta])) {
                        		swapper = true;
                				continue;
                			}
            			}
            			Writes.swap(array, pta+2, pta+3, 1, true, false);
        			}
        			Writes.swap(array, pta, pta+1, 1, true, false);
    			} else if(cmpB(array[pta+2], array[pta+3])) {
        			Writes.swap(array, pta+2, pta+3, 1, true, false);
    			}
    			if(cmpB(array[pta+1], array[pta+2])) {
    				Writes.swap(array, pta+1, pta+2, 1, true, false);
    				conditionalPair(array, pta);
    				conditionalPair(array, pta+2);
    				conditionalPair(array, pta+1);
    			}
    			pte = pta - 1;
    			reverse = (pte - pts) / 2;
    			do {
    				Writes.swap(array, pts++, pte--, 1, true, false);
    			} while(reverse-- > 0);
    			if(count % 2 == 0) {
    				pta -= 4;
    				parityTailFlip8(array, pta);
    			} else {
    				count--;
    				parityTailSwap8(array, pta);
    			}
    			pta+=8;
    			continue;
    		}
    		if(pts == offs0) {
    			switch(nmemb % 8) {
					case 7: if(!cmpB(array[pta+5], array[pta+6])) break;
					case 6: if(!cmpB(array[pta+4], array[pta+5])) break;
					case 5: if(!cmpB(array[pta+3], array[pta+4])) break;
					case 4: if(!cmpB(array[pta+2], array[pta+3])) break;
					case 3: if(!cmpB(array[pta+1], array[pta+2])) break;
					case 2: if(!cmpB(array[pta], array[pta+1])) break;
    				case 1: if(!cmpB(array[pta-1], array[pta])) break;
    				case 0:
    					pte = pts + nmemb - 1;
    					reverse = (pte - pts) / 2;
    					do {
    						Writes.swap(array, pts++, pte--, 1, true, false);
    					} while(reverse-- > 0);
    					Writes.deleteExternalArray(swap);
    					return 1;
    			}
    		}
    		pte = pta - 1;
    		reverse = (pte - pts) / 2;
    		do {
    			Writes.swap(array, pts++, pte--, 1, true, false);
    		} while(reverse-- > 0);
    		break;
    	}
    	tailSwap(array, pta, nmemb%8);
    	pta = offs0;
    	for(count = nmemb/32; count-- > 0; pta+=32) {
    		if(!cmpB(array[pta+7], array[pta+8]) && !cmpB(array[pta+15], array[pta+16]) && !cmpB(array[pta+23], array[pta+24]))
    			continue;
    		parityMerge(swap, array, 0, pta, 8, 16);
    		parityMerge(swap, array, 16, pta+16, 8, 16);
    		parityMerge(array, swap, pta, 0, 16, 32);
    	}
    	if(nmemb % 32 > 8) {
    		tailMerge(array, swap, pta, 0, 32, nmemb % 32, 8);
    	}
		Writes.deleteExternalArray(swap);
    	return 0;
    }
    
    void forwardMerge(int[] dest, int[] from, int offs1, int offs0, int block) {
    	int ptl = offs0, ptr = offs0 + block, m = ptr - 1, e = ptr + block - 1, y, x;
    	if(!cmpB(from[m], from[e-block/4])) {
    		while(ptl<m-1) {
    			if(!cmpB(from[ptl+1], from[ptr])) {
    				Writes.write(dest, offs1++, from[ptl++], 1, true, dest!=main);
    				Writes.write(dest, offs1++, from[ptl++], 1, true, dest!=main);
    			} else if(cmpB(from[ptl], from[ptr+1])) {
    				Writes.write(dest, offs1++, from[ptr++], 1, true, dest!=main);
    				Writes.write(dest, offs1++, from[ptr++], 1, true, dest!=main);
    			} else {
    				y = cmp(from[ptl], from[ptr]); x=y^1; Writes.write(dest, offs1+x, from[ptr], 1, true, dest!=main); ptr++; Writes.write(dest, offs1+y, from[ptl], 1, true, dest!=main); ptl++; offs1 += 2;
    				y = cmp(from[ptl], from[ptr]); x=y^1; Writes.write(dest, offs1+x, from[ptr], 1, true, dest!=main); ptr+=y; Writes.write(dest, offs1+y, from[ptl], 1, true, dest!=main); ptl+=x; offs1 ++;
    			}
    		}
    		while(ptl<=m) {
    			Writes.write(dest, offs1++, cmp(from[ptl], from[ptr]) == 0 ? from[ptl++] : from[ptr++], 1, true, dest!=main);
    		}
    		do {
    			Writes.write(dest, offs1++, from[ptr++], 1, true, false);
    		} while(ptr <= e);
    	} else if(cmpB(from[m-block/4], from[e])) {
    		while(ptr<e-1) {
    			if(cmpB(from[ptl],from[ptr+1])) {
    				Writes.write(dest, offs1++, from[ptr++], 1, true, dest!=main);
    				Writes.write(dest, offs1++, from[ptr++], 1, true, dest!=main);
    			} else if(!cmpB(from[ptl+1],from[ptr])) {
    				Writes.write(dest, offs1++, from[ptl++], 1, true, dest!=main);
    				Writes.write(dest, offs1++, from[ptl++], 1, true, dest!=main);
    			} else {
    				y = cmp(from[ptl], from[ptr]); x=y^1; Writes.write(dest, offs1+x, from[ptr], 1, true, dest!=main); ptr++; Writes.write(dest, offs1+y, from[ptl], 1, true, dest!=main); ptl++; offs1 += 2;
    				y = cmp(from[ptl], from[ptr]); x=y^1; Writes.write(dest, offs1+x, from[ptr], 1, true, dest!=main); ptr+=y; Writes.write(dest, offs1+y, from[ptl], 1, true, dest!=main); ptl+=x; offs1 ++;
    			}
    		}
    		while(ptr<=e) {
    			Writes.write(dest, offs1++, cmp(from[ptl], from[ptr]) == 1 ? from[ptr++] : from[ptl++], 1, true, dest!=main);
    		}
    		do {
    			Writes.write(dest, offs1++, from[ptl++], 1, true, false);
    		} while(ptl <= m);
    	} else {
    		parityMerge(dest, from, offs1, offs0, block, 2 * block);
    	}
    }
    
    void quadMergeBlock(int[] array, int[] swap, int offs0, int offs1, int block) {
    	int c_max = offs0 + block, c, pts;
    	if(!cmpB(array[c_max-1], array[c_max])) {
    		c_max += 2 * block;
        	if(!cmpB(array[c_max-1], array[c_max])) {
        		c_max -= block;
            	if(!cmpB(array[c_max-1], array[c_max])) {
            		return;
            	}
            	pts = offs1;
            	c = offs0;
            	do
            		Writes.write(swap, pts++, array[c++], 1, true, swap!=main);
            	while(c < c_max);
            	c_max = c + 2 * block;
            	do
            		Writes.write(swap, pts++, array[c++], 1, true, swap!=main);
            	while(c < c_max);
            	forwardMerge(array, swap, offs0, offs1, 2 * block);
            	return;
        	}
        	pts = offs1;
        	c = offs0;
        	c_max = c + 2 * block;
        	do
        		Writes.write(swap, pts++, array[c++], 1, true, swap!=main);
        	while(c < c_max);
    	} else {
    		forwardMerge(swap, array, offs1, offs0, block);
    	}
		forwardMerge(swap, array, offs1 + 2 * block, offs0 + 2 * block, block);
		forwardMerge(array, swap, offs0, offs1, 2 * block);
    }
    
    int quadMerge(int[] array, int[] swap, int offs0, int offs1, int swapSize, int nmemb, int block) {
    	int pte = offs0 + nmemb, pta;
    	block *= 4;
    	while(block <= nmemb && block <= swapSize) {
    		pta = offs0;
    		do {
    			quadMergeBlock(array, swap, pta, offs1, block / 4);
    			pta += block;
    		} while(pta + block <= pte);
    		tailMerge(array, swap, pta, offs1, swapSize, pte - pta, block / 4);
    		block *= 4;
    	}
    	
    	tailMerge(array, swap, offs0, offs1, swapSize, nmemb, block / 4);
    	return block / 2;
    }
    
    void partialForwardMerge(int[] array, int[] swap, int offs0, int offs1, int nmemb, int block) {
    	int r = offs0 + block, e = offs0 + nmemb - 1, s = offs1, m = offs1 + block - 1, y, x;
    	Writes.arraycopy(array, offs0, swap, offs1, block, 1, true, swap!=main);
    	while(s < m - 1 && r < e - 1) {
    		if(cmpB(swap[s], array[r+1])) {
    			Writes.write(array, offs0++, array[r++], 1, true, array!=main);
    			Writes.write(array, offs0++, array[r++], 1, true, array!=main);
    		} else if(!cmpB(swap[s+1], array[r])) {
    			Writes.write(array, offs0++, swap[s++], 1, true, array!=main);
    			Writes.write(array, offs0++, swap[s++], 1, true, array!=main);
    		} else {
    			y = cmp(swap[s], array[r]); x = y ^ 1; Writes.write(array, offs0 + x, array[r], 1, true, array!=main); r++; Writes.write(array, offs0 + y, swap[s], 1, true, array!=main); s++; offs0+=2;
    			y = cmp(swap[s], array[r]); x = y ^ 1; Writes.write(array, offs0 + x, array[r], 1, true, array!=main); r+=y; Writes.write(array, offs0 + y, swap[s], 1, true, array!=main); s+=x; offs0++;
    		}
    	}
    	while(s <= m && r <= e) {
    		Writes.write(array, offs0++, cmp(swap[s], array[r]) == 0 ? swap[s++] : array[r++], 1, true, array!=main);
    	}
    	while(s <= m) {
    		Writes.write(array, offs0++, swap[s++], 1, true, array!=main);
    	}
    }
    
    void partialBackwardMerge(int[] array, int[] swap, int offs0, int offs1, int nmemb, int block) {
    	int m = offs0 + block - 1, e = offs0 + nmemb - 1, s = offs1 + nmemb - block - 1, x, y;
    	if(!cmpB(array[m], array[m+1]))
    		return;
    	Writes.arraycopy(array, offs0+block, swap, offs1, nmemb-block, 1, true, swap!=main);
    	while(s > offs1 + 1 && m > offs0 + 1) {
    		if(cmpB(array[m-1], swap[s])) {
    			Writes.write(array, e--, array[m--], 1, true, array!=main);
    			Writes.write(array, e--, array[m--], 1, true, array!=main);
    		} else if(!cmpB(array[m], swap[s-1])) {
    			Writes.write(array, e--, swap[s--], 1, true, array!=main);
    			Writes.write(array, e--, swap[s--], 1, true, array!=main);
    		} else {
    			y = cmp(array[m], swap[s]); x = y ^ 1; Writes.write(array, --e + x, swap[s--], 1, true, array!=main); Writes.write(array, e + y, array[m--], 1, true, array!=main); e--;
    			y = cmp(array[m], swap[s]); x = y ^ 1; Writes.write(array, --e + x, swap[s], 1, true, array!=main); s-=x; Writes.write(array, e + y, array[m], 1, true, array!=main); m-=y;
    		}
    	}
    	while(s >= offs1 && m >= offs0) {
    		Writes.write(array, e--, cmp(array[m], swap[s]) == 1 ? array[m--] : swap[s--], 1, true, array!=main);
    	}
    	while(s >= offs1) {
    		Writes.write(array, e--, swap[s--], 1, true, array!=main);
    	}
    }
    
    void tailMerge(int[] array, int[] swap, int offs0, int offs1, int swapSize, int nmemb, int block) {
    	int pta, pte = offs0 + nmemb;
    	while(block < nmemb && block <= swapSize) {
    		pta = offs0;
    		for(; pta + block < pte; pta += 2 * block) {
    			if(pta + block * 2 < pte) {
    				partialBackwardMerge(array, swap, pta, offs1, 2 * block, block);
    				continue;
    			}
    			partialBackwardMerge(array, swap, pta, offs1, pte - pta, block);
    			break;
    		}
    		block *= 2;
    	}
    }
    
    void rotate(int[] array, int[] swap, int offs0, int offs1, int swapSize, int nmemb, int left) {
    	int right = nmemb - left, bridge;
    	if(left <= swapSize) {
    		Writes.arraycopy(array, offs0, swap, offs1, left, 1, true, swap!=main);
    		Writes.arraycopy(array, offs0+left, array, offs0, right, 1, true, array!=main);
    		Writes.arraycopy(swap, offs1, array, offs0+right, left, 1, true, array!=main);
    	} else if(right <= swapSize) {
    		Writes.arraycopy(array, offs0+left, swap, offs1, right, 1, true, swap!=main);
    		Writes.arraycopy(array, offs0, array, offs0+right, left, 1, true, array!=main);
    		Writes.arraycopy(swap, offs1, array, offs0, right, 1, true, swap!=main);
    	} else if((bridge = Math.abs(right - left)) <= swapSize) {
    		if(bridge == 0) {
    			for(int i=0; i<left; i++)
    				Writes.swap(array, offs0+i, offs0+i+left, 1, true, false);
    			return;
    		}
    		if(left < right) {
        		Writes.arraycopy(array, offs0+left, swap, offs1, bridge, 1, true, swap!=main);
    			int pta = offs0 + left - 1, ptb = offs0 + right - 1, ptc = offs0 + nmemb - 1;
    			for(int i=left; i>0; i--) {
    				Writes.write(array, ptb--, array[ptc], 1, true, array!=main);
    				Writes.write(array, ptc--, array[pta--], 1, true, array!=main);
    			}
    			Writes.arraycopy(swap, offs1, array, offs0, bridge, 1, true, array!=main);
    		} else {
        		Writes.arraycopy(array, offs0+right, swap, offs1, bridge, 1, true, swap!=main);
    			int pta = offs0, ptb = offs0 + right, ptc = offs0 + left;
    			for(int i=right; i>0; i--) {
    				Writes.write(array, ptb++, array[pta], 1, true, array!=main);
    				Writes.write(array, pta++, array[ptc++], 1, true, array!=main);
    			}
        		Writes.arraycopy(swap, offs1, array, offs0+nmemb-bridge, bridge, 1, true, array!=main);
    		}
    	} else {
			IndexedRotations.cycleReverse(array, offs0, offs0 + left, offs0 + nmemb, 1, true, false);
		}
    }
    
    int monoboundBinary(int[] array, int value, int offset, int top) {
    	int mid, end = offset + top;
    	while(top > 1) {
    		mid = top / 2;
    		if(!cmpB(value, array[end-mid])) {
    			end -= mid;
    		}
    		top -= mid;
    	}
    	if(!cmpB(value, array[end-1]))
    		end--;
    	return end - offset;
    }
    
    public void blitMergeBlock(int[] array, int[] swap, int offs0, int offs1, int swapSize, int block, int right) {
    	if(!cmpB(array[offs0+block-1], array[offs0+block]))
    		return;
    	int left = monoboundBinary(array, array[offs0+block/2], offs0 + block, right);
    	right -= left;
    	block /= 2;
    	if(left != 0) {
    		rotate(array, swap, offs0 + block, offs1, swapSize, block + left, block);
    		if(left <= swapSize) {
    			partialBackwardMerge(array, swap, offs0, offs1, block + left, block);
    		} else if(block <= swapSize) {
    			partialForwardMerge(array, swap, offs0, offs1, block + left, block);
    		} else {
    			blitMergeBlock(array, swap, offs0, offs1, swapSize, block, left);
    		}
    	}
    	if(right != 0) {
    		if(right <= swapSize) {
    			partialBackwardMerge(array, swap, offs0 + block + left, offs1, block + right, block);
    		} else if(block <= swapSize) {
    			partialForwardMerge(array, swap, offs0 + block + left, offs1, block + right, block);
    		} else {
    			blitMergeBlock(array, swap, offs0 + block + left, offs1, swapSize, block, right);
    		}
    	}
    }
    
    public void blitMerge(int[] array, int[] swap, int offs0, int offs1, int swapSize, int nmemb, int block) {
    	int pte = offs0 + nmemb, pta;
    	while(block < nmemb) {
    		pta = offs0;
    		for(; pta + block < pte; pta += 2 * block) {
    			if(pta + 2 * block < pte) {
    				blitMergeBlock(array, swap, pta, offs1, swapSize, block, block);
    				continue;
    			}
    			blitMergeBlock(array, swap, pta, offs1, swapSize, block, pte - pta - block);
    			break;
    		}
    		block *= 2;
    	}
    }
    
    public void quadSort(int[] array, int start, int nmemb) {
    	main = array;
    	if(nmemb < 32) {
    		tailSwap(array, start, nmemb);
    	} else if(quadSwap(array, start, nmemb) == 0) {
    		int[] swap;
    		int swapSize;
    		if(swapAuxiliary == -1) {
	    		swapSize = 32;
	    		while(swapSize * 4 <= nmemb) {
	    			swapSize *= 2;
	    		}
    		} else {
    			swapSize = swapAuxiliary;
    		}
    		swap = Writes.createExternalArray(swapSize); // we can't do fallback, because it errors out when it can't allocate
    		quadMerge(array, swap, start, 0, swapSize, nmemb, 32);
    		blitMerge(array, swap, start, 0, swapSize, nmemb, 2 * swapSize);
    		Writes.deleteExternalArray(swap);
    	}
    }
    
    public void quadSortSwap(int[] array, int[] swap, int startMain, int startSwap, int swapSize, int nmemb) {
    	main = array;
    	if(nmemb < 32) {
    		tailSwap(array, startMain, nmemb);
    	} else if(quadSwap(array, startMain, nmemb) == 0) {
    		int block = quadMerge(array, swap, startMain, startSwap, swapSize, nmemb, 32);
    		blitMerge(array, swap, startMain, startSwap, swapSize, nmemb, block);
    	}
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	quadSort(array, 0, length);
    }
}