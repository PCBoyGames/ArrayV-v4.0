package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

final public class DrunkenSailorSort extends BogoSorting {
    public DrunkenSailorSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Drunken Sailor");
        this.setRunAllSortsName("Drunken Sailor Sort");
        this.setRunSortName("Drunken Sailor Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setQuestion("Set the inversion luck for this sort:", 80);
        this.setUnreasonableLimit(256);
        this.setBogoSort(false);
    }

    @Override
    public int validateAnswer(int answer) {
        if(answer < 0)
            return 0;
        if(answer > 99)
            return 100;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int luck) {
        boolean invert = false;
        final int slow = 128;
        while(!isArraySorted(array, currentLength)) {
            for(int i=0, m=0, max=0; m<slow*currentLength && i<currentLength-1; m++) {
                if(invert ^ (Reads.compareValues(array[i], array[i+1]) == 1)) {
                    Writes.swap(array, i, i+1, 1, true, false);
                } else if(randInt(1, 101) > luck) {
                    invert = randBoolean();
                    break;
                }
                i += randBoolean() ? 1 : -1;
                if(i < 0)
                    i = max;
                if(i > max)
                    max = i;
            }
        }
    }
}