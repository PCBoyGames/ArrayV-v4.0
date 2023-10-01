package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.insert.*;
import sorts.templates.Sort;
import utils.Shuffles;

/*
 *
MIT License

Copyright (c) 2019 w0rthy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

public class RunInsertionSorts extends MultipleSortThread {
    private Sort InsertionSort;
    private Sort DoubleInsertionSort;
    private Sort BinaryInsertionSort;
    private Sort TriSearchInsertionSort;
    private Sort UnstableInsertionSort;
    private Sort ShellSort;
    private Sort RecursiveShellSort;
    private Sort RendezvousSort;
    private Sort SimplifiedLibrarySort;
    private Sort PatienceSort;
    private Sort ClassicTreeSort;
    private Sort AATreeSort;
    private Sort AVLTreeSort;
    private Sort FibonacciInsertionSort;
    private Sort SplaySort;
    private Sort RoomSort;
    private Sort LibrarySort;
    private Sort CocktailShellSort;
    private Sort ShellSortParallel;
    private Sort ShuffledTreeSort;
    private Sort TreeSort;
    private Sort AdaptiveBinaryInsertionSort;
    private Sort GambitInsertionSort;
    private Sort ReverseInsertionSort;

    public RunInsertionSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 24;
        this.categoryCount = this.sortCount;

        InsertionSort               = new               InsertionSort(this.arrayVisualizer);
        DoubleInsertionSort         = new         DoubleInsertionSort(this.arrayVisualizer);
        BinaryInsertionSort         = new         BinaryInsertionSort(this.arrayVisualizer);
        TriSearchInsertionSort      = new      TriSearchInsertionSort(this.arrayVisualizer);
        FibonacciInsertionSort      = new      FibonacciInsertionSort(this.arrayVisualizer);
        UnstableInsertionSort       = new       UnstableInsertionSort(this.arrayVisualizer);
        ShellSort                   = new                   ShellSort(this.arrayVisualizer);
        RecursiveShellSort          = new          RecursiveShellSort(this.arrayVisualizer);
        RendezvousSort              = new              RendezvousSort(this.arrayVisualizer);
        RoomSort                    = new                    RoomSort(this.arrayVisualizer);
        SimplifiedLibrarySort       = new                 SimplifiedLibrarySort(this.arrayVisualizer);
        PatienceSort                = new                PatienceSort(this.arrayVisualizer);
        ClassicTreeSort             = new             ClassicTreeSort(this.arrayVisualizer);
        AATreeSort                  = new                  AATreeSort(this.arrayVisualizer);
        AVLTreeSort                 = new                 AVLTreeSort(this.arrayVisualizer);
        SplaySort                   = new                   SplaySort(this.arrayVisualizer);
        LibrarySort                 = new          LibrarySort(this.arrayVisualizer);
        CocktailShellSort           = new           CocktailShellSort(this.arrayVisualizer);
        ShellSortParallel           = new           ShellSortParallel(this.arrayVisualizer);
        ShuffledTreeSort            = new            ShuffledTreeSort(this.arrayVisualizer);
        TreeSort                    = new                    TreeSort(this.arrayVisualizer);
        AdaptiveBinaryInsertionSort = new AdaptiveBinaryInsertionSort(this.arrayVisualizer);
        GambitInsertionSort         = new         GambitInsertionSort(this.arrayVisualizer);
        ReverseInsertionSort        = new        ReverseInsertionSort(this.arrayVisualizer);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunInsertionSorts.this.runIndividualSort(InsertionSort,               0, array,  128,  0.005, false);
        RunInsertionSorts.this.runIndividualSort(ReverseInsertionSort,        0, array,  128,  0.005, false);
        RunInsertionSorts.this.runIndividualSort(DoubleInsertionSort,         0, array,  128,  0.002, false);
        RunInsertionSorts.this.runIndividualSort(BinaryInsertionSort,         0, array,  128,  0.025, false);
        RunInsertionSorts.this.runIndividualSort(AdaptiveBinaryInsertionSort, 0, array,  128,  0.025, false);
        RunInsertionSorts.this.runIndividualSort(TriSearchInsertionSort,      0, array,  128,  1,     false);
        RunInsertionSorts.this.runIndividualSort(FibonacciInsertionSort,      0, array,  128,  0.025, false);
        RunInsertionSorts.this.runIndividualSort(GambitInsertionSort,         0, array,  128,  0.025, false);
        RunInsertionSorts.this.runIndividualSort(UnstableInsertionSort,       0, array,  128,  0.2,   false);
        RunInsertionSorts.this.runIndividualSort(ShellSort,                   0, array,  256,  0.1,   false);
        RunInsertionSorts.this.runIndividualSort(CocktailShellSort,           0, array,  256,  0.1,   false);
        RunInsertionSorts.this.runIndividualSort(RecursiveShellSort,          0, array,  256,  0.1,   false);
        RunInsertionSorts.this.runIndividualSort(ShellSortParallel,           0, array,  256,  0.1,   false);
        RunInsertionSorts.this.runIndividualSort(RendezvousSort,              0, array,  256,  0.1,   false);
        RunInsertionSorts.this.runIndividualSort(RoomSort,                    0, array,  512,  0.05,  false);
        RunInsertionSorts.this.runIndividualSort(SimplifiedLibrarySort,       0, array,  2048, 1,     false);
        RunInsertionSorts.this.runIndividualSort(LibrarySort,                 0, array,  2048, 1,     false);
        RunInsertionSorts.this.runIndividualSort(PatienceSort,                0, array,  2048, 1,     false);
        RunInsertionSorts.this.runIndividualSort(ClassicTreeSort,             0, array,  2048, arrayManager.containsShuffle(Shuffles.RANDOM) ? 1 : 5, false);
        RunInsertionSorts.this.runIndividualSort(TreeSort,                    0, array,  2048, arrayManager.containsShuffle(Shuffles.RANDOM) ? 1 : 5, false);
        RunInsertionSorts.this.runIndividualSort(ShuffledTreeSort,            0, array,  2048, 1,     false);
        RunInsertionSorts.this.runIndividualSort(AATreeSort,                  0, array,  2048, 1,     false);
        RunInsertionSorts.this.runIndividualSort(AVLTreeSort,                 0, array,  2048, 1,     false);
        RunInsertionSorts.this.runIndividualSort(SplaySort,                   0, array,  2048, 1,     false);
    }

    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if (arrayVisualizer.isActive())
            return;

        Sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread("InsertionSorts") {
            @Override
            public void run() {
                try{
                    if (runAllActive) {
                        RunInsertionSorts.this.sortNumber = current;
                        RunInsertionSorts.this.sortCount = total;
                    }
                    else {
                        RunInsertionSorts.this.sortNumber = 1;
                    }

                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("Insertion Sorts");

                    RunInsertionSorts.this.executeSortList(array);

                    if (!runAllActive) {
                        arrayVisualizer.setCategory("Run Insertion Sorts");
                        arrayVisualizer.setHeading("Done");
                    }

                    arrayManager.toggleMutableLength(true);
                }
                catch (Exception e) {
                    JErrorPane.invokeErrorMessage(e);
                }
                Sounds.toggleSound(false);
                arrayVisualizer.setSortingThread(null);
            }
        });
        arrayVisualizer.runSortingThread();
    }
}