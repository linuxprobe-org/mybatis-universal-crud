package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.LinkedList;
import java.util.List;

/**
 * 函数
 */
@Getter
public class Function implements QueryRetNeedAlias, Operand {
    /**
     * 表
     */
    private Table table;
    /**
     * 函数名称
     */
    private String funName;
    /**
     * 参数
     */
    private List<FunArg> funArgs;

    private Function() {
    }

    public static FunctionBuilder builder(Table table) {
        return new FunctionBuilder(table);
    }

    /**
     * 函数参数
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class FunArg {
        /**
         * 添加类属性或者列的时候是否添加去重关键词
         */
        private boolean distinct;
        /**
         * 表
         */
        private Table table;
        /**
         * 参数类型
         */
        private ArgType argType;
        /**
         * 参数值
         */
        private Object argValue;
    }

    /**
     * 函数构造器
     */
    public static class FunctionBuilder {
        private Function function;

        private FunctionBuilder(Table table) {
            this.function = new Function();
            this.function.table = table;
            this.function.funArgs = new LinkedList<>();
        }

        public FunctionBuilder setFunName(String name) {
            Assert.notEmpty(name, "function name can not be null");
            this.function.funName = name;
            return this;
        }

        private FunctionBuilder addColumnArg(boolean sure, boolean distinct, Table table, String column) {
            if (!sure) {
                return this;
            }
            Assert.notNull(table, "table can not be null");
            Assert.notEmpty(column, "column can not be null");
            FunArg arg = new FunArg().setArgType(ArgType.COLUMN).setTable(table).setArgValue(column)
                    .setDistinct(distinct);
            this.function.funArgs.add(arg);
            return this;
        }

        public FunctionBuilder addColumnArg(boolean sure, Table table, String column) {
            return this.addColumnArg(sure, false, table, column);
        }

        public FunctionBuilder addDistinctColumnArg(boolean sure, Table table, String column) {
            return this.addColumnArg(sure, true, table, column);
        }

        public FunctionBuilder addColumnArg(Table table, String column) {
            return this.addColumnArg(true, table, column);
        }

        public FunctionBuilder addDistinctColumnArg(Table table, String column) {
            return this.addDistinctColumnArg(true, table, column);
        }

        public FunctionBuilder addColumnArg(String column) {
            return this.addColumnArg(this.function.table, column);
        }

        public FunctionBuilder addDistinctColumnArg(String column) {
            return this.addDistinctColumnArg(this.function.table, column);
        }

        public FunctionBuilder addColumnArg(boolean sure, String column) {
            return this.addColumnArg(sure, this.function.table, column);
        }

        public FunctionBuilder addDistinctColumnArg(boolean sure, String column) {
            return this.addDistinctColumnArg(sure, this.function.table, column);
        }

        private FunctionBuilder addFieldArg(boolean sure, boolean distinct, Table table, String field) {
            if (!sure) {
                return this;
            }
            Assert.notNull(table, "table can not be null");
            Assert.notEmpty(field, "field can not be null");
            if (!(table instanceof EntityTable)) {
                throw new IllegalArgumentException("Only EntityTable is supported");
            }
            FunArg arg = new FunArg().setArgType(ArgType.FILED).setTable(table).setArgValue(field)
                    .setDistinct(distinct);
            this.function.funArgs.add(arg);
            return this;
        }

        public FunctionBuilder addFieldArg(boolean sure, Table table, String field) {
            return this.addFieldArg(sure, false, table, field);
        }

        public FunctionBuilder addDistinctFieldArg(boolean sure, Table table, String field) {
            return this.addFieldArg(sure, true, table, field);
        }

        public FunctionBuilder addFieldArg(Table table, String field) {
            return this.addFieldArg(true, table, field);
        }

        public FunctionBuilder addDistinctFieldArg(Table table, String field) {
            return this.addDistinctFieldArg(true, table, field);
        }

        public FunctionBuilder addFieldArg(String field) {
            return this.addFieldArg(this.function.table, field);
        }

        public FunctionBuilder addDistinctFieldArg(String field) {
            return this.addDistinctFieldArg(this.function.table, field);
        }

        public FunctionBuilder addFieldArg(boolean sure, String field) {
            return this.addFieldArg(sure, this.function.table, field);
        }

        public FunctionBuilder addDistinctFieldArg(boolean sure, String field) {
            return this.addDistinctFieldArg(sure, this.function.table, field);
        }

        public FunctionBuilder addFunArg(boolean sure, Function function) {
            if (!sure) {
                return this;
            }
            Assert.notNull(function, "function can not be null");
            FunArg arg = new FunArg().setArgType(ArgType.FUNC).setArgValue(function);
            this.function.funArgs.add(arg);
            return this;
        }

        public FunctionBuilder addFunArg(Function function) {
            return this.addFunArg(true, function);
        }

        public FunctionBuilder addFormulaArg(boolean sure, Formula formula) {
            if (!sure) {
                return this;
            }
            Assert.notNull(formula, "formula can not be null");
            FunArg arg = new FunArg().setArgType(ArgType.FORMULA).setArgValue(formula);
            this.function.funArgs.add(arg);
            return this;
        }


        public FunctionBuilder addFormulaArg(Formula formula) {
            return this.addFormulaArg(true, formula);
        }

        public FunctionBuilder addValueArg(boolean sure, Object argValue) {
            if (!sure) {
                return this;
            }
            FunArg arg = new FunArg().setArgType(ArgType.VALUE).setArgValue(argValue);
            this.function.funArgs.add(arg);
            return this;
        }


        public FunctionBuilder addValueArg(Object argValue) {
            return this.addValueArg(true, argValue);
        }

        public FunctionBuilder addKeywordsArg(boolean sure, String keywords) {
            if (!sure) {
                return this;
            }
            FunArg arg = new FunArg().setArgType(ArgType.KEYWORDS).setArgValue(keywords);
            this.function.funArgs.add(arg);
            return this;
        }


        public FunctionBuilder addKeywordsArg(String keywords) {
            return this.addKeywordsArg(true, keywords);
        }

        public FunctionBuilder addCaseWhenArg(boolean sure, CaseWhen caseWhen) {
            if (!sure) {
                return this;
            }
            FunArg arg = new FunArg().setArgType(ArgType.CASE_WHEN).setArgValue(caseWhen);
            this.function.funArgs.add(arg);
            return this;
        }


        public FunctionBuilder addCaseWhenArg(CaseWhen caseWhen) {
            return this.addCaseWhenArg(true, caseWhen);
        }

        public Function build() {
            return this.function;
        }
    }
}
