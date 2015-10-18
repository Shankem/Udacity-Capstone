package com.growingcoder.constantreminder;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.growingcoder.constantreminder.data.gen.Reminder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Adapter to handle displaying reminders in a list.
 *
 * @author Pierce
 * @since 10/18/2015.
 */
public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    private OnRecyclerItemClickListener mItemClickListener = null;
    private ReminderComparator mRemindersComparator = new ReminderComparator();
    private List<Reminder> mReminders;

    public ReminderAdapter() {
        mReminders = new ArrayList<Reminder>();
    }

    public void setReminders(List<Reminder> reminders) {
        mReminders = reminders;
        Collections.sort(mReminders, mRemindersComparator);
    }

    public void setItemClickListener(OnRecyclerItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_reminder, parent, false);
        ViewHolder vh = new ViewHolder(v, mItemClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ReminderAdapter.ViewHolder holder, int position) {
        Reminder r = mReminders.get(position);
        holder.mName.setText(r.getName());
        holder.mTime.setText(DateUtils.getRelativeDateTimeString(
                App.sAppContext,
                r.getDateTime().longValue(),
                DateUtils.DAY_IN_MILLIS,
                0l,
                0));

        if (r.getDateTime() > new Date().getTime()) {
            holder.mTime.setTextColor(App.sAppContext.getResources().getColor(R.color.reminder_current));
        } else {
            holder.mTime.setTextColor(App.sAppContext.getResources().getColor(R.color.reminder_past));
        }
    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }

    public Reminder getItem(int position) {
        return mReminders.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mName;
        public TextView mTime;

        private OnRecyclerItemClickListener mListener;

        public ViewHolder(View v, OnRecyclerItemClickListener listener) {
            super(v);
            mListener = listener;
            mName = (TextView) v.findViewById(R.id.card_reminder_textview_title);
            mTime = (TextView) v.findViewById(R.id.card_reminder_textview_time);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (mListener != null) {
                mListener.onItemClick(v, position);
            }
        }
    }

    /**
     * Comparator used for sorting the reminders from oldest to newest.
     */
    private class ReminderComparator implements Comparator<Reminder> {
        @Override
        public int compare(Reminder lhs, Reminder rhs) {
            return lhs.getDateTime().compareTo(rhs.getDateTime());
        }
    }
}
