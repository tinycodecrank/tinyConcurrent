package de.tinycodecrank.concurrent;

import java.util.function.Consumer;
import java.util.function.Function;

public final class DelayedAdaptingTask<C>
{
	private final int			timeOut;
	private final Consumer<C>	executor;
	
	private volatile C			value;
	private volatile long		waitUntil;
	private volatile boolean	abort		= false;
	private volatile boolean	finished	= false;
	
	/**
	 * Waits until the timeout has passed before performing the given task upon the
	 * given value.
	 * 
	 * @param task
	 * @param startValue
	 * @param timeOutMillis
	 *            The time in milliseconds to wait before performing the task
	 */
	public DelayedAdaptingTask(Consumer<C> task, C startValue, int timeOutMillis)
	{
		this.executor	= task;
		this.value		= startValue;
		this.timeOut	= timeOutMillis;
		this.waitUntil	= System.currentTimeMillis() + timeOutMillis;
		new Thread(this::run).start();
	}
	
	/**
	 * Performs the given function upon the currently stored value and resets the
	 * timeout as well as the abort flag.
	 * 
	 * @param updater
	 */
	public void update(Function<C, C> updater)
	{
		synchronized (executor)
		{
			abort		= false;
			value		= updater.apply(value);
			waitUntil	= System.currentTimeMillis() + timeOut;
		}
	}
	
	/**
	 * Sets the abort flag.
	 */
	public void abort()
	{
		this.abort = true;
	}
	
	/**
	 * @return true if the task has been performed or aborted
	 */
	public boolean isFinished()
	{
		return finished;
	}
	
	private void run()
	{
		sleep();
		synchronized (executor)
		{
			if (!abort)
			{
				executor.accept(value);
			}
			this.finished = true;
		}
	}
	
	private void sleep()
	{
		long currentTime = System.currentTimeMillis();
		while (!abort && currentTime < waitUntil)
		{
			long sleepTime;
			synchronized (executor)
			{
				sleepTime = waitUntil - currentTime;
			}
			try
			{
				Thread.sleep(sleepTime);
			}
			catch (InterruptedException e)
			{}
			synchronized (executor)
			{
				currentTime = System.currentTimeMillis();
			}
		}
	}
}