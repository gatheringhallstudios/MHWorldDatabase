package com.gatheringhallstudios.mhworlddatabase.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gatheringhallstudios.mhworlddatabase.R;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This is a cell that displays a horizontal layout of icons with an optional left icon.
 */
public class CompactStatIconLayoutCell extends ConstraintLayout {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.generic_icon)
    public ImageView imageView;
    @BindView(R.id.value_layout)
    public LinearLayout linearLayout;

    public CompactStatIconLayoutCell(Context context, @DrawableRes int imgSrc) {
        super(context);
        Drawable drawable = AppCompatResources.getDrawable(getContext(), imgSrc);
        init(drawable);
    }

    public CompactStatIconLayoutCell(Context context) {
        super(context);
        init(null);
    }

    public CompactStatIconLayoutCell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CompactStatIconLayoutCell);

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
     * Set custom drawable for the left icon.
     *
     * @param drawable to use as the left icon. Pass null to hide the icon.
     */
    public void setLeftIconDrawable(@Nullable Drawable drawable) {
        if (drawable != null) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(drawable);
        } else {
            imageView.setVisibility(View.GONE);
        }

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