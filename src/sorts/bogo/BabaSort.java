package sorts.bogo;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

class XY {
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

class Baba extends XY {
	private XY vector;
	private ArrayList<XY> history;
	public Baba() {
		super();
		this.history = new ArrayList<>();
		this.vector = new XY(1, 0);
	}
	public Baba(int x, int y) {
		super(x, y);
		this.history = new ArrayList<>();
		this.vector = new XY(1, 0);
	}
	public void move() {
		this.history.add(new XY(this.x, this.y));
		this.x += this.vector.x;
		this.y += this.vector.y;
	}
	
	public void turnLeft() {
		int y = this.vector.y;
		this.vector.y = this.vector.x;
		this.vector.x = -y;
	}
	
	public void turnRight() {
		int x = this.vector.x;
		this.vector.x = this.vector.y;
		this.vector.y = -x;
	}
	
	public boolean isOOB(XY corner) {
		return this.x >= corner.x || this.y >= corner.y || this.x < 0 || this.y < 0;
	}
	
	// only for current state
	public boolean intersectsOwnPath() {
		for(XY el : this.history) {
			if(this.x == el.x && this.y == el.y) {
				return true;
			}
		}
		return false;
	}
	
	public int movesTaken() {
		return this.history.size();
	}

	public boolean traversesAllIn(XY corner) {
		boolean[][] k = new boolean[corner.y][corner.x];
		for(XY el : this.history) {
			k[el.y][el.x] = true;
		}
		for(int i=0; i<corner.y; i++) {
			for(int j=0; j<corner.x; j++) {
				if(!k[i][j])
					return false;
			}
		}
		return true;
	}
	
	// currentstate index
	public int matrixIndex(XY boundCorner) {
		return this.x + (this.y * boundCorner.x);
	}
	
	public void reset() {
		this.x = this.y = 0;
		this.vector = new XY(1, 0);
		history.clear();
	}
	
	// currentstate index
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
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }
    private void comp(int[] array, int start, int end) {
    	if(Reads.compareIndices(array, start, end, 0.5, true) == 1) {
    		Writes.swap(array, start, end, 0.5, false, false);
    	}
    }
    // Find a pattern that traverses all spots in a grid
    private void traverse(int[] array, int length) {
    	int dimX = (int) Math.sqrt(length), dimY = length / dimX;
    	XY cornerDim = new XY(dimX, dimY);
    	Baba baba = new Baba();
    	while(!baba.traversesAllIn(cornerDim)) {
    		baba.reset();
    		for(int i=0; i<length; i++) {
    			switch(array[i] % 3) {
    				case 0:
    					baba.turnLeft();
    					break;
    				case 1:
    					break;
    				case 2:
    					baba.turnRight();
    			}
				baba.move();
				if(baba.intersectsOwnPath() || baba.isOOB(cornerDim)) {
					this.bogoSwap(array, 0, length, false);
					break;
				}
				if(baba.movesTaken() > 0) {
					int i1 = baba.matrixIndex(cornerDim, baba.movesTaken() - 1),
					   inw = baba.matrixIndex(cornerDim);
					if(i1 > inw) {
						int t = inw;
						inw = i1;
						i1 = t;
					}
					this.comp(array, i1, inw);
				}
    		}
    		// patch in CustomStatistics to make use of this
        	// Statistics.addStat("Baba Traversal");
    	}
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	// Statistics.putStat("Baba Traversal");
    	while(!isArraySorted(array, currentLength)) {
    		this.traverse(array, currentLength);
    	}
    }
}