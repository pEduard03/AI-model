package licenta;
import javax.swing.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.*;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class GraphGenerator {
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:47335/editor?user=mindsdb";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GraphGenerator::createAndShowGUI);
    }

    private static void createAndShowGUI() {
    	 
        JFrame frame = new JFrame("House Price Prediction Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel neighborhoodLabel = new JLabel("Select Neighborhood:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(neighborhoodLabel, gbc);

        String[] neighborhoods = {"alcatraz_ave", "berkeley_hills", "downtown", "south_side", "thowsand_oaks", "westbrae"};
        JComboBox<String> neighborhoodComboBox = new JComboBox<>(neighborhoods);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(neighborhoodComboBox, gbc);

        JLabel roomsLabel = new JLabel("Select Number of Rooms:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(roomsLabel, gbc);

        Integer[] roomNumbers = {0, 1, 2, 3};
        JComboBox<Integer> roomsComboBox = new JComboBox<>(roomNumbers);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(roomsComboBox, gbc);

        JCheckBox locationCheckBox = new JCheckBox("Filter by Location");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(locationCheckBox, gbc);
        locationCheckBox.setSelected(true);
        JLabel locationLabel = new JLabel("Location:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(locationLabel, gbc);

        JSlider locationSlider = new JSlider(JSlider.HORIZONTAL, 0, 2, 0);
        locationSlider.setMajorTickSpacing(1);
        locationSlider.setPaintTicks(true);
        locationSlider.setPaintLabels(true);
        String[] locations = {"Poor", "Great", "Good"};
        locationSlider.setLabelTable(createLabelTable(locations));
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(locationSlider, gbc);

        JButton generateButton = new JButton("Generate Graph");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(generateButton, gbc);

        ChartPanel chartPanel = new ChartPanel(null);

        locationCheckBox.addActionListener(e -> {
            boolean enabled = locationCheckBox.isSelected();
            locationSlider.setEnabled(enabled);
            locationLabel.setEnabled(enabled);
        });

        generateButton.addActionListener(e -> {
            String neighborhood = (String) neighborhoodComboBox.getSelectedItem();
            int rooms = (int) roomsComboBox.getSelectedItem();
            String location = locationCheckBox.isSelected() ? getLocation(locationSlider.getValue()) : null;
            generateGraph(neighborhood, rooms, location, chartPanel);
        });

        frame.add(panel, BorderLayout.NORTH);
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static String getLocation(int value) {
        switch (value) {
            case 0:
                return "poor";
            case 1:
                return "great";
            case 2:
            default:
                return "good";
        }
    }

    private static void generateGraph(String neighborhood, int rooms, String location, ChartPanel chartPanel) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT t.rental_price AS real_price, m.rental_price AS predicted_price, t.number_of_rooms, t.neighborhood " +
                             "FROM example_db.home_rentals AS t " +
                             "JOIN mindsdb.home_rentals_predictor AS m " +
                             "ON t.neighborhood = m.neighborhood " +
                             (location != null ? "WHERE t.location = ? AND " : "WHERE ") +
                             "t.neighborhood = ? AND t.number_of_rooms = ? LIMIT 100;")) {
        	
            int parameterIndex = 1;
            if (location != null) {
                preparedStatement.setString(parameterIndex++, location);
            }
            preparedStatement.setString(parameterIndex++, neighborhood);
            preparedStatement.setInt(parameterIndex++, rooms);

            ResultSet rs = preparedStatement.executeQuery();

            XYSeries realPriceSeries = new XYSeries("Real Price");
            XYSeries predictedPriceSeries = new XYSeries("Predicted Price");

            int houseCount = 1;
            while (rs.next()) {
                int real_price = rs.getInt("real_price");
                int predicted_price = rs.getInt("predicted_price");

                realPriceSeries.add(houseCount, real_price);
                predictedPriceSeries.add(houseCount, predicted_price);

                houseCount++;
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(realPriceSeries);
            dataset.addSeries(predictedPriceSeries);

            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Real and Predicted Prices of Houses in " + neighborhood + " with " + rooms + " Rooms" +
                            (location != null ? " and " + location + " Location" : ""),
                    "House",
                    "Price",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            XYPlot plot = chart.getXYPlot();
            plot.setBackgroundPaint(Color.lightGray);
            
            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            renderer.setSeriesPaint(0, Color.BLUE);
            renderer.setSeriesPaint(1, Color.RED);
            plot.setRenderer(renderer);

            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setTickUnit(new NumberTickUnit(250));

            chartPanel.setChart(chart);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static Hashtable<Integer, JLabel> createLabelTable(String[] labels) {
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = 0; i < labels.length; i++) {
            labelTable.put(i, new JLabel(labels[i]));
        }
        return labelTable;
    }
}