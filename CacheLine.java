package com.cacheline;

/**
 * 填充案例
 * @author shiping.fu
 *
 */
public class CacheLine implements Runnable {
	public final static int NUM_THREADS = 4; // change
	public final static long ITERATIONS = 500L * 1000L * 1000L;
	private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];
	private final int arrayIndex;

	public final static class VolatileLong {

		public long p8, p9, p10, p11, p12, p13, p14; // 填充
		public volatile long value = 0L;
		public long p1, p2, p3, p4, p5, p6, p7; //填充
	}

	static {
		for (int i = 0; i < longs.length; i++) {
			longs[i] = new VolatileLong();
		}
	}

	public CacheLine(final int arrayIndex) {
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
			threads[i] = new Thread(new CacheLine(i));
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
