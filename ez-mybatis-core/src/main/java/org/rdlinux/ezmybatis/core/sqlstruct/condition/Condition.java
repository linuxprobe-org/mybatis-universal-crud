package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;

/**
 * 条件
 */
public interface Condition {
    /**
     * 获取逻辑运算符号
     */
    LoginSymbol getLoginSymbol();

    String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder);

    static enum LoginSymbol {
        OR,
        AND;
    }
}
