package org.platkmframework.databasereader.seed.config;

import java.util.ArrayList;
import java.util.List;

public class TableTreeNode {

    private String table;
    private int count;
    private String schema;
    private List<TableTreeNode> children = new ArrayList<>();

    public TableTreeNode() {}

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<TableTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TableTreeNode> children) {
        this.children = children;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
