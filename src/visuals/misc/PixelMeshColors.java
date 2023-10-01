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

public class PixelMeshColors extends Visual {
	public PixelMeshColors(ArrayVisualizer ArrayVisualizer) {
		super(ArrayVisualizer);
	}

	private Color getGray(int t, int n) {
		int c = (int)(255 * (double)Math.max(0, Math.min(t, n))/n);
		return new Color(c, c, c);
	}

	public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
		if (Renderer.auxActive) return;

		int width = ArrayVisualizer.windowWidth();
		int height = ArrayVisualizer.windowHeight()-50;
		int length = ArrayVisualizer.getCurrentLength();

		int sqrt = (int)Math.ceil(Math.sqrt(length));
		int square = sqrt*sqrt;
		double scale = (double)length / square;

		Color currColor;
		int imgWidth = Math.min(sqrt, width), imgHeight = Math.min(sqrt, height);
		BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

		double xScale = (double)sqrt/imgWidth;
		double yScale = (double)sqrt/imgHeight;

		for (int y = 0; y < imgHeight; y++) {
			int yi = (int)(y * yScale);

			for (int x = 0; x < imgWidth; x++) {
				int xi  = (int)(x * xScale);
				int idx = (int)((yi*sqrt + xi) * scale);

				if (Highlights.fancyFinishActive() && idx < Highlights.getFancyFinishPosition())
					currColor = Color.GREEN;

				else if (ArrayVisualizer.colorEnabled()) {
					if (Highlights.containsPosition(idx)) {
						if (ArrayVisualizer.analysisEnabled()) currColor = Color.LIGHT_GRAY;
						else								  currColor = Color.WHITE;
					}
					else currColor = getIntColor(array[idx], length);
				}
				else {
					if (Highlights.containsPosition(idx)) {
						if (ArrayVisualizer.analysisEnabled()) currColor = Color.BLUE;
						else								  currColor = Color.RED;
					}
					else currColor = getGray(array[idx], length);
				}

				img.setRGB(x, y, currColor.getRGB());
			}
		}
		this.mainRender.drawImage(img, 0, 40, width, height+40, 0, 0, imgWidth, imgHeight, null);
	}
}