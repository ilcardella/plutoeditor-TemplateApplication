package it.polimi.template.controller.block;

import java.util.Observable;
import java.util.Observer;

import it.polimi.template.model.*;

public class PriorityManager extends Node implements Observer {

	@Override
	public Mission run(Mission m) {
		// Here only Failed missions arrives
		if (m != null && m.getStatus() == Mission.FAILED) {
			// increment the priority to HIGH level
			m.getTrips().get(0).setPriority(150);
			m.setStatus(Mission.STANDBY);
			return m;
		}
		return null;
	}

	@Override
	public void update(Observable o, Object arg) {
		Mission m = this.run((Mission) arg);
		setChanged();
		notifyObservers(m);
	}

}
