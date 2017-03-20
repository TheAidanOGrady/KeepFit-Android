package com.aidanogrady.keepfit.date;

import android.os.Bundle;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.View;
import android.widget.DatePicker;

import com.aidanogrady.keepfit.R;

import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.ChronoUnit;

/**
 * The fragment to be displayed for choosing a date.
 *
 * @author Aidan O'Grady
 * @since 0.6
 */
public class DateDialogPreferenceFragment extends PreferenceDialogFragmentCompat {
    /**
     * The DatePicker widget.
     */
    private DatePicker mDatePicker;


    /**
     * Returns a newly created instance of the DateDialogPreferenceFragment and stores key of the
     * related Preference.
     *
     * @param key The key of the related Preference
     * @return A new instance of the fragment
     */
    public static DateDialogPreferenceFragment newInstance(String key) {
        final DateDialogPreferenceFragment fragment = new DateDialogPreferenceFragment();
        final Bundle bundle = new Bundle(1);
        bundle.putString(ARG_KEY, key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mDatePicker = (DatePicker) view.findViewById(R.id.date);
        if (mDatePicker == null) {
            throw new IllegalStateException("Dialog view must contain DatePicker with id 'date'");
        }

        Long longDate = null;
        DialogPreference preference = getPreference();
        if (preference instanceof DateDialogPreference) {
            longDate = ((DateDialogPreference) preference).getDate();
        }

        LocalDate date;
        if (longDate != null) {
            date = LocalDate.ofEpochDay(longDate);
        } else {
            date = LocalDate.now();
        }
        mDatePicker.updateDate(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int year = mDatePicker.getYear();
            int month = mDatePicker.getMonth() + 1;
            int day = mDatePicker.getDayOfMonth();

            LocalDate epoch = LocalDate.ofEpochDay(0);
            LocalDate now = LocalDate.of(year, month, day);
            long date = ChronoUnit.DAYS.between(epoch, now);

            DialogPreference preference = getPreference();
            if (preference instanceof DateDialogPreference) {
                DateDialogPreference dateDialogPreference = (DateDialogPreference) preference;
                if (dateDialogPreference.callChangeListener(date))
                    dateDialogPreference.setDate(date);
            }
        }
    }
}
