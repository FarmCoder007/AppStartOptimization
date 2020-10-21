package com.zhangwen.coldlaunch.task;

import java.util.Collection;
import java.util.Map;

public class CollectionUtils {

  public static <T> boolean isEmpty(Collection<T> list) {
    return list == null || list.isEmpty();
  }

  public static <K, V> boolean isEmpty(Map<K, V> map) {
    return map == null || map.isEmpty();
  }

}
