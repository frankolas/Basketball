package basketballDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sets up Player and Teams Database.
 * 
 * @author Frank Garcia
 */
public class DatabaseSetup {
	public static final String databaseURL = "jdbc:derby:BasketballDatabase;create=true";
	
	public DatabaseSetup() {
			try (Connection connection = DriverManager.getConnection(databaseURL);
					Statement statement = connection.createStatement()) {
				statement.execute("DROP TABLE players");
				statement.execute("DROP TABLE teams");
				
				statement.execute(
						"CREATE TABLE teams ("
						+ "TeamID int not null primary key "
						+ "GENERATED ALWAYS AS IDENTITY "
						+ "(START WITH 000, INCREMENT BY 1), "
						+ "Name varchar(155), "
						+ "City varchar(155), "
						+ "NumberOfChampionships int)");
				statement.execute(
						"CREATE TABLE players ("
						+ "PlayerID int not null primary key GENERATED ALWAYS AS IDENTITY "
						+ "(START WITH 001, INCREMENT BY 1), "
						+ "FirstName varchar(155), "
						+ "LastName varchar(155), "
						+ "Position varchar(155), "
						+ "TeamID int, "
						+ "TotalThreePointShots int, "
						+ "FOREIGN KEY (TeamID) "
						+ "REFERENCES teams(TeamID))");
				statement.execute(
						"INSERT INTO teams ("
						+ "Name, City, NumberOfChampionships) VALUES "
						+ "('Heat', 'Miami', 3), "
						+ "('Golden State Warriors', 'San Francisco', 6), "
						+ "('Lakers', 'Los Angeles', 17), "
						+ "('Pacers', 'Indiana', 0), "
						+ "('Bucks', 'Milwaukee', 2), "
						+ "('Nets', 'Brooklyn', 0), "
						+ "('Trailblazers', 'Portland', 1), "
						+ "('New York Knicks', 'Manhatten', 2), "
						+ "('Mavericks', 'Dallas', 1), "
						+ "('Raptors', 'Toronto', 1), "
						+ "('LA Clippers', 'Los Angeles', 0)");
				
				statement.execute(
						"INSERT INTO players ("
						+ "FirstName, "
						+ "LastName, "
						+ "Position, "
						+ "TeamID, "
						+ "TotalThreePointShots) VALUES ("
						+ "'Ray', 'Allen', 'Shooting Guard', 0, 1972),"
						+ "('Stephen', 'Curry', 'Point Guard', 1, 1821),"
						+ "('Stephen', 'Nash', 'Point Guard', 2, 1685),"
						+ "('Reggie', 'Miller', 'Shooting Guard', 3, 1560),"
						+ "('Kyle', 'Korver', 'Shooting Guard', 4, 1350),"
						+ "('Jason', 'Terry', 'Shooting Guard', 4, 1181),"
						+ "('Jamal', 'Crawford', 'Shooting Guard', 5, 1110),"
						+ "('Paul', 'Pierce', 'Small Forward', 10, 1032),"
						+ "('Damian', 'Lillard', 'Point Guard', 6, 1050),"
						+ "('Jason', 'Kidd', 'Point Guard', 7, 0988),"
						+ "('Dirk', 'Nowitzki', 'Power Forward', 8, 0981),"
						+ "('LeBron', 'James', 'Small Forward', 6, 0979),"
						+ "('Joe', 'Johnson', 'Shooting Guard', 8, 0978),"
						+ "('JJ', 'Redick', 'Shooting Guard', 8, 0950),"
						+ "('Jr', 'Smith', 'Shooting Guard', 2, 0920),"
						+ "('Chauncey', 'Billups', 'Point Guard', 10, 0820),"
						+ "('Kyle', 'Lowry', 'Point Guard', 9, 0817),"
						+ "('Kobe', 'Bryant', 'Shooting Guard', 2, 0817),"
						+ "('Klay', 'Thompson', 'Shooting Guard', 1, 0798),"
						+ "('Rashword', 'Lewis', 'Small Forward', 0, 0787),"
						+ "('Paul', 'George', 'Small Forward', 10, 0760),"
						+ "('Peja', 'Stojakovic', 'Small Forward', 8, 0760),"
						+ "('Wesley', 'Mathews', 'Shooting Guard', 2, 0719),"
						+ "('Dale', 'Ellis', 'Small Forward', 4, 0709),"
						+ "('Dwight', 'Howard', 'Small Forward', 2, 0014)");
	
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("done");
		}
	}