package com.aidanogrady.keepfit.home;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aidanogrady.keepfit.R;
import com.aidanogrady.keepfit.data.model.Update;

import org.threeten.bp.LocalTime;

import java.util.List;
import java.util.Locale;

/**
 * The UpdatesAdapter is an adapter for the update recycler view, which displays the check-ins the
 * user has made during this day.
 *
 * @author Aidan O'Grady
 * @since 0.6
 */
class UpdatesAdapter extends RecyclerView.Adapter<UpdatesAdapter.UpdateViewHolder> {
    /**
     * The list of updates to be displayed.
     */
    private List<Update> mUpdates;


    /**
     * Constricts a mew UpdateAdapter for the given list of history.
     *
     * @param updates the updates to be adapted
     */
    UpdatesAdapter(List<Update> updates) {
        setList(updates);
    }


    /**
     * Replaces the current list of updates with the given list of history.
     *
     * @param updates the list of updates to replace existing
     */
    void replaceData(List<Update> updates) {
        setList(updates);
    }

    /**
     * Sets the list of history to the given list of history.
     *
     * @param updates list of updates
     */
    private void setList(List<Update> updates) {
        if (updates != null) {
            mUpdates = updates;
        }
    }

    @Override
    public UpdateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_update, parent, false);
        return new UpdateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UpdateViewHolder holder, int position) {
        Update update = mUpdates.get(position);
        LocalTime time = LocalTime.ofSecondOfDay(update.getTime());
        String timeString = time.toString();
        String stepsString = String.format(Locale.getDefault(), "%d", update.getSteps());

        String fullString = "At " + timeString + ", you added " + stepsString + " steps";

        SpannableStringBuilder ssb = new SpannableStringBuilder(fullString);
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        int timeIndex = fullString.indexOf(timeString);
        int timeIndexEnd = timeIndex + timeString.length();
        ssb.setSpan(styleSpan, timeIndex, timeIndexEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        int stepsIndex = fullString.indexOf(stepsString);
        int stepsIndexEnd = stepsIndex + stepsString.length();
        ssb.setSpan(styleSpan, stepsIndex, stepsIndexEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        TextView updateTextView = holder.mUpdateTextView;
        updateTextView.setText(ssb);
    }

    @Override
    public int getItemCount() {
        return mUpdates.size();
    }

    class UpdateViewHolder extends RecyclerView.ViewHolder {
        /**
         * The text view displaying the update.
         */
        TextView mUpdateTextView;

        UpdateViewHolder(View itemView) {
            super(itemView);
            mUpdateTextView = (TextView) itemView.findViewById(R.id.update);
        }
    }
}
