package com.dimdol.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

class DatabaseDescription {

    private ConsoleData cd;

    DatabaseDescription() {
        cd = new ConsoleData();
        cd.addEntry("NAME");
        cd.addEntry("TYPE");
    }

    void desc() {
        try (Connection con = SqlRuntime.getInstance().getConnectionFactory().getConnection()) {
            DatabaseMetaData dmd = con.getMetaData();
            addValue("Database Product Name", dmd.getDatabaseProductName());
            addValue("Database Product Version", dmd.getDatabaseProductVersion());
            addValue("Database Product Major Version", dmd.getDatabaseMajorVersion());
            addValue("Database Product Minor Version", dmd.getDatabaseMinorVersion());
            addValue("Driver Name", dmd.getDriverName());
            addValue("Driver Version", dmd.getDriverVersion());
            addValue("Driver Major Version", dmd.getDriverMajorVersion());
            addValue("Driver Minor Version", dmd.getDriverMinorVersion());
            addValue("JDBC Major Version", dmd.getJDBCMajorVersion());
            addValue("JDBC Minor Version", dmd.getJDBCMinorVersion());
            int defaultTransactionIsolation = dmd.getDefaultTransactionIsolation();
            if (defaultTransactionIsolation == Connection.TRANSACTION_NONE) {
                addValue("Default Transaction Isolation", "TRANSACTION_NONE");
            } else if (defaultTransactionIsolation == Connection.TRANSACTION_READ_COMMITTED) {
                addValue("Default Transaction Isolation", "TRANSACTION_READ_COMMITTED");
            } else if (defaultTransactionIsolation == Connection.TRANSACTION_READ_UNCOMMITTED) {
                addValue("Default Transaction Isolation", "TRANSACTION_READ_UNCOMMITTED");
            } else if (defaultTransactionIsolation == Connection.TRANSACTION_REPEATABLE_READ) {
                addValue("Default Transaction Isolation", "TRANSACTION_REPEATABLE_READ");
            } else if (defaultTransactionIsolation == Connection.TRANSACTION_SERIALIZABLE) {
                addValue("Default Transaction Isolation", "TRANSACTION_SERIALIZABLE");
            }

            addValue("Catalog Separator", dmd.getCatalogSeparator());
            addValue("Catalog Term", dmd.getCatalogTerm());
            addValue("Extra Name Characters", dmd.getExtraNameCharacters());
            addValue("Identifier Quote String", dmd.getIdentifierQuoteString());

            addValue("Max Binary Literal Length", dmd.getMaxBinaryLiteralLength());
            addValue("Max Catalog Name Length", dmd.getMaxCatalogNameLength());
            addValue("Max Char Literal Length", dmd.getMaxCharLiteralLength());
            addValue("Max Column Name Length", dmd.getMaxColumnNameLength());
            addValue("Max Columns In GroupBy", dmd.getMaxColumnsInGroupBy());
            addValue("Max Columns In Index", dmd.getMaxColumnsInIndex());
            addValue("Max Columns In OrderBy", dmd.getMaxColumnsInOrderBy());
            addValue("Max Columns In Select", dmd.getMaxColumnsInSelect());
            addValue("Max Columns In Table", dmd.getMaxColumnsInTable());
            addValue("Max Connections", dmd.getMaxConnections());
            addValue("Max Max Cursor Name Length", dmd.getMaxCursorNameLength());
            addValue("Max Index Length", dmd.getMaxIndexLength());
            addValue("Max Logical Lob Size", dmd.getMaxLogicalLobSize());
            addValue("Max ProcedureName Length", dmd.getMaxProcedureNameLength());
            addValue("Max Row Size", dmd.getMaxRowSize());
            addValue("Max Schema Name Length", dmd.getMaxSchemaNameLength());
            addValue("Max Statement Length", dmd.getMaxStatementLength());
            addValue("Max Statements", dmd.getMaxStatements());
            addValue("Max TableNameLength", dmd.getMaxTableNameLength());
            addValue("Max Tables In Select", dmd.getMaxTablesInSelect());
            addValue("Max User Name Length", dmd.getMaxUserNameLength());
            addValue("Numeric Functions", dmd.getNumericFunctions());
            addValue("Procedure Term", dmd.getProcedureTerm());

            int resultSetHoldability = dmd.getResultSetHoldability();
            if (resultSetHoldability == ResultSet.CLOSE_CURSORS_AT_COMMIT) {
                addValue("Result Set Holdability", "CLOSE_CURSORS_AT_COMMIT");
            } else if (resultSetHoldability == ResultSet.HOLD_CURSORS_OVER_COMMIT) {
                addValue("Result Set Holdability", "HOLD_CURSORS_OVER_COMMIT");
            }

            addValue("Row Id Lifetime", dmd.getRowIdLifetime());
            addValue("Schema Term", dmd.getSchemaTerm());
            addValue("Search String Escape", dmd.getSearchStringEscape());
            addValue("SQL Keywords", dmd.getSQLKeywords());
            addValue("SQL State Type", dmd.getSQLStateType());
            addValue("String Functions", dmd.getStringFunctions());
            addValue("System Functions", dmd.getSystemFunctions());
            addValue("Time Date Functions", dmd.getTimeDateFunctions());
            addValue("URL", dmd.getURL());
            addValue("User Name", dmd.getUserName());

            addValue("Inserts Are Detected(TYPE_FORWARD_ONLY)", dmd.insertsAreDetected(ResultSet.TYPE_FORWARD_ONLY));
            addValue("Inserts Are Detected(TYPE_SCROLL_INSENSITIVE)", dmd.insertsAreDetected(ResultSet.TYPE_SCROLL_INSENSITIVE));
            addValue("Inserts Are Detected(TYPE_SCROLL_SENSITIVE)", dmd.insertsAreDetected(ResultSet.TYPE_SCROLL_SENSITIVE));
            addValue("Updates Are Detected(TYPE_FORWARD_ONLY)", dmd.updatesAreDetected(ResultSet.TYPE_FORWARD_ONLY));
            addValue("Updates Are Detected(TYPE_SCROLL_INSENSITIVE)", dmd.updatesAreDetected(ResultSet.TYPE_SCROLL_INSENSITIVE));
            addValue("Updates Are Detected(TYPE_SCROLL_SENSITIVE)", dmd.updatesAreDetected(ResultSet.TYPE_SCROLL_SENSITIVE));

            addValue("Is Catalog At Start", dmd.isCatalogAtStart());
            addValue("Is Read Only", dmd.isReadOnly());
            addValue("Locators Update Copy", dmd.locatorsUpdateCopy());
            addValue("Null Plus Non Null Is Null", dmd.nullPlusNonNullIsNull());
            addValue("Nulls Are Sorted At Start", dmd.nullsAreSortedAtStart());
            addValue("Nulls Are Sorted At End", dmd.nullsAreSortedAtEnd());
            addValue("Nulls Are Sorted High", dmd.nullsAreSortedHigh());
            addValue("Nulls Are Sorted Low", dmd.nullsAreSortedLow());
            addValue("Stores Lower Case Identifiers", dmd.storesLowerCaseIdentifiers());
            addValue("Stores Lower Case Quoted Identifiers", dmd.storesLowerCaseQuotedIdentifiers());
            addValue("Stores Mixed Case Identifiers", dmd.storesMixedCaseIdentifiers());
            addValue("Stores Mixed Case Quoted Identifiers", dmd.storesMixedCaseQuotedIdentifiers());
            addValue("Stores Upper Case Identifiers", dmd.storesUpperCaseIdentifiers());
            addValue("Stores Upper Case Quoted Identifiers", dmd.storesUpperCaseQuotedIdentifiers());
            addValue("Supports Alter Table With Add Column", dmd.supportsAlterTableWithAddColumn());
            addValue("Supports Alter Table With Drop Column", dmd.supportsAlterTableWithDropColumn());
            addValue("Supports ANSI92 Entry Level SQL", dmd.supportsANSI92EntryLevelSQL());
            addValue("Supports ANSI92 Full SQL", dmd.supportsANSI92FullSQL());
            addValue("Supports ANSI92 Intermediate SQL", dmd.supportsANSI92IntermediateSQL());
            addValue("Supports Batch Updates", dmd.supportsBatchUpdates());
            addValue("Supports Catalogs In Data Manipulation", dmd.supportsCatalogsInDataManipulation());
            addValue("Supports Catalogs In Index Definitions", dmd.supportsCatalogsInIndexDefinitions());
            addValue("Supports Catalogs In Privilege Definitions", dmd.supportsCatalogsInPrivilegeDefinitions());
            addValue("Supports Catalogs In Procedure Calls", dmd.supportsCatalogsInProcedureCalls());
            addValue("Supports Catalogs In Table Definitions", dmd.supportsCatalogsInTableDefinitions());

            addValue("Supports Column Aliasing", dmd.supportsColumnAliasing());
            addValue("Supports Convert", dmd.supportsConvert());
            addValue("Supports Core SQL Grammar", dmd.supportsCoreSQLGrammar());
            addValue("Supports Correlated Subqueries", dmd.supportsCorrelatedSubqueries());
            addValue("Supports Data Definition And Data Manipulation Transactions", dmd.supportsDataDefinitionAndDataManipulationTransactions());

            addValue("Supports Data Manipulation TransactionsOnly", dmd.supportsDataManipulationTransactionsOnly());
            addValue("Supports Different Table Correlation Names", dmd.supportsDifferentTableCorrelationNames());
            addValue("Supports Expressions In Order By", dmd.supportsExpressionsInOrderBy());
            addValue("Supports Extended SQL Grammar", dmd.supportsExtendedSQLGrammar());
            addValue("Supports Full OuterJoins", dmd.supportsFullOuterJoins());
            addValue("Supports Get Generated Keys", dmd.supportsGetGeneratedKeys());
            addValue("Supports Group By", dmd.supportsGroupBy());
            addValue("Supports Group By Beyond Select", dmd.supportsGroupByBeyondSelect());
            addValue("Supports Group By Unrelated", dmd.supportsGroupByUnrelated());
            addValue("Supports Integrity Enhancement Facility", dmd.supportsIntegrityEnhancementFacility());
            addValue("Supports Like Escape Clause", dmd.supportsLikeEscapeClause());
            addValue("Supports Limited Outer Joins", dmd.supportsLimitedOuterJoins());
            addValue("Supports Minimum SQL Grammar", dmd.supportsMinimumSQLGrammar());
            addValue("Supports Mixed Case Identifiers", dmd.supportsMixedCaseIdentifiers());

            addValue("Supports Mixed Case Quoted Identifiers", dmd.supportsMixedCaseQuotedIdentifiers());
            addValue("Supports Multiple Open Results", dmd.supportsMultipleOpenResults());
            addValue("Supports Multiple Result Sets", dmd.supportsMultipleResultSets());
            addValue("Supports Multiple Transactions", dmd.supportsMultipleTransactions());
            addValue("Supports Named Parameters", dmd.supportsNamedParameters());
            addValue("Supports Non Nullable Columns", dmd.supportsNonNullableColumns());
            addValue("Supports Open Cursors Across Commit", dmd.supportsOpenCursorsAcrossCommit());
            addValue("Supports Open Cursors Across Rollback", dmd.supportsOpenCursorsAcrossRollback());
            addValue("Supports Open Statements Across Commit", dmd.supportsOpenStatementsAcrossCommit());
            addValue("Supports Open Statements Across Rollback", dmd.supportsOpenStatementsAcrossRollback());
            addValue("Supports Order By Unrelated", dmd.supportsOrderByUnrelated());
            addValue("Supports Outer Joins", dmd.supportsOuterJoins());
            addValue("Supports Positioned Delete", dmd.supportsPositionedDelete());
            addValue("Supports Positioned Update", dmd.supportsPositionedUpdate());
            addValue("Supports Ref Cursors", dmd.supportsRefCursors());
            addValue("Supports Savepoints", dmd.supportsSavepoints());

            addValue("Supports Schemas In Data Manipulation", dmd.supportsSchemasInDataManipulation());
            addValue("Supports Schemas In Index Definitions", dmd.supportsSchemasInIndexDefinitions());
            addValue("Supports Schemas In Privilege Definitions", dmd.supportsSchemasInPrivilegeDefinitions());
            addValue("Supports Schemas In Procedure Calls", dmd.supportsSchemasInProcedureCalls());
            addValue("Supports Schemas In Table Definitions", dmd.supportsSchemasInTableDefinitions());
            addValue("Supports Select For Update", dmd.supportsSelectForUpdate());
            addValue("Supports Statement Pooling", dmd.supportsStatementPooling());
            addValue("Supports Stored Functions Using Call Syntax", dmd.supportsStoredFunctionsUsingCallSyntax());

            addValue("Supports Stored Procedures", dmd.supportsStoredProcedures());
            addValue("Supports Subqueries In Comparisons", dmd.supportsSubqueriesInComparisons());
            addValue("Supports Subqueries In Exists", dmd.supportsSubqueriesInExists());

            addValue("Supports Subqueries In Ins", dmd.supportsSubqueriesInIns());
            addValue("Supports Subqueries In Quantifieds", dmd.supportsSubqueriesInQuantifieds());
            addValue("Supports Table Correlation Names", dmd.supportsTableCorrelationNames());
            addValue("Supports Transactions", dmd.supportsTransactions());
            addValue("Supports Union", dmd.supportsUnion());
            addValue("Supports Union All", dmd.supportsUnionAll());
            addValue("Uses Local File Per Table", dmd.usesLocalFilePerTable());
            addValue("Uses Local Files", dmd.usesLocalFiles());

            addValue("Supports Transaction Isolation Level(TRANSACTION_NONE)", dmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_NONE));
            addValue("Supports Transaction Isolation Level(TRANSACTION_READ_COMMITTED)", dmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED));
            addValue("Supports Transaction Isolation Level(TRANSACTION_READ_UNCOMMITTED)", dmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_UNCOMMITTED));
            addValue("Supports Transaction Isolation Level(TRANSACTION_REPEATABLE_READ)", dmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_REPEATABLE_READ));
            addValue("Supports Transaction Isolation Level(TRANSACTION_SERIALIZABLE)", dmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE));

            addValue("Supports Result Set Type(CONCUR_READ_ONLY)", dmd.supportsResultSetType(ResultSet.CONCUR_READ_ONLY));
            addValue("Supports Result Set Type(CONCUR_UPDATABLE)", dmd.supportsResultSetType(ResultSet.CONCUR_UPDATABLE));
            addValue("Supports Result Set Type(FETCH_FORWARD)", dmd.supportsResultSetType(ResultSet.FETCH_FORWARD));
            addValue("Supports Result Set Type(FETCH_REVERSE)", dmd.supportsResultSetType(ResultSet.FETCH_REVERSE));
            addValue("Supports Result Set Type(FETCH_UNKNOWN)", dmd.supportsResultSetType(ResultSet.FETCH_UNKNOWN));
            addValue("Supports Result Set Type(TYPE_FORWARD_ONLY)", dmd.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY));
            addValue("Supports Result Set Type(TYPE_SCROLL_INSENSITIVE)", dmd.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE));
            addValue("Supports Result Set Type(TYPE_SCROLL_SENSITIVE)", dmd.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE));

            addValue("Supports Result Set Holdability(CLOSE_CURSORS_AT_COMMIT)", dmd.supportsResultSetHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT));
            addValue("Supports Result Set Holdability(HOLD_CURSORS_OVER_COMMIT)", dmd.supportsResultSetHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT));

            addValue("Supports Result Set Concurrency(TYPE_FORWARD_ONLY, CONCUR_READ_ONLY)", dmd.supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY));
            addValue("Supports Result Set Concurrency(TYPE_FORWARD_ONLY, CONCUR_UPDATABLE)", dmd.supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE));

            addValue("Supports Result Set Concurrency(TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY)", dmd.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY));
            addValue("Supports Result Set Concurrency(TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE)", dmd.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE));

            addValue("Supports Result Set Concurrency(TYPE_SCROLL_SENSITIVE, CONCUR_READ_ONLY)", dmd.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY));
            addValue("Supports Result Set Concurrency(TYPE_SCROLL_SENSITIVE, CONCUR_UPDATABLE)", dmd.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE));

            addValue("Own Inserts Are Visible(TYPE_FORWARD_ONLY)", dmd.ownInsertsAreVisible(ResultSet.TYPE_FORWARD_ONLY));
            addValue("Own Inserts Are Visible(TYPE_SCROLL_INSENSITIVE)", dmd.ownInsertsAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE));
            addValue("Own Inserts Are Visible(TYPE_SCROLL_SENSITIVE)", dmd.ownInsertsAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE));

            addValue("Own Updates Are Visible(TYPE_FORWARD_ONLY)", dmd.ownUpdatesAreVisible(ResultSet.TYPE_FORWARD_ONLY));
            addValue("Own Updates Are Visible(TYPE_SCROLL_INSENSITIVE)", dmd.ownUpdatesAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE));
            addValue("Own Updates Are Visible(TYPE_SCROLL_SENSITIVE)", dmd.ownUpdatesAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE));

            addValue("Own Deletes Are Visible(TYPE_FORWARD_ONLY)", dmd.ownDeletesAreVisible(ResultSet.TYPE_FORWARD_ONLY));
            addValue("Own Deletes Are Visible(TYPE_SCROLL_INSENSITIVE)", dmd.ownDeletesAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE));
            addValue("Own Deletes Are Visible(TYPE_SCROLL_SENSITIVE)", dmd.ownDeletesAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE));

            addValue("Others Inserts Are Visible(TYPE_FORWARD_ONLY)", dmd.othersInsertsAreVisible(ResultSet.TYPE_FORWARD_ONLY));
            addValue("Others Inserts Are Visible(TYPE_SCROLL_INSENSITIVE)", dmd.othersInsertsAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE));
            addValue("Others Inserts Are Visible(TYPE_SCROLL_SENSITIVE)", dmd.othersInsertsAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE));

            addValue("Others Updates Are Visible(TYPE_FORWARD_ONLY)", dmd.othersUpdatesAreVisible(ResultSet.TYPE_FORWARD_ONLY));
            addValue("Others Updates Are Visible(TYPE_SCROLL_INSENSITIVE)", dmd.othersUpdatesAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE));
            addValue("Others Updates Are Visible(TYPE_SCROLL_SENSITIVE)", dmd.othersUpdatesAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE));

            addValue("Others Deletes Are Visible(TYPE_FORWARD_ONLY)", dmd.othersDeletesAreVisible(ResultSet.TYPE_FORWARD_ONLY));
            addValue("Others Deletes Are Visible(TYPE_SCROLL_INSENSITIVE)", dmd.othersDeletesAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE));
            addValue("Others Deletes Are Visible(TYPE_SCROLL_SENSITIVE)", dmd.othersDeletesAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE));

            cd.log();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addValue(String name, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put("NAME", name);
        map.put("TYPE", value);
        cd.addValue(map);
    }

}
