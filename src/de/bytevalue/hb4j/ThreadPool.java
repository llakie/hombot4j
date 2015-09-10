package de.bytevalue.hb4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class ThreadPool {
	private static ThreadPool singleton;
	
	private ExecutorService pool;
	
	private ThreadPool() {
		this.reinit();
	}
	
	public synchronized final void reinit() {
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
	
	public synchronized static final ThreadPool getInstance() {
		if(ThreadPool.singleton == null) {
			ThreadPool.singleton = new ThreadPool();
		}
		
		return ThreadPool.singleton;
	}
}
