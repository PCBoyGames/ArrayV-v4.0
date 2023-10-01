package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

the unholiest of fusions between two sorts that have absolutely nothing to do with eachother

 */

public class MagicStalinSort extends Sort {
    public MagicStalinSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Magic Stalin");
        this.setRunAllSortsName("Magic Stalin Sort");
        this.setRunSortName("Magic Stalinsort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void stalin(int[] array, int a, int b, boolean aux) {
        for (int i = a; i < b; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.5);
            if (Reads.compareValues(array[i], array[i+1]) > 0) {
                Writes.write(array, i+1, array[i], 0.5, true, aux);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int[] aux = Writes.createExternalArray(currentLength);
        Writes.arraycopy(array, 0, aux, 0, currentLength, 0.1, true, true);
        stalin(aux, 0, currentLength-1, true);
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int j = 0; j < currentLength; j++) {
                if (Reads.compareValues(array[j], aux[j]) > 0) {
                    Delays.sleep(0.05);
                    Writes.write(array, j, array[j]-1, 0.05, true, false);
                    sorted = false;
                } else if (Reads.compareValues(array[j], aux[j]) < 0) {
                    Delays.sleep(0.05);
                    Writes.write(array, j, array[j]+1, 0.05, true, false);
                    sorted = false;
                }
            }
        }
    }
}
