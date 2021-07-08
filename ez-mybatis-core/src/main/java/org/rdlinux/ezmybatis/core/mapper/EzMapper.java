package org.rdlinux.ezmybatis.core.mapper;

import org.apache.ibatis.annotations.InsertProvider;

import java.io.Serializable;
import java.util.List;

/**
 * 基础mapper
 *
 * @param <Nt> 实体类型
 * @param <Pt> 主键类型
 */
public interface EzMapper<Nt, Pt extends Serializable> {
    /**
     * 插入
     */
    @InsertProvider(type = EzEntityInsertProvider.class, method = "generate")
    int insert(Nt entity);

    /**
     * 批量插入
     */
    @InsertProvider(type = EzEntityBatchInsertProvider.class, method = "generate")
    int batchInsert(List<Nt> entitys);

    /**
     * 更新
     */
    @InsertProvider(type = EzEntityInsertProvider.class, method = "generate")
    int update(Nt entity);

    /**
     * 批量更新
     */
    @InsertProvider(type = EzEntityInsertProvider.class, method = "generate")
    int batchUpdate(List<Nt> entitys);

    /**
     * 删除
     */
    @InsertProvider(type = EzEntityInsertProvider.class, method = "generate")
    int delete(Nt entity);

    /**
     * 批量删除
     */
    @InsertProvider(type = EzEntityInsertProvider.class, method = "generate")
    int batchDelete(List<Nt> entitys);

    /**
     * 根据主键删除
     */
    @InsertProvider(type = EzEntityInsertProvider.class, method = "generate")
    int deleteByPrimaryKey(Pt id);

    /**
     * 根据主键批量删除
     */
    @InsertProvider(type = EzEntityInsertProvider.class, method = "generate")
    int deleteByPrimaryKeys(List<Pt> ids);

    /**
     * 根据主键查询
     */
    @InsertProvider(type = EzEntityInsertProvider.class, method = "generate")
    Nt selectByPrimaryKey(Pt id);

    /**
     * 根据主键批量查询
     */
    @InsertProvider(type = EzEntityInsertProvider.class, method = "generate")
    List<Nt> selectByPrimaryKeys(List<Pt> ids);
}
