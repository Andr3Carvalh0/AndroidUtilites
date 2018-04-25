package pt.andre.android.utilities.Glide;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.annimon.stream.Stream;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.github.florent37.glidepalette.GlidePalette;
import java.util.LinkedList;
import java.util.List;
import pt.andre.android.utilities.Glide.Utilities.ColorWrapper;
import pt.andre.android.utilities.Glide.Utilities.Interfaces.ColorView;
import pt.andre.android.utilities.Glide.Utilities.TextColorWrapper;

public class GlideUtilities {

    public enum Colors {
        VIBRANT(0),
        DARK_VIBRANT(1),
        LIGHT_VIBRANT(2),
        MUTED(3),
        DARK_MUTED(4),
        LIGHT_MUTED(5);

        private int value;

        Colors(int v) {
            this.value = v;
        }

        public int getValue() {
            return value;
        }
    }

    public static GlideUtilities.Builder load(@NonNull View context, @NonNull String url, @NonNull ImageView imageView){
        return new Builder<>(context, url, imageView);
    }

    public static GlideUtilities.Builder load(@NonNull Fragment context, @NonNull String url, @NonNull ImageView imageView){
        return new Builder<>(context, url, imageView);
    }

    static class Builder<T>{
        private String url;
        private T holder;
        private int placeholder = -1;
        private int onError = -1;
        private List<ColorView> views = new LinkedList<>();
        private ImageView view;

        private Builder(){}

        private Builder(T holder, String url, ImageView imageView) {
            this.url = url;
            this.holder = holder;
            this.view = imageView;
        }

        public Builder<T> setLoadingPlaceholder(int placeholder){
            this.placeholder = placeholder;
            return this;
        }

        public Builder<T> setOnLoadingError(int onError){
            this.onError = onError;
            return this;
        }


        public Builder<T> colorView(@NonNull Colors chosenSwatch, Colors fallbackSwatch, @NonNull View... view){
            Stream.of(view)
                    .map(v -> new ColorWrapper(v, chosenSwatch, fallbackSwatch))
                    .forEach(c -> views.add(c));

            return this;
        }

        public Builder<T> colorTextView(@NonNull Colors chosenSwatch, Colors fallbackSwatch, boolean isTitle, @NonNull TextView... view){
            Stream.of(view)
                    .map(v -> new TextColorWrapper(v, chosenSwatch, fallbackSwatch, isTitle))
                    .forEach(c -> views.add(c));

            return this;
        }

        //Terminal Operations
        public void start(){
            RequestBuilder request = prepare();

            processPlaceholders(request);
            processColorExtraction(request);

            request.into(view);
        }

        private RequestBuilder prepare(){
            if(holder instanceof Fragment)
                return Glide.with((Fragment) holder).load(url);

            return Glide.with((View)holder).load(url);
        }

        private void processPlaceholders(RequestBuilder rm){
            final RequestOptions requestOptions = new RequestOptions();

            if(placeholder != -1)
                requestOptions.placeholder(placeholder);

            if(onError != -1)
                requestOptions.error(onError);

            rm.apply(requestOptions);
        }

        private void processColorExtraction(RequestBuilder rm){
            final GlidePalette<Drawable> requestColor = GlidePalette.with(url);

            requestColor.intoCallBack(palette -> {
                        if (palette != null)
                            Stream.of(views).forEach(v -> v.colorView(palette, requestColor));

                    });

            rm.listener(requestColor);
        }
    }

}