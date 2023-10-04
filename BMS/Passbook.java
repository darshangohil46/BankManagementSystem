package BMS;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Passbook {
    String date;
    String name;
    long with_money;
    long depo_money;
    long total_money;

    static int key = 1;
    static Connection con;
    static Scanner sc = new Scanner(System.in);
    static String url = "jdbc:postgresql://localhost:5432/postgres";

    static HashMap<Integer, Passbook> passbook = new HashMap<>();

    protected static void printPassbook() throws Exception {
        con = DriverManager.getConnection(url, "postgres", "462005");
        String sql = "select * from passbook where acc_no='" + Customer.accNumber + "' order by p_date ASC;";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        // ResultSetMetaData rsmd = rs.getMetaData();
        // int total_columns_passbook = rsmd.getColumnCount();
        // int total_columns_passbook

        System.out.println("Your Name :: " + Customer.name);
        System.out.println("Your Account Number :: " + Customer.accNumber);
        System.out.println("Your Phone Number :: " + Customer.phoneNum);
        System.out.println("Your All Transactions");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        // String p_date = null;
        // String p_name = null;
        // long p_withdraw = 0;
        // long p_deposite = 0;
        // long p_total = 0;
        System.out.printf("| %-30s | %-20s | %-10s | %-10s | %-10s |\n", "Date", "Name", "Withdraw", "Deposite",
                "Total");
        System.out.println(
                "------------------------------------------------------------------------------------------------");

        while (rs.next()) {
            String p_date = rs.getString("p_date");
            String p_name = rs.getString("name");
            long p_withdraw = rs.getLong("withdraw");
            long p_deposite = rs.getLong("deposite");
            long p_total = rs.getLong("total");
            passbook.put(key, new Passbook(p_date, p_name, p_withdraw, p_deposite, p_total));
            System.out.printf("| %-30s | %-20s | %-10d | %-10d | %-10d |\n", p_date, p_name, p_withdraw, p_deposite,
                    p_total);
            Thread.sleep(1000);
            key++;
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        while (true) {
            System.out.println("Do You Want to save passbook as File.(Yes/No)");
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("yes")) {
                passbook_saved_as_file();
                return;
            } else if (input.equalsIgnoreCase("no")) {
                return;
            } else {
                System.out.println("Enter (Yes/No)");
            }
        }
    }

    Passbook(String date, String name_in_pass, long withdraw_money, long deposite_money, long total) {
        this.date = date;
        this.name = name_in_pass;
        this.with_money = withdraw_money;
        this.depo_money = deposite_money;
        this.total_money = total;
    }

    private static void passbook_saved_as_file() throws Exception {
        File f = new File("passbook_" + Customer.name + ".txt");
        PrintWriter pw = new PrintWriter(f);
        pw.write("Your Name :: " + Customer.name + "\n");
        pw.write("Your Account Number :: " + Customer.accNumber + "\n");
        pw.write("Your Phone Number :: " + Customer.phoneNum + "\n");
        pw.write("Your All Transactions\n");

        pw.printf("| %-30s | %-20s | %-10s | %-10s | %-10s |\n", "Date", "Name", "Withdraw", "Deposite",
                "Total");
        pw.flush();

        for (Map.Entry<Integer, Passbook> passbook_detail : passbook.entrySet()) {
            String values_by_key_date = passbook_detail.getValue().date;
            String values_by_key_name = passbook_detail.getValue().name;
            long values_by_key_withMoney = passbook_detail.getValue().with_money;
            long values_by_key_depoMoney = passbook_detail.getValue().depo_money;
            long values_by_key_totalMoney = passbook_detail.getValue().total_money;

            pw.printf("| %-30s | %-20s | %-10d | %-10d | %-10d |\n", values_by_key_date, values_by_key_name,
                    values_by_key_withMoney, values_by_key_depoMoney, values_by_key_totalMoney);
            pw.flush();
        }
        pw.close();
        System.out.println("Passbook File Saved. Name Of File " + f.getName());
    }
}
