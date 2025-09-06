import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;



public class AccountsManager {
    private Connection connection;
    private Scanner scanner;

    public  AccountsManager(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    // Adding Money
    public void Credit_money(String AccountNumber)throws SQLException{ 
        System.out.println();
        scanner.nextLine();
        System.out.println("Enter Amount : ");
        double Amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin :");
        String Pin = scanner.nextLine();

        String Query = "Select * from Accounts WHERE Account_number = ? AND Security_pin = ?";
        try {
            connection.setAutoCommit(false);

            if(AccountNumber !=null)
            {
                PreparedStatement preparedstatement = connection.prepareStatement(Query);
                preparedstatement.setString(1, AccountNumber);
                preparedstatement.setString(2, Pin);    
                ResultSet result = preparedstatement.executeQuery();
                
                if(result.next()){
                    String CreditQeuery = "Update Accounts SET Balance = Balance + ? WHERE Account_number = ?";
                    PreparedStatement preparedstatement1 = connection.prepareStatement(CreditQeuery);
                    preparedstatement1.setDouble(1, Amount);
                    preparedstatement1.setString(2, AccountNumber);

                    int affectedRow = preparedstatement1.executeUpdate();
                    if(affectedRow >0 ){
                        System.out.println("Amount credited successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                    }
                    else{
                        System.out.println("Failed to credit amount");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);
    }

    // Reducing Money
    public void Debit_money(String AccountNumber) throws SQLException{
        System.out.println();
        scanner.nextLine();
        System.out.println("Enter Amount : ");
        double Amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin :");
        String Pin = scanner.nextLine();

        String Query = "Select * from Accounts WHERE Account_number = ? AND Security_pin = ?";
        try {
            connection.setAutoCommit(false);

            if(AccountNumber !=null)
            {
                PreparedStatement preparedstatement = connection.prepareStatement(Query);
                preparedstatement.setString(1, AccountNumber);
                preparedstatement.setString(2, Pin);    
                ResultSet result = preparedstatement.executeQuery();
                
                if(result.next())
                {
                    double current_amount =result.getDouble("Balance");
                    if(Amount<=current_amount)
                    {
                        String DebitQeuery = "Update Accounts SET Balance = Balance - ? WHERE Account_number = ?";
                        PreparedStatement preparedstatement1 = connection.prepareStatement(DebitQeuery);
                        preparedstatement1.setDouble(1, Amount);
                        preparedstatement1.setString(2, AccountNumber);

                        int affectedRow = preparedstatement1.executeUpdate();
                        if(affectedRow >0 )
                        {
                            System.out.println(Amount+"Rs Amount debited successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }
                        else
                        {
                            System.out.println("Failed to debit amount");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else
                    {
                        System.out.println("Insufficient Balance !!!");
                    }
                }
                else{
                    System.out.println("Incorrect Pin ");
                }
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);

    }


    //Transfer money
    public void Transfer_money(String Sender_AccountNumber) throws SQLException{
        scanner.nextLine();
        System.out.println();
        System.out.println("Enter Receiver Account number :");
        String ReceiverAccountNumber = scanner.nextLine();
        System.out.println("Enter Amount :");
        double Amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin :");
        String Pin = scanner.nextLine();

        String Query = "Select * from Accounts WHERE Account_number = ? AND Security_pin = ?"; // To retrieve user Account
        
        try {
            connection.setAutoCommit(false);
            // to make sure all account number is validate
            if(Sender_AccountNumber != null || !Sender_AccountNumber.trim().isEmpty() && ReceiverAccountNumber != null || !ReceiverAccountNumber.trim().isEmpty()){
                PreparedStatement preparedStatement = connection.prepareStatement(Query);
                preparedStatement.setString(1, Sender_AccountNumber);
                preparedStatement.setString(2, Pin);

                ResultSet result = preparedStatement.executeQuery();
                if(result.next()){
                    double current_amount = result.getDouble("Balance");
                    if(Amount <= current_amount){
                        String DebitQuery = "Update Accounts SET Balance = Balance - ? WHERE Account_number = ?"; // To send Amount
                        String CreditQuery = "Update Accounts SET Balance = Balance + ? WHERE Account_number = ?"; //To Add Amount 
                        PreparedStatement DebitPreparedstatement = connection.prepareStatement(DebitQuery);
                        PreparedStatement CreditPreparedstatement = connection.prepareStatement(CreditQuery);

                        DebitPreparedstatement.setDouble(1, Amount);
                        DebitPreparedstatement.setString(2, Sender_AccountNumber);
                        CreditPreparedstatement.setDouble(1, Amount);
                        CreditPreparedstatement.setString(2, ReceiverAccountNumber);

                        int DebitAffectedRow = DebitPreparedstatement.executeUpdate();
                        int CreditAffectedRow = CreditPreparedstatement.executeUpdate();

                        if(DebitAffectedRow > 0 && CreditAffectedRow >0){
                            System.out.println();
                            System.out.println("Transaction Successful");
                            System.out.println(Amount+"Transferred Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }
                        else{
                            System.out.println();
                            System.out.println("Transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else{
                        System.out.println();
                        System.out.println("Insufficient Balance");
                    }
                }
                else{
                    System.out.println();
                    System.out.println("Incorrect Pin");
                }
            }
            else{
                System.out.println();
                System.out.println("Incorrect Account number");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);

    }

    // check Money
    public void Check_Balance(String AccountNumber){
        scanner.nextLine();
        System.out.println();
        System.out.println("Enter Security Pin :");
        String Pin = scanner.nextLine();

        String BalanceQuery = "Select Balance From Accounts Where Account_number = ? AND Security_pin = ?";

        try{
            PreparedStatement preparedstatement = connection.prepareStatement(BalanceQuery);
            preparedstatement.setString(1, AccountNumber);
            preparedstatement.setString(2, Pin);

            ResultSet result = preparedstatement.executeQuery();

            if (result.next()) {
                double Balance = result.getDouble("Balance");
                System.out.println("Your account Balance :"+Balance);
                
            } else {
                System.out.println("Incorrect Pin");
            }

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
