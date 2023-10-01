package visuals.misc;

import java.awt.Color;
import java.awt.image.BufferedImage;

import main.ArrayVisualizer;
import utils.Highlights;
import utils.Renderer;
import visuals.Visual;

/*
 *
MIT License

Copyright (c) 2020-2021 ArrayV 4.0 Team

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

public class HilbertCurve extends Visual {
	public HilbertCurve(ArrayVisualizer ArrayVisualizer) {
		super(ArrayVisualizer);
	}

	private int cachedLen = 0, sqrt = 0;
	private double scale;
	private int[] indices;

	private Color getGray(int t, int n) {
		int c = (int)(255 * (double)Math.max(0, Math.min(t, n))/n);
		return new Color(c, c, c);
	}

	private int multx2i(int a, int b) {
		return b<64?(a*b)/127:b<128?(a*(b+1))/127:255-(((255-a)*(256-b))/128);
	}
	private Color multx2(Color a, Color b) {
		return new Color(multx2i(a.getRed(), b.getRed()), multx2i(a.getGreen(), b.getGreen()), multx2i(a.getBlue(), b.getBlue()));
	}

	private static boolean mixedMesh = false;

	//recursive hilbert curve generator using turtle graphics
	//preconditions: ptr.length > 1, n is a power of 4
	private void hilbert(int[] ptr, int n, int down, int right) {
		if (n == 1) {
			this.indices[ptr[0]] = ptr[1]++;
			return;
		}
		this.hilbert(ptr, n/4, right, down); ptr[0] += down;
		this.hilbert(ptr, n/4, down, right); ptr[0] += right;
		this.hilbert(ptr, n/4, down, right); ptr[0] -= down;
		this.hilbert(ptr, n/4, -right, -down);
	}

	public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
		if (Renderer.auxActive) return;

		int width = ArrayVisualizer.windowWidth();
		int height = ArrayVisualizer.windowHeight()-50;
		int length = ArrayVisualizer.getCurrentLength();

		if (length != this.cachedLen) {
			this.cachedLen = length;

			//calculate smallest power of 2 >= ceiling(sqrt(length)) or 2^ceil(log2(length)/2)
			int sqrtTmp = 1 << ((32-Integer.numberOfLeadingZeros(length)) / 2);
			int square  = sqrtTmp * sqrtTmp;
			this.scale = (double)length / square;

			if (sqrtTmp != this.sqrt) {
				this.sqrt = sqrtTmp;
				this.indices = new int[square];
				this.hilbert(new int[] {0, 0}, square, this.sqrt, 1);
			}
		}

		Color currColor;
		int imgWidth = Math.min(sqrt, width), imgHeight = Math.min(sqrt, height);
		BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

		double xScale = (double)sqrt/imgWidth;
		double yScale = (double)sqrt/imgHeight;

		for (int y = 0; y < imgHeight; y++) {
			int yi = (int)(y * yScale);

			for (int x = 0; x < imgWidth; x++) {
				int xi  = (int)(x * xScale);
				int idx = (int)(this.indices[yi*sqrt + xi] * scale);

				if (Highlights.fancyFinishActive() && idx < Highlights.getFancyFinishPosition())
					currColor = Color.GREEN;

				else if (ArrayVisualizer.colorEnabled()) {
					if (Highlights.containsPosition(idx))
						currColor = ArrayVisualizer.analysisEnabled() ? Color.LIGHT_GRAY
						                                              : Color.WHITE;
					else
						currColor = mixedMesh ? multx2(getIntColor(array[idx]*sqrt, length), getGray(array[idx]/sqrt+1,(length-1)/sqrt+2))
						                      : getIntColor(array[idx], length);
				}
				else {
					if (Highlights.containsPosition(idx))
						currColor = ArrayVisualizer.analysisEnabled() ? Color.BLUE : Color.RED;

					else
						currColor = getGray(array[idx], length);
				}
				img.setRGB(x, y, currColor.getRGB());
			}
		}
		this.mainRender.drawImage(img, 0, 40, width, height+40, 0, 0, imgWidth, imgHeight, null);
	}
}