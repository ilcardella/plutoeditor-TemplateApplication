package it.polimi.template.controller;

import it.polimi.template.model.Action;
import it.polimi.template.model.Item;
import it.polimi.template.model.Mission;
import it.polimi.template.model.Trip;
import it.polimi.template.view.TripsPage;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

public class TripsPageController {

	private TripsPage tripsPage;
	private List<Item> items;
	private String missionName;
	private char tripCounter = 65;

	public TripsPageController(TripsPage tripsPage, List<Item> items,
			String missionName) {
		this.tripsPage = tripsPage;
		this.items = items;
		this.missionName = missionName;
		
		this.tripsPage.setDropTargetListener();

	}

	class DropTargetListener extends DropTargetAdapter {

		private DropTarget dropTarget;
		private JLabel label;

		public DropTargetListener(JLabel label) {
			this.label = label;
			dropTarget = new DropTarget(label, DnDConstants.ACTION_COPY, this,
					true, null);
		}
		
		@Override
		public void drop(DropTargetDropEvent event) {
			try {
				Point p = event.getLocation();
				Transferable tr = event.getTransferable();

				String action = (String) tr
						.getTransferData(DataFlavor.stringFlavor);

				if (event.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					event.acceptDrop(DnDConstants.ACTION_COPY);
					event.dropComplete(true);

					// if drop event is ok, create the Trip and set the name
					Trip trip = new Trip();
					trip.setName(missionName + " - " + tripCounter);
					tripCounter++;
					
					if( action.equals(Action.PICK_ITEM.toString()) || action.equals(Action.RELEASE_ITEM.toString()) ){
						
						// create a string list with the name of the items
						List<String> itemsNames = new ArrayList<String>();
						for(Item i: items)
							itemsNames.add(i.getName());
						
						// get the items from the user input
						String iName = tripsPage.showItemsPanel(itemsNames);
						
						// set the item to the trip
						for(Item i: items)
							if(i.getName().equals(iName))
								trip.setItem(i);
						
						// set the priority to the trip
						int priority = tripsPage.showPriorityPanel();
						trip.setPriority(priority);
						
						// set the delay
						int delay = tripsPage.showDelayPanel();
						trip.setDelay(delay);
						
						
						
					}
				}

				event.rejectDrop();
			} catch (Exception e) {
				e.printStackTrace();
				event.rejectDrop();
			}

		}

	}

}
