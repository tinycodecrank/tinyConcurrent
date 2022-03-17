package de.tinycodecrank.concurrent;

public final class ScheduledTask extends Thread
{
	private volatile long		startTime	= System.currentTimeMillis();
	private final Runnable		action;
	private volatile long		waitTimeMillis;
	private volatile boolean	repeats;
	private volatile boolean	canceled	= false;
	
	/**
	 * Waits until the given time has elapsed before performing the given action
	 * 
	 * @param action
	 *            The Action to perform
	 * @param waitTimeMillis
	 *            The time in milliseconds to wait before performing the action
	 * @param repeats
	 *            if true waits the specified amount of time after the action is
	 *            performed and performs it again until the task is canceled
	 */
	public ScheduledTask(Runnable action, long waitTimeMillis, boolean repeats)
	{
		this.action			= action;
		this.waitTimeMillis	= waitTimeMillis;
		this.repeats		= repeats;
		this.setDaemon(true);
	}
	
	/**
	 * @param waitTimeMillis
	 *            the time in milliseconds the scheduler should wait before
	 *            executing the provided action
	 */
	public void setWaitTime(long waitTimeMillis)
	{
		this.waitTimeMillis = waitTimeMillis;
		this.interrupt();
	}
	
	/**
	 * Cancels the execution of the provided task
	 */
	public void cancle()
	{
		this.canceled = true;
		this.interrupt();
	}
	
	/**
	 * resets the timer
	 */
	public void reset()
	{
		this.startTime = System.currentTimeMillis();
	}
	
	/**
	 * starts the scheduled Task
	 */
	@Override
	public void run()
	{
		this.canceled = false;
		do
		{
			reset();
			waitAndRun();
		}
		while (repeats && !canceled);
	}
	
	/**
	 * awaits the timer to elapse and then executes the given action provided the
	 * action was not canceled
	 */
	private void waitAndRun()
	{
		long timeLeft = timeLeft();
		while (timeLeft > 0 && !canceled)
		{
			try
			{
				Thread.sleep(timeLeft);
			}
			catch (InterruptedException e)
			{}
			timeLeft = timeLeft();
		}
		if (!canceled)
		{
			action.run();
		}
	}
	
	/**
	 * @return milliseconds until the current timer is elapsed
	 */
	private long timeLeft()
	{
		return startTime + waitTimeMillis - System.currentTimeMillis();
	}
}