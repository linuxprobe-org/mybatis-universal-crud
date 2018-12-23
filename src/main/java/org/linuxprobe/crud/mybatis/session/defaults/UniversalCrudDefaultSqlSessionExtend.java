package org.linuxprobe.crud.mybatis.session.defaults;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.linuxprobe.crud.core.annoatation.PrimaryKey.Strategy;
import org.linuxprobe.crud.core.content.EntityInfo;
import org.linuxprobe.crud.core.content.UniversalCrudContent;
import org.linuxprobe.crud.core.proxy.ModelCglib;
import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.core.sql.generator.DeleteSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.InsertSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.SelectSqlGenerator;
import org.linuxprobe.crud.mybatis.session.SqlSessionExtend;
import org.linuxprobe.crud.utils.EntityUtils;
import org.linuxprobe.crud.utils.FieldUtil;

public class UniversalCrudDefaultSqlSessionExtend implements SqlSessionExtend {
	private static final String selectStatement = "org.linuxprobe.crud.mapper.UniversalMapper.universalSelect";
	private static final String selectOneStatement = "org.linuxprobe.crud.mapper.UniversalMapper.universalSelectOne";
	private static final String selectCountStatement = "org.linuxprobe.crud.mapper.UniversalMapper.selectCount";
	private static final String insertStatement = "org.linuxprobe.crud.mapper.UniversalMapper.insert";
	private static final String deleteStatement = "org.linuxprobe.crud.mapper.UniversalMapper.delete";
	private static final String updateStatement = "org.linuxprobe.crud.mapper.UniversalMapper.update";

	private final SqlSession sqlSession;

