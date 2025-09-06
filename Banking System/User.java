
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class User
{
    private Connection connection;
    private Scanner scanner;

    public User(Connection connection, Scanner scanner)
    {
        this.connection = connection;
        this.scanner=scanner;
    }

    public void Register()
    {
        System.out.println();
        scanner.nextLine();
        System.out.println("Enter full Name : ");
        String Full_name = scanner.nextLine();
        System.out.println("Enter Email : ");
        String Email = scanner.nextLine();
        System.out.println("Enter Password : ");
        String Password = scanner.nextLine();

        if(User_exist(Email)){
            System.out.println("User already Exist !!!");
            System.out.println("Please Login");
            return;
        }
        String register_query  = "INSERT INTO User(Account_holder_Name,Email,Password) Values(?, ?, ?)";
        try(PreparedStatement preparedstatement = connection.prepareStatement(register_query)) 
        {
            preparedstatement.setString(1, Full_name);
            preparedstatement.setString(2, Email);
            preparedstatement.setString(3, Password);

            int affectedRow = preparedstatement.executeUpdate();

            if(affectedRow>0){
                System.out.println("User Registered Successfully");
            }
            else{
                System.out.println("Registration failed");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public String Login()
    {
        System.out.println();
        scanner.nextLine();
        System.out.println("Please Enter your email : ");
        String Email = scanner.nextLine();
        System.out.println("Enter your password : ");
        String Password =  scanner.nextLine();

        String Loginquery="Select * from User WHERE Email = ? AND Password = ?;";

        try(PreparedStatement preparedstatement = connection.prepareStatement(Loginquery)){
            preparedstatement.setString(1, Email);
            preparedstatement.setString(2, Password);

            ResultSet result = preparedstatement.executeQuery();
            if(result.next()){
                return Email;
            }
            else{
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public boolean  User_exist(String Email)
    {

        String Existquery = "Select * from User WHERE Email = ?;";
        try(PreparedStatement preparedstatement = connection.prepareStatement(Existquery))
        {
            preparedstatement.setString(1, Email);
            ResultSet result = preparedstatement.executeQuery();
            if(result.next())
            {
                return true;
            }
            else{
                return false;
            }   
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
         return false;
    }
}

