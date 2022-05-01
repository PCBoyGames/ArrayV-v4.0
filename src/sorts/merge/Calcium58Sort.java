package sorts.merge;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;
public final class Calcium58Sort extends Sort {
	public Calcium58Sort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Calcium-58");
		this.setRunAllSortsName("Calcium-58 Sort");
		this.setRunSortName("Calcium-58sort");
		this.setCategory("Impractical Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

    private void t(int[] array, int a, int b, int len) {
        for (int i = 0; i < len; i++) {
            Writes.swap(array, a + i, b + i, 1, true, false);
        }
    }
    private int b(int[] array, int l, int r, int k) {
    	while(l<r) {
    		int m=(l&r)+((l^r)>>1);
    		if(Reads.compareValues(array[m], k) < 0) {
    			r=m;
    		} else {
    			l=m+1;
    		}
    	}
    	return l;
    }
	private void milk(int[] array, int s, int m, int e) {
		if((e-s <= 16 || s == m || m == e) && s < e) {
			InsertionSort i = new InsertionSort(arrayVisualizer);
			i.customInsertSort(array, s, e, 0.5, false);
			return;
		}
		int rz = e-m, r = m, z = s+(m-s)-rz, l = z;
		while(rz > 0) {
			if(Reads.compareValues(array[l], array[r]) == 1) {
				t(array,l,r,rz);
			} else {
				l++;
				rz--;
			}
		}
		this.Milk(array, m, e);
		if(z>s) {
			int c=b(array,z,e,array[z-1]);
			for(int i=z; i<c; i+=z-s) {
				this.milk(array, i-(z-s), i, Math.min(i+z-s, c));
			}
		}
	}
	private void Milk(int[] array, int s, int e) {
		int m = (e-s+1)/3;
		if(m<1)
			return;
		this.Milk(array,s+m,e);
		this.Milk(array,s,e-m);
		this.milk(array, s, e-m, e);
	}
	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		Milk(array, 0, length);
	}
}