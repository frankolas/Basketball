package basketballDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Allows the GUI to interact with the database.
 * 
 * @author Frank Garcia
 *
 */

public class DatabaseInterface
{
	public String databaseURL;
	public DatabaseInterface()
	{
		databaseURL = "jdbc:derby:BasketballDatabase;create=true";
	}
	
	/**Adds a player to the table players.
	 * 
	 * @param firstName	the first name of the player
	 * @param lastName the last name of the player
	 * @param position	the position of the player
	 * @param teamID	the team id of the player
	 * @param threepntrs	the record of three pointers for the player
	 */
	public void addPlayer(String firstName, String lastName, String position, String teamID, String threepntrs)
	{
		try(Connection connection = DriverManager.getConnection(databaseURL);
				Statement statement = connection.createStatement())
		{
			statement.execute("INSERT INTO players (FirstName, LastName, Position, TeamID, TotalThreePointShots) VALUES "
					+ "('" + firstName + "', "
					+ "'" + lastName + "', "
					+ "'" + position + "',"
					+ teamID + ", " 
					+ threepntrs + ")");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}
	
	/**Removes a player from the table based on the id entered by the user.
	 * 
	 * @param id	the team id
	 */
	public void removePlayer(String id)
	{
		try(Connection connection = DriverManager.getConnection(databaseURL);
				Statement statement = connection.createStatement())
		{
			statement.execute("DELETE FROM players WHERE PlayerID=" + id);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}
	
	/**Generates the table of players for the JTable.
	 * 
	 * @param column	the column of the JTable
	 * @return	the table that will fill JTable
	 */
	public Object[][] getPlayerTable(String column)
	{
		Object[][] table = null;
		try(Connection connection = DriverManager.getConnection(databaseURL);
				Statement statement = connection.createStatement())
		{
			ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM players");
			resultSet.next();
			int rows = resultSet.getInt(1);
			resultSet = statement.executeQuery("SELECT * FROM players");
			resultSet.next();
			int cols = resultSet.getMetaData().getColumnCount();
			table = new Object[rows][cols];
			resultSet = statement.executeQuery("SELECT * FROM players ORDER BY " + column);
			
			for(int r=0; r<rows; r++)
			{
				resultSet.next();
				for(int c=0; c<cols;c++)
				{
				table[r][c] = resultSet.getObject(c+1);
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return table;
	}
	
	/**Fills an arraylist with the names of each city on the teams table.
	 * 
	 * @return	arraylist with each city on the teams table
	 * @throws SQLException
	 */
	public ArrayList<Integer> getPlayers() throws SQLException {
		ArrayList<Integer> players = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(databaseURL);
				Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery("SELECT PlayerID FROM players");
			while(resultSet.next()) {
				int player = resultSet.getInt("PlayerID");
				players.add(player);
			}
		}
		return players;
	}
	
	/**Fills an arraylist with the names of each city on the teams table.
	 * 
	 * @return	arraylist with each city on the teams table
	 * @throws SQLException
	 */
	public ArrayList<String> getPositions() throws SQLException {
		ArrayList<String> players = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(databaseURL);
				Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery("SELECT Position FROM players");
			while(resultSet.next()) {
				String position = resultSet.getString("Position");
				players.add(position);
			}
		}
		return players;
	}
	
	/**Returns the player table.
	 * 
	 * @return	the player table
	 */
	public Object[][] getPlayerTable()
	{
		return getPlayerTable("PlayerID");
	}
	
	/**Generates the table of players for the JTable based on if their position
	 * was a position entered by the player.
	 * 
	 * @param column	the column of the JTable
	 * @return	the table that will fill JTable
	 */
	public Object[][] getFilteredTable(String position)
	   {
	    Object[][] table = null;
	    try(Connection connection = DriverManager.getConnection(databaseURL);
	            Statement statement = connection.createStatement())
	    {
	    	ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM players WHERE Position='"+position+"'");
	        resultSet.next();
	        int rows = resultSet.getInt(1);
	        resultSet = statement.executeQuery("SELECT * FROM players");
	        resultSet.next();
	        int cols = resultSet.getMetaData().getColumnCount();
	        table = new Object[rows][cols];
	        resultSet = statement.executeQuery("SELECT * FROM players WHERE Position='"+position+"'");
	
	        for(int r=0; r<rows; r++)
	        {
	            resultSet.next();
	            for(int c=0; c<cols;c++)
	            {
	            table[r][c] = resultSet.getObject(c+1);
	            }
	        }
	    }
	    catch (SQLException e)
	    {
	        e.printStackTrace();
	    }
	    return table;
    }
}