package com.DecolaTech.D2;
import java.sql.SQLException;

import com.DecolaTech.D2.persistence.config.ConnectionConfig;
import com.DecolaTech.D2.persistence.migration.MigrationsStrategy;

public class Main {

	public static void main(String[] args) throws SQLException{
		try(var connection = ConnectionConfig.getConnection()){
			new MigrationsStrategy(connection).executeMigration();;
		}
	}

}
