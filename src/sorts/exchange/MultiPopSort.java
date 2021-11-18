package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class MultiPopSort extends BogoSorting {
    public MultiPopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Multilingual Pop");
        this.setRunAllSortsName("Multilingual Pop Sort");
        this.setRunSortName("Multilingual Pop Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    protected void bubble(int[] array, int start, int end, int dir) {
        int i = start;
        int j = start;
        int lastswap = end - 1;
        while (lastswap >= start + 1) {
            i = start;
            j = start;
            while (i <= lastswap) {
                if (Reads.compareIndices(array, i - 1, i, 0.1, true) == dir) {
                    Writes.swap(array, i - 1, i, 0.1, true, false);
                    j = i;
                }
                i++;
            }
            lastswap = j;
        }
    }
	
	protected void cocktail(int[] array, int start, int end, int dir) {
		for(int s = start, e = end; s < e;) {
            int consecSorted = 1;
            for(int i = s; i < e; i++) {
                if (Reads.compareIndices(array, i - 1, i, 0.1, true) == dir) {
                    Writes.swap(array, i - 1, i, 0.1, true, false);
                    consecSorted = 1;
                } else consecSorted++;
            }
            e -= consecSorted;
            consecSorted = 1;
            for(int i = e; i > s; i--) {
                if (Reads.compareIndices(array, i - 2, i - 1, 0.1, true) == dir) {
                    Writes.swap(array, i - 2, i - 1, 0.1, true, false);
                    consecSorted = 1;
                } else consecSorted++;
            }
            s += consecSorted;
        }
	}
	
	protected void oddeven(int[] array, int start, int end, int dir) {
		boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = start + 1; i < end; i += 2) {
                if (Reads.compareIndices(array, i - 1, i, 0.1, true) == dir) {
                    Writes.swap(array, i - 1, i, 0.1, true, false);
                    sorted = false;
                }
            }
            for (int i = start; i < end; i += 2) {
                if (Reads.compareIndices(array, i - 1, i, 0.1, true) == dir) {
                    Writes.swap(array, i - 1, i, 0.1, true, false);
                    sorted = false;
                }
            }
        }
	}
	
	protected void gnome(int[] array, int start, int end, int dir) {
		int pos = start;
		for(int i = start; i <= end; i++) {
            pos = i;
        	while(pos > start && Reads.compareIndices(array, pos - 2, pos - 1, 0.1, true) == dir) {
            	Writes.swap(array, pos - 2, pos - 1, 0.1, true, false);
            	pos--;
        	}
		}
	}
    
    protected void clamber(int[] array, int start, int end, int dir) {
		int left = start;
        int right = start + 1;
        while (right <= end) {
            left = start;
            while (left < right) {
                if (Reads.compareIndices(array, left - 1, right - 1, 0.1, true) == dir) {
                    while (left < right) {
                        Writes.swap(array, left - 1, right - 1, 0.1, true, false);
                        left++;
                    }
                }
                left++;
            }
            right++;
        }
	}
    
    protected void dandelion(int[] array, int start, int end, int dir) {
		for (int b = start - 1; b < end - 1; ) {
            Highlights.markArray(1, b);
            int pointer = b;
            boolean anyswap = false;
            while (pointer < end - 1 && Reads.compareIndices(array, pointer + 1, pointer, 0.1, true) == dir * -1) {
                Writes.swap(array, pointer, pointer + 1, 0.1, true, false);
                anyswap = true;
                pointer++;
            } 
            if (anyswap) {
                if (b > start - 1) {
                    b--;
                    continue;
                }
            }
            b++;
        }
	}
    
    protected void insert(int[] array, int start, int end, int dir) {
        int pos;
        int current;
        for(int i = start - 1; i < end; i++) {
            current = array[i];
            pos = i - 1;
            while(pos >= start - 1 && Reads.compareValues(array[pos], current) == dir) {
                Writes.write(array, pos + 1, array[pos], 0.1, true, false);
                pos--;
            }
            Writes.write(array, pos + 1, current, 0.1, true, false);
        }
    }
    
    protected void ignorant(int[] array, int start, int end, int dir) {
        int left = start;
        int first = start;
        int right = start + 1;
        int pull = start;
        boolean anyswaps = false;
        while (left != end) {
            right = left + 1;
            anyswaps = false;
            while (right <= end) {
                if (Reads.compareIndices(array, left - 1, right - 1, 0, false) == dir) {
                    if (!anyswaps && left != start) first = left;
                    pull = right - 1;
                    while (pull >= left) {
                        Writes.swap(array, pull - 1, pull, 0.1, true, false);
                        pull--;
                    }
                    left++;
                    anyswaps = true;
                }
                right++;
            }
            if (anyswaps) left = first;
            else left++;
        }
    }
	
	protected void choose(int[] array, int start, int end, int dir) {
		int choice = randInt(1, 9);
		if (choice == 1) bubble(array, start, end, dir);
		if (choice == 2) cocktail(array, start, end, dir);
		if (choice == 3) oddeven(array, start, end, dir);
		if (choice == 4) gnome(array, start, end, dir);
        if (choice == 5) clamber(array, start, end, dir);
        if (choice == 6) dandelion(array, start, end, dir);
        if (choice == 7) insert(array, start, end, dir);
        if (choice == 8) ignorant(array, start, end, dir);
        Highlights.clearAllMarks();
	}
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        choose(array, 1, (int) Math.floor((currentLength + 1) / 4), -1);
        choose(array, (int) Math.floor((currentLength + 1) / 4) + 1, (int) Math.floor((currentLength + 1) / 2), 1);
        choose(array, (int) Math.floor((currentLength + 1) / 2) + 1, (int) Math.floor (((currentLength + 1) * 3) / 4), -1);
        choose(array, (int) Math.floor(((currentLength + 1) * 3) / 4) + 1, currentLength, 1);
        choose(array, 1, (int) Math.floor((currentLength + 1) / 2), -1);
        choose(array, (int) Math.floor((currentLength + 1) / 2) + 1, currentLength, 1);
        choose(array, 1, currentLength, 1);
    }
}