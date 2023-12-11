package com.example.testjavafx;

import java.sql.*;
import java.util.Scanner;

public class Queries {

    public void test() {
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/onlinestore";
        String user = "root";
        String password = "root";
        try {
            conn = DriverManager.getConnection(url, user, password);
            Statement stmt  = conn.createStatement();
            String sql = "select * from product";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                int productId = rs.getInt("productId");
                String name = rs.getString("name");
                String brand = rs.getString("brand");
                String description = rs.getString("description");
                System.out.println("productId: " + productId + "\t\tname: " + name + "\t\t\tbrand: " + brand + "\t\t\tdescription: " + description);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch(SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


    public static void func1() {
        //executa un query fara parametri
        String sql = "select * from product";
        try (Connection conn = MySQLJDBCUtil.getConnection()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int productId = rs.getInt("productId");
                String name = rs.getString("name");
                String brand = rs.getString("brand");
                String description = rs.getString("description");
                System.out.println("productId: " + productId + "\t\tname: " + name + "\t\t\tbrand: " + brand + "\t\t\tdescription: " + description);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public static void func2() {
        //cauta in bd utilizatorii cu numele de familie citit de la tastatura - query cu parametru - prepared statement
        Scanner sc = new Scanner(System.in);
        System.out.println("Introdu un nume de familie: \n");
        String lastNameToBeSearched = sc.nextLine();
        String sql = "select * from user u where u.lastName = ?";
        try (Connection conn = MySQLJDBCUtil.getConnection()) {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, lastNameToBeSearched);
            ResultSet rs = pst.executeQuery();
            boolean found = false;
            while (rs.next()) {
                int userId = rs.getInt("userId");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                if (lastName.equals(lastNameToBeSearched)) {
                    System.out.println("userId: " + userId + "\tfirstName: " + firstName + "\tlastName: " + lastName);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("Nu exista niciun user cu numele de familie " + lastNameToBeSearched);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public static void func3() {
        //adauga date citite de la tastatura in bd
        String sql = "insert into user (userId, firstName, lastName, email, password, address, phoneNumber, userType)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?)";
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Introdu un id: ");
        int userId = sc.nextInt();
        sc.nextLine(); // Consume the newline character left by nextInt()
        System.out.println("2. Introdu un prenume: ");
        String firstName = sc.nextLine();
        System.out.println("3. Introdu un nume de familie: ");
        String lastName = sc.nextLine();
        System.out.println("4. Introdu un email: ");
        String email = sc.nextLine();
        System.out.println("5. Introdu o parola: ");
        String password = sc.nextLine();
        System.out.println("6. Introdu o adresa: ");
        String address = sc.nextLine();
        System.out.println("7. Introdu un numar de telefon: ");
        String phoneNumber = sc.nextLine();
        System.out.println("8. Introdu tipul de utilizator: ");
        String userType = sc.nextLine();
        try (Connection conn = MySQLJDBCUtil.getConnection()) {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, userId);
            pst.setString(2, firstName);
            pst.setString(3, lastName);
            pst.setString(4, email);
            pst.setString(5, password);
            pst.setString(6, address);
            pst.setString(7, phoneNumber);
            pst.setString(8, userType);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully.");
            } else {
                System.out.println("Failed to insert data.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void func4() {
        //modifica adresa unui user din bd citit de la tastatura
        String sql = "update user u set u.address = ? where u.firstName = ? and u.lastName = ?";
        Scanner sc = new Scanner(System.in);
        System.out.println("---Schimbare adresa--- \nIntrodu prenumele user-ului: ");
        String firstName = sc.nextLine();
        System.out.println("Introdu numele de familie al user-ului: ");
        String lastName = sc.nextLine();
        System.out.println("Introdu noua adresa: ");
        String newAddress = sc.nextLine();

        try (Connection conn = MySQLJDBCUtil.getConnection()) {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, newAddress);
            pst.setString(2, firstName);
            pst.setString(3, lastName);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Adresa actualizata cu succes.");
            } else {
                System.out.println("Nu s-a putut actualiza adresa. Verifica informatiile introduse.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void func5() {
        //sterge din db un user citit de la tastatura
        String sql = "delete from user u where u.firstName = ? and u.lastName = ?";
        Scanner sc = new Scanner(System.in);
        System.out.println("---Stergere user---\nIntrodu prenumele user-ului: ");
        String firstName = sc.nextLine();
        System.out.println("Introdu numele de familie al user-ului: ");
        String lastName = sc.nextLine();
        try(Connection conn = MySQLJDBCUtil.getConnection()) {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, firstName);
            pst.setString(2, lastName);
            int rowsAffected = pst.executeUpdate();
            if(rowsAffected > 0) {
                System.out.println("User sters cu succes.");
            } else {
                System.out.println("Nu s-a putut sterge user-ul. Verifica informatiile introduse.");
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) {
        func1();
//        func2();
//        func3();
//        func4();
//        func5();
        //added a comment on this line

    }
}
