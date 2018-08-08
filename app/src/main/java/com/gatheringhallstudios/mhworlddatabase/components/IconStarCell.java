package com.gatheringhallstudios.mhworlddatabase.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gatheringhallstudios.mhworlddatabase.R;

/**
 * This view is a custom cell with an icon on the left followed by some stars.
 * Used in Monster Summary page to display element/status effectiveness
 */

public class IconStarCell extends LinearLayout {

    private final String TAG = getClass().getSimpleName();

    ImageView imageView;
    LinearLayout starLayout;
    LinearLayout altStarLayout;

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

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.cell_icon_star, this, true);

        imageView = findViewById(R.id.generic_icon);
        starLayout = findViewById(R.id.star_layout);
        altStarLayout = findViewById(R.id.alt_star_layout);

        setLeftIconDrawable(drawable);
        setStars(numStars);
    }

    /**
     * Display a number of stars
     */
    public void setStars(int numStars) {
        addStarsToLayout(starLayout, numStars);
    }

    public void setAltStars(int numStars) {
        findViewById(R.id.alt_star_section).setVisibility(View.VISIBLE);
        addStarsToLayout(altStarLayout, numStars);
    }

    private void addStarsToLayout(ViewGroup layout, int numStars) {
        layout.removeAllViews();
        for (int i = 0; i < 3; i++) {
            ImageView star = new ImageView(getContext());
            star.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_ui_effective_star));

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.height = getResources().getDimensionPixelSize(R.dimen.image_size_xsmall);
            lp.width = getResources().getDimensionPixelSize(R.dimen.image_size_xsmall);
            star.setLayoutParams(lp);

            if (i >= numStars) {
                star.setAlpha(0.20f);
            }

            layout.addView(star);

            // Invalidate to trigger layout update
            invalidate();
        }
    }

    /**
     * Set custom drawable for the left icon
     * @param drawable
     */
    public void setLeftIconDrawable(Drawable drawable) {
        imageView.setImageDrawable(drawable);

        // Invalidate to trigger layout update
        invalidate();
    }
}
