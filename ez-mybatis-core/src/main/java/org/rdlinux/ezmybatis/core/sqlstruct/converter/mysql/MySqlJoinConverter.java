package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Join;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.join.JoinType;

public class MySqlJoinConverter extends AbstractConverter<Join> implements Converter<Join> {
    private static volatile MySqlJoinConverter instance;

    protected MySqlJoinConverter() {
    }

    public static MySqlJoinConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlJoinConverter.class) {
                if (instance == null) {
                    instance = new MySqlJoinConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration, Join join,
                                        MybatisParamHolder mybatisParamHolder) {
        if (join == null) {
            return sqlBuilder;
        }
        if (!join.isSure()) {
            return sqlBuilder;
        }
        StringBuilder sonSql = new StringBuilder();
        if (join.getJoinType() != JoinType.CrossJoin) {
            sonSql = MySqlWhereConverter.conditionsToSqlPart(new StringBuilder(), configuration, mybatisParamHolder,
                    join.getOnConditions());
            if (sonSql.length() == 0) {
                return sqlBuilder;
            }
        }
        sqlBuilder.append(join.getJoinType().toSqlStruct()).append(join.getJoinTable().toSqlStruct(type, configuration,
                mybatisParamHolder));
        if (join.getJoinType() != JoinType.CrossJoin) {
            sqlBuilder.append(" ON ");
        }
        sqlBuilder.append(sonSql);
        if (join.getJoins() != null && !join.getJoins().isEmpty()) {
            for (Join sonJoin : join.getJoins()) {
                sqlBuilder.append(this.doToSqlPart(type, new StringBuilder(), configuration, sonJoin,
                        mybatisParamHolder));
            }
        }
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}