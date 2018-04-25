package pt.andre.android.utilities.Glide.Utilities;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.florent37.glidepalette.GlidePalette;
import pt.andre.android.utilities.Glide.GlideUtilities;
import pt.andre.android.utilities.Glide.Utilities.Interfaces.ColorView;

public class ColorWrapper extends ColorView {

    public ColorWrapper(View view, GlideUtilities.Colors chosenSwatch, GlideUtilities.Colors fallbackSwatch) {
        super(view, chosenSwatch, fallbackSwatch);
    }

    @Override
    public void colorView(Palette palette, GlidePalette<Drawable> requestColor) {
        Palette.Swatch swatch = getPalette(chosenSwatch, palette);

        if(swatch == null && fallbackSwatch != null)
            swatch = getPalette(fallbackSwatch, palette);

        if(swatch == null)
            return;

        if (view instanceof TextView) {
            ((TextView) view).setTextColor(ColorStateList.valueOf(swatch.getRgb()));
            return;
        }

        if (view instanceof FloatingActionButton){
            view.setBackgroundTintList(ColorStateList.valueOf(swatch.getRgb()));
            return;
        }

        if (view instanceof ImageView) {
            ((ImageView) view).setColorFilter(swatch.getRgb(), android.graphics.PorterDuff.Mode.SRC_IN);
            return;
        }

        //fallback
        view.setBackgroundColor(swatch.getRgb());
    }
}
