package com.gatheringhallstudios.mhworlddatabase.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gatheringhallstudios.mhworlddatabase.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This is a full height, full width cell that displays an icon, label, and value. Used to generate
 * data rows in RecyclerView or inside XML layouts.
 */

public class CompactStatLayoutCell extends ConstraintLayout {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.generic_icon)
    public ImageView imageView;
    @BindView(R.id.value_layout)
    public LinearLayout linearLayout;

    public CompactStatLayoutCell(Context context, @DrawableRes int imgSrc) {
        super(context);
        Drawable drawable = ContextCompat.getDrawable(getContext(), imgSrc);
        init(drawable);
    }

    public CompactStatLayoutCell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CompactStatLayoutCell);

        // Set values from attributes
        Drawable drawable;
        try {
            drawable = attributes.getDrawable(R.styleable.CompactStatCell_iconSrc);

        } finally {
            // Typed arrays should be recycled after use
            attributes.recycle();
        }

        init(drawable);
    }

    public void init(Drawable drawable) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cell_compact_layout_stat, this, true);
        ButterKnife.bind(this);
        setLeftIconDrawable(drawable);
    }

    /**
     * Set custom drawable for the left icon
     */
    public void setLeftIconDrawable(Drawable drawable) {
        imageView.setImageDrawable(drawable);

        // Invalidate to trigger layout update
        invalidate();
    }

    public void addLayoutIcon(Drawable drawable) {
        ImageView image = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams((int) getResources().getDimension(R.dimen.image_size_xsmall),
                (int) getResources().getDimension(R.dimen.image_size_xsmall));
        image.setLayoutParams(layoutParams);
        image.setImageDrawable(drawable);
        linearLayout.addView(image);
    }
}