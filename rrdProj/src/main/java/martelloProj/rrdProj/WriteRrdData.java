package org.rrd4j.core;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class WriteRrdData {
	public WriteRrdData(String srcBasedir, String srcGuid, String srcName,
			String dstBasedir, String dstGuid, String dstName, long startTime) throws IOException {
		this.src = new RrdExperimentDb(srcBasedir, srcGuid, srcName);
		this.dst = new RrdExperimentDb(dstBasedir, dstGuid, dstName);
		
		this.startTime = startTime;
	}
	
	private RrdExperimentDb src;
	private RrdExperimentDb dst;
	private long startTime;
	
	public void writeData() throws IOException, InterruptedException {
		int counter = 0;
		while(!dst.allFilled()) {
			counter++;
			System.out.println("The last update time for dst rrd file is " + dst.getLastArchiveUpdateTime());
			final Sample sample = dst.generateSample(startTime, counter);
			
			for (int i = 0; i < dst.getDb().getDsCount(); i++) {
				if (!src.getDb().getDatasource(i).getName().equals(dst.getDb().getDatasource(i).getName())) {
					throw new IllegalArgumentException("Expecting datasource to be the same");
				}
				Datasource ds = src.getDb().getDatasource(i);
				String dsName = ds.getName();
				
				List<Double> values = src.findAvailableValues(i);
				if (values.isEmpty()) {
					System.out.println("Datasource " + dsName + " is empty");
					continue;
				}
				Double value = values.get(counter % values.size());
				System.out.println("Now updating " + dsName + " with value " + value);
				sample.setValue(i, value);
			}
			
			sample.update();
			System.out.println("Now the rrd starts at " + dst.getHighFrequencyStartTime());
			System.out.println("Perform next update!");
			System.out.println("");
			System.out.println("");
			System.out.println("------------");
		}
	}
}
