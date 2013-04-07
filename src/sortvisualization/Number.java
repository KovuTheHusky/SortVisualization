package sortvisualization;

public class Number implements Comparable<Number> {
	
	private int key;
	private int value;
	private boolean highlighted;
	private boolean dirty;
	private AudioEngine audio;

	public Number(int key, int value, AudioEngine audio) {
		this.key = key;
		this.value = value;
		this.highlighted = false;
		this.dirty = true;
		this.audio = audio;
	}
	
	public String toString() {
		return "[" + key + " " + value + "]";
	}

	public int compareTo(Number that) {
		this.highlighted();
		that.highlighted();
		AudioEngine.play(this, that, 10);
		return this.value - that.value;
	}
	
	public boolean equals(Number that) {
		return this.compareTo(that) == 0;
	}
	
	public boolean lt(Number that) {
		return this.compareTo(that) < 0;
	}
	
	public boolean gt(Number that) {
		return this.compareTo(that) > 0;
	}
	
	public boolean lte(Number that) {
		return this.compareTo(that) <= 0;
	}
	
	public boolean gte(Number that) {
		return this.compareTo(that) >= 0;
	}
	
	public int compareToInt(int num) {
		Number that = new Number(-1, num, this.audio);
		return this.compareTo(that);
	}
	
	public boolean equals(int that) {
		return this.compareToInt(that) == 0;
	}
	
	public boolean lt(int that) {
		return this.compareToInt(that) < 0;
	}
	
	public boolean gt(int that) {
		return this.compareToInt(that) > 0;
	}
	
	public boolean lte(int that) {
		return this.compareToInt(that) <= 0;
	}
	
	public boolean gte(int that) {
		return this.compareToInt(that) >= 0;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void highlighted() {
		this.highlighted = true;
	}
	
	public boolean isHighlighted() {
		boolean ret = false;
		if (this.highlighted) ret = true;
		this.highlighted = false;
		return ret;
	}
	
	public void dirty() {
		this.dirty = true;
	}
	
	public boolean isDirty() {
		boolean ret = false;
		if (this.dirty) ret = true;
		this.dirty = false;
		return ret;
	}

}
