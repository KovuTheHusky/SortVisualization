package sortvisualization;

import java.util.Random;

public class Randomize implements Runnable {
	private Window window;
	private Number[] array;

	public Randomize(Window window) {
		this.window = window;
		this.array = window.getArray();
	}

	@Override
	public void run() {
		Random random = new Random();
		for (int i = 0; i < array.length; ++i)
			array[i] = new Number(random.nextInt(window.getMaximum() - 1) + 1, window.getAudioEngine());
	}
}
