package org.rrd4j.core;

import java.io.IOException;
import java.sql.Date;

public class RrdMain {
	public static void main(String[] args) throws IOException, InterruptedException{
		
		String srcbasedir = "C:/var/lib/marwatch/rrd";
		String srcguid = "#07541f9f-3051-4c97-877c-22beba95d47d";
		String srcname = "siptraffic.inPegs";
		
		String dstbasedir = "C:/var/lib/marwatch/rrd";
		String dstguid = "7de5bbd5-5037-4a8a-8362-486e2ca84271";
		String dstname = srcname;
		Long now = System.currentTimeMillis()/1000L;
		
		//WriteRrdData wrd = new WriteRrdData(srcbasedir, srcguid, srcname, dstbasedir, dstguid, dstname, now);
		//wrd.writeData();
		
		//CopyRrdData cdb = new CopyRrdData(dstbasedir, dstguid, dstname, now);
		//cdb.writeData();
		
		InspectRrdData ird = new InspectRrdData(srcbasedir, dstguid, srcname);
		//ird.printUpdateInfo();
		ird.printValues();
		
		//RrdExperimentDb red = new RrdExperimentDb(srcbasedir, srcguid, srcname);
		//System.out.println(red.getArcSteps());
	}	
}
//https://ywang.sipseller.net/dashboard/container/?template=device&device=567730e0-1e5c-4f5b-afe4-8f689e512aea#start=1521486210000&end=1552997163000
//https://ywang.sipseller.net/dashboard/container/?template=device&device=657884f9-aa44-4ebf-bda7-57d5be81e06e#start=1521849600000&end=1529712000000