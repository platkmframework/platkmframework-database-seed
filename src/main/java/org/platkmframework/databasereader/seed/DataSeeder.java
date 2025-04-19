/**
 * ****************************************************************************
 *  Copyright(c) 2025 the original author Eduardo Iglesias Taylor.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  	 https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Contributors:
 *  	Eduardo Iglesias Taylor - initial API and implementation
 * *****************************************************************************
 */
package org.platkmframework.databasereader.seed;

import org.apache.commons.lang3.StringUtils;
import org.platkmframework.databasereader.model.Column;
import org.platkmframework.databasereader.model.Table;
import org.platkmframework.databasereader.seed.config.TableTreeNode;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public class DataSeeder {

    private final Random random = new Random();
    private final Map<String, Set<Object>> insertedPKs = new HashMap<>();

    public DataSeeder() {
    }

    public void seed(Connection con, List<Table> tables, TableTreeNode node) throws Exception {
        insertRandomData(con, tables, node);
    }

    private void insertRandomData(Connection con, List<Table> tables, TableTreeNode node) throws SQLException {
        String fullTableName = StringUtils.isNotBlank(node.getSchema())?node.getSchema() + ".":node.getTable();
        int count = node.getCount();

        Table table = tables.stream().filter(t->t.getName().equals(node.getTable())).findFirst().orElse(null);

        for (int i = 0; i < count; i++) {
            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();
            List<Object> parameters = new ArrayList<>();

            for (Column colDef : table.getColumn()) {
                /**String[] parts = colDef.split(" ", 2);
                String col = parts[0];
                String colType = parts[1];*/

                columns.append(colDef.getName()).append(",");
                Object value = null;

                if (colDef.isPk()) {
                    int val = random.nextInt(1_000_000);
                    value = val;
                    insertedPKs.computeIfAbsent(node.getTable(), k -> new HashSet<>()).add(val);
                } else if (colDef.isFk()) {
                    Set<Object> refs = insertedPKs.getOrDefault(colDef.getFktablename(), new HashSet<>());
                    if (!refs.isEmpty()) {
                        value = refs.stream().findAny().orElse(null);
                    } else {
                        System.err.println("⚠️ No hay valores en " + colDef.getFktablename() + " para la FK de " + colDef.getName());
                    }
                } else {
                    value = generateValueForSQLType(colDef.getJavaSqlType());
                }
                values.append("?,");
                parameters.add(value);
            }

            String sql = String.format("INSERT INTO %s (%s) VALUES (%s)",
                    fullTableName,
                    columns.substring(0, columns.length() - 1),
                    values.substring(0, values.length() - 1));

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                for (int j = 0; j < parameters.size(); j++) {
                    stmt.setObject(j + 1, parameters.get(j));
                }
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error inserting into " + fullTableName + ": " + e.getMessage());
            }
        }

    }

    private Object generateValueForSQLType(int sqlType) {
        switch (sqlType) {
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.LONGVARCHAR:
                return randomString();
            case Types.INTEGER:
            case Types.SMALLINT:
            case Types.TINYINT:
                return random.nextInt(1000);
            case Types.BIGINT:
                return random.nextLong();
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                return random.nextDouble() * 100;
            case Types.DECIMAL:
            case Types.NUMERIC:
                return Math.round(random.nextDouble() * 10000.0) / 100.0;
            case Types.BOOLEAN:
            case Types.BIT:
                return random.nextBoolean();
            case Types.DATE:
                return java.sql.Date.valueOf(LocalDate.now().minusDays(random.nextInt(1000)));
            case Types.TIMESTAMP:
            case Types.TIMESTAMP_WITH_TIMEZONE:
                return new Timestamp(System.currentTimeMillis() - random.nextInt(1_000_000_000));
            default:
                return null;
        }
    }

    private String randomString() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
