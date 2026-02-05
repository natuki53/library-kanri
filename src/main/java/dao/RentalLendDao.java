package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Lend;

public class RentalLendDao {

    private static final String URL =
        "jdbc:mysql://localhost:3306/library-touroku?useSSL=false&serverTimezone=Asia/Tokyo";
    private static final String USER = "root";
    private static final String PASS = "";

    // ユーザー貸出上限
    public static final int MAX_LEND = 3;

    // 貸出期間
    private static final int LEND_DAYS = 14;

    /* ======================
       全貸出数（制御用）
       ====================== */
    public int countAllLend(int userId) {
        String sql = "SELECT COUNT(*) FROM lend WHERE user_id=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
            return MAX_LEND; // 安全側
        }
    }

    /* ======================
       有効貸出数（14日以内・表示用）
       ====================== */
    public int countValidLend(int userId) {
        String sql = """
            SELECT COUNT(*)
            FROM lend
            WHERE user_id=?
              AND CURDATE() <= DATE_ADD(lend_date, INTERVAL ? DAY)
        """;

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, LEND_DAYS);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /* ======================
       既に借りているか（14日以内）
       ====================== */
    public boolean isAlreadyLent(int userId, String bookName) {
        String sql = """
            SELECT COUNT(*)
            FROM lend
            WHERE user_id=?
              AND bookname=?
              AND CURDATE() <= DATE_ADD(lend_date, INTERVAL ? DAY)
        """;

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, bookName);
            ps.setInt(3, LEND_DAYS);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /* ======================
       貸出処理（完全防御）
       ====================== */
    public boolean lendBook(int userId, String userName, String bookName) {

        // 上限チェック（DB基準）
        if (countAllLend(userId) >= MAX_LEND) return false;

        String updateBook =
            "UPDATE list SET number = number - 1 WHERE book=? AND number > 0";
        String insertLend =
            "INSERT INTO lend(user_id, name, bookname, lend_date) VALUES(?,?,?,CURRENT_DATE)";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            con.setAutoCommit(false);

            try (PreparedStatement psBook = con.prepareStatement(updateBook);
                 PreparedStatement psLend = con.prepareStatement(insertLend)) {

                psBook.setString(1, bookName);
                if (psBook.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }

                psLend.setInt(1, userId);
                psLend.setString(2, userName);
                psLend.setString(3, bookName);
                psLend.executeUpdate();

                con.commit();
                return true;

            } catch (Exception e) {
                con.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ======================
       貸出中一覧（14日以内）
       ====================== */
    public List<Lend> findLendingBooksByUser(int userId) {

        List<Lend> list = new ArrayList<>();
        String sql = """
            SELECT name, bookname, lend_date
            FROM lend
            WHERE user_id=?
              AND CURDATE() <= DATE_ADD(lend_date, INTERVAL ? DAY)
            ORDER BY lend_date
        """;

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, LEND_DAYS);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Lend l = new Lend();
                l.setName(rs.getString("name"));
                l.setBookname(rs.getString("bookname"));
                l.setLendDate(rs.getDate("lend_date"));
                list.add(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}