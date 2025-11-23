package com.skupina1.accountmanagement.db;

import com.skupina1.accountmanagement.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    private static final HikariDataSource source;

    static{
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Config.getDBUrl());
        config.setUsername(Config.getDBUser());
        config.setPassword(Config.getDBPassword());
        config.setMaximumPoolSize(10);
        source = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return source.getConnection();
    }
}
