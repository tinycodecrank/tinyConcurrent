package de.tinycodecrank.concurrent.yield;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public final class BiYield<P1, P2, Ret>
{
	private final ArrayList<BiFunction<P1, P2, Ret>>	generator;
	private final BiConsumer<P1, P2>					terminator;
	
	private int index = 0;
	
	private BiYield(ArrayList<BiFunction<P1, P2, Ret>> generator, BiConsumer<P1, P2> terminator)
	{
		this.generator	= generator;
		this.terminator	= terminator;
	}
	
	public static <P1, P2, Ret> YieldBuilder<P1, P2, Ret> create()
	{
		return new YieldBuilder<>();
	}
	
	public boolean hasNext()
	{
		return index < generator.size();
	}
	
	public Ret next(P1 p1, P2 p2)
	{
		return generator.get(index++).apply(p1, p2);
	}
	
	public int size()
	{
		return generator.size() - index;
	}
	
	public void close(P1 p1, P2 p2)
	{
		terminator.accept(p1, p2);
	}
	
	@Override
	public String toString()
	{
		throw new IllegalStateException("");
	}
	
	public static final class YieldBuilder<P1, P2, Ret>
	{
		private final ArrayList<BiFunction<P1, P2, Ret>> generator = new ArrayList<>();
		
		YieldBuilder()
		{}
		
		@SafeVarargs
		YieldBuilder(BiFunction<P1, P2, Ret>... generator)
		{
			for (BiFunction<P1, P2, Ret> g : generator)
			{
				this.generator.add(g);
			}
		}
		
		public YieldBuilder<P1, P2, Ret> yield(BiFunction<P1, P2, Ret> generator)
		{
			this.generator.add(generator);
			return this;
		}
		
		public BiYield<P1, P2, Ret> end()
		{
			return end((p1, p2) ->
			{});
		}
		
		public BiYield<P1, P2, Ret> end(BiConsumer<P1, P2> terminator)
		{
			final ArrayList<BiFunction<P1, P2, Ret>> gen = new ArrayList<>(generator.size());
			gen.addAll(generator);
			return new BiYield<>(gen, terminator);
		}
	}
}