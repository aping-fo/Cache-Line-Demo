package com.cacheline;

/**
 * 伪共享例子
 * @author shiping.fu
 *
 */
public class FalseSharing implements Runnable {
	public final static int NUM_THREADS = 4; // change
	public final static long ITERATIONS = 500L * 1000L * 1000L;
	private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];
	private final int arrayIndex;

	public final static class VolatileLong {
		public volatile long value = 0L;
	}

	static {
		for (int i = 0; i < longs.length; i++) {
			longs[i] = new VolatileLong();
		}
	}

	public FalseSharing(final int arrayIndex) {
		this.arrayIndex = arrayIndex;
	}

	public static void main(final String[] args) throws Exception {
		final long start = System.currentTimeMillis();
		runTest();
		System.out.println("duration = " + (System.currentTimeMillis() - start));
	}

	private static void runTest() throws InterruptedException {
		Thread[] threads = new Thread[NUM_THREADS];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new FalseSharing(i));
		}
		for (Thread t : threads) {
			t.start();
		}
		for (Thread t : threads) {
			t.join();
		}
	}

	@Override
	public void run() {
		long i = ITERATIONS + 1;
		while (0 != --i) {
			longs[arrayIndex].value = i;
		}
	}
}
