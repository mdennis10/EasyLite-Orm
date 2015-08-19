package com.easyliteorm;


import android.os.AsyncTask;
import android.util.Log;


public class EasyLiteAsyncTask <T> extends AsyncTask<String,Void,T> {
    protected final ResponseListener<T> listener;
    protected final Action<T> action;

    public EasyLiteAsyncTask(Action<T> action, ResponseListener<T> listener) {
        this.listener = listener;
        this.action   = action;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (action == null){
            cancel(true);
            Log.w("EasyLite", "Action not supplied to EasyLiteAsyncTask");
        }

        if (listener == null){
            cancel(true);
            Log.w("EasyLite", "ResponseListener not supplied to EasyLiteAsyncTask");
        }
    }

    @Override
    protected T doInBackground(String... params) {
        return this.action.execute();
    }

    @Override
    protected void onPostExecute(T response) {
        listener.onComplete(response);
    }
}
