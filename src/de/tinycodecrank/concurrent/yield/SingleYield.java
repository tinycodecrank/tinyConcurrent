package de.tinycodecrank.concurrent.yield;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public final class SingleYield<Params, Ret>
{
	private final ArrayList<Function<Params, Ret>>	generator;
	private final Consumer<Params>					terminator;
	
	private int index = 0;
	
	private SingleYield(ArrayList<Function<Params, Ret>> generator, Consumer<Params> terminator)
	{
		this.generator	= generator;
		this.terminator	= terminator;
	}
	
	public static <Params, Ret> YieldBuilder<Params, Ret> create()
	{
		return new YieldBuilder<>();
	}
	
	public boolean hasNext()
	{
		return index < generator.size();
	}
	
	public Ret next(Params params)
	{
		return generator.get(index++).apply(params);
	}
	
	public int size()
	{
		return generator.size() - index;
	}
	
	public void close(Params params)
	{
		terminator.accept(params);
	}
	
	@Override
	public String toString()
	{
		throw new IllegalStateException("");
	}
	
	public static final class YieldBuilder<Params, Ret>
	{
		private final ArrayList<Function<Params, Ret>> generator = new ArrayList<>();
		
		YieldBuilder()
		{}
		
		@SafeVarargs
		YieldBuilder(Function<Params, Ret>... generator)
		{
			for (Function<Params, Ret> g : generator)
			{
				this.generator.add(g);
			}
		}
		
		public YieldBuilder<Params, Ret> yield(Function<Params, Ret> generator)
		{
			this.generator.add(generator);
			return this;
		}
		
		public SingleYield<Params, Ret> end()
		{
			return end(_p ->
			{});
		}
		
		public SingleYield<Params, Ret> end(Consumer<Params> terminator)
		{
			final ArrayList<Function<Params, Ret>> gen = new ArrayList<>(generator.size());
			gen.addAll(generator);
			return new SingleYield<>(gen, terminator);
		}
	}
}