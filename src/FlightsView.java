import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class FlightsView extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private UserAccount userAccount;
    private FlightRepository flightRepository;
    private boolean isAllFlight;

    public FlightsView(List<Flight> flights, UserAccount userAccount, FlightRepository flightRepository) {
        this.userAccount = userAccount;
        this.flightRepository = flightRepository;
        this.isAllFlight = true;
        initializeUI(true);
        // set up window attributes
        setTitle("All Flights");
        setSize(1000, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    flightRepository.saveFlightRepository();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private List<Flight> getFlights() {
        if (isAllFlight) {
            return flightRepository.getAllFlights();
        } else {
            return flightRepository.getFlightsByUUIDs(userAccount.getFlightList());
        }
    }

    private void initializeUI(boolean isAllFlights) {
        String[] columnNames = {
                "Flight ID", "Departure", "Arrival",
                "From", "To", "Airline",
                "Occupancy", "Remaining Tickets", "Action"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 8 ? JButton.class : super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;  // Only the button column is editable
            }
        };
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);
        table.getColumn("Action").setCellRenderer(new ButtonRenderer());

        List<Flight> flights = getFlights();
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), flights));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel navigationPanel = new JPanel();
        JButton viewAllFlightsButton = new JButton("View All Flights");
        JButton viewMyFlightsButton = new JButton("View My Flights");
        navigationPanel.add(viewAllFlightsButton);
        navigationPanel.add(viewMyFlightsButton);
        add(navigationPanel, BorderLayout.SOUTH);


        viewAllFlightsButton.addActionListener((ActionEvent e) -> {
            // Handle view all flights button action
            // You can refresh the table with all flights here
            this.isAllFlight = true;
            refresh();
            setTitle("All Flights");
        });

        viewMyFlightsButton.addActionListener((ActionEvent e) -> {
            // Handle view my flights button action
            // You can filter and refresh the table with flights booked by the user here
            this.isAllFlight = false;
            refresh();
            setTitle("My Flights");
        });

        loadFlights(flights, isAllFlights);
    }

    private void refresh() {
        loadFlights(getFlights(), isAllFlight);
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), getFlights()));
    }

    private void loadFlights(List<Flight> flights, boolean isAllFlights) {
        model.setRowCount(0);
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
        private boolean isPushed;
        private List<Flight> flights;

        public ButtonEditor(JCheckBox checkBox, List<Flight> flights) {
            super(checkBox);
            this.flights = flights;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener((ActionEvent e) -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                // This is where you can place your booking logic
                int rowIndex = table.convertRowIndexToModel(table.getEditingRow());
                Flight flight = flights.get(rowIndex);
                UUID flightId = flight.getFlightId();
                if (isAllFlight) {
                    userAccount.addFlight(flightId);
                    flightRepository.bookFlight(flightId);
                    JOptionPane.showMessageDialog(button, "Booking Flight: " + flightId);
                    refresh();
                } else {
                    userAccount.cancelFlight(flightId);
                    flightRepository.cancelFlight(flightId);
                    JOptionPane.showMessageDialog(button, "Cancel Flight: " + flightId);
                    refresh();
                }
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
