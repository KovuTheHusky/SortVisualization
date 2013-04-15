package sortvisualization;

import java.util.Random;

public class Randomize implements Runnable {
	
	private int length;
	private int min;
	private int max;

	public Randomize(int length) {
		this.length = length;
		this.min = SortVisualization.getMinimum();
		this.max = SortVisualization.getMaximum();
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
