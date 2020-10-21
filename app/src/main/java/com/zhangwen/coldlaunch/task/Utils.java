package com.zhangwen.coldlaunch.task;
import java.util.concurrent.CountDownLatch;

/**
 * Created by tangqisen on 2017/5/26.
 */

public class Utils {

  public static Runnable markDepend(CountDownLatch countDownLatch, Runnable runnable) {
    return new DependRunnable(countDownLatch, runnable);
  }

  public static Runnable markDepended(CountDownLatch countDownLatch, Runnable runnable) {
    return new DependedRunnable(countDownLatch, runnable);
  }

  public static Runnable markTimeRecord(String taskName, Runnable runnable, boolean onUIThread) {
    return new TimeRecordRunnable(taskName, runnable, onUIThread);
  }

  private static class TimeRecordRunnable implements Runnable {

    // TODO. 如果有必要的话，要将这些耗时时间上传到 UT（或者其他平台）上去.

    private final String taskName;
    private final Runnable runnable;
    private final boolean onUIThread;

    public TimeRecordRunnable(String taskName, Runnable runnable, boolean onUIThread) {
      this.taskName = taskName;
      this.runnable = runnable;
      this.onUIThread = onUIThread;
    }

    @Override
    public void run() {
      long startTime = System.currentTimeMillis();
      LaunchRunTime.getInstance().markKey("key_" + taskName);
      this.runnable.run();
      long endTime = System.currentTimeMillis();
      long runTime = endTime - startTime;
      if (onUIThread) {
        LaunchRunTime.getInstance().markRunTime("rt_" + taskName, runTime);
//        TaskLog.e("Finish executing Task(%s) at UI-Thread, and cost time %d ms", taskName, runTime);
      } else {
        // 非ui线程的启动耗时暂不统计
//        TaskLog.d("Finish executing Task(%s), and cost time %d ms", taskName, runTime);
      }
    }

  }

  private static class DependedRunnable implements Runnable {

    private final CountDownLatch countDownLatch;
    private final Runnable runnable;

    public DependedRunnable(CountDownLatch countDownLatch, Runnable runnable) {
      this.countDownLatch = countDownLatch;
      this.runnable = runnable;
    }

    @Override
    public void run() {
      this.runnable.run();
      countDownLatch.countDown();
    }
  }

  private static class DependRunnable implements Runnable {

    private final CountDownLatch countDownLatch;
    private final Runnable runnable;

    private DependRunnable(CountDownLatch countDownLatch, Runnable runnable) {
      this.countDownLatch = countDownLatch;
      this.runnable = runnable;
    }

    @Override
    public void run() {
      try {
        countDownLatch.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      this.runnable.run();
    }
  }

}