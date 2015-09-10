package de.bytevalue.hb4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class ThreadPool {
	private static ThreadPool singleton;
	
	private ExecutorService pool;
	
	private ThreadPool() {
		this.pool = Executors.newCachedThreadPool();
	}
	
	static {
		singleton = new ThreadPool();
	}
	
	public final void init() {
		this.shutdown();
		this.pool = Executors.newCachedThreadPool();
	}
	
	public final void execute(Runnable command) {
		this.pool.execute(command);
	}
	
	public final void shutdown() {
		if(this.pool == null) {
			return;
		}
		
		if(this.pool.isShutdown()) {
			return;
		}
		
		try {
			this.pool.awaitTermination(1000, TimeUnit.MILLISECONDS);
		}
		catch (InterruptedException e) {
			// Ignore
		}
		
		this.pool.shutdown();
	}
	
	public static final ThreadPool getInstance() {
		return singleton;
	}
}
