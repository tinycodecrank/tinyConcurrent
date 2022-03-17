package de.tinycodecrank.concurrent.defer;

import java.util.function.Supplier;

import de.tinycodecrank.collections.Stack;

public final class Defer
{
	public static Defer deferResult()
	{
		return new Defer();
	}
	
	public static DeferVoid deferVoid()
	{
		return new DeferVoid();
	}
	
	private final Stack<Runnable> stack = new Stack<>();
	
	private Defer()
	{}
	
	public void def(Runnable action)
	{
		stack.push(action);
	}
	
	public <Ret> Ret ret(Supplier<Ret> ret)
	{
		final Ret result = ret.get();
		stack.forEach(Runnable::run);
		return result;
	}
	
	public static final class DeferVoid
	{
		private final Stack<Runnable> stack = new Stack<>();
		
		private DeferVoid()
		{}
		
		public DeferVoid def(Runnable action)
		{
			stack.push(action);
			return this;
		}
		
		public void ret()
		{
			stack.forEach(Runnable::run);
		}
	}
}