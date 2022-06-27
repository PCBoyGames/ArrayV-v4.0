package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * 
MIT License

Copyright (c) 2022 aphitorite

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

final public class GAVLTreeSort extends Sort {
	public GAVLTreeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
		this.setSortListName("GAVL Tree");
		this.setRunAllSortsName("Gapped AVL Balanced Tree Sort");
		this.setRunSortName("Gapped AVL Balanced Tree sort");
		this.setCategory("Insertion Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}
	
	private int log2(int n) {
		return 31-Integer.numberOfLeadingZeros(n);
	}
	
	//@return - tree buffer space needed to contain k elements
	private int treeBufferSpace(int k) {
		int log = this.log2(k);
		return (1 << log) * (log + 2) - 1;
	}
	
	private class GAVLTree {
		private final int[] array, tree, gSize;
		private final int o;
		
		private int g, size, kSize;
		
		//implementation of GAVL requires extra buffer space separate from the tree's buffer space for simplicity
		//its possible to perform all operations within the same buffer space as the tree
		
		//O(log^2 b) insert time and O(log b) search time
		//requires O(b log b) buffer space 
		
		public GAVLTree(int[] array, int[] tree, int[] gSize, int o) {
			this.array = array;
			
			this.o  = o;  //output position
			
			this.g     = 2;            //gap size
			this.size  = 0;            //count of how many elements in gaps
			this.kSize = (1<<(g-1))-1; //how many gapped keys (always in the form 2^k-1)
			
			this.tree  = tree;
			this.gSize = gSize;
			
			this.treeWrite(this.keyPos(0), o); //swap first element to key position
		}
		
		//for visualization
		private void treeWrite(int idx, int valIdx) {
			Highlights.markArray(1, valIdx);
			Writes.write(tree, idx, array[valIdx], 0.25, false, true);
		}
		
		private void insertTo(int a, int b) {
			int temp = tree[a];
			while(a > b) {
				Highlights.markArray(1, a/(this.g+1));
				Writes.write(tree, a, tree[--a], 0.25, false, true);
			}
			Highlights.markArray(1, a/(this.g+1));
			Writes.write(tree, a, temp, 0.25, false, true);
		}
		
		private int gapPos(int idx) {
			return idx*(this.g+1);
		}
		private int keyPos(int idx) {
			return this.gapPos(idx+1)-1;
		}
		
		private void exchangeOut(int a, int b) { 
			int po = this.o;
			
			for(int i = a;; i++) {
				int s = this.gSize[i];
				int j = this.gapPos(i);
				
				while(s-- > 0) Writes.write(array, po++, tree[j++], 0.25, true, false);
				
				if(i == b-1) return;
				
				Writes.write(array, po++, tree[this.keyPos(i)], 0.25, true, false);
			}
		}
		private void exchangeIn(int a, int b) { 
			int po = this.o;
			
			for(int i = a;; i++) {
				int s = this.gSize[i];
				int j = this.gapPos(i);
				
				while(s-- > 0) this.treeWrite(j++, po++);
				
				if(i == b-1) return;
				
				this.treeWrite(this.keyPos(i), po++);
			}
		}
		
		private void increaseSize() { //precondition: this.size == this.kSize+1
			this.exchangeOut(0, this.kSize+1);
			
			for(int i = 0; i < this.kSize+1; i++)
				Writes.write(gSize, i, 0, 0, false, true);
			
			this.g++;
			this.kSize += this.size;
			this.size  = 0;
			
			this.exchangeIn(0, this.kSize+1);
		}
		
		private int keySearch(int val) { //right binary search
			int a = 0, b = this.kSize;
			
			while(a < b) {
				int m = a+(b-a)/2;
				Highlights.markArray(2, m);
				Delays.sleep(0.25);
				
				if(Reads.compareValues(val, tree[this.keyPos(m)]) < 0) 
					b = m;
				else     
					a = m+1;
			}
			Highlights.clearMark(2);
			return a; //index of keys[] / gSize
		}
		private int rightBinSearch(int a, int b, int val) {
			while(a < b) {
				int m = a+(b-a)/2;
				Highlights.markArray(2, m/(this.g+1));
				Delays.sleep(0.25);
				
				if(Reads.compareValues(val, tree[m]) < 0) 
					b = m;
				else     
					a = m+1;
			}
			Highlights.clearMark(2);
			return a;
		}
		private int[] gapSearch(int idx, int val) { //returns: position index in gap, tail index of gap before insertion
			int gPos  = this.gapPos(idx);
			int gTail = gPos+this.gSize[idx];
			
			return new int[] {this.rightBinSearch(gPos, gTail, val), gTail};
		}
		
		//rebalancing
		
		private int minGap(int a, int b) {
			int min = this.gSize[a];
			
			for(int i = a+1; i < b; i++) {
				int minI = this.gSize[i];
				if(minI < min) min = minI;
			}
			return min;
		}
		private void incrRange(int a, int b) {
			while(a < b) Writes.write(gSize, a, gSize[a++]+1, 0, false, true);
		}
		private void decrRange(int a, int b) {
			while(a < b) Writes.write(gSize, a, gSize[a++]-1, 0, false, true);
		}
		private void rebalance(int idx) {
			for(int lvl = 0, curPos = idx; lvl < this.g-2; lvl++) {
				int s = 1 << lvl;
				int sibPos = curPos ^ s;
				int minPos = Math.min(curPos, sibPos);
				
				int curMin = this.minGap(curPos, curPos+s);
				int sibMin = this.minGap(sibPos, sibPos+s);
				
				if(curMin > sibMin) {
					if(curMin-sibMin == 1) return;
					
					this.exchangeOut(minPos, minPos+s*2);
					this.incrRange(sibPos, sibPos+s);
					this.decrRange(curPos, curPos+s);
					this.exchangeIn(minPos, minPos+s*2);
				}
				else if(curMin < sibMin) {
					if(sibMin-curMin == 1) return;
					
					this.exchangeOut(minPos, minPos+s*2);
					this.incrRange(curPos, curPos+s);
					this.decrRange(sibPos, sibPos+s);
					this.exchangeIn(minPos, minPos+s*2);
				}
				curPos = minPos;
			}
		}
		
		public void insert(int idx) {
			if(this.size == this.kSize+1) this.increaseSize();
			
			int val = array[idx];
			
			int loc  = this.keySearch(val);
			int[] t  = this.gapSearch(loc, val);
			int gPos = t[0], gTail = t[1];
			
			this.treeWrite(gTail, idx);
			this.insertTo(gTail, gPos);
			Writes.write(gSize, loc, gSize[loc]+1, 0, false, true);
			this.size++;
			
			this.rebalance(loc); //rebalance after insertion
		}
		
		public void free() {
			this.exchangeOut(0, this.kSize+1);
		}
	}
	
	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int[] gSize = new int[1 << this.log2(length)];
		Writes.changeAllocAmount(gSize.length);
		int[] tree  = Writes.createExternalArray(this.treeBufferSpace(length));
		
		GAVLTree gavl = new GAVLTree(array, tree, gSize, 0);
		for(int i = 1; i < length; i++) {
			Highlights.markArray(3, i);
			gavl.insert(i);
		}
		gavl.free();
		Writes.changeAllocAmount(-gSize.length);
		Writes.deleteExternalArray(tree);
	}
}
