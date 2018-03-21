package org.rrd4j.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InspectRrdData {
	public InspectRrdData (String basedir, String guid, String name) throws IOException {
		this.basedir = basedir;
		this.guid = guid;
		this.name = name;
		this.rrdPath = basedir + "/" + guid + "/"+ name + ".rrd";
		RrdSyncThreadPool pool = new RrdSyncThreadPool();
		RrdNioBackendFactory factory = new RrdNioBackendFactory();
		factory.setSyncThreadPool(pool);
		this.db = new RrdDb (rrdPath, true, factory);
	}
	
	private String basedir;
	private String guid;
	private String name;
	private String rrdPath;
	private RrdDb db;
	
	public void printDs() throws IOException {
		for (Datasource ds : db.getDatasources()) {
			System.out.println("One of the datasources is " + ds.getName());
		}
	}
	
	public void printNonNaNValue(Archive archive) throws IOException {
		for (int i = 0; i < archive.getParentDb().getDsCount(); i++) {
			List<Double> collection = new ArrayList<Double>();
			for (Double value : archive.getRobin(i).getValues()) {
				if (!Double.isNaN(value)) {
					collection.add(value);
				}
			}
			
			System.out.println("The datasource " + archive.getParentDb().getDatasource(i).getName());;
			System.out.println("has value " + collection + " available");
			System.out.println("-----");
		}
	}
	
	public RrdDb getDb() {
		return this.db;
	}
	
	public void printValues() throws IOException {
		for (Archive archive : db.getArchives()) {
			System.out.println("Now printing archive with step " + archive.getArcStep() + "\n");
			printNonNaNValue(archive);
			System.out.println(" ");
		}
	}
	
	public void printUpdateInfo() throws IOException {
		System.out.println("This rrd file is last updated on " + db.getLastUpdateTime());
		for (Archive archive : db.getArchives()) {
			System.out.println("The archive of step " + archive.getArcStep() + " is last updated on " + archive.getEndTime());
			System.out.println("Now it starts on " + archive.getStartTime());
			if(isEmpty(archive)) {
				System.out.println("This archive is empty");
			} else {
				System.out.println("This archive is not empty");
			}
			System.out.println("It stores " + archive.getRows() + " rows of data!");
			System.out.println("-------");
		}
	}
	
	private boolean isEmpty(Archive archive) throws IOException {
		List<Double> collection = new ArrayList<Double>();
		for (int i = 0; i < db.getDsCount(); i++) {
			for (Double value : archive.getRobin(i).getValues()) {
				if (!Double.isNaN(value)) {
					collection.add(value);
				}
			}
		}
		return collection.isEmpty();
	}
}
