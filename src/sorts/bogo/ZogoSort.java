package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*
 ,_*_*_*_*_*_*_*_*_*_*_*_*_.
 * ~~~~~~ Zogo Sort ~~~~~~ *
 |       Part of the       |
 *  "Dissort Can You Make" *
 |         series          |
 '*'*'*'*'*'*'*'*'*'*'*'*'*'
 */

public class ZogoSort extends BogoSorting {
    public ZogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Zogo");
        this.setRunAllSortsName("Zogo Sort");
        this.setRunSortName("Zogosort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(true);
    }

    private void semiShuffle3(int[] array, int length) {
    	int rand = randInt(0, length),
    		head = randBoolean() ? 0 : length - 1,
    		tail = head == 0 ? length - 1 : 0;

    	Writes.swap(array, rand, head, 0.05, true, false);
    	Writes.swap(array, rand, tail, 0.05, true, false);
    	if(randBoolean())
    		Writes.swap(array, rand, head, 0.05, true, false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while(!this.isArraySorted(array, length))
        	this.semiShuffle3(array, length);
    }
}
