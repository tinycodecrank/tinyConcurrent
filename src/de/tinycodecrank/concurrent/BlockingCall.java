package de.tinycodecrank.concurrent;

import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Predicate;

import de.tinycodecrank.monads.opt.Opt;

public final class BlockingCall<R>
{
	private volatile Opt<R>		result	= Opt.empty();
	private volatile boolean	done	= false;
	
	private BlockingCall()
	{}
	
	public static <R> Opt<R> runAndWait(Consumer<Consumer<R>> call)
	{
		BlockingCall<R> handler = new BlockingCall<>();
		call.accept(handler::listen);
		
		synchronized (handler)
		{
			while (!handler.done)
			{
				try
				{
					handler.wait();
				}
				catch (InterruptedException e)
				{}
			}
		}
		return handler.result;
	}
	
	public static <R> Opt<R> runAndWait(Predicate<Consumer<R>> call)
	{
		BlockingCall<R> handler = new BlockingCall<>();
		if (call.test(handler::listen))
		{
			synchronized (handler)
			{
				while (!handler.done)
				{
					try
					{
						handler.wait();
					}
					catch (InterruptedException e)
					{}
				}
			}
		}
		return handler.result;
	}
	
	public static <R> Opt<R> runAndWait(Consumer<Consumer<R>> call, long timeout) throws TimeoutException
	{
		BlockingCall<R>	handler	= new BlockingCall<>();
		Timer			timer	= new Timer(timeout);
		call.accept(handler::listen);
		synchronized (handler)
		{
			timer.doWhile(Opt.of(handler::isWorking), handler::waitTime);
		}
		return handler.result;
		
	}
	
	public static <R> Opt<R> runAndWait(Predicate<Consumer<R>> call, long timeout) throws TimeoutException
	{
		BlockingCall<R>	handler	= new BlockingCall<>();
		Timer			timer	= new Timer(timeout);
		if (call.test(handler::listen))
		{
			synchronized (handler)
			{
				timer.doWhile(Opt.of(handler::isWorking), handler::waitTime);
			}
		}
		return handler.result;
	}
	
	private void waitTime(long timeLeft)
	{
		try
		{
			wait(timeLeft);
		}
		catch (InterruptedException e)
		{}
	}
	
	private void listen(R r)
	{
		synchronized (this)
		{
			this.result	= Opt.of(r);
			this.done	= true;
			this.notifyAll();
		}
	}
	
	private boolean isWorking()
	{
		return !done;
	}
}