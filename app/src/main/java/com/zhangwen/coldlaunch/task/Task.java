package com.zhangwen.coldlaunch.task;

/**
 * 定义可执行的任务，封装起来，便于日后统计。
 *
 * @author qisen.tqs@alibaba-inc.com
 */
public class Task {

  private final String taskName;
  private Runnable runnable;

  public Task(String taskName, Runnable runnable) {
    this.taskName = taskName;
    this.runnable = runnable;
  }

  public void execute() {
      this.runnable.run();
  }

  public Runnable getRunnable() {
    return runnable;
  }

  void wrapRunnable(Runnable runnable) {
    this.runnable = runnable;
  }

  String getTaskName() {
    return taskName;
  }
}
