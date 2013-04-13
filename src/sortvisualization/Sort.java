package sortvisualization;

public abstract class Sort implements Runnable {
	
	private Number[] array;

	public Sort(Number[] array) {
		this.array = array;
	}

	@Override
	public abstract void run();

	protected boolean isSorted() {
		if (array.length <= 1)
			return true;
		for (int i = 1; i < array.length; ++i)
			if (array[i].lt(array[i - 1]))
				return false;
		return true;
	}
	
	protected void swap(int a, int b) {
		array[a].dirty();
		array[b].dirty();
		Number temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}

}
