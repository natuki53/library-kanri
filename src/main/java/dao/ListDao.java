package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Book;

public class ListDao {

    private static final String URL =
        "jdbc:mysql://localhost:3306/library-touroku?useSSL=false&serverTimezone=Asia/Tokyo";
    private static final String USER = "root";
    private static final String PASS = "";

    public List<Book> findAll() {

        List<Book> list = new ArrayList<>();
        String sql = "SELECT book, number FROM list ORDER BY book";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Book b = new Book();
                b.setBook(rs.getString("book"));
                b.setNumber(rs.getInt("number"));
                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Book> findByKeyword(String keyword) {

        List<Book> list = new ArrayList<>();
        String sql = """
            SELECT book, number
            FROM list
            WHERE book LIKE ?
            ORDER BY book
        """;

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Book b = new Book();
                b.setBook(rs.getString("book"));
                b.setNumber(rs.getInt("number"));
                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}