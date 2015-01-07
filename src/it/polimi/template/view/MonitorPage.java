package it.polimi.template.view;

import java.awt.BorderLayout;

import it.polimi.template.controller.DronesPageStartButtonListener;
import it.polimi.template.controller.MissionsPageOkButtonListener;
import it.polimi.template.model.*;

import java.awt.Color;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class MonitorPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private ArrayList<Mission> missions = new ArrayList<Mission>();
	private ArrayList<Drone> drones = new ArrayList<Drone>();

	public MonitorPage() {
	
		initUI();
	}

	public final void initUI() {

		final MissionsPageOkButtonListener mpob = new MissionsPageOkButtonListener();
		final DronesPageStartButtonListener dpsbl = new DronesPageStartButtonListener();
		final ArrayList<Trip> trips = new ArrayList<Trip>();

		setLayout(new BorderLayout());

		DefaultTableModel model = new DefaultTableModel();
		final JTable table = new JTable(model);
		model.addColumn("ID");
		model.addColumn("Trip");
		model.addColumn("Status");

		JScrollPane tablePane = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		JTextArea text = new JTextArea();
		JScrollPane textPane = new JScrollPane(text);
		text.setBackground(Color.LIGHT_GRAY);
		text.append("console\n" + "Here there is the log of the execution\n"
				+ "Pluto");
		text.setForeground(Color.RED);

		JPanel buttonsPane = new JPanel();
		JButton start = new JButton("Start");
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				for (Mission m : missions) {

					dpsbl.sortTripsByPriority(m.getTrips());
					mpob.startDroneAllocator(m, drones);
					dpsbl.startTripLauncher(m);
					dpsbl.startTripMonitor(m);

					for (Trip t : m.getTrips()) {

						trips.add(t);

						if (!t.getUsed()) {

							DefaultTableModel model = (DefaultTableModel) table
									.getModel();

							if (t.getDrone() == null)
								model.addRow(new Object[] { "", t.getName(),
										t.getStatus() });
							else
								model.addRow(new Object[] {
										t.getDrone().getId(), t.getName(),
										t.getStatus() });
							t.setUsed(true);

						}
					}

				}

			}
		});
		JButton stop = new JButton("Stop");
		stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				DefaultTableModel dm = (DefaultTableModel) table.getModel();
				int rowCount = dm.getRowCount();
				// Remove rows one by one from the end of the table
				for (int i = rowCount - 1; i >= 0; i--) {
					dm.removeRow(i);
				}
			}

		});

		buttonsPane.add(start);
		buttonsPane.add(stop);

		getContentPane().add(tablePane, BorderLayout.NORTH);
		getContentPane().add(buttonsPane, BorderLayout.EAST);
		getContentPane().add(textPane);

		setTitle("Pluto-Drones Page");
		setSize(1000, 800);
		setLocationRelativeTo(null);
	}

}