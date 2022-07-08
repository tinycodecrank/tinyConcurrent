package de.tinycodecrank.concurrent.yield;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Supplier;

public final class YieldSupplier<Ret> implements Iterator<Ret>, AutoCloseable
{
	private final ArrayList<Supplier<Ret>>	generator;
	private final Runnable					terminator;
	
	private int index = 0;
	
	private YieldSupplier(ArrayList<Supplier<Ret>> generator, Runnable terminator)
	{
		this.generator	= generator;
		this.terminator	= terminator;
	}
	
	public static <Ret> YieldBuilder<Ret> create()
	{
		return new YieldBuilder<>();
	}
	
	@Override
	public boolean hasNext()
	{
		return index < generator.size();
	}
	
	@Override
	public Ret next()
	{
		return generator.get(index++).get();
	}
	
	/**
	 * Calls next and then close
	 * 
	 * @return The next value
	 */
	public Ret last()
	{
		final var ret = next();
		close();
		return ret;
	}
	
	public int size()
	{
		return generator.size() - index;
	}
	
	@Override
	public void close()
	{
		terminator.run();
	}
	
	@Override
	public String toString()
	{
		throw new IllegalStateException("");
	}
	
	public static final class YieldBuilder<Ret>
	{
		private final ArrayList<Supplier<Ret>> generator = new ArrayList<>();
		
		YieldBuilder()
		{}
		
		@SafeVarargs
		YieldBuilder(Supplier<Ret>... generator)
		{
			for (Supplier<Ret> g : generator)
			{
				this.generator.add(g);
			}
		}
		
		public YieldBuilder<Ret> yield(Supplier<Ret> generator)
		{
			this.generator.add(generator);
			return this;
		}
		
		public YieldSupplier<Ret> end()
		{
			return end(() ->
			{});
		}
		
		public YieldSupplier<Ret> end(Runnable terminator)
		{
			final ArrayList<Supplier<Ret>> gen = new ArrayList<>(generator.size());
			gen.addAll(generator);
			return new YieldSupplier<>(gen, terminator);
		}
	}
}