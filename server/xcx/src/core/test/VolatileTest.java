package core.test;

public class VolatileTest 
{
	
	public static volatile int race = 0;
	
	public static final int THREADS_COUNT = 20;
	
	public synchronized static void increase()
	{
		race++;
	}
	

	public static void main(String[] args) 
	{
		Thread[] threads = new Thread[THREADS_COUNT];
		for(Thread thread : threads)
		{
			thread = new Thread(new Runnable() {
				
				@Override
				public void run() 
				{
					for(int i = 0; i < 10000; i++)
					{
						increase();
					}
					
				}
			});
			thread.start();
		}
		
		while(Thread.activeCount() > 1)
		{
			Thread.yield();
		}
		
		System.out.println("race: " + race);

	}

}
