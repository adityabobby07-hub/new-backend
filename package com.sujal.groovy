package com.sujal.expensetracker;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 * DatabaseHelper - Handles all database operations
 * Uses DatabaseConfig for clean configuration
 */
public class DatabaseHelper {

    public static Connection getConnection() throws Exception {
        Class.forName(DatabaseConfig.DRIVER);

        return DriverManager.getConnection(
                DatabaseConfig.DB_URL,
                DatabaseConfig.DB_USER,
                DatabaseConfig.DB_PASSWORD
        );
    }

    // ====================== TRANSACTION METHODS ======================

    public static void addTransaction(String type, double amount,
                                      String category, String description) {

        try (Connection con = getConnection()) {

            String sql = "INSERT INTO transactions "
                    + "(type, amount, category, description, transaction_date) "
                    + "VALUES (?, ?, ?, ?, CURDATE())";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, type);
            ps.setDouble(2, amount);
            ps.setString(3, category);
            ps.setString(4, description);

            ps.executeUpdate();

            System.out.println("✅ Transaction Added Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ====================== REMINDER METHODS ======================

    public static void addReminder(String title, double amount,
                                   String dueDate, boolean recurring) {

        try (Connection con = getConnection()) {

            String sql = "INSERT INTO reminders "
                    + "(title, amount, due_date, is_recurring) "
                    + "VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, title);
            ps.setDouble(2, amount);
            ps.setString(3, dueDate);
            ps.setBoolean(4, recurring);

            ps.executeUpdate();

            System.out.println("✅ Reminder Set Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ====================== BUDGET CHECK ======================

    public static void checkBudgetLimit(double totalExpenseThisMonth) {

        if (totalExpenseThisMonth >
                DatabaseConfig.MAX_EXPENSE_LIMIT
                        * DatabaseConfig.ALERT_THRESHOLD) {

            JOptionPane.showMessageDialog(
                    null,
                    "⚠️ BUDGET ALERT!\n\n"
                            + "You have used more than 80% of your monthly budget.\n"
                            + "Current Expense: ₹" + totalExpenseThisMonth
                            + "\n\nPlease spend carefully!",
                    "Budget Warning",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
}