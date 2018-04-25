package pt.andre.android.utilities.Glide.Utilities;

import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.TextView;
import com.github.florent37.glidepalette.GlidePalette;
import pt.andre.android.utilities.Glide.GlideUtilities;
import pt.andre.android.utilities.Glide.Utilities.Interfaces.ColorView;

public class TextColorWrapper extends ColorView {
    private boolean isTitle;

    public TextColorWrapper(View view, GlideUtilities.Colors chosenSwatch, GlideUtilities.Colors fallbackSwatch, boolean isTitle) {
        super(view, chosenSwatch, fallbackSwatch);
        this.isTitle = isTitle;
    }

    @Override
    public void colorView(Palette palette, GlidePalette<Drawable> requestColor) {
        requestColor.use(chosenSwatch.getValue())
                .intoTextColor((TextView) view, isTitle ? GlidePalette.Swatch.TITLE_TEXT_COLOR : GlidePalette.Swatch.BODY_TEXT_COLOR);
    }
}