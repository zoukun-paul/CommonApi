package com.frame.kzou.config.mybatis;

import com.frame.kzou.enums.DateFormatEnum;
import com.frame.kzou.utils.DateUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/8
 * Time: 22:45
 * Description: 自定义类型转换 String - Date
 */

@MappedTypes({Date.class})
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.CHAR})
public class CustomerDataHandler extends BaseTypeHandler<Date> {

    /**
     * insert
     * @param ps
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
        String dateString = DateUtil.format(DateFormatEnum.STANDARD, parameter);
        ps.setString(i, dateString);
    }

    /**
     * query
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    @Override
    public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String val = rs.getString(columnName);
        return DateUtil.unFormat(DateFormatEnum.STANDARD, val);
    }

    /**
     * query
     * @param rs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String val = rs.getString(columnIndex);
        return DateUtil.unFormat(DateFormatEnum.STANDARD, val);
    }

    /**
     * query
     * @param cs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String val = cs.getString(columnIndex);
        return DateUtil.unFormat(DateFormatEnum.STANDARD, val);
    }

}
