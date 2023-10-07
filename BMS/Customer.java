package BMS;

import java.util.*;
import java.sql.*;

public class Customer {
    static Connection con;
    static Scanner sc = new Scanner(System.in);
    static long balance = 0;
    static String accNumber;
    static String phoneNum;
    static String name;
    static String url = "jdbc:postgresql://localhost:5432/postgres";
    static long depo_money = 0;
    static String name_passbook = "";
    static long with_money = 0;

    public static void custumer() throws Exception {
        con = DriverManager.getConnection(url, "postgres", "462005");

        System.out.print("Enter Your Account number: ");
        String accountNumber = sc.nextLine();
        String sql = " select * from all_accounts;";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        boolean b = false;
        int acc_exist = 0;
        while (rs.next()) {
            String table_name = rs.getString("name");
            String table_accountNum = rs.getString("account_num");
            String table_phoneNum = rs.getString("phone_num");
            long table_balance = rs.getLong("balance");

            if (accountNumber.equals(table_accountNum)) {
                name = table_name;
                accNumber = table_accountNum;
                phoneNum = table_phoneNum;
                balance = table_balance;
                acc_exist = 1;
                b = true;
            }
        }
        if (!b) {
            System.out.println("Your Account Not Exist In This Bank");
        }
        if (acc_exist != 0) {
            boolean x = true;
            while (x) {
                System.out.println("**************************************");
                System.out.println("1. Withdraw Money\n2. Deposit Money\n3. Print Passbook\n0. Exit");
                System.out.println("**************************************");
                System.out.print("Enter Choice: ");
                String choice = sc.next();
                sc.nextLine();
                String c = choice.trim();
                if (c.equals("1")) {
                    withdrawMoney();
                    x = false;
                } else if (c.equals("2")) {
                    depositeMoney();
                    x = false;
                } else if (c.equals("3")) {
                    Passbook.printPassbook();
                    x = false;
                } else if (c.equals("0")) {
                    x = false;
                } else {
                    System.out.println("Enter valid number...");
                }
            }
        }
    }

    private static void depositeMoney() throws Exception {
        String sql = "select balance from bank_app where account_num='" + accNumber + "';";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
            balance = rs.getLong("balance");
        }

        System.out.print("Enter Amount You Want To Deposite: ");
        depo_money = sc.nextLong();
        System.out.print("Enter Your Name: ");
        sc.nextLine();
        name_passbook = sc.nextLine();

        if (depo_money <= 0) {
            System.out.println("Enter Valid Rupees...");
            return;
        }

        String sql1 = "update bank_app set balance = ? where account_num=?";
        String sql2 = "update all_accounts set balance = ? where account_num=?";
        PreparedStatement ps1 = con.prepareStatement(sql1);
        PreparedStatement ps2 = con.prepareStatement(sql2);
        // total = balance + depo_money;
        ps1.setLong(1, balance + depo_money);
        ps1.setString(2, accNumber);
        ps2.setLong(1, balance + depo_money);
        ps2.setString(2, accNumber);
        int p1 = ps1.executeUpdate();
        int p2 = ps2.executeUpdate();
        if (p1 > 0 && p2 > 0) {
            System.out.println("Money Deposite Successfully");
        } else {
            System.out.println("Your Money Not Deposite");
        }
        System.out.println("Now, Your Balance is: " + (balance + depo_money));
        store_passbook();
        balance = 0;
        depo_money = 0;
    }

    protected static void store_passbook() throws Exception {
        // store in passbook
        String sql_passbook = "insert into passbook(p_date, name, withdraw, deposite, total, acc_no) values(?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql_passbook);
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT NOW();");
        Timestamp date_time = null;
        if (rs.next()) {
            date_time = rs.getTimestamp(1);
        }
        System.out.println("Money Transaction Done at " + date_time);
        ps.setTimestamp(1, date_time);
        ps.setString(2, name_passbook);
        ps.setLong(3, with_money);
        ps.setLong(4, depo_money);
        ps.setLong(5, balance + depo_money - with_money);
        ps.setString(6, accNumber);
        ps.executeUpdate();
    }

    protected static void withdrawMoney() throws Exception {
        String sql = "select balance from bank_app where account_num='" + accNumber + "';";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
            balance = rs.getLong("balance");
        }

        if (balance <= 0) {
            System.out.println("Insufficient Balance");
        } else {
            System.out.print("Enter Amount You Want To Withdraw: ");
            with_money = sc.nextLong();
            System.out.print("Enter Your Name: ");
            sc.nextLine();
            name_passbook = sc.nextLine();

            if (with_money <= 0) {
                System.out.println("Invalid Rupees...");
                return;
            }

            String sql1 = "update bank_app set balance = ? where account_num=?";
            String sql2 = "update all_accounts set balance = ? where account_num=?";
            PreparedStatement ps1 = con.prepareStatement(sql1);
            PreparedStatement ps2 = con.prepareStatement(sql2);
            // total = balance - with_money;
            ps1.setLong(1, balance - with_money);
            ps1.setString(2, accNumber);
            ps2.setLong(1, balance - with_money);
            ps2.setString(2, accNumber);
            int p1 = ps1.executeUpdate();
            int p2 = ps2.executeUpdate();
            if (p1 > 0 && p2 > 0) {
                System.out.println("Money Deposite Successfully");
            } else {
                System.out.println("Your Money Not Deposite");
            }
            System.out.println("Now, Your Balance is: " + (balance - with_money));
            store_passbook();
            balance = 0;
            with_money = 0;
        }
    }
}
