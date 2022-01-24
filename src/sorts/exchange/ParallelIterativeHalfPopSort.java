package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.ParallelSort;

final public class ParallelIterativeHalfPopSort extends ParallelSort {
    public ParallelIterativeHalfPopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Parallel Iterative Half-Pop");
        this.setRunAllSortsName("Parallel Iterative Half-Pop Sort");
        this.setRunSortName("Parallel Iterative Half-Popsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    // Optibubbles down as close to half of the numbers as possible, then returns the middle
    
    public int bubbleHalf(int[] array, int start, int end, int d) {
    	int lastDone = end, mid = start+(end-start)/2;
    	while(lastDone > mid) {
    		int swap = start;
    		for(int i=start; i<lastDone-1; i++) {
    			if(Reads.compareIndices(array, i, i+1, 0.1, true) == d) {
    				Writes.swap(array, i, i+1, 0.1, true, false);
    				swap=i+1;
    			}
    		}
    		lastDone=swap;
    	}
    	return lastDone;
    }
    
    @SuppressWarnings("unchecked")
	protected <T, U> U cast(T v, Class<? super U> c) {
    	return (U) v;
    }
    
    protected Integer bubbleThread(Object... data) {
    	return (Integer) run("bubbleHalf", data);
    }
    
    public void insert(int[] array, int start, int end, double sleep, boolean aux, int d) {
    	for(int i=start+1; i<end; i++) {
    		int t=array[i], j=i-1;
    		while(j >= start) {
    			int comp = Reads.compareValues(array[j], t);
    			if(comp == -d || comp == 0)
    				break;
    			Writes.write(array, j+1, array[j], sleep, true, aux);
    			j--;
    		}
			Writes.write(array, j+1, t, sleep, true, aux);
    	}
    }
    
    protected Void insertThread(Object... data) {
    	assert data.length==6;
    	run("insert", data);
    	return null;
    }
    
    public void pop(int[] array, int start, int end) {
    	int d = -1, g = 2;
		Func[] pool = new Func[end-start];
    	while(g < 2*(end-start)) {
    		int t = d;
    		for(int j=0, i=start; i<end; j++, i+=g) {
    			pool[j] = new Func(array, i, Math.min(i+g, end), d)
    					  .setConsumer(this::bubbleThread);
    			pool[j].start();
    			d *= -1;
    		}
    		for(int i=0; i<(end-start-1)/g+1; i++) {
    			try {
    				pool[i].join();
    			} catch(InterruptedException e) {
    				Thread.currentThread().interrupt();
    			}
    		}
    		for(int j=0, i=start; i<end; j++, i+=g) {
    			int k = cast(pool[j].returnVal, int.class);
    			pool[j] = new Func(array, i, k, 0.5, false, t)
    					  .setConsumer(this::insertThread);
    			pool[j].start();
    			t *= -1;
    		}
    		for(int i=0; i<(end-start-1)/g+1; i++) {
    			try {
    				pool[i].join();
    			} catch(InterruptedException e) {
    				Thread.currentThread().interrupt();
    			}
    		}
    		g *= 2;
    	}
    	if(d == 1) {
    		Writes.reversal(array, start, end-1, 1, true, false);
    	}
    }
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	pop(array, 0, currentLength);
    }
}