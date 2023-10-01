package sorts.esoteric;

import java.util.Random;

import java.awt.*;
import javax.swing.JFrame;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class BadUserSort extends BogoSorting {
    public BadUserSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bad User");
        this.setRunAllSortsName("Bad User Sort");
        this.setRunSortName("Bad User Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(6);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) throws InterruptedException {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        Random rng = new Random();
        JFrame window = ArrayVisualizer.getInstance().getMainWindow();
        Point temp = window.getLocation();
        while (!isArraySorted(array, currentLength)) {
            window.setLocation(Math.max(rng.nextInt((int) size.getWidth() - window.getWidth()), 1), Math.max(rng.nextInt((int) size.getHeight() - window.getHeight()), 1));
            bogoSwap(array, 0, currentLength, false);
        }
        for (int i = 0; i < 100; i++) {
            window.setLocation(temp);
            Thread.sleep(1);
        }
    }
}