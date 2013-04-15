package sortvisualization;

public class RecursiveMergeSort extends Sort {
	
	Number[] array;

	public RecursiveMergeSort(Number[] array) {
		super(array);
		this.array = array;
	}

	@Override
	public void run() {
		mergeSortRecursive(array, 0, array.length);
		Window.setBusy(false);
	}
	
	private void mergeSortRecursive(Number[] array, int start, int end) {
		if (end - start <= 1)
			return;
		int middle = start + (end - start) / 2;
		mergeSortRecursive(array, start, middle);
		mergeSortRecursive(array, middle, end);
		merge(start, middle, end);
	}
	
	private void merge(int start, int middle, int end) {
		Number[] merge = new Number[end - start];
		int l = 0, r = 0, pos = 0;
		while (l < middle - start && r < end - middle) {
			if (array[start + l].lt(array[middle + r]))
				merge[pos++] = array[start + l++];
			else
				merge[pos++] = array[middle + r++];
		}
		while (r < end - middle)
			merge[pos++] = array[middle + r++];
		while (l < middle - start)
			merge[pos++] = array[start + l++];
		for (int i = 0; i < merge.length; ++i) {
			array[start++] = merge[i];
			array[start - 1].dirty();
		}
	}

}