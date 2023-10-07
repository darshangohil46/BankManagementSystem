package BMS;

import java.sql.*;
import java.util.Scanner;

public class Loan extends Thread {
    static Connection con;
    static String url = "jdbc:postgresql://localhost:5432/postgres";

    static Scanner sc = new Scanner(System.in);

    public static void loan() throws Exception {
        con = DriverManager.getConnection(url, "postgres", "462005");
        while (true) {
            System.out.println(
                    "0. Exit\n1. Car Loan (Upto 10,00,000)\n2. Home Loan (Upto 50,00,000)\n3. Education Loan (Upto 60,00,000)");
            System.out.println("4. Show Loan Details");
            String choice = sc.next();
            sc.nextLine();
            String c = choice.trim();
            if (c.equalsIgnoreCase("1")) {
                carLoan();
            } else if (c.equalsIgnoreCase("2")) {
                homeLoan();
            } else if (c.equalsIgnoreCase("3")) {
                educationLoan();
            } else if (c.equalsIgnoreCase("0")) {
                break;
            } else if (c.equalsIgnoreCase("4")) {
                show_loan_details();
                break;
            } else {
                System.out.println("Enter Valid Choice...");
            }
        }
    }

    static String name = "";
    static String phone = "";

    private static void show_loan_details() throws Exception {
        System.out.print("Enter Loan ID: ");
        long loan_id = sc.nextLong();
        String sql = "select * from loan;";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            long loan_id_from_table = rs.getLong("loan_id");
            if (loan_id == loan_id_from_table) {
                System.out.println("Your Loan Details");
                System.out.println(rs.getString("type"));
                System.out.println("Loan ID: " + loan_id);
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Phone Number: " + rs.getString("phone"));
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("Loan Amount: " + rs.getLong("amount"));
                System.out.println("Bank Details\n" + rs.getString("bank_details"));
                System.out.println("EMI: " + rs.getString("emi"));
                return;
            }
        }
        System.out.println("Your Loan ID Not Found In This Bank.");
    }

    private static boolean check_other_loan() throws Exception {
        System.out.print("Enter Name: ");
        name = sc.nextLine();
        System.out.print("Enter Your Phone Number: ");
        phone = sc.nextLine();

        int loan_applieded = 0;
        String sql = "select * from loan;";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String name_from_table = rs.getString("name");
            String phone_from_table = rs.getString("phone");
            long loan_id = rs.getLong("loan_id");
            if (name_from_table.equalsIgnoreCase(name) && phone_from_table.equals(phone)) {
                System.out.println("Your Loan is already in progress");
                System.out.println("Your Loan ID :: " + loan_id);
                loan_applieded = 1;
                return false;
            }
        }

        if (loan_applieded == 0) {
            return true;
        }
        return true;
    }

    private static void carLoan() throws Exception {
        boolean b = check_other_loan();
        while (b) {
            System.out.println("Car Loan Rate: 7.50%");
            System.out.print("Enter Your Address: ");
            String address = sc.nextLine();
            System.out.print("Enter Your Bank Account Number: ");
            String accNum = sc.nextLine();
            System.out.print("Enter Your Bank Name: ");
            String bankName = sc.nextLine();
            System.out.print("Enter Your Bank Branch: ");
            String branch = sc.nextLine();

            System.out.print("Enter Loan Amount: ");
            long loanAmount = sc.nextLong();

            System.out.print("Enter the loan tenure in months: ");
            int months = sc.nextInt();

            double emi = EMICalculator((double) loanAmount, 7.50, months);
            System.out.println("Your EMI is: " + emi);

            System.out.print("Do You Continue For Loan? (Yes/No) ");
            String input_yes_no = sc.next();
            sc.nextLine();
            String bank_data = "Bank Name: " + bankName + "Account no.: \n" + accNum + "Branch: \n" + branch + "\n";
            if (input_yes_no.equalsIgnoreCase("yes")) {
                while (true) {
                    if (loanAmount <= 1000000 && loanAmount > 0) {
                        String sql = "insert into loan(name, phone, address, bank_details, amount, type, loan_id) values (?,?,?,?,?,?,?)";
                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setString(1, name);
                        ps.setString(2, phone);
                        ps.setString(3, address);
                        ps.setString(4, bank_data);
                        ps.setLong(5, loanAmount);
                        ps.setString(6, "Car Loan");
                        long loan_id = (long) (Math.random() * 100000000);
                        ps.setLong(7, loan_id);
                        ps.setString(8, emi + "\nMonths: " + months);
                        int x = ps.executeUpdate();
                        if (x > 0) {
                            System.out.println("Loan Process Done");
                            Thread.sleep(1000);
                            System.out.println("Your Loan ID: " + loan_id);
                            return;
                        } else {
                            System.out.println("Your Loan Process Fail");
                        }
                        break;
                    } else {
                        System.out.println("Loan Amount is not valid");
                    }
                }
            } else if (input_yes_no.equalsIgnoreCase("no")) {
                return;
            } else {
                System.out.println("Enter (Yes/No)");
            }
        }
        System.out.println("First! You Complete Your Previous Loan...");
    }

    private static void homeLoan() throws Exception {
        boolean b = check_other_loan();
        while (b) {
            System.out.println("Rate: 8.50%");
            System.out.print("Enter Your Address: ");
            String address = sc.nextLine();
            System.out.print("Enter Your Bank Account Number: ");
            String accNum = sc.nextLine();
            System.out.print("Enter Your Bank Name: ");
            String bankName = sc.nextLine();
            System.out.print("Enter Your Bank Branch: ");
            String branch = sc.nextLine();
            
            System.out.print("Enter Loan Amount: ");
            long loanAmount = sc.nextLong();

            System.out.print("Enter the loan tenure in months: ");
            int months = sc.nextInt();

            double emi = EMICalculator((double) loanAmount, 8.50, months);
            System.out.println("Your EMI is: " + emi);

            System.out.print("Do You Continue For Loan? (Yes/No) ");
            String input_yes_no = sc.next();
            sc.nextLine();
            String bank_data = "Bank Name: " + bankName + "Account no.: \n" + accNum + "Branch: \n" + branch + "\n";

            if (input_yes_no.equalsIgnoreCase("yes")) {
                System.out.println("Give The Address Of The House Where You Want To Take Home Loan.");
                String loan_address = sc.nextLine();
                while (true) {
                    if (loanAmount <= 5000000 && loanAmount > 0) {
                        String sql = "insert into loan(name, phone, address, bank_details, amount, type, loan_id) values (?,?,?,?,?,?,?)";
                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setString(1, name);
                        ps.setString(2, phone);
                        ps.setString(3, address);
                        ps.setString(4, bank_data);
                        ps.setLong(5, loanAmount);
                        ps.setString(6, "Home Loan\nAddress: " + loan_address);
                        long loan_id = (long) (Math.random() * 100000000);
                        ps.setLong(7, loan_id);
                        ps.setString(8, emi + "\nMonths: " + months);
                        int x = ps.executeUpdate();
                        if (x > 0) {
                            System.out.println("Loan Process Done");
                            Thread.sleep(1000);
                            System.out.println("Your Loan ID: " + loan_id);
                            return;
                        } else {
                            System.out.println("Your Loan Process Fail");
                        }
                        break;
                    } else {
                        System.out.println("Loan Amount is not valid");
                    }
                }
            } else if (input_yes_no.equalsIgnoreCase("no")) {
                return;
            } else {
                System.out.println("Enter (Yes/No)");
            }
        }
        System.out.println("First! You Complete Your Previous Loan...");
    }

    private static void educationLoan() throws Exception {
        boolean b = check_other_loan();
        while (b) {
            System.out.println("Eduaction Loan Rate: 9.00%");
            System.out.print("Enter Your Address: ");
            String address = sc.nextLine();
            System.out.print("Enter Your Bank Account Number: ");
            String accNum = sc.nextLine();
            System.out.print("Enter Your Bank Name: ");
            String bankName = sc.nextLine();
            System.out.print("Enter Your Bank Branch: ");
            String branch = sc.nextLine();
            
            System.out.print("Enter Loan Amount: ");
            long loanAmount = sc.nextLong();

            System.out.print("Enter the loan tenure in months: ");
            int months = sc.nextInt();

            double emi = EMICalculator((double) loanAmount, 9.00, months);
            System.out.println("Your EMI is: " + emi);

            System.out.print("Do You Continue For Loan? (Yes/No) ");
            String input_yes_no = sc.next();
            sc.nextLine();
            String bank_data = "Bank Name: " + bankName + "Account no.: \n" + accNum + "Branch: \n" + branch + "\n";

            if (input_yes_no.equalsIgnoreCase("yes")) {
                System.out.print("Education (Foreign/India)");
                String country = sc.nextLine();
                System.out.print("Enter University Name: ");
                String university = sc.nextLine();
                while (true) {
                    if (loanAmount <= 5000000 && loanAmount > 0) {
                        String sql = "insert into loan(name, phone, address, bank_details, amount, type, loan_id) values (?,?,?,?,?,?,?)";
                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setString(1, name);
                        ps.setString(2, phone);
                        ps.setString(3, address);
                        ps.setString(4, bank_data);
                        ps.setLong(5, loanAmount);
                        ps.setString(6, "Education Loan\ncCountry: " + country + "\nUniversity: " + university);
                        long loan_id = (long) (Math.random() * 100000000);
                        ps.setLong(7, loan_id);
                        ps.setString(8, emi + "\nMonths: " + months);
                        int x = ps.executeUpdate();
                        if (x > 0) {
                            System.out.println("Loan Process Done");
                            Thread.sleep(1000);
                            System.out.println("Your Loan ID: " + loan_id);
                            return;
                        } else {
                            System.out.println("Your Loan Process Fail");
                        }
                        break;
                    } else {
                        System.out.println("Loan Amount is not valid");
                    }
                }
            } else if (input_yes_no.equalsIgnoreCase("no")) {
                return;
            } else {
                System.out.println("Enter (Yes/No)");
            }
        }
        System.out.println("First! You Complete Your Previous Loan...");
    }

    private static double EMICalculator(double principal, double rate, int months) {
    double monthlyInterestRate = rate / 100 / 12;
    double emi = (principal * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, months))
            / (Math.pow(1 + monthlyInterestRate, months) - 1);
    return emi;
    }
}
