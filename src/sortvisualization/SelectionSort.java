package sortvisualization;

public class SelectionSort extends Sort {
	
	private Number[] array;

	public SelectionSort(Number[] array) {
		super(array);
		this.array = array;
	}

	@Override
	public void run() {
		array = SortVisualization.getArray();
		if (array.length <= 1)
			return;
		for (int i = 0; i < array.length; ++i) {
			int min = i;
			for (int j = i + 1; j < array.length; ++j) {
				if (array[j].lt(array[min]))
					min = j;
			}
			if (min != i)
				swap(i, min);
		}
		Window.setRunning(false);
	}

}
