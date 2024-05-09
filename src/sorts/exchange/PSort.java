package sorts.exchange;

import main.ArrayVisualizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

import sorts.templates.BogoSorting;

class SwapMap {
	public static utils.Writes Writes;
	private ArrayList<Integer> swaps;
	private static final Random r = new Random();
	public SwapMap() {
		swaps = new ArrayList<>();
	}
	public SwapMap(Integer... swapMap) {
		this();
		Arrays.stream(swapMap).forEachOrdered(swaps::add);
	}
	public void push(int swapA, int swapB) {
		swaps.add(swapA);
		swaps.add(swapB);
	}
	public void popRandom() {
		int nxt = (r.nextInt(swaps.size())|1)-1;
		swaps.remove(nxt);
		swaps.remove(nxt);
	}
	public int[] apply(int[] array, int len) {
		int[] z = Writes.createExternalArray(len);
		Writes.arraycopy(array, 0, z, 0, len, 0, false, true);
		for (int i=0; i<swaps.size(); i+=2) {
			int a = swaps.get(i),
				b = swaps.get(i+1);
			Writes.swap(z, a, b, 1, true, false);
		}
		Writes.deleteExternalArray(z);
		return z;
	}
	public void applyIP(int[] array) {
		for (int i=0; i<swaps.size(); i+=2) {
			int a = swaps.get(i),
				b = swaps.get(i+1);
			Writes.swap(array, a, b, 1, true, false);
		}
	}
}
final public class PSort extends BogoSorting {
    public PSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("P-Sort");
        this.setRunAllSortsName("P Sort");
        this.setRunSortName("P-Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8);
        this.setBogoSort(false);
    }
    private long fact(int k) {
    	long z = 1;
    	for (int i=2; i<=k; i++) {
    		z*=i;
    	}
    	return z;
    }
    Stack<SwapMap> permutations;
    private void generateAllSortedPermutations(int[] array, int length) {
    	long f = fact(length);
    	Random r = new Random();
    	for (long k=0; k<f; k++) { // o h g o d
    		SwapMap map = new SwapMap();
    		for (int j=0; j<length; j++) {
	    		map.push(r.nextInt(length), r.nextInt(length));
	    	}
    		if (isArraySorted(map.apply(array, length), length)) {
    			permutations.push(map);
    		}
    	}
    }
    private void applyAll(int[] array) {
    	while (!permutations.empty()) {
    		SwapMap last = permutations.pop();
    		last.applyIP(array);
    	}
    }

    @Override
    public void runSort(int[] array, int currentLen, int bucketCount) {
    	Highlights.clearAllMarks();
    	SwapMap.Writes = Writes;
    	this.permutations = new Stack<>();
    	do {
    		this.generateAllSortedPermutations(array, currentLen);
    		this.applyAll(array);
    	} while (!isArraySorted(array, currentLen));
    }
}