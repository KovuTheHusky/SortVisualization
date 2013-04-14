package sortvisualization;

public class InsertionSort extends Sort {
	
	Number[] array;

	public InsertionSort(Number[] array) {
		super(array);
		this.array = array;
	}

	@Override
	public void run() {
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
		Window.setBusy(false);
	}
	
}
