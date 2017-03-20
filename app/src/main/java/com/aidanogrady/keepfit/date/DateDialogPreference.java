package com.aidanogrady.keepfit.date;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;

import com.aidanogrady.keepfit.R;

/**
 * The DateDialogPreference allows for the selection of dates within a dialog displayed for a
 * particular preference.
 *
 * @author Aidan O'Grady
 * @since 0.6
 */
public class DateDialogPreference extends DialogPreference {
    /**
     * The date the user has selected. The long format is used for easy storage, and can be
     * converted at a later point.
     */
    private long mDate;

    /**
     * The resource ID of the layout of this preference.
     */
    private int mDialogLayoutResId = R.layout.pref_dialog_date;

    /**
     * Constructs a new DateDialogPreference.
     *
     * @param ctx  the context of this dialog preference.
     */
    public DateDialogPreference(Context ctx) {
        this(ctx, null);
    }

    /**
     * Constructs a new DateDialogPreference.
     *
     * @param ctx  the context of this dialog preference.
     * @param attrs  the attribute set to be added to this preference
     */
    public DateDialogPreference(Context ctx, AttributeSet attrs) {
        this(ctx, attrs, R.attr.preferenceStyle);
    }

    /**
     * Constructs a new DateDialogPreference.
     *
     * @param ctx the context of this dialog preference.
     * @param attrs the attribute set to be added to this preference
     * @param defStyleAttr the style of the preference
     */
    public DateDialogPreference(Context ctx, AttributeSet attrs, int defStyleAttr) {
        this(ctx, attrs, defStyleAttr, defStyleAttr);
    }

    /**
     * Constructs a new DateDialogPreference.
     *
     * @param ctx the context of this dialog preference.
     * @param attrs the attribute set to be added to this preference
     * @param defStyleAttr the style of the preference
     * @param defStyleRes the next style
     */
    public DateDialogPreference(Context ctx, AttributeSet attrs, int defStyleAttr,
                                int defStyleRes) {
        super(ctx, attrs, defStyleAttr, defStyleRes);
    }


    /**
     * Returns the date selected.
     *
     * @return date
     */
    public long getDate() {
        return mDate;
    }

    /**
     * Sets the date to the given date.
     *
     * @param date  the date to be set
     */
    public void setDate(long date) {
        mDate = date;
        persistLong(date);
    }

    @Override
    public int getDialogLayoutResource() {
        return mDialogLayoutResId;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setDate(restorePersistedValue ? getPersistedLong(mDate) : (Long) defaultValue);
    }
}
