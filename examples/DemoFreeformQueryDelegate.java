package com.vaadin.addon.sqlcontainer.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.vaadin.addon.sqlcontainer.RowItem;
import com.vaadin.addon.sqlcontainer.TemporaryRowId;
import com.vaadin.addon.sqlcontainer.Util;
import com.vaadin.addon.sqlcontainer.query.FreeformStatementDelegate;
import com.vaadin.addon.sqlcontainer.query.OrderBy;
import com.vaadin.addon.sqlcontainer.query.generator.StatementHelper;
import com.vaadin.addon.sqlcontainer.query.generator.filter.FilterToWhereTranslator;
import com.vaadin.data.Container.Filter;

@SuppressWarnings("serial")
public class DemoFreeformQueryDelegate implements FreeformStatementDelegate {

    private List<Filter> filters;
    private List<OrderBy> orderBys;

    @Deprecated
    public String getQueryString(int offset, int limit)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Use getQueryStatement method.");
    }

    public StatementHelper getQueryStatement(int offset, int limit)
            throws UnsupportedOperationException {
        StatementHelper sh = new StatementHelper();
        StringBuffer query = new StringBuffer("SELECT * FROM PEOPLE ");
        if (filters != null) {
            query.append(FilterToWhereTranslator.getWhereStringForFilters(
                    filters, sh));
        }
        query.append(getOrderByString());
        if (offset != 0 || limit != 0) {
            query.append(" LIMIT ").append(limit);
            query.append(" OFFSET ").append(offset);
        }
        sh.setQueryString(query.toString());
        return sh;
    }

    private String getOrderByString() {
        StringBuffer orderBuffer = new StringBuffer("");
        if (orderBys != null && !orderBys.isEmpty()) {
            orderBuffer.append(" ORDER BY ");
            OrderBy lastOrderBy = orderBys.get(orderBys.size() - 1);
            for (OrderBy orderBy : orderBys) {
                orderBuffer.append(Util.escapeSQL(orderBy.getColumn()));
                if (orderBy.isAscending()) {
                    orderBuffer.append(" ASC");
                } else {
                    orderBuffer.append(" DESC");
                }
                if (orderBy != lastOrderBy) {
                    orderBuffer.append(", ");
                }
            }
        }
        return orderBuffer.toString();
    }

    @Deprecated
    public String getCountQuery() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Use getCountStatement method.");
    }

    public StatementHelper getCountStatement()
            throws UnsupportedOperationException {
        StatementHelper sh = new StatementHelper();
        StringBuffer query = new StringBuffer("SELECT COUNT(*) FROM PEOPLE ");
        if (filters != null) {
            query.append(FilterToWhereTranslator.getWhereStringForFilters(
                    filters, sh));
        }
        sh.setQueryString(query.toString());
        return sh;
    }

    public void setFilters(List<Filter> filters)
            throws UnsupportedOperationException {
        this.filters = filters;
    }

    public void setOrderBy(List<OrderBy> orderBys)
            throws UnsupportedOperationException {
        this.orderBys = orderBys;
    }

    public int storeRow(Connection conn, RowItem row) throws SQLException {
        PreparedStatement statement = null;
        if (row.getId() instanceof TemporaryRowId) {
            statement = conn
                    .prepareStatement("INSERT INTO PEOPLE VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            setRowValues(statement, row);
        } else {
            statement = conn
                    .prepareStatement("UPDATE PEOPLE SET FIRSTNAME = ?, LASTNAME = ?, COMPANY = ?, MOBILE = ?, WORKPHONE = ?, HOMEPHONE = ?, WORKEMAIL = ?, HOMEEMAIL = ?, STREET = ?, ZIP = ?, CITY = ?, STATE = ?, COUNTRY = ? WHERE ID = ?");
            setRowValues(statement, row);
            statement
                    .setInt(14, (Integer) row.getItemProperty("ID").getValue());
        }

        int retval = statement.executeUpdate();
        statement.close();
        return retval;
    }

    private void setRowValues(PreparedStatement statement, RowItem row)
            throws SQLException {
        statement.setString(1, (String) row.getItemProperty("FIRSTNAME")
                .getValue());
        statement.setString(2, (String) row.getItemProperty("LASTNAME")
                .getValue());
        statement.setString(3, (String) row.getItemProperty("COMPANY")
                .getValue());
        statement.setString(4, (String) row.getItemProperty("MOBILE")
                .getValue());
        statement.setString(5, (String) row.getItemProperty("WORKPHONE")
                .getValue());
        statement.setString(6, (String) row.getItemProperty("HOMEPHONE")
                .getValue());
        statement.setString(7, (String) row.getItemProperty("WORKEMAIL")
                .getValue());
        statement.setString(8, (String) row.getItemProperty("HOMEEMAIL")
                .getValue());
        statement.setString(9, (String) row.getItemProperty("STREET")
                .getValue());
        statement.setString(10, (String) row.getItemProperty("ZIP").getValue());
        statement
                .setString(11, (String) row.getItemProperty("CITY").getValue());
        statement.setString(12, (String) row.getItemProperty("STATE")
                .getValue());
        statement.setString(13, (String) row.getItemProperty("COUNTRY")
                .getValue());
    }

    public boolean removeRow(Connection conn, RowItem row)
            throws UnsupportedOperationException, SQLException {
        PreparedStatement statement = conn
                .prepareStatement("DELETE FROM people WHERE ID = ?");
        statement.setInt(1, (Integer) row.getItemProperty("ID").getValue());
        int rowsChanged = statement.executeUpdate();
        statement.close();
        return rowsChanged == 1;
    }

    @Deprecated
    public String getContainsRowQueryString(Object... keys)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "Please use getContainsRowQueryStatement method.");
    }

    public StatementHelper getContainsRowQueryStatement(Object... keys)
            throws UnsupportedOperationException {
        StatementHelper sh = new StatementHelper();
        StringBuffer query = new StringBuffer(
                "SELECT * FROM people WHERE ID = ?");
        sh.addParameterValue(keys[0]);
        sh.setQueryString(query.toString());
        return sh;
    }
}
