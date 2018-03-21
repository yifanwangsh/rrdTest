package org.rrd4j.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RrdExperimentDb {
	public RrdExperimentDb(String basedir, String guid, String name) throws IOException {
		this.basedir = basedir;
		this.guid = guid;
		this.name = name;
		this.path = basedir + "/" + guid + "/" + name + ".rrd";
		RrdSyncThreadPool pool = new RrdSyncThreadPool();
		RrdNioBackendFactory factory = new RrdNioBackendFactory();
		factory.setSyncThreadPool(pool);
		this.db = new RrdDb(path, false, factory);
	}
	
	private String basedir;
	private String guid;
	private String name;
	private String path;
	private RrdDb db;
	
	public long getLastArchiveUpdateTime() throws IOException {
		return this.db.getLastArchiveUpdateTime();
	}
	
	public long getHighFrequencyStartTime() throws IOException {
		return findHighFrequencyArchive().getStartTime();
	}
	
	public RrdDb getDb() {
		return this.db;
	}
	
	public List<Double> findAvailableValues(int dsIndex) throws IOException {
		List<Double> collection = new ArrayList<Double>();
		Archive archive = findHighFrequencyArchive();
		for (Double value : archive.getRobin(dsIndex).getValues()) {
			if (!Double.isNaN(value)) {
				collection.add(value);
			}
		}
		return collection;
	}
	
	private Archive findHighFrequencyArchive() throws IOException {
		for (Archive archive : db.getArchives()) {
			if (archive.getSteps() == 1) {
				return archive;
			}
		}
		throw new IllegalArgumentException();
	}
	
	public List<Long> getArcSteps() throws IOException {
		List<Long> collection = new ArrayList<Long>();
		for (Archive archive : db.getArchives()) {
			collection.add(archive.getArcStep());
		}
		return collection;
	}
	
	private Archive getArchiveBySteps(long arcStep) throws IOException {
		for (Archive archive : db.getArchives()) {
			if (archive.getArcStep() == arcStep) return archive;
		}
		throw new IllegalArgumentException();
	}
	
	public Sample generateSample(long startTime, int sampleCount) throws IOException {
		long min = Long.MAX_VALUE;
		for (long steps : getArcSteps()) {
			if (steps <= min) min = steps;
		}
		
		final Sample sample = db.createSample(startTime + sampleCount * min);
		return sample;
	}
	
	public boolean allFilled() throws IOException {
		
		long mostSteps = 0L;
		for (long steps : getArcSteps()) {
			if (steps > mostSteps) mostSteps = steps;
		}
		Archive archive = getArchiveBySteps(mostSteps);
		for (int i = 0; i < db.getDsCount(); i++) {
			if (Double.isNaN(archive.getRobin(i).getValue(0)) && !db.getDatasources()[i].getName().equals("_PROTOTYPE"))
				return false;
		}
		return true;
	}
}
