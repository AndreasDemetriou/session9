package ru.sbt.jschool.session9;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ExecutionManagerImpl implements ExecutionManager {
    private List<Future> lft = new ArrayList<Future>();
    @Override
    public Context execute(final Runnable callback, Runnable... tasks) throws InterruptedException {
        final ExecutorService service = Executors.newFixedThreadPool(tasks.length);
        for (Runnable task : tasks) {
            lft.add(service.submit(task));
        }
        Context resultContext = new ContextImpl(lft);
        service.shutdown();
        Runnable callbackRun = new Runnable(){
            @Override
            public void run() {
                try {
                    if(service.awaitTermination(1,TimeUnit.DAYS)) {
                        callback.run();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        ExecutorService serviceForCallback = Executors.newFixedThreadPool(1);
        serviceForCallback.submit(callbackRun);
        serviceForCallback.shutdown();
        return resultContext;
    }
}
