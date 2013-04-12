package sortvisualization;

public class Sort implements Runnable {
	
	Number[] array;

	public Sort() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		array = SortVisualization.getArray();
		if (array.length <= 1)
			return;
		for (int i = 0; i < array.length; ++i) {
			Number valueToInsert = array[i];
			int holePos = i;
			while (holePos > 0 && array[holePos].lt(array[holePos - 1])) {
				swap(holePos, holePos - 1);
				--holePos;
			}
			array[holePos] = valueToInsert;
			array[holePos].dirty();
		}
	}
	
	private void swap(int a, int b) {
		array[a].dirty();
		array[b].dirty();
		Number temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}

}
