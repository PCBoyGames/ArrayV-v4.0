package visuals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import main.ArrayVisualizer;
import utils.Highlights;
import utils.Renderer;

public abstract class Visual {
    protected Graphics2D mainRender;
    protected Graphics2D extraRender;

    public Visual(ArrayVisualizer ArrayVisualizer) {
        this.updateRender(ArrayVisualizer);
    }

    public void updateRender(ArrayVisualizer ArrayVisualizer) {
        this.mainRender = ArrayVisualizer.getMainRender();
        this.extraRender = ArrayVisualizer.getExtraRender();
    }

    public static Color getIntColor(int i, int length) {
        return Color.getHSBColor(((float) i / length), 0.8F, 0.8F);
    }

    public static Color getIntColor(int i, int length, int alpha) {
        int a = Color.HSBtoRGB(((float) i / length), 0.8f, 0.8f);
        Color b = Color.decode(String.valueOf(a));
        return new Color(b.getRed(), b.getGreen(), b.getBlue(), alpha);
    }

    public static Color getIntColor(int i, int length, float S, float B) {
        return Color.getHSBColor(((float) i / length), S, B);
    }

    public static Color getIntColor(int i, int length, float S, float B, int alpha) {
        int a = Color.HSBtoRGB(((float) i / length), S, B);
        Color b = Color.decode(String.valueOf(a));
        return new Color(b.getRed(), b.getGreen(), b.getBlue(), alpha);
    }

    public static void markBar(Graphics2D bar, boolean color, boolean rainbow, boolean analysis) {
        if (color || rainbow) {
            if (analysis) bar.setColor(Color.LIGHT_GRAY);
            else         bar.setColor(Color.WHITE);
        }
        else if (analysis)    bar.setColor(Color.BLUE);
        else                 bar.setColor(Color.RED);
    }

    public static void lineMark(Graphics2D line, double width, boolean color, boolean analysis) {
        line.setStroke(new BasicStroke((float) (9f * (width / 1280f))));
        if (color) line.setColor(Color.BLACK);
        else if (analysis) line.setColor(Color.BLUE);
        else line.setColor(Color.RED);
    }

    public static void setRectColor(Graphics2D rect, boolean color, boolean analysis) {
        if (color) rect.setColor(Color.WHITE);
        else if (analysis) rect.setColor(Color.BLUE);
        else rect.setColor(Color.RED);
    }

    public abstract void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights);
}