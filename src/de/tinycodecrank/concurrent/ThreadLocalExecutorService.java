package de.tinycodecrank.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ThreadLocalExecutorService implements ExecutorService
{
	@Override
	public void execute(Runnable command)
	{
		command.run();
	}
	
	@Override
	public void shutdown()
	{}
	
	@Override
	public List<Runnable> shutdownNow()
	{
		return null;
	}
	
	@Override
	public boolean isShutdown()
	{
		return true;
	}
	
	@Override
	public boolean isTerminated()
	{
		return true;
	}
	
	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
	{
		return true;
	}
	
	@Override
	public <T> Future<T> submit(Callable<T> task)
	{
		throw new UnsupportedOperationException("Method not supported");
	}
	
	@Override
	public <T> Future<T> submit(Runnable task, T result)
	{
		throw new UnsupportedOperationException("Method not supported");
	}
	
	@Override
	public Future<?> submit(Runnable task)
	{
		throw new UnsupportedOperationException("Method not supported");
	}
	
	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException
	{
		throw new UnsupportedOperationException("Method not supported");
	}
	
	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
		throws InterruptedException
	{
		throw new UnsupportedOperationException("Method not supported");
	}
	
	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException
	{
		throw new UnsupportedOperationException("Method not supported");
	}
	
	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
		throws InterruptedException,
		ExecutionException,
		TimeoutException
	{
		throw new UnsupportedOperationException("Method not supported");
	}
	
}
