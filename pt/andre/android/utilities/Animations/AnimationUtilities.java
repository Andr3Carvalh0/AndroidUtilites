package pt.andre.android.utilities.Animations;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import java.util.LinkedList;
import java.util.List;
import pt.andre.android.utilities.Functional.Consumer;
import pt.andre.android.utilities.Functional.Supplier;

public class AnimationUtilities {

    enum ANIMATION_TYPES{
        FADEIN(() -> {
            android.view.animation.Animation animation = new AlphaAnimation(0, 1);
            animation.setInterpolator(new DecelerateInterpolator());
            return animation;
        }),

        FADEOUT(() -> {
            android.view.animation.Animation animation = new AlphaAnimation(1, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            return animation;
        });

        private Supplier<android.view.animation.Animation> animation;

        ANIMATION_TYPES(Supplier<android.view.animation.Animation> animation) {
            this.animation = animation;
        }

        public Supplier<android.view.animation.Animation> getAnimation() {
            return animation;
        }
    }

    public static class Builder{
        private View v;
        private List<Long> animationDuration = new LinkedList<>();
        private List<ANIMATION_TYPES> animationType = new LinkedList<>();
        private static final long DEFAULT_ANIMATION_DURATION = 500;
        private List<Consumer<android.view.animation.Animation>> onAnimationStart = new LinkedList<>();
        private List<Consumer<android.view.animation.Animation>> onAnimationEnd = new LinkedList<>();
        private LinkedList<Consumer<android.view.animation.Animation>> onAnimationRepeat = new LinkedList<>();
        private List<Long> intervalBetweenAnimations = new LinkedList<>();
        private int index = 0;

        private Builder(){}

        Builder(View v) {
            this.v = v;
        }

        public Builder setAnimationDuration(long time){
            replaceOnIndex(animationDuration, index, time, () -> DEFAULT_ANIMATION_DURATION);
            return this;
        }

        public Builder setFadeInAnimation(){
            replaceOnIndex(animationType, index, ANIMATION_TYPES.FADEIN, () -> ANIMATION_TYPES.FADEIN);
            return this;
        }

        public Builder setFadeOutAnimation(){
            replaceOnIndex(animationType, index, ANIMATION_TYPES.FADEOUT, () -> ANIMATION_TYPES.FADEIN);
            return this;
        }

        public Builder setIntervalBetweenAnimations(long interval){
            replaceOnIndex(intervalBetweenAnimations, index, interval, () -> DEFAULT_ANIMATION_DURATION);
            return this;
        }

        public Builder addOnAnimationEndEvent(Consumer<android.view.animation.Animation> event){
            replaceOnIndex(onAnimationEnd, index, event, () -> (anim) -> {});
            return this;
        }

        public Builder addOnAnimationStartEvent(Consumer<android.view.animation.Animation> event){
            replaceOnIndex(onAnimationStart, index, event, () -> (anim) -> {});
            return this;
        }

        public Builder addOnAnimationRepeatEvent(Consumer<android.view.animation.Animation> event){
            replaceOnIndex(onAnimationRepeat, index, event, () -> (anim) -> {});
            return this;
        }

        public Builder followedByAnimation(){
            this.index++;
            return this;
        }

        // Terminal Operations
        public Animation buildAnimation(){
            if(animationType.size() == 0)
                return new Animation(v, ANIMATION_TYPES.FADEIN.getAnimation().get());

            return createAnimation(0, animationType.size() - 1);
        }

        private Animation createAnimation(int index, int max){
            android.view.animation.Animation animation = animationType.get(index).getAnimation().get();
            animation.setDuration(animationDuration.size() > index ? animationDuration.get(index) : DEFAULT_ANIMATION_DURATION);

            final int[] in = {index};
            animation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
                @Override
                public void onAnimationStart(android.view.animation.Animation animation) {
                    if(onAnimationStart.size()!= 0
                            && onAnimationStart.size() > in[0] && onAnimationStart.get(in[0]) != null)
                        onAnimationStart.get(in[0]).accept(animation);
                }

                @Override
                public void onAnimationEnd(android.view.animation.Animation animation) {
                    if(onAnimationEnd.size()!= 0
                            && onAnimationEnd.size() > in[0] && onAnimationEnd.get(in[0]) != null)
                        onAnimationEnd.get(in[0]).accept(animation);

                    if(in[0] != max)
                        new Handler().postDelayed(() -> createAnimation(++in[0], max).start(),
                                intervalBetweenAnimations.size() > in[0]
                                        ? intervalBetweenAnimations.get(in[0])
                                        : DEFAULT_ANIMATION_DURATION);
                }

                @Override
                public void onAnimationRepeat(android.view.animation.Animation animation) {
                    if(onAnimationRepeat.size()!= 0
                            && onAnimationRepeat.size() > in[0] && onAnimationRepeat.get(in[0]) != null)
                        onAnimationRepeat.get(in[0]).accept(animation);
                }
            });

            return new Animation(v, animation);
        }

        private <T extends Object> void replaceOnIndex(List<T>list, int index, T value, Supplier<T> getEmptyObject){
            if(list.size() != 0 && onAnimationRepeat.size() > index && list.get(index) != null)
                list.remove(index);

            if(index > list.size()){
                int initial = list.size() > 0 ? list.size() - 1 : 0;

                for (int i = initial; i <= index; i++)
                    list.add(i, getEmptyObject.get());
            }

            list.add(index, value);
        }

    }

    public static class Animation {
        private View v;
        private android.view.animation.Animation animation;

        private Animation(){ }

        Animation(View v, android.view.animation.Animation animation) {
            this.v = v;
            this.animation = animation;
        }

        public void start(){
            v.startAnimation(animation);
        }

        public android.view.animation.Animation getAnimation() {
            return animation;
        }

        public void stop(){
            v.clearAnimation();
        }
    }

    public static AnimationUtilities.Builder forView(@NonNull View v){
        return new Builder(v);
    }

}
