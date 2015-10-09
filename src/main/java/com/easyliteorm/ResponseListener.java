package com.easyliteorm;

/**
 * Callback handler that is called once
 * operation as ended
 * @author Mario
 */
public interface ResponseListener <T> {

    void onComplete (T response);
}
