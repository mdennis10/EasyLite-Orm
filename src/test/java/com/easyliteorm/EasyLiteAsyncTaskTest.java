package com.easyliteorm;

import com.easyliteorm.model.Note;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.ExecutionException;

/**
 * Created by Mario on 18/8/2015.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class EasyLiteAsyncTaskTest {

    @Test public void constructorParametersAreInstaniatedTest (){
        EasyLiteAsyncTask<Note> task = new EasyLiteAsyncTask<Note>(Mockito.mock(Action.class),Mockito.mock(ResponseListener.class));
        Assert.assertNotNull(task.listener);
        Assert.assertNotNull(task.action);
    }

    @Test public void stopAsyncTaskWhenNullActionIsSupplied (){
        EasyLiteAsyncTask<String> task = new EasyLiteAsyncTask<String>(null,Mockito.mock(ResponseListener.class));
        task.execute();
        Robolectric.runBackgroundTasks();
        Assert.assertTrue(task.isCancelled());
    }

    @Test public void stopAsyncTaskWhenNullResponseListenerIsSupplied (){
        EasyLiteAsyncTask<String> task = new EasyLiteAsyncTask<String>(Mockito.mock(Action.class),null);
        task.execute();
        Robolectric.runBackgroundTasks();
        Assert.assertTrue(task.isCancelled());
    }

    @Test
    public void doInBackground_executedSuccessfullyTest () throws ExecutionException, InterruptedException {
        Action<Note> action = Mockito.mock(Action.class);
        Mockito.when(action.execute())
                .thenReturn(new Note());

        EasyLiteAsyncTask<Note> task = new EasyLiteAsyncTask<Note>(action,Mockito.mock(ResponseListener.class));
        task.execute();
        Robolectric.runBackgroundTasks();
        Assert.assertNotNull(task.get());
    }

    @Test
    public void onPostExecute_callsResponseListenerOnCompleteMethod (){
        Action<Note> action = Mockito.mock(Action.class);
        Note response = new Note();
        Mockito.when(action.execute())
                .thenReturn(response);

        ResponseListener<Note> listener = Mockito.mock(ResponseListener.class);

        EasyLiteAsyncTask<Note> task = new EasyLiteAsyncTask<Note>(action,listener);
        task.execute();
        Robolectric.runBackgroundTasks();
        Mockito.verify(listener).onComplete(response);
    }
}