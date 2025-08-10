package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*
,_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_.
* ~~~~~~~~~ Plain Drunk Sort. ~~~~~~~~~ *
|              Part of the              |
*        "Dissort Can You Make"         *
|                series                 |
'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'
*/

public class PlainDrunkSort extends BogoSorting {
    public PlainDrunkSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Plain Drunk");
        this.setRunAllSortsName("Drunk Sort");
        this.setRunSortName("Plain Drunk Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setQuestion("Set the inversion luck for this sort:", 100);
        this.setUnreasonableLimit(256);
        this.setBogoSort(false);
    }

    @Override
    public int validateAnswer(int answer) {
    	if (answer < 0)
    		return 0;
    	if (answer > 99)
    		return 100;
    	return answer;
    }

    protected boolean isRangeReversed(int[] array, int start, int end, boolean mark, boolean markLast) {
        for (int i = start; i < end - 1; ++i) {
            if (Reads.compareIndices(array, i, i + 1, 0, mark) < 0) {
                if (markLast) Highlights.markArray(3, i + 1);
                return false;
            }
        }
        return true;
    }

    private boolean drunkPass(int[] array, int a, int b, int luck, boolean invert) {
    	if (a >= b - 1) return !invert;

    	for (int i = a + 1; i <= b; i++) {
    		for (int j = 0; invert?!isRangeReversed(array, a, i, true, false):!isRangeSorted(array, a, i); ) {
    			if (Reads.compareIndices(array, a + j, a + (j + 1) % (i - a), 0.5, true) > 0 ^ invert) {
    				invert ^= (randInt(0, 101) > luck);
    				Writes.swap(array, a + j, a + (j + 1) % (i - a), 0.5, true, false);
    			}
    			j = (j + (randBoolean() ? i - a - 1 : 1)) % (i - a);
    		}
    	}
    	return invert ^ drunkPass(array, a, randInt(a + 1, b), luck, invert) ^ drunkPass(array, randInt(a + 1, b), b, luck, invert);
    }

    @Override
    public void runSort(int[] array, int currentLength, int luck) {
    	boolean invert = false;
        do {
        	invert ^= drunkPass(array, 0, currentLength, luck, invert);
        } while(!isArraySorted(array, currentLength));
    }
}