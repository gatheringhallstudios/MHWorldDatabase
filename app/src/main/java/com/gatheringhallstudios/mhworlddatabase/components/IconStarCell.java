package com.gatheringhallstudios.mhworlddatabase.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gatheringhallstudios.mhworlddatabase.R;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * This view is a custom cell with an icon on the left followed by some stars.
 * Used in Monster Summary page to display element/status effectiveness
 */

public class IconStarCell extends LinearLayout {

    private final String TAG = getClass().getSimpleName();

    ImageView imageView;
    LinearLayout starLayout;

    public IconStarCell(Context context, @DrawableRes int imgSrc, int numStars) {
        super(context);
        Drawable drawable = ContextCompat.getDrawable(getContext(), imgSrc);
        init(drawable, numStars);
    }

    public IconStarCell(Context context) {
        super(context);
        init(null, 0);
    }

    public IconStarCell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.IconStarCell);

        // Set values from attributes
        Drawable drawable;
        int numStars;
        try {
            drawable = attributes.getDrawable(R.styleable.IconStarCell_iconSrc);
            numStars = attributes.getInt(R.styleable.IconStarCell_numStars, 0);
        } finally {
            // Typed arrays should be recycled after use
            attributes.recycle();
        }

        init(drawable, numStars);
    }

    public void init(Drawable drawable, int numStars) {
        this.setOrientation(HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cell_icon_star, this, true);

        // Bind views
        imageView = this.findViewById(R.id.generic_icon);
        starLayout = this.findViewById(R.id.star_layout);

        setLeftIconDrawable(drawable);
        setStars(numStars);
    }

    /**
     * Display a number of stars
     */
    public void setStars(int numStars) {
        starLayout.removeAllViews();
        for (int i = 0; i < numStars; i++) {
            ImageView star = new ImageView(getContext());

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int height = getResources().getDimensionPixelSize(R.dimen.image_size_small);
            int width = getResources().getDimensionPixelSize(R.dimen.image_size_small);
            int padding = getResources().getDimensionPixelSize(R.dimen.default_padding_margin_small);

            lp.height = height;
            lp.width = width;
            star.setPadding(0, padding, padding, padding);

            // TODO Set to star drawable once available
            star.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.question_mark_grey));

            star.setLayoutParams(lp);
            starLayout.addView(star);

            // Invalidate to trigger layout update
            invalidate();
        }
    }

    /**
     * Set custom drawable for the left icon
     *
     * @param drawable
     */
    public void setLeftIconDrawable(Drawable drawable) {
        imageView.setImageDrawable(drawable);

        // Invalidate to trigger layout update
        invalidate();
    }
}
