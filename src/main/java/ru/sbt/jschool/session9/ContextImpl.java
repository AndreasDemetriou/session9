package ru.sbt.jschool.session9;

import java.util.List;
import java.util.concurrent.Future;

public class ContextImpl implements Context {
    private List<Future> ft;
    ContextImpl(List<Future> ft){
        this.ft = ft;
    }
    private int ctc,ftc,itc;
    private void updateData(){
        ctc = 0;
        ftc = 0;
        itc = 0;
        for (Future future:
                ft) {
            try {
                if(future.isCancelled()) itc++;
                if(future.isDone()&&(future.get() == null)) ctc++;
            } catch (Exception e){
                ftc++;
            }
        }
    }

    @Override
    public int getCompletedTaskCount(){
        updateData();
        return ctc;
    }

    @Override
    public int getFailedTaskCount() {
        updateData();
        return ftc;
    }

    @Override
    public int getInterruptedTaskCount() {
        updateData();
        return itc;
    }

    @Override
    public void interrupt() {
        for (Future f:
             ft) {
            f.cancel(false);
        }
    }

    @Override
    public boolean isFinished() {
        if(ftc>0) return false;
        else
            if(itc+ctc == ft.size()) return true;
            else return false;
    }
}
