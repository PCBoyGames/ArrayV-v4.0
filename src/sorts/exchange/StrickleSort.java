package sorts.exchange;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import main.ArrayVisualizer;
import sorts.insert.SelsertionSort;
import sorts.merge.OptimizedNaturalRotateMergeSort;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class StrickleSort extends MadhouseTools {
    public StrickleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Strickle");
        this.setRunAllSortsName("Strickle Sort");
        this.setRunSortName("Strickle Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(false);
    }

    protected void strangePass(int[] array, int start, int end) {
        int currentLen = end;
        int offset = start + 1;
            double mult = 1.0;
            int bound = 1;
            while (offset <= (start + end) / 2 && offset < currentLen) {
                mult = 1;
                bound = 1;
                while (offset + mult <= currentLen) {
                    if (Reads.compareIndices(array, (int) (offset + mult / 2) - 1, (int) (offset + mult) - 1, 0.05, true) > 0) {
                        Writes.swap(array, (int) (offset + mult / 2) - 1, (int) (offset + mult) - 1, 0.05, true, false);
                        if (mult == 1 / 2) {
                            bound *= 2;
                            mult = bound;
                        } else mult /= 2;
                    } else {
                        bound *= 2;
                        mult = bound;
                    }
                }
                offset++;
            }
    }

    protected void recurHelper(int[] array, int start, int end, int depth) {
        Writes.recordDepth(depth);
        if (end - start <= 1 || isSorted(array, start, end)) return;
        strangePass(array, start, end);
        Writes.recursion();
        recurHelper(array, start, (int) (start + end) / 2, depth + 1);
        Writes.recursion();
        recurHelper(array, (int) (start + end) / 2, end, depth + 1);
        if (Reads.compareIndices(array, ((start + end) / 2) - 1, (start + end) / 2, 0.05, true) > 0) {
            OptimizedNaturalRotateMergeSort optinrm = new OptimizedNaturalRotateMergeSort(arrayVisualizer);
            optinrm.merge(array, start, (start + end) / 2, end, 0);
        }

    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        recurHelper(array, 0, currentLength, 0);

        /*int par = parX(array, 0, currentLength, 0, false);
        if (isSorted(array, 0, currentLength)) par = 0;
        String shuffle = arrayVisualizer.getHeading() + " L: " + arrayVisualizer.getCurrentLength();
        Path path = FileSystems.getDefault().getPath("strickleOut-" + arrayVisualizer.getCurrentLength() + ".txt");
        File output = new File("strickleOut-" + arrayVisualizer.getCurrentLength() + ".txt");
        try {
            if (!output.createNewFile()) {
                //output.delete();
                //output.createNewFile();
            }
            String currentOutput = "";
            List<String> lines;
            lines = Files.readAllLines(path);
            for (String line : lines) currentOutput += line + "\n";
            PrintWriter outputWrite;
            outputWrite = new PrintWriter(output, "UTF-8");
            outputWrite.append(currentOutput + shuffle + ", Par(X): " + par + "\n");
            outputWrite.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }
}