package visuals.dots;

import java.awt.Color;
import java.awt.image.BufferedImage;

import main.ArrayVisualizer;
import utils.Highlights;
import utils.Renderer;
import visuals.Visual;

/*
 *
MIT License

Copyright (c) 2024 aphitorite

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

final public class DataTrace extends Visual {
	public DataTrace(ArrayVisualizer ArrayVisualizer) {
		super(ArrayVisualizer);
	}

	private final int X_OFFSET = 20;
	private final int Y_OFFSET = 40;

	private int cachedLen = 0, cachedHeight = 0, cachedWidth, offset;
	private double idxScale, widthScale;
	private long cachedWrites = 0;
	private boolean change = false;

	private BufferedImage trace;

	private Color getGray(int t, int n) { //can be added to Visual.java
		int c = (int)(255 * (double)Math.max(0, Math.min(t, n))/n);
		return new Color(c, c, c);
	}

	@Override
	public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
		if (Renderer.auxActive) return;

		int w = ArrayVisualizer.windowWidth()  - 20;
		int h = ArrayVisualizer.windowHeight() - 50;
		int n = ArrayVisualizer.getCurrentLength();

		int imgW = Math.min(w, n);

		if (n != this.cachedLen || h != this.cachedHeight || w != this.cachedWidth) {
			this.cachedLen    = n;
			this.cachedHeight = h;
			this.cachedWidth  = w;
			imgW = Math.min(w, n);

			this.idxScale   = (double)imgW/n;
			this.widthScale = (double)w/n;
			this.offset = 0;

			this.trace = new BufferedImage(imgW, h, BufferedImage.TYPE_INT_RGB);
		}
		this.mainRender.drawImage(this.trace,
		                          0 + X_OFFSET, Y_OFFSET,
		                          w + X_OFFSET, h - this.offset - 1 + Y_OFFSET,
		                          0, this.offset + 1,
		                          imgW, h,
		                          null);

		this.mainRender.drawImage(this.trace,
		                          0 + X_OFFSET, h - this.offset - 1 + Y_OFFSET,
		                          w + X_OFFSET, h + Y_OFFSET,
		                          0, 0,
		                          imgW, this.offset + 1,
		                          null);

		if (this.change) this.offset = (this.offset+1) % h;

		long c = ArrayVisualizer.getWrites().writes;
		this.change = this.cachedWrites != (this.cachedWrites = c);

		int rectW = (int)Math.max(4, Math.ceil(this.widthScale));

		for (int i = 0; i < n; i++) {
			int idx = (int)(i * this.widthScale);
			int currColor = ArrayVisualizer.colorEnabled() ? getIntColor(array[i], n).getRGB()
			                                               : this.getGray(array[i], n).getRGB();

			this.trace.setRGB((int)(i * this.idxScale), this.offset, currColor);

			if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition()) {
				this.mainRender.setColor(Color.WHITE);
				this.mainRender.fillRect(idx + X_OFFSET, h-6 + Y_OFFSET, rectW, 6);
			}
			if (Highlights.containsPosition(i)) {
				this.mainRender.setColor(ArrayVisualizer.colorEnabled() ? (ArrayVisualizer.analysisEnabled() ? Color.LIGHT_GRAY : getIntColor(array[idx], n, 0.25f, 1))
				                                                        : (ArrayVisualizer.analysisEnabled() ? Color.BLUE       : Color.RED  ));
				this.mainRender.fillRect(idx + X_OFFSET, h-6 + Y_OFFSET, rectW, 6);
			}
		}
	}
}