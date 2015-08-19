package com.easyliteorm;

/**
 * Created by Mario on 18/8/2015.
 */
public interface ResponseListener <T> {

    void onComplete (T response);
}
