import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class FlightsGUI extends JFrame{

    private JTable table;
    private DefaultTableModel model;
    private int userId;
    private AccountRepository accountRepository;
    private FlightRepository flightRepository;
    private boolean isAllFlights;

    public FlightsGUI(int userId, FlightRepository flightRepository, AccountRepository accountRepository) {
        // setup attributes
        this.userId = userId;
        this.flightRepository = flightRepository;
        this.accountRepository = accountRepository;
        this.isAllFlights = true;
        // setup table and model
        String[] columnNames = {
                "Flight ID", "Departure", "Arrival",
                "From", "To", "Airline",
                "Occupancy", "Remaining Tickets", "Action"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        // setup navigation bar
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        JPanel navigationPanel = new JPanel();
        JButton viewAllFlightsButton = new JButton("View All Flights");
        JButton viewMyFlightsButton = new JButton("View My Flights");
        navigationPanel.add(viewAllFlightsButton);
        navigationPanel.add(viewMyFlightsButton);
        add(navigationPanel, BorderLayout.SOUTH);
        viewAllFlightsButton.addActionListener((ActionEvent e) -> {
            this.isAllFlights = true;
            refresh();
            setTitle("All Flights");
        });
        viewMyFlightsButton.addActionListener((ActionEvent e) -> {
            this.isAllFlights = false;
            refresh();
            setTitle("My Flights");
        });
        // load data to table
        refresh();
        setTitle("All Flights");
        // setup table attributes
        setSize(1000, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        // on close listener
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    flightRepository.saveFlightRepository();
                    accountRepository.updateDatabase();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    private List<Flight> getFlights() {
        if (isAllFlights) {
            return flightRepository.getAllFlights();
        } else {
            return flightRepository.getFlightsByUUIDs(accountRepository.getAllFlights(userId));
        }
    }
    private void refresh() {
        // Reset the whole table
        model.setRowCount(0);
        // Reset Button
        table.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JCheckBox()));
        // Fetch data from repository and add to table
        List<Flight> flights = getFlights();
        for (Flight flight : flights) {
            String CSVRow = flight.toCSVRow();
            String[] fields = CSVRow.split(",");
            String buttonText = isAllFlights ? "book" : "cancel";
            Object[] row = new Object[]{
                    fields[0],
                    fields[1],
                    fields[2],
                    fields[3],
                    fields[4],
                    fields[5],
                    fields[6],
                    fields[7],
                    buttonText
            };
            model.addRow(row);
        }
    }
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int rowIndex = table.getSelectedRow();
                    Flight flight = getFlights().get(rowIndex);
                    UUID flightId = flight.getFlightId();
                    if (isAllFlights) {
                        try {
                            accountRepository.bookFlight(userId, flightId);
                        } catch (CloneNotSupportedException ex) {
                            throw new RuntimeException(ex);
                        }
                        flightRepository.bookFlight(flightId);
                        JOptionPane.showMessageDialog(button, "Booking Flight: " + flightId);
                    } else {
                        try {
                            accountRepository.cancelFlight(userId, flightId);
                        } catch (CloneNotSupportedException ex) {
                            throw new RuntimeException(ex);
                        }
                        flightRepository.cancelFlight(flightId);
                        JOptionPane.showMessageDialog(button, "Cancel Flight: " + flightId);
                    }
                    fireEditingStopped();
                    refresh();
                }
            });
        }
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            return button;
        }
        public boolean stopCellEditing() {
            return super.stopCellEditing();
        }
    }
}
