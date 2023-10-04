# Bank Management System (BMS)

# Description:
The Bank Management System (BMS) is a Java-based application designed to manage various aspects of banking operations. The system provides functionalities for opening and removing accounts, banking transactions such as withdrawals and deposits, passbook maintenance, and a dedicated loan department for handling car, home, and education loans.

# Compile:
javac Bms.java
# Run:
java -cp "path/to/postgresql-42.6.0.jar;." Bms

# Database Setup:
Create a PostgreSQL database with the following tables:
"bank_app" for basic account information
"all_accounts" for comprehensive account details
"passbook" for transaction records
"loan" for loan details

# Banking Department:
Open a new account.
Remove an existing account.
Access help for using the Bank App.

# Customer Department:
Withdraw funds from an account.
Deposit funds into an account.
View passbook for transaction history.

# Loan Department:
Apply for car, home, or education loans.

# Database Connection:
Connection URL: jdbc:postgresql://localhost:5432/postgres
Username: your_usename
Password: your_postgresql_paasword

# Note:
Ensure the PostgreSQL JDBC driver (postgresql-42.6.0.jar) is in the classpath.
The necessary database tables should be created as specified in the comments in the code.
