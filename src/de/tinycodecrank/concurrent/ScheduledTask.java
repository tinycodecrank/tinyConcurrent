package de.tinycodecrank.concurrent;

public final class ScheduledTask extends Thread
{
	private volatile long startTime = System.currentTimeMillis();
	private final Runnable action;
	private volatile long waitTimeMillis;
	private volatile boolean repeats;
	private volatile boolean cancled = false;
	
	public ScheduledTask(Runnable action, long waitTimeMillis, boolean repeats)
	{
		this.action = action;
		this.waitTimeMillis = waitTimeMillis;
		this.repeats = repeats;
		this.setDaemon(true);
	}
	
	public void setWaitTime(long waitTimeMillis)
	{
		this.waitTimeMillis = waitTimeMillis;
		this.interrupt();
	}
	
	public void cancle()
	{
		this.cancled = true;
		this.interrupt();
	}
	
	public void reset()
	{
		this.startTime = System.currentTimeMillis();
	}
	
	@Override
	public void run()
	{
		this.cancled = false;
		do
		{
			reset();
			waitAndRun();
		}
		while(repeats && !cancled);
	}
	
	private void waitAndRun()
	{
		long timeLeft = timeLeft();
		while(timeLeft > 0 && !cancled)
		{
			try
			{
				Thread.sleep(timeLeft);
			}
			catch(InterruptedException e){}
			timeLeft = timeLeft();
		}
		if(!cancled)
		{
			action.run();
		}
	}
	
	private long timeLeft()
	{
		return startTime + waitTimeMillis - System.currentTimeMillis();
	}
}