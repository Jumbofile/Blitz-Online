package database;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



public class DatabaseController implements IDatabase { /// most of the gamePersist package taken from Lab06 ----CITING
	private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	static {
		try {
			Class.forName("org.h2.Driver");
		} catch (Exception e) {
			throw new IllegalStateException("Could not load H2 driver");
		}
	}
	
	//decleration
	
	static class RowList extends ArrayList<List<String>> {
		private static final long serialVersionUID = 1L;
	}
	
	private static final String PAD =
			"                                                    " +
			"                                                    " +
			"                                                    " +
			"                                                    ";
		private static final String SEP =
			"----------------------------------------------------" +
			"----------------------------------------------------" +
			"----------------------------------------------------" +
			"----------------------------------------------------";
	
	private interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}

	private static final int MAX_ATTEMPTS = 10;

	//used for printing sql statments
	private static void printRow(List<String> row, List<Integer> colWidths) {
		for (int i = 0; i < row.size(); i++) {
			if (i > 0) {
				System.out.println(" ");
			}
			String item = row.get(i);
			System.out.println(PAD.substring(0, colWidths.get(i) - item.length()));
			System.out.println(item);
		}
		System.out.println("\n");
	}

	private static void printSeparator(List<Integer> colWidths) {
		List<String> sepRow = new ArrayList<String>();
		for (Integer w : colWidths) {
			sepRow.add(SEP.substring(0, w));
		}
		printRow(sepRow, colWidths);
	}

	private static RowList getRows(ResultSet resultSet, int numColumns) throws SQLException {
		RowList rowList = new RowList();
		while (resultSet.next()) {
			List<String> row = new ArrayList<String>();
			for (int i = 1; i <= numColumns; i++) {
				row.add(resultSet.getObject(i).toString());
			}
			rowList.add(row);
		}
		return rowList;
	}

	
	public<ResultType> ResultType executeTransaction(Transaction<ResultType> txn) {
		try {
			return doExecuteTransaction(txn);
		} catch (SQLException e) {
			throw new PersistenceException("Transaction failed", e);
		}
	}
	
	private static List<Integer> getColumnWidths(List<String> colNames, RowList rowList) {
		List<Integer> colWidths = new ArrayList<Integer>();
		for (String colName : colNames) {
			colWidths.add(colName.length());
		}
		for (List<String> row: rowList) {
			for (int i = 0; i < row.size(); i++) {
				colWidths.set(i, Math.max(colWidths.get(i), row.get(i).length()));
			}
		}
		return colWidths;
	}
	
	public<ResultType> ResultType doExecuteTransaction(Transaction<ResultType> txn) throws SQLException {
		Connection conn = connect();
		
		try {
			int numAttempts = 0;
			boolean success = false;
			ResultType result = null;
			
			while (!success && numAttempts < MAX_ATTEMPTS) {
				try {
					result = txn.execute(conn);
					conn.commit();
					success = true;
				} catch (SQLException e) {
					if (e.getSQLState() != null && e.getSQLState().equals("41000")) {
						// Deadlock: retry (unless max retry count has been reached)
						numAttempts++;
					} else {
						// Some other kind of SQLException
						throw e;
					}
				}
			}
			
			if (!success) {
				throw new SQLException("Transaction failed (too many retries)");
			}
			
			// Success!
			return result;
		} finally {
			DBUtil.closeQuietly(conn);
		}
	}

	private Connection connect() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:h2:./tank.db;create=true");
		
		// Set autocommit to false to allow execution of
		// multiple queries/statements as part of the same transaction.
		conn.setAutoCommit(false);
		
		return conn;
	}

	/***
	 * Loads init data for testing only
	 */

	public void loadInitialData() { ///taken from lab06
		System.out.println("Loading initial data...");

	}
	//shutdown=true
	public void createTables() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
								
				try {
					System.out.println("Creating account table...");


					stmt = conn.prepareStatement( //creates account table
							"create table account (" +
									"	uid bigint auto_increment," +
									"   email varchar(40),"     +
									"	username varchar(20),"  +
									"	password varchar(100)," +
									"	name varchar(100)," +
									"	timestamp varchar(100), " +
									"   type INT" +
									")"
					);
					stmt.executeUpdate();

					return true;
				} finally {
					DBUtil.closeQuietly(stmt);
					System.out.println("Success!");
                }
			}
		});
	}
	/*
	BD METHODS BELOW
	 */

	public boolean registerAccount(String username, String pass, String email, String name) throws SQLException {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				//Connection conn = null;
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				ResultSet resultSet2 = null;

				try {
					// retreive username attribute from login
					stmt = conn.prepareStatement("select username " // user attribute
							+ "  from account " // from account table
							+ "  where username = ?"

					);

					// substitute the title entered by the user for the placeholder in
					// the query
					stmt.setString(1, username);

					// execute the query
					resultSet = stmt.executeQuery();

					if (!resultSet.next()) { /// if username doesnt exist

						// retreive username attribute from login
						stmt2 = conn.prepareStatement("select email " // user attribute
								+ "  from account " // from account table
								+ "  where email = ?"

						);

						stmt2.setString(1, email);

						// execute the query
						resultSet2 = stmt2.executeQuery();

						if (!resultSet2.next()) { /// if email doesnt exist
							Date myDate = new Date();
							String date = sdf.format(myDate);
							System.out.println(date);

							String sql = "insert into account(username, password, email, name, timestamp, type)" + " values(?, ?, ?, ?, ?, ?)";

							stmt2 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

							stmt2.setString(1, username);
							stmt2.setString(2, pass);
							stmt2.setString(3, email);
							stmt2.setString(4, name);
							stmt2.setString(5, date);
							stmt2.setInt(6, 0);
							stmt2.executeUpdate();

							ResultSet rs = stmt2.getGeneratedKeys();
							rs.next();
							int uid = rs.getInt(1);

							//use this!
							//stmt2 = conn.prepareStatement(
							//		"insert into userstats(UID, points, plunks, wins, loss) values (?,?,?,?,?)"
							//);

							/*stmt2.setInt(1, uid);
							stmt2.setInt(2, 0);
							stmt2.setInt(3, 0);
							stmt2.setInt(4, 0);
							stmt2.setInt(5, 0);
							stmt2.executeUpdate();*/

							return true;

						} else {
							return false; // email already exists
						}
					}else{
						return false;
					}

				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(resultSet2);
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
	});
	}

	//create lobby
	public boolean createLobby(int gameType, int leaderUID, String ip) throws SQLException {
		return executeTransaction(new Transaction<Boolean>() {
		  @Override
		  public Boolean execute(Connection conn) throws SQLException {
			  return true;
		  }
		});
	}
    // The main method creates the database tables and loads the initial data.
	public static void main(String[] args) throws IOException {
		System.out.println("Creating tables...");
		DatabaseController db = new DatabaseController();
		db.createTables();

		db.loadInitialData();
		
		System.out.println("Success!");
	}
}