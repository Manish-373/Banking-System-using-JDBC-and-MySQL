import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
public class BankingSystem{

    private  static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private  static final String user = "root";
    private  static final String password = "M@372003";     


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded");
        } catch (ClassNotFoundException e) {
            e.getMessage();
        }

        try{
            Connection connection = DriverManager.getConnection(url,user,password);
            User user = new User(connection,scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountsManager accountmanager = new AccountsManager(connection, scanner);

            String email;
            String acc_number;

            while(true){
                System.out.println("-----------------------*****************-----------------------");
                System.out.println("===================WELCOME TO BANKING SYSTEM===================");
                System.out.println("-----------------------*****************-----------------------");
                System.out.println();
                System.out.println("Select your choice :");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println();

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        user.Register();
                        break;
                    case 2:
                            email = user.Login();
                            if(email !=null )
                            {
                                System.out.println();
                                System.out.println("User Logged in Successfully");
                                 if(!accounts.Account_exist(email))
                                {
                                    System.out.println();
                                    System.out.println("1. Open Account");
                                    System.out.println("2. Exit");  
                                    if(scanner.nextInt()==1)
                                        {
                                            acc_number = accounts.createAccount(email);
                                            System.out.println("Account created successfully");
                                            System.out.println("Your account number is : "+acc_number);
                                        }
                                        else
                                        {
                                            break;
                                        }
                                }
                            acc_number= accounts.getAccountNumber(email);
                            int choice2 = 0;
                            while (choice2!= 5) 
                                {
                                    System.out.println();
                                    System.out.println("1. Debit Money");
                                    System.out.println("2. Credit Money");
                                    System.out.println("3. Transfer Money");
                                    System.out.println("4. Check Balance");
                                    System.out.println("5. Log Out");
                                    System.out.println("enter your choice :");
                                    choice2 = scanner.nextInt();
                                    switch (choice2) 
                                    {
                                        case 1:
                                            accountmanager.Debit_money(acc_number);
                                            break;
                                         case 2:
                                            accountmanager.Credit_money(acc_number);
                                            break;
                                        case 3:
                                            accountmanager.Transfer_money(acc_number);
                                            break;
                                        case 4:
                                             accountmanager.Check_Balance(acc_number);
                                             break;
                                        case 5:
                                            System.out.println("Logging Out !!!");
                                            break;
                                        default:
                                            System.out.println("Invalid Choice");
                                            break;
                                    }
                                }
                            }else{
                                System.out.println("Enter valid Email or Password"); 
                            }
                        break;
                    case 3:
                        System.out.println("Thank you for using Banking System");
                        System.out.println("Exiting System!");
                        return;
                        
                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }
}