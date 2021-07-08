package org.rdlinux.ezmybatis.core.Interceptor.listener;

import java.util.List;

/**
 * 插入事件监听器
 */
public interface InsertListener {
    void onInsert(Object entity);

    void onBatchInsert(List<Object> entity);
}
