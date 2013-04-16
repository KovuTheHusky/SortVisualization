package sortvisualization;

public abstract class Sort implements Runnable {
	
	protected Number[] array;

	public Sort() {
		this.array = SortVisualization.getArray();
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
	
	protected boolean stop() {
		if (Window.isStopping())
			Window.stop();
		return Window.isStopping();
	}

}
