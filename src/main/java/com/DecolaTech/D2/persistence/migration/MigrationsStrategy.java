package com.DecolaTech.D2.persistence.migration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import com.DecolaTech.D2.persistence.config.ConnectionConfig;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MigrationsStrategy {

    private final Connection connection;

    public void executeMigration() {
        var originalOut = System.out;
        var originalError = System.err;
        try (var fos = new FileOutputStream("liquibase.log")) {
            System.setOut(new PrintStream(fos));
            System.setErr(new PrintStream(fos));

            try (var connection = ConnectionConfig.getConnection()) {
                var jdbcConnection = new JdbcConnection(connection);

                var liquibase = new Liquibase(
                        "/db/changelog/db.changelog-master.yml",
                        new ClassLoaderResourceAccessor(),
                        jdbcConnection);

                liquibase.update();

            } catch (SQLException | LiquibaseException e) {
                e.printStackTrace();
            }
            System.setErr(originalError);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.setOut(originalOut);
            System.setErr(originalError);
        }
    }

}
