import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Accounts {
    
    private Connection connection;
    private Scanner scanner;


    public Accounts(Connection connection,Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public String createAccount(String Email){
        if(!Account_exist(Email)){
            System.out.println();
            scanner.nextLine();
            System.out.println("Enter your name : ");
            String HoldersName  = scanner.nextLine();
            System.out.println("Enter you Initial Amount: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Set your 4 digit  Security Pin :");
            String SecurityPin = scanner.nextLine();


            String Createquery = "INSERT INTO Accounts (Account_number,Account_holder_Name,Email,Balance,Security_pin) values(?, ?, ?, ?, ?)";
            try(PreparedStatement preparedstatement = connection.prepareStatement(Createquery)) {
                String AccountNumber = generateAccountNumber();
                preparedstatement.setString(1, AccountNumber);
                preparedstatement.setString(2, HoldersName);
                preparedstatement.setString(3, Email);
                preparedstatement.setDouble(4, balance);
                preparedstatement.setString(5, SecurityPin);

                int affectedrow = preparedstatement.executeUpdate();
                if(affectedrow > 0){
                    System.out.println("Account Created Successfully");
                    return  AccountNumber;  
                }   
                else{
                    throw new RuntimeException("Failed to create account");
                    //System.out.println("Failed to create account");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }       
        throw new RuntimeException("Account already Exist");
    }


    public String  getAccountNumber(String Email){
        String Accoungetquery = "Select Account_number From Accounts WHERE Email = ?;";
        try(PreparedStatement preparedstatement = connection.prepareStatement(Accoungetquery)){
            preparedstatement.setString(1, Email);
            ResultSet result = preparedstatement.executeQuery();

            if (result.next()) 
            {
                return result.getString("Account_number");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account does not exist !!!");
    }


    public boolean  Account_exist(String Email){
        String Existquery = "Select * From Accounts WHERE Email = ?;";

        try(PreparedStatement preparedstatement = connection.prepareStatement(Existquery)) {
            preparedstatement.setString(1, Email);
            ResultSet result = preparedstatement.executeQuery();

            if (result.next()) {
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    private String generateAccountNumber() {
    String prefix = "BMS";
    long startingNumber = 10000100L;

    try {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT account_number FROM Accounts ORDER BY account_number DESC LIMIT 1"); // to get last generated account number

        if (resultSet.next()) {
            String lastAccount = resultSet.getString("account_number");

            // Extract numeric part (remove prefix)
            long lastNumber = Long.parseLong(lastAccount.replace(prefix, "")); // to extract only long digit number
            return prefix + (lastNumber + 1);
        } else {
            // First account creation
            return prefix + startingNumber;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return prefix + startingNumber;
}

}
