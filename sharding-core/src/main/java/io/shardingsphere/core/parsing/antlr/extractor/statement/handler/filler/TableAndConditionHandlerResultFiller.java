/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.core.parsing.antlr.extractor.statement.handler.filler;

import io.shardingsphere.core.metadata.table.ShardingTableMetaData;
import io.shardingsphere.core.parsing.antlr.extractor.statement.handler.result.TableAndConditionExtractResult;
import io.shardingsphere.core.parsing.antlr.extractor.statement.handler.result.TableExtractResult;
import io.shardingsphere.core.parsing.parser.context.condition.OrCondition;
import io.shardingsphere.core.parsing.parser.sql.SQLStatement;
import io.shardingsphere.core.rule.ShardingRule;

/**
 * Table and condition handler result filler.
 * 
 * @author duhongjun
 */
public class TableAndConditionHandlerResultFiller extends AbstractHandlerResultFiller {
    
    public TableAndConditionHandlerResultFiller() {
        super(TableAndConditionExtractResult.class);
    }
    
    @Override
    protected void fillSQLStatement(Object extractResult, SQLStatement statement, ShardingRule shardingRule, ShardingTableMetaData shardingTableMetaData) {
        TableAndConditionExtractResult tableAndConditionResult = (TableAndConditionExtractResult)extractResult;
        for(TableExtractResult each : tableAndConditionResult.getTableExtractResults()) {
            HandlerResultFiller filler = HandlerResultFillerRegistry.getFiller(each);
            if(null != filler) {
                filler.fill(each, statement, shardingRule, shardingTableMetaData);
            }
        }
        OrCondition orCondition = tableAndConditionResult.getConditions().optimize();
        statement.getConditions().getOrCondition().getAndConditions().addAll(orCondition.getAndConditions());
    }
}
