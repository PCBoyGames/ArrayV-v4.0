package sorts.exchange;

import java.util.LinkedList;
import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class CaliforniumSortStable extends Sort {   
	public CaliforniumSortStable(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
		this.setSortListName("Californium (Stable Reversals)");
		this.setRunAllSortsName("Californium Sort (Stable Reversals)");
		this.setRunSortName("Californiumsort (Stable Reversals)");
		this.setCategory("Exchange Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(true);
		this.setUnreasonableLimit(512);
		this.setBogoSort(false);
	}
	private void bubbleReversal(int[] array, int start, int length, double sleep, boolean mark, boolean auxwrite) {
        if (length - start <= 0) return;
        else if (length - start == 1) {
            if (Reads.compareIndices(array, start, length, sleep, mark) != 0) Writes.swap(array, start, length, sleep, mark, auxwrite);
        } else {
			Writes.reversals++;
            for (int i = length; i > start; i--) for (int j = start; j < i; j++) if (Reads.compareIndices(array, j, j + 1, sleep, mark) != 0) Writes.swap(array, j, j + 1, sleep, mark, auxwrite);
        }
    }
	private int sqrtrnd(int v) {
		int p=0;
		while(++p*p<v);
		return p;
	}
	private void californium(int[] array, int a, int b, int depth) {
		Writes.recordDepth(depth);
		if(b - a == 2) {
			if(Reads.compareIndices(array, a, a+1, 1, true) > 0)
				Writes.swap(array, a, a+1, 1, true, false);
		}
		if(a >= b - 2)
			return;
		LinkedList<Integer> recurse = new LinkedList<>(), // stack to recursively Californium on
		                    maxstack = new LinkedList<>(); // list of sqrt(n) maximum items (dupes not included)
		int i = b, // boundary
		    j, k, m, me = 0, b0 = b; // temp
		boolean shd = false;
		bndchk:
		while(i > a + 1) {
			Writes.changeAllocAmount(-maxstack.size()+1);
			recurse.addFirst(i = b0);
			maxstack.clear();
			for(k = sqrtrnd(i - a); k-- > 0;) {
				shd = !maxstack.isEmpty() && Reads.compareIndexValue(array, a, maxstack.getFirst(), 1, true) >= 0;
				me = 0;
				for(j = (m = a) + 1; j < i; j++) {
					int p = Reads.compareIndices(array, m, j, 0.01, true);
					if(p == 0) me++;
					if(p < 0 || shd)
						if(maxstack.isEmpty() || Reads.compareIndexValue(array, j, maxstack.getFirst(), 1, true) < 0) {
							m = j;
							shd = false;
							me = 0;
						}
				}
				if(shd || me >= i - a - 1)
					break bndchk;
				maxstack.addFirst(array[m]);
				Writes.changeAllocAmount(1);
				k -= me;
			}
			cnt:
			for(j = b0 = a; j < i; j++) {
				for(int v : maxstack) {
					if(Reads.compareIndexValue(array, j, v, 0.01, true) == 0) {
						bubbleReversal(array, b0, j - 1, 0.1, true, false);
						continue cnt;
					}
				}
				bubbleReversal(array, b0, j - 1, 0.1, true, false);
				bubbleReversal(array, a, b0 - 1, 0.1, true, false);
				Writes.multiSwap(array, j, b0++, 0.1, true, false);
			}
		}
		if(me == b - a - 1)
			return;
		j = a;
		for(k = 0; k < recurse.size(); k++) {
			Writes.recursion();
			californium(array, j, j = recurse.get(k), depth + 1);
		}
		Writes.changeAllocAmount(-recurse.size());
	}
	
	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) {
		californium(array, 0, sortLength, 0);
	}
}