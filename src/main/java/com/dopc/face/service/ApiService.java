package com.dopc.face.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerResultSetMetaData;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.jdbc.result.ResultSetMetaData;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import oracle.jdbc.OracleResultSetMetaData;
import oracle.jdbc.pool.OracleDataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.jdbc.PgResultSetMetaData;

/**
 * @author DOPC
 *
 * Service to manage API
 */
@Service
public class ApiService {
	
	@Value("${app.face.config.path}")
	private String appFaceConfigPath;

	public List<Map<String, Object>> expose(String Authorization, String map, Map<String, String> requestParams) throws ParseException, SQLException, IOException {
		
		File file = new File(appFaceConfigPath + map + ".json");
		FileReader fileReader = new FileReader(file);
		
	    if(file.exists()){
	        
	        JSONObject config = (JSONObject) (new JSONParser(JSONParser.IGNORE_CONTROL_CHAR)).parse(fileReader);
	        
	        fileReader.close();
	        
	        checkAuth(Authorization, config);
	        
	        List<Map<String, Object>> result = executeQuery(config, requestParams);
	        
	        return result;
	    }
	    
        fileReader.close();
	    	
	    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found config for " + map);
	}
	
	private void checkAuth(String authorization, JSONObject config) throws ParseException, IOException {
		String securityName = (String) config.get("security");
		if (securityName != null) {
			String _authorization = (String) getSecurity(securityName, "Authorization");
			
			if (_authorization != null && !_authorization.equals(authorization)) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Resource not authorized, you need valid authorization");
			}
		}
		
	}

	private List<Map<String, Object>> executeQuery(JSONObject config, Map<String, String> requestParams) throws SQLException, ParseException, IOException {
		Connection connection = getSqlConnexion(config);
		
		String query = buildQuery(config, requestParams);
		        
		ResultSet resultSet = connection.createStatement().executeQuery(query);
		
		List<Map<String, Object>> result = formatResult(config, resultSet);
		
        if (!connection.isClosed()) {
        	connection.close();
		}
		return result;
	}

	private List<Map<String, Object>> formatResult(JSONObject config, ResultSet rs) throws SQLException, ParseException, IOException {
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        
        String dataSourceName = (String) config.get("datasource");
        String type = (String) getDataSource(dataSourceName,"type");
        
		switch (type) {
		case "MYSQL":
			ResultSetMetaData mdMYSQL = (ResultSetMetaData) rs.getMetaData();
			
		    int columnsMYSQL = mdMYSQL.getColumnCount();
		    while (rs.next()){
		        Map<String, Object> row = new HashMap<String, Object>(columnsMYSQL);
		        for(int i = 1; i <= columnsMYSQL; ++i)
		            row.put(mdMYSQL.getColumnName(i), rs.getObject(i));
		        rows.add(row);
		    }
			break;
		case "TSQL":
			SQLServerResultSetMetaData mdTSQL = (SQLServerResultSetMetaData) rs.getMetaData();
			
		    int columnsTSQL = mdTSQL.getColumnCount();
		    while (rs.next()){
		        Map<String, Object> row = new HashMap<String, Object>(columnsTSQL);
		        for(int i = 1; i <= columnsTSQL; ++i)
		            row.put(mdTSQL.getColumnName(i), rs.getObject(i));
		        rows.add(row);
		    }
			break;
		case "PLSQL":
			OracleResultSetMetaData mdPLSQL = (OracleResultSetMetaData) rs.getMetaData();
			
		    int columnsPLSQL = mdPLSQL.getColumnCount();
		    while (rs.next()){
		        Map<String, Object> row = new HashMap<String, Object>(columnsPLSQL);
		        for(int i = 1; i <= columnsPLSQL; ++i)
		            row.put(mdPLSQL.getColumnName(i), rs.getObject(i));
		        rows.add(row);
		    }
			break;
		case "POSTGRES":
			PgResultSetMetaData mdPGSQL = (PgResultSetMetaData) rs.getMetaData();
			
		    int columnsPGSQL = mdPGSQL.getColumnCount();
		    while (rs.next()){
		        Map<String, Object> row = new HashMap<String, Object>(columnsPGSQL);
		        for(int i = 1; i <= columnsPGSQL; ++i)
		            row.put(mdPGSQL.getColumnName(i), rs.getObject(i));
		        rows.add(row);
		    }
			break;
		default:
			break;
		}
	    
	    return rows;
	}

	private String buildQuery(JSONObject config, Map<String, String> requestParams) {
		
		String select = getConfig(config,"query","select");
		String from = getConfig(config,"query","from");
		String where = getConfig(config,"query","where");
		String orderby = getConfig(config,"query","orderby");
		String query = "select " + select + " from " + from + ( where.equals(null) || where.equals("") ?  "" : " where " + where + " " ) + ( orderby.equals(null) || orderby.equals("") ?  "" : " order by " + orderby + " " ) + ";";
				
		for (String key : requestParams.keySet()) {
	        query = query.replace(":" + key, requestParams.get(key));
	    }
		
		return query;
	}
	
	private String getConfig(JSONObject config, String parent, String child) {
		return (String) ((JSONObject) config.get(parent)).get(child);
	}
	
	private Object getDataSource(String dataSourceName, String param) throws ParseException, IOException {
		File file = new File(appFaceConfigPath + "datasource/" + dataSourceName + ".json");
		FileReader fileReader = new FileReader(file);
		
	    if(file.exists()){
	    	
	    	Object dataSource = (Object) ((JSONObject) (new JSONParser(JSONParser.IGNORE_CONTROL_CHAR)).parse(fileReader)).get(param);
	    	
	    	fileReader.close();
	    	
	        return dataSource;
	    }
	    
	    fileReader.close();
	    	    	
	    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found datasource for " + dataSourceName);

	}
	
	private Object getSecurity(String securityName, String param) throws ParseException, IOException {
		File file = new File(appFaceConfigPath + "security/" + securityName + ".json");
		FileReader fileReader = new FileReader(file);
		
	    if(file.exists()){
	    	
	    	Object security = (Object) ((JSONObject) (new JSONParser(JSONParser.IGNORE_CONTROL_CHAR)).parse(fileReader)).get(param);
	    	
	    	fileReader.close();
	    	
	        return security;
	    }
	    
	    fileReader.close();
	    	    	
	    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found datasource for " + securityName);

	}

	private Connection getSqlConnexion(JSONObject config) throws SQLException, ParseException, IOException {
		
		String dataSourceName = (String) config.get("datasource");
		
		String type = (String) getDataSource(dataSourceName,"type");
		String server = (String) getDataSource(dataSourceName,"server");
		Integer port = (Integer) getDataSource(dataSourceName,"port");
		//Integer port = (Integer)  ((JSONObject) config.get(dataSourceName)).get("port");
		String database = (String) getDataSource(dataSourceName,"database");
		String user = (String) getDataSource(dataSourceName,"user");
		String password = (String) getDataSource(dataSourceName,"password");
		
		Connection connection = null;
		
		switch (type) {
			case "MYSQL":
				connection = getMySqlConnection(server, port, database, user, password);
				break;
			case "TSQL":
				connection = getSqlServerConnection(server, port, database, user, password);
				break;
			case "PLSQL":
				connection = getOracleSqlConnection(server, port, database, user, password);
				break;
			case "POSTGRES":
				connection = getPostgresSqlConnection(server, port, database, user, password);
				break;
	
			default:
				break;
		}
		
		return connection;
	}
	

	private Connection getMySqlConnection(String server, Integer port, String database, String user, String password) throws SQLException {
		
		MysqlDataSource source = new MysqlDataSource();
		
		source.setServerName(server);
		if (port != null ) 
			source.setPort(port);
		source.setDatabaseName(database);
		source.setUser(user);
		source.setPassword(password);
		
		return  source.getConnection();
	}
	
	private Connection getSqlServerConnection(String server, Integer port, String database, String user, String password) throws SQLException {
		
		SQLServerDataSource source = new SQLServerDataSource();
		
		source.setServerName(server);
		if (port != null ) 
			source.setPortNumber(port);
		source.setDatabaseName(database);
		source.setUser(user);
		source.setPassword(password);
		
		return  source.getConnection();
	}
	
	
	private Connection getOracleSqlConnection(String server, Integer port, String database, String user, String password) throws SQLException {
		
		OracleDataSource source = new OracleDataSource();
		
		String url = "jdbc:oracle:thin:@";
		url = url + server + ":";
		url = url + (port != null ? port : "1521") + ":";
		url = url + database;
		source.setUser(user);
		source.setPassword(password);
		source.setURL(url);
		
		return  source.getConnection();
	}
	
	
	private Connection getPostgresSqlConnection(String server, Integer port, String database, String user, String password) throws SQLException {
		
		PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
		
		String[] serverNames = { server };
		int[] portNumbers = { port != null ? port : 5432  };
		
		pgSimpleDataSource.setServerNames(serverNames );
		if (port != null )
			pgSimpleDataSource.setPortNumbers(portNumbers);
		pgSimpleDataSource.setDatabaseName(database);
		pgSimpleDataSource.setUser(user);
		pgSimpleDataSource.setPassword(password);
		
		return  pgSimpleDataSource.getConnection();
	}
}
