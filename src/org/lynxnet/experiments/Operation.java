package org.lynxnet.experiments;

//import oracle.jdbc.pool.OracleDataSource;

import java.sql.*;
import java.math.BigDecimal;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 24/8/2006
 * Time: 21:14:53
 * To change this template use File | Settings | File Templates.
 */
public class Operation {
    private Connection c;

    private void coreLogic(long empId) throws SQLException {
        try {
            System.out.println("Starting.");
            System.in.read(new byte[] {0});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        BigDecimal salary = selectSalary(empId);
        try {
            System.out.println("Read. The salary is: " + salary);
            System.in.read(new byte[] {0});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        salary = indexSalary(salary);
        try {
            System.out.println("Indexed. The salary is: " + salary);
            System.in.read(new byte[] {0});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println("Updated. The salary is: " + salary);
        updateSalary(empId, salary);
    }

    public void execute(Connection aConn, long empId)
        throws SQLException {
        c = aConn;
        c.setAutoCommit(false);
        try {
            coreLogic(empId);
            try {
                System.out.println("Commit:");
                System.in.read(new byte[] {0});
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            c.commit();
        } catch (SQLException e) {
            c.rollback();
            throw e;
        }
    }

    private BigDecimal selectSalary(long empId) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "SELECT salary FROM emp WHERE emp_id = ?");
//                "SELECT salary FROM emp WHERE emp_id = ? FOR UPDATE");
        ResultSet rs = null;
        try {
            ps.setLong(1, empId);
            rs = ps.executeQuery();
            rs.next();
            return rs.getBigDecimal("salary");
        } finally {
            if (rs != null) {
                rs.close();
            }
            ps.close();
        }
    }

    private BigDecimal indexSalary(BigDecimal salary) {
        return salary.multiply(new BigDecimal("1.1"));
    }

    private void updateSalary(long empId, BigDecimal salary)
            throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "UPDATE emp SET salary = ? WHERE emp_id = ?");
        try {
            ps.setBigDecimal(1, salary);
            ps.setLong(2, empId);
            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

    public static void main(String args[]) throws Exception {
/*
MySQL stuff
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/emp", "root", "qwerty");
//        c.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        c.setAutoCommit(false);
        Operation op = new Operation();
        op.execute(c, Long.parseLong(args[0]));
*/
//        OracleDataSource ods = new OracleDataSource();
//        ods.setUser("emp");
//        ods.setPassword("qwerty");
//        ods.setURL("jdbc:oracle:thin:@moonshine:1521:orcl");
//        Connection c = ods.getConnection();
//        c.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
//        c.setAutoCommit(false);
//        Operation op = new Operation();
//        op.execute(c, Long.parseLong(args[0]));
    }

}
