package training.com.common;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.LinearProgressButton;

import training.com.chatgcmapplication.R;

/**
 * Created by enclaveit on 2/26/16.
 */
public class MorphinButtonCreator {
    private int mMorphCounter1 = 1;

    public void morphToSuccess(final MorphingButton btnMorph, Context context) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(integer(R.integer.mb_animation, context))
                .cornerRadius(dimen(R.dimen.mb_height_56, context))
                .width(dimen(R.dimen.mb_height_56, context))
                .height(dimen(R.dimen.mb_height_56, context))
                .color(color(R.color.mb_green, context))
                .colorPressed(color(R.color.mb_green_dark, context))
                .icon(R.drawable.ic_done);
        btnMorph.morph(circle);
    }

    public void morphToSquare(final MorphingButton btnMorph, int duration, Context context) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_corner_radius_2, context))
                .width(dimen(R.dimen.mb_width_100, context))
                .height(dimen(R.dimen.mb_height_56, context))
                .color(color(R.color.mb_blue, context))
                .colorPressed(color(R.color.mb_blue_dark, context))
                .text("Send");
        btnMorph.morph(square);
    }

    public void simulateProgress1(@NonNull final LinearProgressButton button, final Context context) {
        int progressColor = color(R.color.mb_purple, context);
        int color = color(R.color.mb_gray, context);
        int progressCornerRadius = dimen(R.dimen.mb_corner_radius_4, context);
        int width = dimen(R.dimen.mb_width_200, context);
        int height = dimen(R.dimen.mb_height_8, context);
        int duration = integer(R.integer.mb_animation, context);

        ProgressGenerator generator = new ProgressGenerator(new ProgressGenerator.OnCompleteListener() {
            @Override
            public void onComplete() {
                morphToSuccess(button, context);
                button.unblockTouch();
            }
        });
        button.blockTouch(); // prevent user from clicking while button is in progress
        button.morphToProgress(color, progressColor, progressCornerRadius, width, height, duration);
        generator.start(button);
    }

    public void onMorphButton1Clicked(final LinearProgressButton btnMorph, Context context) {
        if (mMorphCounter1 == 0) {
            mMorphCounter1++;
            morphToSquare(btnMorph, integer(R.integer.mb_animation, context), context);
        } else if (mMorphCounter1 == 1) {
            mMorphCounter1 = 0;
            simulateProgress1(btnMorph, context);
        }
    }

    public int dimen(@DimenRes int resId, Context context) {
        return (int) context.getResources().getDimension(resId);
    }

    public int color(@ColorRes int resId, Context context) {
        return context.getResources().getColor(resId);
    }

    public int integer(@IntegerRes int resId, Context context) {
        return context.getResources().getInteger(resId);
    }
}
