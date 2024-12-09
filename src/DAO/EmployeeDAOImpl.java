package DAO;

import Model.Employee;
import Model.Poste;
import Model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAOI {

    @Override
    public void add(Employee employee) {
        String sql = "INSERT INTO Employe (nom, prenom, email, phone, salaire, role, poste) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employee.getNom());
            stmt.setString(2, employee.getPrenom());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getPhone());
            stmt.setDouble(5, employee.getSalaire());
            stmt.setString(6, employee.getRole().name());
            stmt.setString(7, employee.getPoste().name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Employe WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Employee> listAll() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employe";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getDouble("salaire"),
                        // Gestion des erreurs si "role" ou "poste" ne correspond pas
                        Role.valueOf(rs.getString("role").toUpperCase()),  // Ajoutez .toUpperCase() si nécessaire
                        Poste.valueOf(rs.getString("poste").toUpperCase()) // Ajoutez .toUpperCase() si nécessaire
                );
                employee.setId(rs.getInt("id"));
                employees.add(employee);
            }
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return employees;
    }



    public Employee findById(int id) {
        String sql = "SELECT * FROM Employe WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Employee(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getDouble("salaire"),
                        Role.valueOf(rs.getString("role").toUpperCase()),  // Convertir en majuscule
                        Poste.valueOf(rs.getString("poste").toUpperCase()) // Convertir en majuscule
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void update(Employee employee, int id) {
        String sql = "UPDATE Employe SET nom = ?, prenom = ?, email = ?, phone = ?, salaire = ?, role = ?, poste = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employee.getNom());
            stmt.setString(2, employee.getPrenom());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getPhone());
            stmt.setDouble(5, employee.getSalaire());
            stmt.setString(6, employee.getRole().name());
            stmt.setString(7, employee.getPoste().name());
            stmt.setInt(8, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}