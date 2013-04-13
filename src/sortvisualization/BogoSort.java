package sortvisualization;

import java.util.Random;

public class BogoSort extends Sort {

	Number[] array;
	
	public BogoSort(Number[] array) {
		super(array);
		this.array = array;
	}

	@Override
	public void run() {
		Random r = new Random();
		while (!isSorted())
			for (int i = 0; i < array.length; ++i)
				swap(i, (int)r.nextInt(array.length));
		Window.setRunning(false);
	}

}
