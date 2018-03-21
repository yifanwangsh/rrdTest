package org.rrd4j.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CopyRrdData {
	public CopyRrdData(String basedir, String guid, String name, Long now) throws IOException {
		this.rrdPath =  basedir + "/" + guid + "/" + name + ".rrd";
		RrdSyncThreadPool pool = new RrdSyncThreadPool();	//default size	
		RrdNioBackendFactory factory = new RrdNioBackendFactory();
		factory.setSyncThreadPool(pool);
		db = new RrdDb(rrdPath, false, factory);
		
		this.startTime = now;
	}
	
	private String rrdPath;
	private RrdDb db;
	int counter = 0;
	private long startTime;
	
	public String getPath() {
		return rrdPath;
	}
	
	public void writeData() throws IOException, InterruptedException {
		System.out.println("The rrd last updates on " + db.getLastArchiveUpdateTime());
		final Sample sample = db.createSample(startTime + 300);
		
		for (int i = 0; i < db.getDsCount(); i++) {
			Datasource ds = db.getDatasource(i);
			System.out.println("Now updating datasource " + ds.getName());
			String dsName = ds.getName();
			
			for (int j = 0; j < db.getArcCount(); j++) {
				Archive archive = db.getArchive(j);
				List<Double> sourceValues = findAvailableValues(archive, i);
				//System.out.println("We have " + sourceValues.toString() + " as source values");
				
				if (Double.isNaN(archive.getRobin(i).getValue(0))) {
					Double value = sourceValues.get(counter%sourceValues.size());
					System.out.println("We will update value " + value + " into " + dsName);
					sample.setValue(dsName, value);
					
				} else System.exit(0);

			}
		}
		counter++;
		sample.update();
		System.out.println("Now the rrd starts at " + db.getArchives()[0].getStartTime());
		System.out.println("Perform next update!");
		System.out.println("");
		System.out.println("");
		System.out.println("------------");
		Thread.sleep(50);
		writeData();
	}
	
	private List<Double> findAvailableValues (Archive archive, int robinIndex) throws IOException {
		List<Double> collection = new LinkedList<Double>();
		for (Double value : archive.getRobin(robinIndex).getValues()) {
			if (!Double.isNaN(value)) {
				collection.add(value);
			}
		}
		return collection;
	}
}
