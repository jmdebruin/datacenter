/*
 *	Copyright (C) 2017 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib;

import java.util.Date;
import java.util.Random;

public class Task extends Thread {
	
	public String id;
	public String name;
	
	/**
	 * Set the interval time.
	 * @param interval
	 */
	public void setInterval(int interval){
		this.interval = interval;
	}
	
	//public String[] getLog(int n){
	//	return logger.getLog(n);
	//}
	
	/**
	 * Prepares the task for doRun()
	 * Override this!
	 */
	protected void doInit(){
		state=STATE.RUNNING;
		//prepare something
	}
	
	/**
	 * Runs the task;
	 * Set the state to RESETING if something fails;
	 * Override this!
	 */
	protected void doRun(){
		//on failure set state to restart
	}

	/**
	 * Prepares the task for a new doInit();
	 * Override this!
	 */
	protected void doReset(){
		state=STATE.STARTING;
	}
	
	/**
	 * Cleanup for a shutdown
	 * Override this!
	 */
	protected void doStop(){
		state=STATE.STOPPING;

	}
	

	/**
	 * Sleep until its time to do a new run. 
	 * @param dt : Runtime to distract
	 */
	private void doSleep(long interval){
		try {
			Thread.sleep(interval+ generator.nextInt((int) (interval / 2 )) - interval / 4);
		} catch (InterruptedException e) { state=STATE.STOPPING; }
	}

	@Override
	public void run() {
		doSleep(startDelay);
		while (true){
			Long t=new Date().getTime();
			switch (state) {
			case STARTING:
				doInit();
				doSleep(startDelay);
				break;
			case RESETING:
				doReset();
				doSleep(resetDelay);
				break;
			case RUNNING:
				doRun();
				doSleep(interval + new Date().getTime() - t);
				break;
			case STOPPING:
				doStop();
				return;
			}
		}
	}
	
	protected static enum STATE {STARTING, RESETING, RUNNING, STOPPING}
	protected STATE state = STATE.STARTING;
	
	private static Random generator = new Random();
	private long fiveMinutes = 5 * 60 * 1000;
	private long interval   = fiveMinutes;
	private long startDelay = fiveMinutes;
	private long resetDelay = fiveMinutes;
}

