package visuals.misc;

import java.awt.Color;

import main.ArrayVisualizer;
import utils.Highlights;
import utils.Renderer;
import visuals.Visual;

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

public class TriangleMesh extends Visual {
    public TriangleMesh(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }

    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
        int triperrow = (int) Math.floor(Math.sqrt(ArrayVisualizer.getCurrentLength()));
        double trih = 2 * (ArrayVisualizer.windowHeight() - 45) / (Math.ceil(Math.sqrt(ArrayVisualizer.getCurrentLength())) + 1);
        double triw = ArrayVisualizer.windowWidth() / Math.floor(Math.sqrt(ArrayVisualizer.getCurrentLength()));
        double curx = 0;
        double cury = 30;
        int[] triptsx = new int[3];
        int[] triptsy = new int[3];
        for (int i = 0; i < ArrayVisualizer.getCurrentLength(); i++) {
            if (Highlights.containsPosition(i) && ArrayVisualizer.getCurrentLength() != 2) {
                if (ArrayVisualizer.analysisEnabled()) this.mainRender.setColor(Color.LIGHT_GRAY);
                else this.mainRender.setColor(getIntColor(array[i], ArrayVisualizer.getCurrentLength(), 0.25f, 1));
            }
            else if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition()) this.mainRender.setColor(getIntColor(array[i], ArrayVisualizer.getCurrentLength(), 0.25f, 1));
            else this.mainRender.setColor(getIntColor(array[i], ArrayVisualizer.getCurrentLength()));
            boolean direction = false;
            if ((i / triperrow) % 2 == 0) direction = true;
            if (!direction) {
                triptsx[0] = (int) curx;
                triptsx[1] = (int) curx;
                curx += triw;
                triptsx[2] = (int) curx;
                triptsy[0] = (int) cury;
                triptsy[2] = (int) (cury + (trih / 2));
                triptsy[1] = (int) (cury +  trih);
            } else {
                triptsx[2] = (int) curx;
                curx += triw;
                triptsx[0] = (int) curx;
                triptsx[1] = (int) curx;
                triptsy[0] = (int) cury;
                triptsy[2] = (int) (cury + (trih / 2));
                triptsy[1] = (int) (cury +  trih);
            }
            this.mainRender.fillPolygon(triptsx, triptsy, 3);
            if ((i + 1) % triperrow == 0) {
                curx = 0;
                cury += trih / 2;
            }
        }
    }
}