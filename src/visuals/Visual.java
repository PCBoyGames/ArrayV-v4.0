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

    private static int[][] cmapMagma = {{43,17,94},{45,16,96},{47,16,98},{48,16,101},{48,16,101},{50,16,103},{52,16,104},{53,15,106},{55,15,108},{55,15,108},{57,15,110},{59,15,111},{60,15,113},{62,15,114},{64,15,115},{64,15,115},{66,15,116},{67,15,117},{69,15,118},{71,15,119},{71,15,119},{72,16,120},{74,16,121},{75,16,121},{77,17,122},{79,17,123},{79,17,123},{80,18,123},{82,18,124},{83,19,124},{85,19,125},{85,19,125},{87,20,125},{88,21,126},{90,21,126},{91,22,126},{91,22,126},{93,23,126},{94,23,127},{96,24,127},{97,24,127},{99,25,127},{99,25,127},{101,26,128},{102,26,128},{104,27,128},{105,28,128},{105,28,128},{107,28,128},{108,29,128},{110,30,129},{111,30,129},{111,30,129},{113,31,129},{115,31,129},{116,32,129},{118,33,129},{119,33,129},{119,33,129},{121,34,129},{122,34,129},{124,35,129},{126,36,129},{126,36,129},{127,36,129},{129,37,129},{130,37,129},{132,38,129},{133,38,129},{133,38,129},{135,39,129},{137,40,129},{138,40,129},{140,41,128},{140,41,128},{141,41,128},{143,42,128},{145,42,128},{146,43,128},{146,43,128},{148,43,128},{149,44,128},{151,44,127},{153,45,127},{154,45,127},{154,45,127},{156,46,127},{158,46,126},{159,47,126},{161,47,126},{161,47,126},{163,48,126},{164,48,125},{166,49,125},{167,49,125},{167,49,125},{169,50,124},{171,51,124},{172,51,123},{174,52,123},{176,52,123},{176,52,123},{177,53,122},{179,53,122},{181,54,121},{182,54,121},{182,54,121},{184,55,120},{185,55,120},{187,56,119},{189,57,119},{190,57,118},{190,57,118},{192,58,117},{194,58,117},{195,59,116},{197,60,116},{197,60,116},{198,60,115},{200,61,114},{202,62,114},{203,62,113},{203,62,113},{205,63,112},{206,64,112},{208,65,111},{209,66,110},{211,66,109},{211,66,109},{212,67,109},{214,68,108},{215,69,107},{217,70,106},{217,70,106},{218,71,105},{220,72,105},{221,73,104},{222,74,103},{222,74,103},{224,75,102},{225,76,102},{226,77,101},{228,78,100},{229,80,99},{229,80,99},{230,81,98},{231,82,98},{232,84,97},{234,85,96},{234,85,96},{235,86,96},{236,88,95},{237,89,95},{238,91,94},{238,93,93},{238,93,93},{239,94,93},{240,96,93},{241,97,92},{242,99,92},{242,99,92},{243,101,92},{243,103,91},{244,104,91},{245,106,91},{245,106,91},{245,108,91},{246,110,91},{246,112,91},{247,113,91},{247,115,92},{247,115,92},{248,117,92},{248,119,92},{249,121,92},{249,123,93},{249,123,93},{249,125,93},{250,127,94},{250,128,94},{250,130,95},{250,130,95},{251,132,96},{251,134,96},{251,136,97},{251,138,98},{252,140,99},{252,140,99},{252,142,99},{252,144,100},{252,146,101},{252,147,102},{252,147,102},{253,149,103},{253,151,104},{253,153,105},{253,155,106},{253,157,107},{253,157,107},{253,159,108},{253,161,110},{253,162,111},{253,164,112},{253,164,112},{254,166,113},{254,168,115},{254,170,116},{254,172,117},{254,172,117},{254,174,118},{254,175,120},{254,177,121},{254,179,123},{254,181,124},{254,181,124},{254,183,125},{254,185,127},{254,187,128},{254,188,130},{254,188,130},{254,190,131},{254,192,133},{254,194,134},{254,196,136},{254,196,136},{254,198,137},{254,199,139},{254,201,141},{254,203,142},{253,205,144},{253,205,144},{253,207,146},{253,209,147},{253,210,149},{253,212,151},{253,212,151},{253,214,152},{253,216,154},{253,218,156},{253,220,157},{253,221,159},{253,221,159},{253,223,161},{253,225,163},{252,227,165},{252,229,166},{252,229,166},{252,230,168},{252,232,170},{252,234,172},{252,234,172}};

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

    public static Color getHeatColor(int h) {
        int[] c = cmapMagma[h/40];
        return new Color(c[0], c[1], c[2]);
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