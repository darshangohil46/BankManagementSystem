package BMS;

import java.sql.*;
import java.util.*;

public class Bank extends Thread {
    static Connection con;
    static Scanner sc = new Scanner(System.in);
    static String url = "jdbc:postgresql://localhost:5432/postgres";

    public static void bank() throws Exception {
        con = DriverManager.getConnection(url, "postgres", "462005");
        boolean b = true;
        while (b) {
            System.out.println("**************************************");
            System.out
                    .println("1. Open Bank Application\n2. Open Account In Bank\n3. Remove Account From Bank\n0. Exit");
            System.out.println("**************************************");
            System.out.print("Enter Choice: ");
            String choice = sc.nextLine();
            String c = choice.trim();
            if (c.equals("1")) {
                bank_app();
            } else if (c.equals("2")) {
                create_account();
            } else if (c.equalsIgnoreCase("3")) {
                delete_account();
            } else if (c.equals("0")) {
                b = false;
            } else {
                System.out.println("Enter valid number...");
            }
        }
    }

    static String name;
    static String phoneNum;
    static String accountNum;
    static String pin;
    static long balance = 0;

    private static void bank_app() throws Exception {
        boolean b = true;
        while (b) {
            System.out.println("Are you new for this Application?\nFor Register Enter(Yes)\nFor Login Enter(No)");
            System.out.println("For Exit Enter(Exit)");
            String input = sc.next();
            // if for registration in application
            if (input.equalsIgnoreCase("yes")) {
                registration();
            }
            // else if for login in application
            else if (input.equalsIgnoreCase("no")) {
                login_app();
                // b = false;
            } else if (input.equalsIgnoreCase("Exit")) {
                b = false;
            } else {
                System.out.println("Enter (Yes/No/Exit)");
            }
            b = false;
        }
    }

