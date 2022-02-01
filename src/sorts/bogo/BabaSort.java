package sorts.bogo;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;
import utils.Statistics;

/*
 *
MIT License

Copyright (c) 2021 Distray

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

class XY { // Basic class for X/Y coordinates
    public int x;
    public int y;
    public XY() {
        x=0;
        y=0;
    }
    public XY(int x, int y) {
        this.x=x;
        this.y=y;
    }
}

class Baba extends XY { // Baba itself
    private XY vector; // Movement vector (gets applied in Baba.move())
    private ArrayList<XY> history; // History of Baba's previous locations in the grid
    public Baba() { // Baba constructor
        super();
        this.history = new ArrayList<>();
        this.vector = new XY(1, 0);
    }
    public Baba(int x, int y) { // Baba constructor with predefined XY coords (never used)
        super(x, y);
        this.history = new ArrayList<>();
        this.vector = new XY(1, 0);
    }
    public void move() { // Move Baba in its current direction
        this.history.add(new XY(this.x, this.y)); // Track Baba's position before moving,
        this.x += this.vector.x; // and move Baba.
        this.y += this.vector.y;
    }

    public void turnLeft() { // Turns Baba left
        int y = this.vector.y;
        this.vector.y = this.vector.x;
        this.vector.x = -y;
    }

    public void turnRight() { // Turns Baba right
        int x = this.vector.x;
        this.vector.x = this.vector.y;
        this.vector.y = -x;
    }

    // Checks if Baba is out of bounds, given the bottom right corner of the grid
    public boolean isOOB(XY corner) {
        return this.x >= corner.x || this.y >= corner.y || this.x < 0 || this.y < 0;
    }

    // Checks if Baba intersects its own path
    // (only for current state)
    public boolean intersectsOwnPath() {
        for(XY el : this.history) { // Iterate through Baba's location history,
            if(this.x == el.x && this.y == el.y) {
                return true; // and break when an equal X/Y coord is found
            }
        }
        return false;
    }

    public int movesTaken() { // Tells how many moves Baba has made
        return this.history.size();
    }

    // Checks if Baba has traversed every position in the grid
    // (given the bottom right corner of it)
    public boolean traversesAllIn(XY corner) {
        boolean[][] grid = new boolean[corner.y][corner.x]; // Initialize the entire grid
        for(XY el : this.history) { // For each of Baba's previous moves,
            grid[el.y][el.x] = true; // flag Baba's position at that point on the grid
        }
        for(int i=0; i<corner.y; i++) { // Go through the grid.
            for(int j=0; j<corner.x; j++) {
                if(!grid[i][j]) // If any part of the grid doesn't have a flag,
                    return false; // break away, and say no.
            }
        }
        return true;
    }

    // Tells Baba's current position if the grid was flattened, given
    // the bottom right corner of the grid
    public int matrixIndex(XY boundCorner) {
        return this.x + (this.y * boundCorner.x);
    }

    // Resets Baba's state
    public void reset() {
        this.x = this.y = 0;
        this.vector = new XY(1, 0);
        history.clear();
    }

    // Tells one of Baba's previous positions if the grid was flattened,
    // given the bottom right corner of the grid
    public int matrixIndex(XY boundCorner, int inHistory) {
        XY state = this.history.get(inHistory);
        return state.x + (state.y * boundCorner.x);
    }
}
final public class BabaSort extends BogoSorting {
    public BabaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Baba");
        this.setRunAllSortsName("Baba Is Sort");
        this.setRunSortName("Babasort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8);
        this.setBogoSort(false);
    }
    private void compSwap(int[] array, int start, int end) {
        if(Reads.compareIndices(array, start, end, 0.5, true) == 1) {
            Writes.swap(array, start, end, 0.5, false, false);
        }
    }
    // Find a pattern that traverses all spots in a grid
    private void traverse(int[] array, int length) {
        int dimX = (int) Math.sqrt(length), dimY = length / dimX; // Took this from Matrixsort

        // Define the bounds of Baba's movement,
        XY cornerDim = new XY(dimX, dimY);

        // and initialize Baba.
        Baba baba = new Baba();

        while(!baba.traversesAllIn(cornerDim)) {
            baba.reset(); // Reset Baba before we continue (redundant during the first iteration)
            for(int i=0; i<length; i++) {
                switch(Math.abs(array[i]) % 3) { // Makes Baba turn according to the array input
                    case 0:
                        baba.turnLeft();
                        break;
                    case 1:
                        break;
                    case 2:
                        baba.turnRight();
                }
                baba.move();
                // If Baba makes an invalid move (OOB or intersecting own path),
                if(baba.intersectsOwnPath() || baba.isOOB(cornerDim)) {
                    // shuffle the array, and try again.
                    this.bogoSwap(array, 0, length, false);
                    break;
                }
                if(baba.movesTaken() > 0) { // We aren't able to do anything if Baba only has a current state

                    // get Baba's previous and current states
                    int babaPrev = baba.matrixIndex(cornerDim, baba.movesTaken() - 1),
                         babaNow = baba.matrixIndex(cornerDim);

                    // Ensure that we can't compswap in the wrong direction
                    if(babaPrev > babaNow) {
                        int temp = babaPrev;
                        babaPrev = babaNow;
                        babaNow = temp;
                    }

                    // Compare the matrix indices we got from Baba
                    this.compSwap(array, babaPrev, babaNow);
                }
            }
            // Increment the stat, and continue
            Statistics.addStat("Baba Traversal");
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        Statistics.putStat("Baba Traversal"); // Initialize Baba Traversal stat
        while(!isArraySorted(array, currentLength)) { // While the array isn't sorted,
            this.traverse(array, currentLength); // make a Baba, and try to make it run through the matrix
        }
    }
}