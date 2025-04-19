package org.platkmframework.databasereader.seed.config;

public class SeedConfigData {

    private String url;
    private String driver;
    private String user;
    private String password;
    private String schema;
    private String catalog;
    private TableTreeNode node;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TableTreeNode getNode() {
        return node;
    }

    public void setNode(TableTreeNode node) {
        this.node = node;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }
}
