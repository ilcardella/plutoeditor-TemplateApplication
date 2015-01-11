package it.polimi.template.controller.thread;

import it.polimi.template.controller.MonitorPageController;
import it.polimi.template.model.Mission;
import it.polimi.template.model.editor.DroneAllocator;
import it.polimi.template.model.editor.MissionCreator;
import it.polimi.template.model.editor.TripLauncher;
import it.polimi.template.model.editor.TripMonitor;

import javax.swing.SwingWorker;

public class MyWorker extends SwingWorker<Integer, String> {

	private MonitorPageController parent;
	private Mission m;
	private MissionCreator mc = new MissionCreator();
	private DroneAllocator da = new DroneAllocator();
	private TripLauncher tl = new TripLauncher();
	private TripMonitor tm = new TripMonitor();

	public MyWorker(Mission mission, MonitorPageController parent) {
		this.m = mission;
		this.parent = parent;
	}

	@Override
	protected Integer doInBackground() throws Exception {
		
		// This part is generated by the graphical editor

		m = mc.run(m);

		parent.notifyUpdateOfStatus(m);

		while (m.getStatus() != Mission.COMPLETED) {

			// TODO Nel caso del Clock
			// viene sempre controllato se esiste un delay sul next trip
			// se esiste si aspetta che sia passato il tempo
			// altrimenti si continua normalmente
			// m = clock.run(m);
			
			m = da.run(m);

			parent.notifyUpdateOfStatus(m);

			m = tl.run(m);

			parent.notifyUpdateOfStatus(m);

			m = tm.run(m);

			parent.notifyUpdateOfStatus(m);

			if (m.getStatus() == Mission.FAILED)
				
				//TODO Nel caso del Priority Manager
				// viene inserita la missione 
				// il pm cambia la priorit� e risetta gli stati
				// il "break non serve pi� e si ritorna all'inizio del ciclo
				// m = pm.run(m)
				break;
		}

		return 42;
	}

}
