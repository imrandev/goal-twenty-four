package com.techdev.goalbuzz.service.schedule;

import android.content.Context;

public interface ScheduleService<T> {
    boolean schedule(T object, Context context);
    void schedule(T object, Context context, OnScheduleCallback onScheduleCallback);
    void scheduleOrCancel(T object, Context context, OnScheduleCallback onScheduleCallback, OnCancelCallback onCancelCallback);
    boolean cancel(int eventId, Context context);
    void cancel(int eventId, Context context, OnCancelCallback onCancelCallback);
    interface OnScheduleCallback {
        void onScheduled();
    }
    interface OnCancelCallback {
        void onCanceled();
    }
}
