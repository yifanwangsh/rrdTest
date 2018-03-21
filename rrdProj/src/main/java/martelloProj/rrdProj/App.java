package org.rrd4j.core;

import java.io.IOException;
import java.sql.Date;

import org.json.JSONObject;

import com.martellotech.marwatch.rrd.RRDFileStore.InvalidKeyName;
import com.martellotech.marwatch.rrd.RRDFileStore.NotFound;

public class App {
	public static void main(String[] args) throws IOException, InvalidKeyName, NotFound, InterruptedException{
		
		String srcbasedir = "C:/var/lib/marwatch/rrd";
		String srcguid = "f8a794fe-84bb-4704-9ae6-4c00a16d9cb8";
		String srcname = "siptraffic.inPegs";
		
		String dstbasedir = "C:/var/lib/marwatch/rrd";
		String dstguid = "31f8b761-a8e5-44a8-8968-76d097d3e8c9";
		String dstname = srcname;
		Long now = System.currentTimeMillis()/1000L;
		
		//WriteRrdData wrd = new WriteRrdData(srcbasedir, srcguid, srcname, dstbasedir, dstguid, dstname, now);
		//wrd.writeData();
		
		//CopyRrdData cdb = new CopyRrdData(dstbasedir, dstguid, dstname, now);
		//cdb.writeData();
		
		InspectRrdData ird = new InspectRrdData(srcbasedir, srcguid, srcname);
		ird.printUpdateInfo();
		ird.printValues();
		
		//RrdExperimentDb red = new RrdExperimentDb(srcbasedir, srcguid, srcname);
		//System.out.println(red.getArcSteps());
	}	
}
//https://ywang.sipseller.net/dashboard/container/?template=device&device=567730e0-1e5c-4f5b-afe4-8f689e512aea#start=1521486210000&end=1552997163000
