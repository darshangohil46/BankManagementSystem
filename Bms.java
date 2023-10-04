import java.sql.*;
import java.util.*;
import BMS.*;

// java -cp "D:\dvdrental_database\postgresql-42.6.0.jar;" Bms

// create table bank_app(name varchar(20), account_num varchar(20), phone_num varchar(20), pin varchar(20)
// balance bigint, id serial, type varchar(20));

// create table all_accounts (id SERIAL Primary key, name varchar(20), account_num varchar(20), 
// address varchar(200), acc_other varchar(1000), phone_Num varchar(20), balance bigint, type VARCHAR(10));

// create table passbook(p_date timestamp, name varchar(20), withdraw bigint, deposite bigint,total bigint,
// acc_no varchar(20));

// create table loan(id SERIAL PRIMARY KEY, name VARCHAR(30), phone VARCHAR(30),
// address VARCHAR(30), bank_details VARCHAR(300), amount BIGINT, loan_id BIGINT);

public class Bms {
    static Connection con;
    static Scanner sc = new Scanner(System.in);
    static String url = "jdbc:postgresql://localhost:5432/postgres";

    public static void main(String[] args) throws Exception {
        con = DriverManager.getConnection(url, "postgres", "462005");
        boolean b = true;
        while (b) {
            System.out.println("*********************************");
            System.out.println("1. Banking(Open Acc/Remove Acc/Help For Bank App)");
            System.out.println("2. Customer Department(Withdraw/Deposite/Passbook)");
            System.out.println("3. Loan Department(Car/Home/Education Loan)\n0. Exit");
            System.out.println("*********************************");
            System.out.print("Enter Choice: ");
            String choice = sc.nextLine();
            String c = choice.trim();
            if (c.equals("1")) {
                Bank.bank();
            } else if (c.equals("2")) {
                Customer.custumer();
            } else if (c.equals("3")) {
                Loan.loan();
            } else if (c.equals("0")) {
                b = false;
            } else {
                System.out.println("Enter valid number...");
            }
        }
    }
}
