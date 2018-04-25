package pt.andre.android.utilities.Glide.Utilities.Interfaces;

import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.view.View;
import com.github.florent37.glidepalette.GlidePalette;

import pt.andre.android.utilities.Glide.GlideUtilities;

public abstract class ColorView {
    protected final View view;
    protected final GlideUtilities.Colors chosenSwatch;
    protected final GlideUtilities.Colors fallbackSwatch;

    public ColorView(View view, GlideUtilities.Colors chosenSwatch, GlideUtilities.Colors fallbackSwatch) {
        this.view = view;
        this.chosenSwatch = chosenSwatch;
        this.fallbackSwatch = fallbackSwatch;
    }

    public abstract void colorView(Palette palette, GlidePalette<Drawable> color);

    public View getView() {
        return view;
    }

    protected Palette.Swatch getPalette(GlideUtilities.Colors color, Palette palette) {
        switch (color){
            case MUTED:
                return palette.getMutedSwatch();
            case VIBRANT:
                return palette.getVibrantSwatch();
            case LIGHT_MUTED:
                return palette.getLightMutedSwatch();
            case LIGHT_VIBRANT:
                return palette.getLightVibrantSwatch();
            case DARK_MUTED:
                return palette.getDarkMutedSwatch();
            default:
                return palette.getDarkVibrantSwatch();
        }
    }

}
