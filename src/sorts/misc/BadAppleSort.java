package sorts.misc;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import java.util.Scanner;
import java.io.File;

final public class BadAppleSort extends Sort {
    public BadAppleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bad Apple");
        this.setRunAllSortsName("Bad Apple Sort");
        this.setRunSortName("Bad Apple Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        File folder = null;

        try {
            folder = new File("C:/Users/patri/Documents/ba_frames/numbers");
        }
        catch(Exception e) {
            System.out.println("File not found");
            return;
        }

        Scanner scanner = null;

        for (final File file : folder.listFiles()) {
            try {
                scanner = new Scanner(file);
            }
            catch (Exception e) {
                System.out.println("File not found");
                return;
            }
            scanner.useDelimiter(" ");

            int current = 0;
            while (scanner.hasNext())
                Writes.write(array, current++, Integer.parseInt(scanner.next()), 1, true, false);

            scanner.close();
        }
        for (int i = 0; i < currentLength; i++)
            Writes.write(array, i, 0, 1, true, false);
    }
}
