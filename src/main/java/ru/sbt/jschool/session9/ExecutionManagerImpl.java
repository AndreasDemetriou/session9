package ru.sbt.jschool.session9;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutionManagerImpl implements ExecutionManager {
    private List<Future> lft = new ArrayList<Future>();
    @Override
    public Context execute(final Runnable callback, Runnable... tasks) {
        final ExecutorService service = Executors.newFixedThreadPool(tasks.length);
        for (Runnable task : tasks) {
            lft.add(service.submit(task));
        }
        service.shutdown();
        new Runnable(){
            @Override
            public void run() {
                for (;;)
                    if (service.isTerminated()){
                        callback.run();
                        break;
                    }
            }
        }.run();
        return new ContextImpl(lft);
    }
}
