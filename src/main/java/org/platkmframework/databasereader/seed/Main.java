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


import com.fasterxml.jackson.databind.ObjectMapper;
import org.platkmframework.databasereader.core.DatabaseReader;
import org.platkmframework.databasereader.model.Table;
import org.platkmframework.databasereader.seed.config.SeedConfigData;

import java.io.File;
import java.sql.*;
import java.util.*;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
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
            processSeed.processByLevel(con, tables, seedConfigData.getTableTree());

        } catch (Exception ex) {
             throw new Exception(ex);
        }
    }


}
