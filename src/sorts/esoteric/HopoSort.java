package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

Worse than WorstWorstsort!

 */

public class HopoSort extends BogoSorting {

    int[] aux;

    public HopoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Hopo");
        this.setRunAllSortsName("Hopo Sort");
        this.setRunSortName("Hoposort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1);
        this.setBogoSort(true);
    }

    public boolean bogoIsSorted(int[] array, int a, int b) {
        int r;
        while (true) {
            boolean loop = false;
            r = a != b ? randInt(a, b) : a;
            switch (Reads.compareIndices(array, r, r+1, 0.5, true)) {
                case 1: Writes.write(aux, r, 1, 0.5, true, true); break;
                default: Writes.write(aux, r, -1, 0.5, true, true); break;
            }
            for (int i = 0; i < b; i++) {
                switch (Reads.compareIndexValue(aux, i, 0, 0.1, true)) {
                    case -1: break;
                    case 0: loop = true; break;
                    default: return false;
                }
            }
            if (loop) continue;
            return true;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        aux = Writes.createExternalArray(currentLength-1);
        while (!bogoIsSorted(array, 0, currentLength-1));
    }
}
