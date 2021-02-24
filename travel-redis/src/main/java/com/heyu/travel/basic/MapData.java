package com.heyu.travel.basic;

import java.util.Map;

/**
 * @Description Map结构
 */
@FunctionalInterface
public interface MapData<K, V> {

    Map<K, V> getData();

}
