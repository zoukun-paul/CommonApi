package com.frame.kzou.config.mybatis;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/9
 * Time: 12:21
 * Description:
 *          boolean     false --> 0
 *                      true  --> !0
 */
public class OpenShareFieldHandler extends BaseTypeHandler<Boolean> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
        int val = parameter == null ? 0 : (parameter ? 1 : 0);
        ps.setInt(i, val);
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int val = rs.getInt(columnName);
        return val != 0;
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int val = rs.getInt(columnIndex);
        return val != 0;
    }

    @Override
    public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int val = cs.getInt(columnIndex);
        return val != 0;
    }
}
