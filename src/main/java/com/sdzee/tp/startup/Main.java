package com.sdzee.tp.startup;

import java.io.File;

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
			deployer.deploy(archive.toURI()); 
		/*	deployer.deploy(archive.toURI(), "--name=hello", "--contextroot=/"); 	*/
         
           
		/* 	File file = new File("tp10-0.0.1-SNAPSHOT.war"); 

			deployer.deploy(file); */
			
	     } 
		
}
