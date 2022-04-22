package basketballDatabase;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import javax.swing.JTable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Displays a database showing players by their player id, first name, last name,
 * position that they play, team id, and their record of successful three point shots.
 * 
 * Author: Frank Garcia
 */
@SuppressWarnings("serial")
public class BasketballGUI extends JFrame {
	DatabaseSetup dataSet = new DatabaseSetup();
	private Object[] columnNames = { "Player ID", "First Name", "Last Name", "Position", "Team ID",
			"Three point shots" };
	private JPanel contentPane;
	private JTextField firstName;
	private JTextField removeTextField;
	private JTable basketballPlayerTable;
	private DatabaseInterface database;
	private JTextField lastName;
	private JTextField position;
	private JTextField teamID;
	private JTextField threepntrs;
	private JTextField positionName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BasketballGUI frame = new BasketballGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("serial")
	public BasketballGUI() {
		setTitle("Basketball Database");
		database = new DatabaseInterface();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 647, 584);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JLabel Title = new JLabel("Basket Ball Players");
		Title.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(Title, BorderLayout.NORTH);

		JPanel bottomPanel = new JPanel();
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		Box horizontalBox = Box.createHorizontalBox();
		bottomPanel.add(horizontalBox);

		textfields(horizontalBox);
		addPlayer(bottomPanel);
		removePlayer(bottomPanel);
		sortingQueries(bottomPanel);

		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new GridLayout(1, 0, 0, 0));
		Object[][] data = { { "", "", "", "", "", "" } };
		basketballPlayerTable = new JTable(data, columnNames);
		basketballPlayerTable.setEnabled(false);
		basketballPlayerTable.setRowSelectionAllowed(false);
		basketballPlayerTable.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] {
				"Player ID", "First Name", "Last Name", "Position", "Team ID", "Three point shots"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, true, true, true, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		JScrollPane tablePane = new JScrollPane(basketballPlayerTable);
		mainPanel.add(tablePane);
		
		JPanel panel = new JPanel();
		tablePane.setRowHeaderView(panel);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));
	}

	/**Series of buttons for sorting the playeres.
	 * Buttons can sort by player's position, team, and by last name.
	 * 
	 * @param bottomPanel JPanel containing the buttons
	 */
	private void sortingQueries(JPanel bottomPanel) {
		JButton listButton = new JButton("List All");
		listButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				updateTable("PlayerID");
			}
		});
		bottomPanel.add(listButton);
		
		JButton queryButton1 = new JButton("Sort by Position");	
		queryButton1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				updateTable("Position");
			}
		});
		bottomPanel.add(queryButton1);
		
		JButton queryButton2 = new JButton("Sort by Team");	
		queryButton2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				updateTable("TeamID");
			}
		});
		bottomPanel.add(queryButton2);
		
		JButton queryButton3 = new JButton("List Players by Last Name");	
		queryButton3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				updateTable("LastName");
			}
		});
		bottomPanel.add(queryButton3);
	}

	/**Adds a player to the table based on the user's inputs.
	 * 
	 * @param bottomPanel	JPanel containing the addPlayer textfield and button
	 */
	private void addPlayer(JPanel bottomPanel) {
		JButton addButton = new JButton("Add");
		
		addButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(numberValidator(teamID.getText(), threepntrs.getText()))
					database.addPlayer(firstName.getText(), lastName.getText(), 
							position.getText(), teamID.getText(), threepntrs.getText());
				updateTable("PlayerID");
			}
		});
		bottomPanel.add(addButton);
	}
	
	/**Removes a player to the table based on the user's inputs.
	 * 
	 * @param bottomPanel	JPanel containing the remove textfield and button
	 */
	private void removePlayer(JPanel bottomPanel) {
		removeTextField = new JTextField();
		removeTextField.setToolTipText("Remove by player ID");
		bottomPanel.add(removeTextField);
		removeTextField.setColumns(10);
		JButton removeButton = new JButton("Remove");
		
		removeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					nameValidator(removeTextField.getText());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				database.removePlayer(removeTextField.getText());
				updateTable("PlayerID");
			}
		});
		bottomPanel.add(removeButton);
		positionFilter(bottomPanel);
	}
	
	/**Filters the players by the position entered by the user.
	 * If the position does not exist a warning will pop up to tell the user.
	 * 
	 * @param bottomPanel	JPanel containing the position textfield and button
	 */
	private void positionFilter(JPanel bottomPanel) {
		positionName = new JTextField();
		positionName.setToolTipText("Type a position.");
		bottomPanel.add(positionName);
		positionName.setColumns(10);
		
		JButton positionFilter = new JButton("Show Players in Position");
		positionFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					positionValidator(positionName.getText());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		bottomPanel.add(positionFilter);
	}

	/**Series of textfields declared to allow the user to enter in information
	 * when adding a player.
	 * 
	 * @param horizontalBox	the style of JPanel that is containing the textfields
	 */
	private void textfields(Box horizontalBox) {
		firstName = new JTextField();
		firstName.setName("");
		firstName.setToolTipText("First Name");
		horizontalBox.add(firstName);
		firstName.setColumns(10);
		
		lastName = new JTextField();
		lastName.setToolTipText("Last Name");
		horizontalBox.add(lastName);
		lastName.setColumns(10);
		
		position = new JTextField();
		position.setToolTipText("Position");
		horizontalBox.add(position);
		position.setColumns(10);
		
		teamID = new JTextField();
		teamID.setToolTipText("Team ID");
		horizontalBox.add(teamID);
		teamID.setColumns(10);
		
		threepntrs = new JTextField();
		threepntrs.setToolTipText("3-pointers");
		horizontalBox.add(threepntrs);
		threepntrs.setColumns(10);
	}

	/**
	 * Method to update the table.
	 */
	private void updateTable(String column) {
		DefaultTableModel newTableModel = new DefaultTableModel(database.getPlayerTable(column), columnNames);
		basketballPlayerTable.setModel(newTableModel);
	}
	
	/**
	 * Method to update the table.
	 * 
	 * @param position the position being filtered
	 */
	private void updateTableFilter(String position) {
        DefaultTableModel newTableModel = new DefaultTableModel(database.getFilteredTable(position), columnNames);
        basketballPlayerTable.setModel(newTableModel);
    }
	
	/**
	 * Method to validate if name exists in table.
	 * If name does not exist then error window will display.
	 * @throws SQLException 
	 */
	private void nameValidator(String text) throws SQLException {
		ArrayList<Integer> players = database.getPlayers();
		if (!players.contains(Integer.parseInt(text)))
			JOptionPane.showMessageDialog(contentPane, "Player does not exist.",
					"Error Window", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Method to validate if name exists in table.
	 * If name does not exist then error window will display.
	 * @throws SQLException 
	 */
	private void positionValidator(String text) throws SQLException {
		ArrayList<String> players = database.getPositions();
		if (players.contains(text))
			updateTableFilter(positionName.getText());
		else
			JOptionPane.showMessageDialog(contentPane, "Position does not exist.",
					"Error Window", JOptionPane.ERROR_MESSAGE);
	}
	
	/**Method to validate if the text entered in the number fields for 
	 * team id and three pointers are numbers.
	 * 
	 * @param teamId	teamid of the player being added
	 * @param threepntrs	amount of three pointers of the player being added
	 * @return	returns whether the user's entries are valid numbers
	 */
	private boolean numberValidator(String teamId, String threepntrs) {
		try {
			Integer.parseInt(teamId);
			Integer.parseInt(threepntrs);
			return true;
		} catch (NumberFormatException e){
			JOptionPane.showMessageDialog(contentPane,  "Needs to be a number.",
					"Error Window", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
}