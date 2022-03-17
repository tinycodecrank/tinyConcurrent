package de.tinycodecrank.concurrent.yield;

import java.util.ArrayList;

import de.tinycodecrank.functions.TriFunction;
import de.tinycodecrank.functions.void_.TriConsumer;

public final class TriYield<P1, P2, P3, Ret>
{
	private final ArrayList<TriFunction<P1, P2, P3, Ret>>	generator;
	private final TriConsumer<P1, P2, P3>					terminator;
	
	private int index = 0;
	
	private TriYield(ArrayList<TriFunction<P1, P2, P3, Ret>> generator, TriConsumer<P1, P2, P3> terminator)
	{
		this.generator	= generator;
		this.terminator	= terminator;
	}
	
	public static <P1, P2, P3, Ret> YieldBuilder<P1, P2, P3, Ret> create()
	{
		return new YieldBuilder<>();
	}
	
	public boolean hasNext()
	{
		return index < generator.size();
	}
	
	public Ret next(P1 p1, P2 p2, P3 p3)
	{
		return generator.get(index++).apply(p1, p2, p3);
	}
	
	public int size()
	{
		return generator.size() - index;
	}
	
	public void close(P1 p1, P2 p2, P3 p3)
	{
		terminator.accept(p1, p2, p3);
	}
	
	@Override
	public String toString()
	{
		throw new IllegalStateException("");
	}
	
	public static final class YieldBuilder<P1, P2, P3, Ret>
	{
		private final ArrayList<TriFunction<P1, P2, P3, Ret>> generator = new ArrayList<>();
		
		YieldBuilder()
		{}
		
		@SafeVarargs
		YieldBuilder(TriFunction<P1, P2, P3, Ret>... generator)
		{
			for (TriFunction<P1, P2, P3, Ret> g : generator)
			{
				this.generator.add(g);
			}
		}
		
		public YieldBuilder<P1, P2, P3, Ret> yield(TriFunction<P1, P2, P3, Ret> generator)
		{
			this.generator.add(generator);
			return this;
		}
		
		public TriYield<P1, P2, P3, Ret> end()
		{
			return end((p1, p2, p3) ->
			{});
		}
		
		public TriYield<P1, P2, P3, Ret> end(TriConsumer<P1, P2, P3> terminator)
		{
			final ArrayList<TriFunction<P1, P2, P3, Ret>> gen = new ArrayList<>(generator.size());
			gen.addAll(generator);
			return new TriYield<>(gen, terminator);
		}
	}
}