package sorts.esoteric;

import java.util.ArrayList;
import java.util.Random;
import java.lang.reflect.Field;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class PathUwUgenSort extends Sort {
    public PathUwUgenSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("PathUwUgen");
        this.setRunAllSortsName("PathUwUgen Sort");
        this.setRunSortName("PathUwUgen Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(6);
        this.setBogoSort(false);
        this.setPathogenic(true);
        this.setPathogenName("STG-25 \"Joker\"");
    }
    public static PathUwUgenSort self = null;
    private int compSorts;
    private Sort[] sorts = null;
    private int[][] idxTable = null;
    private static final Random r = new Random();

    public static class Shuffled extends Sort {
    	private int idxName;
    	private int idxCat;
    	private int idxSort;
    	public Shuffled(ArrayVisualizer arrayVisualizer) {
    		super(arrayVisualizer);
    	}
    	private void setSortIndices(int[] vals) {
    		idxName = vals[0];
    		idxCat = vals[1];
    		idxSort = vals[2];
    	}
    	public Shuffled(ArrayVisualizer arrayVisualizer, int index, boolean compare) {
    		super(arrayVisualizer);
    		int[] v = PathUwUgenSort.self.idxTable[index + (compare ? 0 : PathUwUgenSort.self.compSorts)];
    		setSortIndices(v);
    		instantiate();
    	}
    	private void instantiate() {
    		Sort wrapped = PathUwUgenSort.self.sorts[idxSort];
			Object v;
    		for (Field i : wrapped.getClass().getDeclaredFields()) {
				try {
					v = i.get(wrapped);
	    			i.set(this, v);
				} catch (IllegalArgumentException | IllegalAccessException e) {
				}
    		}
    	}
    	public String getSortListName() {
    		return PathUwUgenSort.self.sorts[idxName].getSortListName();
    	}
    	public String getRunAllSortsName() {
    		return PathUwUgenSort.self.sorts[idxName].getRunAllSortsName();
    	}
    	public String getRunSortName() {
    		return PathUwUgenSort.self.sorts[idxName].getRunSortName();
    	}
    	public String getCategory() {
    		return PathUwUgenSort.self.sorts[idxCat].getCategory();
    	}
        public boolean isComparisonBased() {
            return PathUwUgenSort.self.sorts[idxSort].isComparisonBased();
        }
        public boolean usesBuckets() {
            return PathUwUgenSort.self.sorts[idxSort].usesBuckets();
        }
        public boolean isRadixSort() {
            return PathUwUgenSort.self.sorts[idxSort].isRadixSort();
        }
    	public String getQuestion() {
    		return PathUwUgenSort.self.sorts[idxSort].getQuestion();
    	}
    	public int getDefaultAnswer() {
    		return PathUwUgenSort.self.sorts[idxSort].getDefaultAnswer();
    	}
    	public int validateAnswer(int v) {
    		return PathUwUgenSort.self.sorts[idxSort].validateAnswer(v);
    	}
    	public boolean isPathogenic() {
    		return PathUwUgenSort.self.sorts[idxSort].isPathogenic();
    	}
    	public String getPathogenName() {
    		return PathUwUgenSort.self.sorts[idxSort].getPathogenName();
    	}
    	@Override
    	public void runSort(int[] array, int length, int buckets) throws Exception {
			System.err.println("Joke's on you, it's actually " + PathUwUgenSort.self.sorts[idxSort].getRunSortName() + " from the " + PathUwUgenSort.self.sorts[idxSort].getCategory() + " category!");
    		PathUwUgenSort.self.sorts[idxSort].runSort(array, length, buckets);
    	}
    }

    private int[] newShuffledArr(int size) {
    	int[] a = new int[size];
    	for (int i = 1; i < size; i++) {
    		a[i] = i;
    		int rv = r.nextInt(i+1);
    		Writes.swap(a, rv, i, 0, false, false);
    	}
    	return a;
    }

    private void infect() {
    	ArrayList<Sort> sorts0 = arrayVisualizer.getSortAnalyzer().comparisonSorts,
    					sorts1 = arrayVisualizer.getSortAnalyzer().distributionSorts;
    	ArrayList<Sort> sortsCoalesced = new ArrayList<>();
    	sortsCoalesced.addAll(sorts0);
    	sortsCoalesced.addAll(sorts1);

    	sorts = sortsCoalesced.toArray(new Sort[0]);
    	compSorts = sorts0.size();

    	int[] idxsName = newShuffledArr(sortsCoalesced.size());
    	int[] idxsCat = newShuffledArr(sortsCoalesced.size());
    	int[] idxsSort = newShuffledArr(sortsCoalesced.size());
    	idxTable = new int[sortsCoalesced.size()][];

    	int j = 0;

    	for (int i = 0; i < sorts0.size(); i++, j++) {
    		Shuffled v = new Shuffled(arrayVisualizer);
    		v.setSortIndices(new int[] {idxsName[j], idxsCat[j], idxsSort[j]});
    		sorts0.set(i, v);
    	}
    	for (int i = 0; i < sorts1.size(); i++, j++) {
    		Shuffled v = new Shuffled(arrayVisualizer);
    		v.setSortIndices(new int[] {idxsName[j], idxsCat[j], idxsSort[j]});
    		sorts1.set(i, v);
    	}

    	// sort (optional)
    	// arrayVisualizer.getSortAnalyzer().sortSorts();

    	// then set the index table to what you'd expect
    	for (int i = j = 0; i < sorts0.size(); i++, j++) {
    		Shuffled sf = (Shuffled)(sorts0.get(i));
    		idxTable[j] = new int[] {sf.idxName, sf.idxCat, sf.idxSort};
    	}
    	for (int i = 0; i < sorts1.size(); i++, j++) {
    		Shuffled sf = (Shuffled)(sorts1.get(i));
    		idxTable[j] = new int[] {sf.idxName, sf.idxCat, sf.idxSort};
    	}

        arrayVisualizer.refreshSorts();
    }

	private void horror(int[] A, int a, int b, int a1, int b1, int v, int d) {
		if (a > b1 || b > b1 || a < a1 || b < a1)
			return;

		if (a != b && Reads.compareValues(A[a], A[b]) == -v) {
			Writes.swap(A, a, b, 0.025, true, false);
		}
		Delays.sleep(0.005D);
		Writes.recordDepth(d++);
		Highlights.markArray(1, a);
		Highlights.markArray(2, b);
		Writes.recursion(2);
		horror(A, a + 1, b, a1, b1, v, d);
		horror(A, a, b - 1, a1, b1, v, d);
	}

	private void the_horror(int[] A, int a, int b, int a1, int b1, int v, int d) {
		if (a > b1 || b > b1 || a < a1 || b < a1)
			return;

		if (a != b && Reads.compareValues(A[a], A[b]) == -1) {
			Writes.swap(A, a, b, 0.025, true, false);
		}
		Delays.sleep(0.005D);
		Writes.recordDepth(d++);
		Highlights.markArray(1, a);
		Highlights.markArray(2, b);
		for (int i = 0; i < b1 - a1; i++) {
			for (int j = 0; j < b1 - a1; j++) {
				if (i+j>0) {
					Writes.recursion(2);
					the_horror(A, b - v * i, a + v * j, a1, b1, -v, d);
					the_horror(A, a + v * j, b - v * i, a1, b1, v, d);
				}
			}
		}
		if (d<2)horror(A, a, b, a1, b1, v, d);
	}

	@Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
		if (self == null) {
	    	self = this;
	    	infect();
		}
		the_horror(array, 0, currentLength-1, 0, currentLength-1, 1, 0);
    }
}