package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

final public class TheMarkovCouncilBurnsThemselvesOnSomeSoupSort extends BogoSorting {
    public TheMarkovCouncilBurnsThemselvesOnSomeSoupSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("The Markov Council Burns Themselves On Some Soup");
        this.setRunAllSortsName("The Markov Council Burns Themselves On Some Soup Sort");
        this.setRunSortName("The Markov Council Burns Themselves On Some Soupsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while(!isArraySorted(array, length)) {
            for(int i=1; i<length; i++) {
                int markov = i;
                while(
                     ((markov > 0 && Reads.compareValues(array[markov-1], array[markov]) >= 0) ||
                      (markov < i && Reads.compareValues(array[markov], array[markov+1]) < 0)) &&
                      randInt(0, 10000) != 0) {
                    int d = randInt(-1, 2);
                    if(markov+d >= 0 && markov+d <= i) {
                        Writes.swap(array, markov+d, markov, 1, true, false);
                        markov += d;
                    } else if(markov+d < 0) {
                        markov++;
                    } else if(markov+d > i)
                        markov--;
                }
            }
        }
    }
}
