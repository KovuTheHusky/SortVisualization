package sortvisualization;

public class InsertionSort extends Sort {
	
	public InsertionSort() {
		super();
	}

	@Override
	public void run() {
		if (array.length <= 1)
			return;
		for (int i = 0; i < array.length; ++i) {
			Number valueToInsert = array[i];
			int holePos = i;
			while (holePos > 0 && array[holePos].lt(array[holePos - 1])) {
				if (this.stop()) return;
				swap(holePos, holePos - 1);
				--holePos;
			}
			array[holePos] = valueToInsert;
			array[holePos].dirty();
		}
	}
	
}
