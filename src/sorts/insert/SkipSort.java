package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

import java.util.Random;

/*
 *
MIT License

Copyright (c) 2023 aphitorite

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

public final class SkipSort extends Sort {
	public SkipSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Skip");
		this.setRunAllSortsName("Skip Sort");
		this.setRunSortName("Skipsort");
		this.setCategory("Insertion Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	private Random rng;

	private class Node {
		private int idx;
		private Node lower, next;

		public Node(int idx) {
			this.idx = idx;
			Writes.changeAllocAmount(1);

			this.lower = null;
			this.next  = null;
		}

		public int getIdx() {
			return this.idx;
		}
		public Node getNext() {
			return this.next;
		}
		public Node getLower() {
			return this.lower;
		}

		private void visualize(int a, int b) {
			Highlights.markArray(1, a);
			Highlights.markArray(2, b);
			Writes.changeAuxWrites(1);
			Delays.sleep(0.25);
		}
		public void setNext(Node next) {
			this.visualize(this.getIdx(), next == null ? this.getIdx() : next.getIdx());

			Writes.startLap();
			this.next = next;
			Writes.stopLap();
		}
		public void setLower(Node lower) {
			this.visualize(this.getIdx(), lower == null ? this.getIdx() : lower.getIdx());

			Writes.startLap();
			this.lower = lower;
			Writes.stopLap();
		}
	}

	private Node insertNode(int[] array, Node curr, int idx) {
		while(curr.getNext() != null && Reads.compareIndices(array, idx, curr.getNext().getIdx(), 0.25, true) > 0)
			curr = curr.getNext();

		if(curr.getLower() == null) {
			Node newNode = new Node(idx);
			newNode.setNext(curr.getNext());
			curr.setNext(newNode);

			return newNode;
		}

		Node newLower = this.insertNode(array, curr.getLower(), idx);

		if(newLower != null && this.rng.nextBoolean()) {
			Node newNode = new Node(idx);
			newNode.setNext(curr.getNext());
			curr.setNext(newNode);
			newNode.setLower(newLower);

			return newNode;
		}
		return null;
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		this.rng = new Random();

		Node head   = new Node(0);
		Node bottom = head;

		int iter = 30-Integer.numberOfLeadingZeros(length); // log2(n) - 1

		while(iter-- > 0) {
			Node newHead = new Node(0);
			newHead.setLower(head);
			head = newHead;
		}

		for(int i = 0; i < length; i++)
			this.insertNode(array, head, i);

		int[] tmp = Writes.createExternalArray(length);

		for(int i = 0; i < length; i++) {
			bottom = bottom.getNext();
			int idx = bottom.getIdx();

			Highlights.markArray(2, idx);
			Writes.write(tmp, i, array[idx], 1, true, true);
		}
		Highlights.clearMark(2);
		Writes.arraycopy(tmp, 0, array, 0, length, 1, true, false);
		Writes.deleteExternalArray(tmp);
	}
}
