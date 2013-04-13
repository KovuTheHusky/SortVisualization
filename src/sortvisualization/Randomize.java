package sortvisualization;

import java.util.Random;

public class Randomize implements Runnable {
	
	private int length;
	private int min;
	private int max;

	public Randomize(int length, int min, int max) {
		this.length = length;
		this.min = min;
		this.max = max;
	}

	@Override
	public void run() {
		Random random = new Random();
		Number[] array = new Number[length];
		for (int i = 0; i < array.length; ++i)
			array[i] = new Number(random.nextInt(this.max - this.min) + this.min);
		SortVisualization.setArray(array);
	}

}
