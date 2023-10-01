package sorts.tests;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class TestingMusic extends Sort {

    int[] valkeep;
    int maxnotes = 1;
    int len;

    public TestingMusic(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Testing Music");
        this.setRunAllSortsName("Testing Music");
        this.setRunSortName("Testing Music");
        this.setCategory("Tests");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void note(int[] array, int pos, int pitch, int sleep) {
        Highlights.markArray(pos, pos - 1);
        Writes.write(array, pos - 1, len * (pitch - 25) / 80, sleep, false, false);
        if (sleep > 0) Highlights.clearAllMarks();
        //if (sleep > 0) try {Thread.sleep(sleep);} catch (InterruptedException e) {e.printStackTrace();}
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        valkeep = new int[maxnotes];
        for (int i = 0; i < maxnotes; i++) valkeep[i] = array[i];
        len = currentLength;
        Delays.setSleepRatio(1);

        // Set up tempo information. tempo is a BPM value, and b represents the ms value for one beat.
        int tempo = 120;
        int b = 60000 / tempo;

        // Duplicate first note for syncing.


        note(array, 1, 59, b);
        note(array, 1, 61, b);
        note(array, 1, 64, b);
        note(array, 1, 66, b);
        note(array, 1, 71, b*2);
        note(array, 1, 68, b);
        note(array, 1, 66, b/2);
        note(array, 1, 64, b/2);
        note(array, 1, 63, b);
        note(array, 1, 61, b);
        note(array, 1, 64, b);
        note(array, 1, 59, b);
        note(array, 1, 58, b*4);
        note(array, 1, 61, b);
        note(array, 1, 59, b);
        note(array, 1, 63, b);
        note(array, 1, 58, b);
        note(array, 1, 57, b*4);
        note(array, 1, 63, b);
        note(array, 1, 61, b);
        note(array, 1, 64, b);
        note(array, 1, 59, b);
        note(array, 1, 61, b*4);
        note(array, 1, 59, b);
        note(array, 1, 61, b);
        note(array, 1, 64, b);
        note(array, 1, 66, b);
        note(array, 1, 71, b*2);
        note(array, 1, 68, b);
        note(array, 1, 66, b/2);
        note(array, 1, 64, b/2);
        note(array, 1, 63, b);
        note(array, 1, 61, b);
        note(array, 1, 64, b);
        note(array, 1, 59, b);
        note(array, 1, 58, b*4);
        note(array, 1, 64, b);
        note(array, 1, 63, b);
        note(array, 1, 66, b);
        note(array, 1, 61, b);
        note(array, 1, 59, b*4);
        note(array, 1, 66, b*4);
        note(array, 1, 68, b*4);

        // Duplicate last note for syncing.
        //note(array, 1, 68, 0);

        for (int i = 0; i < maxnotes; i++) Writes.write(array, i, valkeep[i], 1, true, false);
    }
}