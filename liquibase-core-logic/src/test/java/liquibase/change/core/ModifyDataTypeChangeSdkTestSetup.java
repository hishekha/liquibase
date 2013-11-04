package liquibase.change.core;

import junit.framework.Assert;
import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.diff.DiffResult;
import liquibase.diff.ObjectDifferences;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.Column;
import liquibase.structure.core.Table;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class ModifyDataTypeChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws Exception {
        ModifyDataTypeChange change = (ModifyDataTypeChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        String dataType = change.getNewDataType();
        if (dataType.startsWith("int")) {
            dataType = "varchar(20)";
        } else {
            dataType = "int";
        }
        createTableChange.addColumn(new ColumnConfig().setName(change.getColumnName()).setType(dataType));
        createTableChange.addColumn(new ColumnConfig().setName("other_column").setType("varchar(10)"));

        return new Change[] {createTableChange };

    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        ModifyDataTypeChange change = (ModifyDataTypeChange) getChange();

        ObjectDifferences colDiff = diffResult.getChangedObject(new Column(Table.class, change.getCatalogName(), change.getSchemaName(), change.getTableName(), change.getColumnName()), getDatabase());
        assertNotNull(colDiff);
        assertNotNull(colDiff.getDifference("type"));
//todo        assertEquals(change.getNewDataType(), colDiff.getDifference("type").getComparedValue().toString());
    }
}
