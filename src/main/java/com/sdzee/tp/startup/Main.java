package com.sdzee.tp.startup;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.glassfish.embeddable.Deployer;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.glassfish.embeddable.archive.ScatteredArchive;

	public class Main {

		public static void main(String[] args) throws Exception {
			
	     	String port = System.getenv("PORT");
	     	port = port != null ? port : "8080";
			GlassFishProperties gfProps = new GlassFishProperties();
			gfProps.setPort("http-listener", Integer.parseInt(port));
			
			GlassFish glassfish = GlassFishRuntime.bootstrap().newGlassFish(gfProps);
			glassfish.start();
			
			File webRoot = new File("src/main/webapp");
			File classRoot = new File("target", "classes");

			Deployer deployer = glassfish.getDeployer();
			ScatteredArchive archive = new ScatteredArchive("hello", ScatteredArchive.Type.WAR, webRoot);
			archive.addClassPath(classRoot);
			deployer.deploy(archive.toURI(), "--name=hello", "--contextroot=/");
			
	     } 
		
		public static Connection getConnection() throws URISyntaxException, SQLException {
		  
			String username = "bb60ca509a1705";
			String password = "fcd3cb7a";
		    String dbUrl = "jdbc:mysql://eu-cdbr-west-03.cleardb.net:3306/heroku_bcc81ae0aa09e3a?reconnect=true";
		    		
			/*  URI dbUri = new URI(System.getenv("CLEARDB_DATABASE_URL"));

		    String username = dbUri.getUserInfo().split(":")[0];
		    String password = dbUri.getUserInfo().split(":")[1];
		    String dbUrl = "jdbc:mysql://" + dbUri.getHost() + dbUri.getPath(); */

		    return DriverManager.getConnection(dbUrl, username, password); 
		
	      }
		
}
