package com.codegym.dao;

import com.codegym.model.User;
import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {
    // Các phương thức cũ đã có...
    // ...

    // Phương thức bài trước (không dùng transaction)
    public void insertUpdateWithoutTransaction() throws SQLException;

    // THÊM MỚI: Phương thức dùng transaction
    public void insertUpdateUseTransaction() throws SQLException;
}