    private static boolean setPin() throws Exception {
        int i = 0;
        boolean b = true;
        while (b) {
            System.out.print("Create Your PIN(4 Digits): ");
            pin = sc.next();
            sc.nextLine();
            if (check_any_num(4, pin)) {
                String sql = "select pin from bank_app";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    String pin_from_table = rs.getString("pin");
                    System.out.println(pin_from_table);
                    if (pin_from_table == pin) {
                        i = 1;
                        break;
                    }
                }
                if (i == 1) {
                    System.out.println("Your PIN is already used by Someone.");
                    System.out.println("Try Again");
                    setPin();
                } else {
                    System.out.println("Your PIN " + pin + " saved successfully.");
                    b = false;
                }
            }
        }
        return true;
    }

    // this method for check phone nuber, account number and pin
    private static boolean check_any_num(int length, String num) {
        boolean check_num = true;
        while (check_num) {
            try {
                if (num.length() == length) {
                    Long.parseLong(num);
                    return true;
                } else {
                    System.out.println("Enter valid number.");
                    return false;
                }
            } catch (Exception e) {
                System.out.println("Enter valid number.");
                return false;
            }
        }
        return true;
    }

    private static void registration() throws Exception {
        System.out.print("Enter Your Name: ");
        String name = sc.next();
        sc.nextLine();
        boolean b1 = true;
        while (b1) {
            System.out.print("Enter Phone Number: ");
            phoneNum = sc.next();
            sc.nextLine();
            if (check_any_num(10, phoneNum)) {
                b1 = false;
            }
        }
        // check account number
        boolean b2 = true;
        while (b2) {
            System.out.print("Enter Account Number(16 Digits): ");
            accountNum = sc.next();
            sc.nextLine();
            if (check_any_num(16, accountNum)) {
                b2 = false;
            }
        }
        // check PIN
        boolean b3 = true;
        while (b3) {
            setPin();
            b3 = false;
        }
        int i = 0;
        if (!b1 && !b2 && !b3) {
            // data insertion in tabel bank_app
            String type = "";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select account_num, type from all_accounts");
            while (rs.next()) {
                String table_accountNum = rs.getString("account_num");
                type = rs.getString("type");
                if (table_accountNum.equalsIgnoreCase(accountNum)) {
                    i = 1;
                }
            }
            // if user have account in bank then user will elligible for restration
            if (i == 1) {
                String sql_insertion = "insert into bank_app(name, account_num, phone_num, pin, balance, type) values(?,?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(sql_insertion);
                ps.setString(1, name);
                ps.setString(2, accountNum);
                ps.setString(3, phoneNum);
                ps.setString(4, pin);
                ps.setLong(5, Customer.balance);
                ps.setString(6, type);
                int x = ps.executeUpdate();
                if (x > 0) {
                    System.out.println("Registration is Complete.");
                } else {
                    System.out.println("Registration is Fail.");
                }
            } else if (i == 0) {
                System.out.println("Registration is Fail.");
                System.out.println("Your account is not in this bank");
                System.out.println("So, First You Open Account in this Bank.");
                System.out.println();
            }
        }
    }

    private static void login_app() throws Exception {
        System.out.print("Enter Your PIN: ");
        String insert_pin = sc.next();
        sc.nextLine();

        String sql = " select * from bank_app;";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        boolean b = false;
        while (rs.next()) {
            String table_name = rs.getString("name");
            String table_accountNum = rs.getString("account_num");
            String table_phoneNum = rs.getString("phone_num");
            String table_pin = rs.getString("pin");
            long table_balance = rs.getLong("balance");

            if (insert_pin.equals(table_pin)) {
                name = table_name;
                accountNum = table_accountNum;
                phoneNum = table_phoneNum;
                pin = table_pin;
                balance = table_balance;
                System.out.println("Login Done.");
                b = true;
            }
        }
        if (!b) {
            System.out.println("First, You check you are is resister on this app or not?");
            System.out.println("Login Fail.");
        }
        onlineMoneTransfer();
    }

    private static void onlineMoneTransfer() throws Exception {
        while (true) {
            System.out.print("Do You Want To Transfer Money Online? (Yes/NO) ");
            String input_yes_no = sc.nextLine();
            if (input_yes_no.equalsIgnoreCase("yes")) {
                if (balance > 0) {
                    System.out.print("Enter Money You Want To Transfer: ");
                    long transferMoney = sc.nextLong();
                    if (transferMoney < balance) {
                        Customer.withdrawMoney();
                    } else {
                        System.out.println("Insufficient Money In Account");
                        System.out.println("Your Balance Is: " + balance);
                    }
                } else {
                    System.out.println("Your Balance: " + balance);
                }
            } else if (input_yes_no.equalsIgnoreCase("no")) {
                break;
            } else {
                System.out.println("Enter (Yes/No)");
            }
        }
    }

    static String name1;
    static String phoneNum1;
    static String accountNum1;
    static String address1;
    static String other_bank = "";

    protected static void create_account() throws Exception {
        System.out.print("Enter Your Name: ");
        name1 = sc.next();
        boolean b1 = true;
        while (b1) {
            System.out.print("Enter Phone Number: ");
            phoneNum1 = sc.next();
            sc.nextLine();
            if (check_any_num(10, phoneNum1)) {
                b1 = false;
            }
        }
        System.out.print("Enter Your Address: ");
        address1 = sc.nextLine();

        System.out.println("Do you have Accounts in Other Bank? (Yes/No) ");
        String yes_no = sc.nextLine();
        if (yes_no.equalsIgnoreCase("yes")) {
            System.out.print("Enter Account Number: ");
            String acc = sc.nextLine();
            System.out.print("Enter Bank Name: ");
            String bankName = sc.nextLine();
            System.out.print("Enter Branch: ");
            String branch = sc.nextLine();
            other_bank = "Bank Name: " + bankName + "\nBranch: " + branch + "\nAcc no.: " + acc + "\n";
        } else if (yes_no.equalsIgnoreCase("No")) {
            other_bank = "No\n";
        }

        // generate account number
        long acc_num = (long) (Math.random() * 10000);
        String accountNum1 = "098765432100" + "" + acc_num;

        System.out.println("Choose Account type(Current/Saving)");
        String type = sc.nextLine();
        boolean b = true;
        while (b) {
            if (type.equalsIgnoreCase("Current")) {
                type = "Current";
                b = false;
            } else if (type.equalsIgnoreCase("Saving")) {
                type = "Saving";
                b = false;
            } else {
                System.out.println("Enter valid...");
            }
        }

        // data insertion in tabel all_accounts
        String sql_insertion = "insert into all_accounts(name, account_num, address, acc_other, phone_num, balance, type) values(?,?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql_insertion);
        ps.setString(1, name1);
        ps.setString(2, accountNum1);
        ps.setString(3, address1);
        ps.setString(4, other_bank);
        ps.setString(5, phoneNum1);
        ps.setLong(6, 0);
        ps.setString(7, type);

        int x = ps.executeUpdate();
        if (x > 0) {
            System.out.println("Your Request is in process... Wait...");
            Thread.sleep(5000);
            System.out.println("Congratulations!");
            System.out.println("Your Account Created Successfully");
            System.out.print("Your Account Number is: ");
            System.out.println(accountNum1);
        } else {
            System.out.println("Your Account Creation Fail");
        }
    }

    private static void delete_account() throws Exception {
        System.out.print("Enter Account Number: ");
        String acc_no = sc.nextLine();

        String sql1 = "delete from bank_app where account_num='" + acc_no + "'";
        Statement st1 = con.createStatement();
        boolean x = st1.execute(sql1);
        String sql2 = "delete from all_accounts where account_num='" + acc_no + "'";
        Statement st2 = con.createStatement();
        boolean y = st2.execute(sql2);
        String sql3 = "delete from passbook where acc_no='" + acc_no + "'";
        Statement st3 = con.createStatement();
        boolean z = st3.execute(sql3);
        if (!x && !y && !z) {
            System.out.println("Account Deletion Done.");
        } else {
            System.out.println("Account Not Found.");
        }
    }
}
