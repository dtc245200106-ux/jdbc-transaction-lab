package com.codegym.dao;

import com.codegym.model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;

public class UserDAO implements IUserDAO {

    // ===== CÁC HẰNG SỐ SQL (giữ nguyên từ bài trước) =====
    private static final String SQL_INSERT = "INSERT INTO Employee (name, salary, created_Date) VALUES (?,?,?)";
    private static final String SQL_UPDATE = "UPDATE Employee SET salary=? WHERE name=?";
    private static final String SQL_TABLE_CREATE = "CREATE TABLE Employee"
            + "("
            + " id INT(11) AUTO_INCREMENT,"
            + " name VARCHAR(100) NOT NULL,"
            + " salary DECIMAL(15, 2) NOT NULL,"
            + " created_Date TIMESTAMP,"
            + " PRIMARY KEY (id)"
            + ")";
    private static final String SQL_TABLE_DROP = "DROP TABLE IF EXISTS Employee";

    // ... Các phương thức cũ giữ nguyên ...

    @Override
    public void insertUpdateUseTransaction() {
        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             PreparedStatement psInsert = conn.prepareStatement(SQL_INSERT);
             PreparedStatement psUpdate = conn.prepareStatement(SQL_UPDATE)) {

            statement.execute(SQL_TABLE_DROP);
            statement.execute(SQL_TABLE_CREATE);

            // ==========================================
            // 1. BẮT ĐẦU TRANSACTION BLOCK
            // Tắt chế độ lưu tự động (mặc định là true)
            // ==========================================
            conn.setAutoCommit(false);

            // 2. Chạy danh sách lệnh Insert
            psInsert.setString(1, "Quynh");
            psInsert.setBigDecimal(2, new BigDecimal(10));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();

            psInsert.setString(1, "Ngan");
            psInsert.setBigDecimal(2, new BigDecimal(20));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();

            // 3. Chạy lệnh Update (CỐ TÌNH TẠO LỖI để test rollback)
            psUpdate.setBigDecimal(2, new BigDecimal(999.99));
            // Đúng ra phải là: psUpdate.setBigDecimal(1, new BigDecimal(999.99));

            psUpdate.setString(2, "Quynh");
            psUpdate.execute();

            // ==========================================
            // 4. KẾT THÚC TRANSACTION BLOCK
            // Nếu code chạy trót lọt đến đây, tiến hành ghi dữ liệu
            // ==========================================
            conn.commit();

            // Thói quen tốt: Bật lại chế độ auto-commit về mặc định
            conn.setAutoCommit(true);

        } catch (Exception e) {
            System.out.println("Lỗi xảy ra, Transaction sẽ tự động huỷ bỏ (rollback) khi đóng kết nối!");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}