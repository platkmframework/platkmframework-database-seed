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

import org.platkmframework.databasereader.model.Table;
import org.platkmframework.databasereader.seed.config.TableTreeNode;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public class ProcessSeed {

    private final DataSeeder dataSeeder;

    public ProcessSeed() {
        dataSeeder = new DataSeeder();
    }

    public void processByLevel(Connection con, List<Table> tables, List<TableTreeNode> tableTreeNodes) throws Exception {
        Queue<TableTreeNode> currentLevel = new LinkedList<>(tableTreeNodes);

        while (!currentLevel.isEmpty()) {
            Queue<TableTreeNode> nextLevel = new LinkedList<>();

            for (TableTreeNode tableTreeNode : currentLevel) {
                dataSeeder.seed(con, tables, tableTreeNode);
                nextLevel.addAll(tableTreeNode.getChildren());
            }
            currentLevel = nextLevel;
        }
    }

}
