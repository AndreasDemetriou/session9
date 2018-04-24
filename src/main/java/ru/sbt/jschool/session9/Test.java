package ru.sbt.jschool.session9;

public class Test {
    public static void main(String[] args) {
        Runnable callback =  new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello from callback!");
            }
        };
        Runnable a,b,c;
        a = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello from a!");
            }
        };
        b = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Context cnt = new ExecutionManagerImpl().execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Inner b callback");
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Inner b");
                    }
                });
                System.out.println(cnt.getCompletedTaskCount()+" Completed in b");
                System.out.println("Hello from b!");
                int x = 1/0;// provoke an exception to check counter of failed tasks
            }
        };
        c = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Hello from c!");
            }
        };
        Context cnt = new ExecutionManagerImpl().execute(callback,a,b,c);
        System.out.println("Completed tasks: " + cnt.getCompletedTaskCount());
        System.out.println("Failed tasks: " + cnt.getFailedTaskCount());

    }
}
