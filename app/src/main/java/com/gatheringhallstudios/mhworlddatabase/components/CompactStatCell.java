package com.gatheringhallstudios.mhworlddatabase.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.gatheringhallstudios.mhworlddatabase.R;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

/**
 * This is a full height, full width cell that displays an icon, label, and value. Used to generate
 * data rows in RecyclerView or inside XML layouts.
 */

public class CompactStatCell extends ConstraintLayout {

    private final String TAG = getClass().getSimpleName();

    ImageView imageView;
    TextView labelView;

    public CompactStatCell(Context context, @DrawableRes int imgSrc, String labelText) {
        super(context);
        Drawable drawable = ContextCompat.getDrawable(getContext(), imgSrc);
        init(drawable, labelText);
    }

    public CompactStatCell(Context context) {
        super(context);
        init(null, "");
    }

    public CompactStatCell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CompactStatCell);

        // Set values from attributes
        Drawable drawable;
        String labelText;
        try {
            drawable = attributes.getDrawable(R.styleable.CompactStatCell_iconSrc);
            labelText = attributes.getString(R.styleable.CompactStatCell_labelText);
        } finally {
            // Typed arrays should be recycled after use
            attributes.recycle();
        }

        init(drawable, labelText);
    }

    public void init(Drawable drawable, String labelText) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cell_compact_stat, this, true);

        // Bind views
        imageView = this.findViewById(R.id.generic_icon);
        labelView = this.findViewById(R.id.label_text);

        setLeftIconDrawable(drawable);
        setLabelText(labelText);
    }

    /**
     * Set custom drawable for the left icon
     */
    public void setLeftIconDrawable(Drawable drawable) {
        imageView.setImageDrawable(drawable);

        // Invalidate to trigger layout update
        invalidate();
    }

    public void setLabelText(String labelText) {
        labelView.setText(labelText);
    }

}
