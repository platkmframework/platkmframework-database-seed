package org.platkmframework.databasereader.seed;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.platkmframework.databasereader.core.DatabaseReader;
import org.platkmframework.databasereader.model.Table;
import org.platkmframework.databasereader.seed.config.SeedConfigData;
import org.platkmframework.databasereader.seed.config.TableTreeNode;

import java.io.File;
import java.sql.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        SeedConfigData seedConfigData = mapper.readValue(new File("config.json"), SeedConfigData.class);

        try {
            Class.forName(seedConfigData.getDriver());
            Connection con = DriverManager.getConnection(seedConfigData.getUrl(),
                    seedConfigData.getUser(),
                    seedConfigData.getPassword());

            DatabaseReader databaseReader = new DatabaseReader(con, null);
            List<Table> tables = databaseReader.readTables(seedConfigData.getCatalog(), seedConfigData.getSchema(), "%",  new String[] {"TABLE", "VIEW"});

            ProcessSeed processSeed = new ProcessSeed();
            processSeed.process(con, tables, seedConfigData.getNode());

        } catch (Exception ex) {
             throw new Exception(ex);
        }
        /**
        String url = "jdbc:postgresql://localhost:5432/seed_db"; //args[0];
        String user = "postgres"; //args[1];
        String password = "IobFn1FAVTEbFBaumTgpgw"; //args[2];
        String[] argtables = new String[]{"persona:10", "multa:5", "vehiculo:5"};
        // Parámetros de inserción
        Map<String, Integer> insertTables = new HashMap<>();
        //for (int i = 3; i < args.length; i++) {
        for (int i = 0; i < argtables.length; i++) {
            String[] parts = argtables[i].split(":");
            if (parts.length != 2) {
                System.err.println("Formato incorrecto para argumento: " + argtables[i]);
                System.exit(1);
            }
            insertTables.put(parts[0], Integer.parseInt(parts[1]));
        }

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Conectado a la base de datos.");
            DatabaseSchemaTreeBuilder builder = new DatabaseSchemaTreeBuilder();
            TableTreeNode root = builder.buildTree(conn);

            // Validar dependencias
            Set<String> missingDependencies = new HashSet<>();
            Map<String, TableTreeNode> tableMap = new HashMap<>();
            collectNodes(root, tableMap);

            for (String table : insertTables.keySet()) {
                TableTreeNode node = tableMap.get(table);
                if (node == null) {
                    System.err.println("Tabla no encontrada: " + table);
                    System.exit(1);
                }
                for (String colDef : node.getColumns()) {
                    if (colDef.contains("FOREIGN KEY")) {
                        String refTable = colDef.split("->")[1].split("\\(")[0].trim();
                        if (!insertTables.containsKey(refTable)) {
                            missingDependencies.add(refTable);
                        }
                    }
                }
            }

            if (!missingDependencies.isEmpty()) {
                System.err.println("Faltan tablas dependientes (llaves foráneas): " + missingDependencies);
                System.exit(1);
            }

            // Crear subárbol con tablas a insertar
            TableTreeNode insertRoot = new TableTreeNode("db", "custom", "root");
            for (String table : insertTables.keySet()) {
                TableTreeNode sub = tableMap.get(table);
                insertRoot.addChild(sub);
            }

            // Insertar
            DataSeeder seeder = new DataSeeder(conn);
            for (String table : insertTables.keySet()) {
                seeder.seed(tableMap.get(table), insertTables.get(table));
            }

            System.out.println("Inserción finalizada.");
        }
         */
    }

    private static void collectNodes(TableTreeNode node, Map<String, TableTreeNode> map) {
        map.put(node.getTable(), node);
        for (TableTreeNode child : node.getChildren()) {
            collectNodes(child, map);
        }
    }
}
