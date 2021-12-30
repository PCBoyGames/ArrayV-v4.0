package sorts.esoteric;

import main.ArrayVisualizer;
import utils.Complex;
import sorts.templates.BogoSorting;

public final class MandelbrotSort extends BogoSorting {
  public MandelbrotSort(ArrayVisualizer arrayVisualizer) {
    super(arrayVisualizer);
    this.setSortListName("Mandelbrot");
    this.setRunAllSortsName("Mandelbrot Sort");
    this.setRunSortName("Mandelbrot Sort");
    this.setCategory("Esoteric Sorts");
    this.setComparisonBased(true);
    this.setBucketSort(false);
    this.setRadixSort(false);
    this.setUnreasonablySlow(true);
    this.setUnreasonableLimit(1024);
    this.setBogoSort(false);
  }
  private static double rXstart, rXend, rYstart, rYend;
  
  private int mandelbrotIters(int startX, int run, int max) {
	  double rXrange = (rXend - rXstart),
			 rYrange = (rYend - rYstart),
			 cX = ((startX / (double)max) * rXrange) + rXstart,
			 cY = ((run / (double)max) * rYrange) + rYstart;
	  Complex c = new Complex(cX, cY),
			  z = new Complex(0, 0);
	  int iters = 0;
	  do {
		  z = z.pow(2).add(c);
		  Highlights.markArray(1, (int) (z.abs() * ((max / rXrange) - 1)));
		  Highlights.markArray(2, iters);
		  Delays.sleep(0.1);
		  Reads.addComparison(); // technically a comparison
		  iters++;
	  } while(iters < max && z.abs() < 4);
	  return iters-1;
  }
  public void mandelSort(int[] array, int length) {
	  while(!isArraySorted(array, length)) {
		  int unstableParts = 0, realUns = 0;
		  for(int j=0; j<length; j++) {
			  for(int i=0; i<length; i++) {
				  int mandelK = this.mandelbrotIters(i, j, length);
				  if(mandelK == length-1) { // fallback for unstable segments
					  if(realUns >= length) {
						  int rand1 = randInt(0, length),
							  rand2 = randInt(rand1, length);
						  if(Reads.compareValues(array[rand1], array[rand2]) == 1) {
							  Writes.swap(array, rand1, rand2, 1, true, false);
						  }
					  } else {
						  if(Reads.compareValues(array[unstableParts], array[unstableParts+1]) == 1) {
							  Writes.swap(array, unstableParts, unstableParts+1, 1, true, false);
						  }
						  unstableParts+=2;
						  unstableParts %= length-1;
					  }
					  realUns++;
				  } else {
					  if(Reads.compareValues(array[mandelK], array[mandelK + 1]) == 1) {
						  Writes.swap(array, mandelK, mandelK + 1, 1, true, false);
						  realUns /= 2;
					  }
					  unstableParts = 0;
				  }
			  }
		  }
	  }
  }
  @Override
  public void runSort(int[] array, int length, int bucketCount) {
	  rXstart = -2.1;
	  rXend = 0.6;
	  rYstart = -1.2;
	  rYend = 1.2;
	  this.mandelSort(array, length);
  }
}