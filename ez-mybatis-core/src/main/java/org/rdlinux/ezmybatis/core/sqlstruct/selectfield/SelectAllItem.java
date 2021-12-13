package org.rdlinux.ezmybatis.core.sqlstruct.selectfield;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlstruct.Table;
import org.rdlinux.ezmybatis.core.utils.Assert;

public class SelectAllItem implements SelectItem {
    private Table table;

    public SelectAllItem(Table table) {
        Assert.notNull(table, "table can not be null");
        this.table = table;
    }

    public Table getTable() {
        return this.table;
    }

    public void setTable(Table table) {
        Assert.notNull(table, "table can not be null");
        this.table = table;
    }

    @Override
    public String toSqlPart(Configuration configuration) {
        return " " + this.table.getAlias() + ".* ";
    }
}
