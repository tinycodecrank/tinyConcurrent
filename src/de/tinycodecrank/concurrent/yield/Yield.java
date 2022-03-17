package de.tinycodecrank.concurrent.yield;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Yield
{
	@SafeVarargs
	public static <Ret> YieldSupplier.YieldBuilder<Ret> create(Supplier<Ret>... generator)
	{
		return new YieldSupplier.YieldBuilder<Ret>(generator);
	}
	
	public static <Ret> YieldSupplier<Ret> empty0()
	{
		return new YieldSupplier.YieldBuilder<Ret>().end();
	}
	
	public static <Ret> YieldSupplier<Ret> close(Runnable terminator)
	{
		return new YieldSupplier.YieldBuilder<Ret>().end(terminator);
	}
	
	@SafeVarargs
	public static <P, Ret> SingleYield.YieldBuilder<P, Ret> create(Function<P, Ret>... generator)
	{
		return new SingleYield.YieldBuilder<P, Ret>(generator);
	}
	
	public static <P, Ret> SingleYield<P, Ret> empty1()
	{
		return new SingleYield.YieldBuilder<P, Ret>().end();
	}
	
	public static <P, Ret> SingleYield<P, Ret> close(Consumer<P> terminator)
	{
		return new SingleYield.YieldBuilder<P, Ret>().end(terminator);
	}
	
	@SafeVarargs
	public static <P1, P2, Ret> BiYield.YieldBuilder<P1, P2, Ret> create(BiFunction<P1, P2, Ret>... terminator)
	{
		return new BiYield.YieldBuilder<P1, P2, Ret>(terminator);
	}
	
	public static <P1, P2, Ret> BiYield<P1, P2, Ret> empty2()
	{
		return new BiYield.YieldBuilder<P1, P2, Ret>().end();
	}
	
	public static <P1, P2, Ret> BiYield<P1, P2, Ret> close(BiConsumer<P1, P2> terminator)
	{
		return new BiYield.YieldBuilder<P1, P2, Ret>().end(terminator);
	}
}