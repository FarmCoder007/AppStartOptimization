package com.zhangwen.coldlaunch.task;


import com.zhangwen.coldlaunch.task.launch.LaunchTaskFactory;
import com.zhangwen.coldlaunch.task.launch.LaunchType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 管理任务的执行，是一个简易的并发框架。
 * <p>
 * 该极简框架支持一下几种操作类型
 * <p>
 * 1. 普通异步任务
 * 2. UI 线程执行任务
 * 3. 依赖于其他任务的任务
 * <p>
 * TODO. 目前还没有栅栏任务的需要，后续需要时在继续进行添加.
 * 4. 栅栏任务
 * <p>
 * TODO. 进程杀掉的时候，会迅速重启，能预先做一些缓存的东西，所以这个地方是可以做一些工作的.
 * <p>
 * TODO. 是否需要在任务顺利结束，或者任务失败，都必须得执行完后面的操作才行
 * <p>
 * TODO. 为了避免UI线程上的任务，依赖于其他线程的情况。如果依赖的其他线程比较耗时，那么会卡顿 UI 线程的。
 *
 * @author qisen.tqs@alibaba-inc.com
 */
public class TaskManager {

  private List<Task> uiTasks = new ArrayList<>();
  private List<Task> asyncTasks = new ArrayList<>();

  public TaskManager add(LaunchType type, boolean onUIThread) {
    add(LaunchTaskFactory.newTask(type), onUIThread);
    return this;
  }

  public TaskManager add(Task task, boolean onUIThread) {
    task.wrapRunnable(Utils.markTimeRecord(task.getTaskName(), task.getRunnable(), onUIThread));
    if (onUIThread) {
      uiTasks.add(task);
    } else {
      asyncTasks.add(task);
    }
    return this;
  }

  public TaskManager depend(Task originTask, boolean onUIThread, Task... tasks) {

    CountDownLatch countDownLatch = new CountDownLatch(tasks.length);

    for (Task task : tasks) {
      if (!uiTasks.contains(task) && !asyncTasks.contains(task)) {
        throw new IllegalStateException("Please add task first.");
      }
      task.wrapRunnable(Utils.markDepended(countDownLatch, task.getRunnable()));
    }
    originTask.wrapRunnable(Utils.markDepend(countDownLatch, originTask.getRunnable()));
    add(originTask, onUIThread);
    return this;
  }

  public TaskManager barrier() {
    return this;
  }

  /**
   * 配置完所有的任务，
   */
  public void start() {
    // 必须先执行非UI线程任务，因为这里的执行线程是 UI 线程。
    for (Task task : asyncTasks) {
      WorkThreadPool.execute(task.getRunnable(), true);
    }

    // 执行 UI 线程任务.
    for (Task task : uiTasks) {
      task.execute();
    }
  }

  public static void main(String[] args) {
    Task a = new Task("A", new Runnable() {
      @Override
      public void run() {
        TaskLog.d("=================I'm A, and I'am executed...");
      }
    });

    Task b = new Task("B", new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        TaskLog.d("=================I'm B, and I'am executed...");
      }
    });

    Task c = new Task("C", new Runnable() {
      @Override
      public void run() {
        TaskLog.d("=================I'm C, and I'am executed...");
      }
    });

    Task d = new Task("D", new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(20);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        TaskLog.d("=================I'm D, and I'am executed...");
      }
    });

    Task e = new Task("E", new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(20);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        TaskLog.d("=================I'm E, and I'am executed...");
      }
    });

    TaskManager taskManager = new TaskManager();
    taskManager
        .add(a, true)
        .add(b, false)
        .depend(c, true, a, b)
        .add(d, false)
        .depend(e, false, a, c)
        .start();
  }

}