	public UniversalCrudDefaultSqlSessionExtend(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T insert(T record) {
		InsertSqlGenerator insertSqlGenerator = UniversalCrudContent.getInsertSqlGenerator();
		sqlSession.insert(insertStatement, insertSqlGenerator.toInsertSql(record));
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(record.getClass());
		if (entityInfo.getPrimaryKey().getPrimaryKey().value().equals(Strategy.NATIVE)) {
			Object idValue = FieldUtil.getFieldValue(record, entityInfo.getPrimaryKey().getField());
			if (idValue == null) {
				Map<String, Object> idMap = sqlSession.selectOne(selectOneStatement, "SELECT LAST_INSERT_ID() as id");
				try {
					Number id = (Number) idMap.get("id");
					if (entityInfo.getPrimaryKey().getField().getType().equals(Long.class)) {
						id = id.longValue();
					} else if (entityInfo.getPrimaryKey().getField().getType().equals(Integer.class)) {
						id = id.intValue();
					} else if (entityInfo.getPrimaryKey().getField().getType().equals(Short.class)) {
						id = id.shortValue();
					}
					FieldUtil.setField(record, entityInfo.getPrimaryKey().getField(), id);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new IllegalArgumentException(e);
				}
			}
		}
		if (FieldUtil.isProxyClass(record.getClass())) {
			return record;
		} else {
			ModelCglib modelCglib = new ModelCglib(this);
			Object proxyRecord = modelCglib.getInstance(record.getClass());
			modelCglib.copy(record);
			return (T) proxyRecord;
		}
	}

	@Override
	public <T> List<T> batchInsert(Collection<T> records) {
		if (records == null || records.isEmpty()) {
			return null;
		} else {
			List<T> result = new LinkedList<>();
			for (T record : records) {
				result.add(this.insert(record));
			}
			return result;
		}
	}

	@Override
	public int deleteByPrimaryKey(Serializable id, Class<?> entityType) {
		DeleteSqlGenerator deleteSqlGenerator = UniversalCrudContent.getDeleteSqlGenerator();
		return sqlSession.delete(deleteStatement, deleteSqlGenerator.toDeleteSqlByPrimaryKey(id, entityType));
	}

	@Override
	public int batchDeleteByPrimaryKey(Collection<Serializable> ids, Class<?> entityType) {
		DeleteSqlGenerator deleteSqlGenerator = UniversalCrudContent.getDeleteSqlGenerator();
		return sqlSession.delete(deleteStatement, deleteSqlGenerator.toBatchDeleteSqlByPrimaryKey(ids, entityType));
	}

	@Override
	public int delete(Object record) {
		DeleteSqlGenerator deleteSqlGenerator = UniversalCrudContent.getDeleteSqlGenerator();
		return sqlSession.delete(deleteStatement, deleteSqlGenerator.toDeleteSql(record));
	}

	@Override
	public int batchDelete(Collection<?> records) {
		DeleteSqlGenerator deleteSqlGenerator = UniversalCrudContent.getDeleteSqlGenerator();
		return sqlSession.delete(deleteStatement, deleteSqlGenerator.toBatchDeleteSql(records));
	}

	@Override
	public <T> List<T> universalSelect(BaseQuery param) {
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>) UniversalCrudContent.getQueryInfo(param.getClass()).getQueryEntityCalss();
		String sql = UniversalCrudContent.getSelectSqlGenerator().toSelectSql(param);
		List<Map<String, Object>> mapperResults = sqlSession.selectList(selectStatement, sql);
		List<T> records = new LinkedList<>();
		for (Map<String, Object> mapperResult : mapperResults) {
			ModelCglib modelCglib = new ModelCglib(this);
			T model = modelCglib.getInstance(type);
			Set<String> columns = mapperResult.keySet();
			for (String column : columns) {
				try {
					EntityUtils.setField(model, column, mapperResult.get(column));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
			modelCglib.clearMark();
			records.add(model);
		}
		return records;
	}

	@Override
	public long selectCount(BaseQuery param) {
		SelectSqlGenerator selectSqlGenerator = UniversalCrudContent.getSelectSqlGenerator();
		return sqlSession.selectOne(selectCountStatement, selectSqlGenerator.toSelectCountSql(param));
	}

	@Override
	public List<Map<String, Object>> selectBySql(String sql) {
		List<Map<String, Object>> reslut = sqlSession.selectList(selectStatement, sql);
		return reslut;
	}

	@Override
	public Map<String, Object> selectOneBySql(String sql) {
		Map<String, Object> mapResult = this.sqlSession.selectOne(selectOneStatement, sql);
		return mapResult;
	}

	@Override
	public <T> List<T> selectBySql(String sql, Class<T> type) {
		List<Map<String, Object>> mapResult = this.selectBySql(sql);
		List<T> records = new LinkedList<>();
		for (Map<String, Object> mapperResult : mapResult) {
			ModelCglib modelCglib = new ModelCglib(this);
			T model = modelCglib.getInstance(type);
			Set<String> columns = mapperResult.keySet();
			for (String column : columns) {
				try {
					EntityUtils.setField(model, column, mapperResult.get(column));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
			modelCglib.clearMark();
			records.add(model);
		}
		return records;
	}

	@Override
	public <T> T selectOneBySql(String sql, Class<T> type) {
		Map<String, Object> mapResult = this.sqlSession.selectOne(selectOneStatement, sql);
		if (mapResult == null) {
			return null;
		}
		ModelCglib modelCglib = new ModelCglib(this);
		T model = modelCglib.getInstance(type);
		Set<String> columns = mapResult.keySet();
		for (String column : columns) {
			try {
				EntityUtils.setField(model, column, mapResult.get(column));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
		modelCglib.clearMark();
		return model;
	}

	@Override
	public <T> T selectByPrimaryKey(Serializable id, Class<T> type) {
		String sql = UniversalCrudContent.getSelectSqlGenerator().toSelectSql(id, type);
		return this.selectOneBySql(sql, type);
	}

	@Override
	public <T> List<T> selectByColumn(String column, Serializable columnValue, Class<T> type) {
		return this.selectBySql(UniversalCrudContent.getSelectSqlGenerator().toSelectSql(column, columnValue, type),
				type);
	}

	@Override
	public <T> List<T> selectByField(String fieldName, Serializable fieldValue, Class<T> type) {
		return this.selectBySql(
				UniversalCrudContent.getSelectSqlGenerator().toSelectSqlByFieldName(fieldName, fieldValue, type), type);
	}

	@Override
	public <T> T selectOneByColumn(String column, Serializable columnValue, Class<T> type) {
		List<T> datas = this.selectByColumn(column, columnValue, type);
		if (datas != null && !datas.isEmpty()) {
			return datas.get(0);
		} else {
			return null;
		}

	}

	@Override
	public <T> T selectOneByField(String fieldName, Serializable fieldValue, Class<T> type) {
		List<T> datas = this.selectByField(fieldName, fieldValue, type);
		if (datas != null && !datas.isEmpty()) {
			return datas.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T globalUpdate(T record) {
		sqlSession.update(updateStatement, UniversalCrudContent.getUpdateSqlGenerator().toGlobalUpdateSql(record));
		if (!FieldUtil.isProxyClass(record.getClass())) {
			ModelCglib modelCglib = new ModelCglib(this);
			Object proxyRecord = modelCglib.getInstance(record.getClass());
			modelCglib.copy(record);
			return (T) proxyRecord;
		}
		return record;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T localUpdate(T record) {
		sqlSession.update(updateStatement, UniversalCrudContent.getUpdateSqlGenerator().toLocalUpdateSql(record));
		if (!FieldUtil.isProxyClass(record.getClass())) {
			ModelCglib modelCglib = new ModelCglib(this);
			Object proxyRecord = modelCglib.getInstance(record.getClass());
			modelCglib.copy(record);
			return (T) proxyRecord;
		}
		return record;
	}
}
