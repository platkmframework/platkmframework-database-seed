package org.platkmframework.databasereader.seed;

import org.platkmframework.databasereader.model.Table;
import org.platkmframework.databasereader.seed.config.TableTreeNode;

import java.sql.Connection;
import java.util.List;

public class ProcessSeed {

    private DataSeeder dataSeeder;

    public ProcessSeed() {
        dataSeeder = new DataSeeder();
    }

    public void process(Connection con, List<Table> tables, TableTreeNode tableTreeNode) throws Exception {

        if(tableTreeNode.getChildren().isEmpty()){
            dataSeeder.seed(con, tables, tableTreeNode);
        }else{
            for(TableTreeNode tableTreeNode1: tableTreeNode.getChildren()){
                process(con, tables, tableTreeNode1);
                dataSeeder.seed(con, tables, tableTreeNode);
            }
        }
    }
}
