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
			
			/*Deployer deployer = glassfish.getDeployer();            
			File file = new File("tp9-0.0.1-SNAPSHOT.war");      
			deployer.deploy(file); */
		}
		
		public static Connection getConnection() throws URISyntaxException, SQLException {
		    URI dbUri = new URI(System.getenv("CLEARDB_DATABASE_URL"));

		    String username = dbUri.getUserInfo().split(":")[0];
		    String password = dbUri.getUserInfo().split(":")[1];
		    String dbUrl = "jdbc:mysql://" + dbUri.getHost() + dbUri.getPath();

		    return DriverManager.getConnection(dbUrl, username, password);
		
	}
		
}