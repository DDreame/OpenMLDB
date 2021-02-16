package com._4paradigm.fesql_auto_test.checker;

import com._4paradigm.fesql.sqlcase.model.ExpectDesc;
import com._4paradigm.fesql.sqlcase.model.Table;
import com._4paradigm.fesql_auto_test.entity.FesqlResult;
import com._4paradigm.fesql_auto_test.util.FesqlUtil;
import com._4paradigm.sql.Schema;
import org.testng.Assert;

import java.sql.ResultSetMetaData;
import java.util.List;

/**
 * @author zhaowei
 * @date 2020/6/16 3:14 PM
 */
// @Slf4j
public class ColumnsChecker extends BaseChecker {

    public ColumnsChecker(ExpectDesc expect, FesqlResult fesqlResult) {
        super(expect, fesqlResult);
    }

    @Override
    public void check() throws Exception {
        log.info("column name check");
        List<String> expectColumns = expect.getColumns();
        if (expectColumns == null || expectColumns.size() == 0) {
            return;
        }
        Schema schema = fesqlResult.getResultSchema();
        if (schema != null) {
            Assert.assertEquals(expectColumns.size(), schema.GetColumnCnt(), "Illegal schema size");
            for (int i = 0; i < expectColumns.size(); i++) {
                Assert.assertEquals(schema.GetColumnName(i), Table.getColumnName(expectColumns.get(i)));
                Assert.assertEquals(schema.GetColumnType(i),
                        FesqlUtil.getColumnType(Table.getColumnType(expectColumns.get(i))));
            }
        } else {
            ResultSetMetaData metaData = fesqlResult.getMetaData();
            Assert.assertEquals(expectColumns.size(), metaData.getColumnCount(), "Illegal schema size");
            for (int i = 0; i < expectColumns.size(); i++) {
                Assert.assertEquals(metaData.getColumnName(i + 1), Table.getColumnName(expectColumns.get(i)));
                Assert.assertEquals(metaData.getColumnType(i + 1),
                        FesqlUtil.getSQLType(Table.getColumnType(expectColumns.get(i))));
            }
        }

    }
}
