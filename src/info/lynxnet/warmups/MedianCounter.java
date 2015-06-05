package info.lynxnet.warmups;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class MedianCounter {
	private List<Integer> items;
	private TreeMap<Integer, Integer> lesserItems;
	private TreeMap<Integer, Integer> biggerItems;
	private int counter;
	private int median;
	
	public MedianCounter() {
		items = new LinkedList<Integer>();
		lesserItems = new TreeMap<Integer, Integer>();
		biggerItems = new TreeMap<Integer, Integer>();
		counter = 0;
		median = 0;
	}
	
	/**
	 * Returns a median 
	 */
	int addItem(int item) {
		items.add(item);
		if (items.size() == 1) {
			median = item;
			lesserItems.put(item, 1);
			return median;
		}
		
		if (items.size() % 2 == 0) {
			if (item > median) {
				addToTheMap(biggerItems, item);
				// no further action necessary, the "true" median is the "border" element    
			} else {
				addToTheMap(lesserItems, item);
				int top = lesserItems.lastKey();
				removeFromTheMap(lesserItems, top);
				addToTheMap(biggerItems, top);
			}
		} else {
			if (item <= median) {
				addToTheMap(lesserItems, item);
				// no further action necessary    
			} else {
				addToTheMap(biggerItems, item);
				int bottom = biggerItems.firstKey();
				removeFromTheMap(biggerItems, bottom);
				addToTheMap(lesserItems, bottom);
			}
		}
		median = lesserItems.lastKey();
		return median;
	}

	int getBagSize(Map<Integer, Integer> map) {
		int result = 0;
		for (int count : map.values()) {
			result += count;
		}
		return result;
	}
	
	void addToTheMap(TreeMap<Integer, Integer> map, Integer item) {
		if (map.containsKey(item)) {
			int counter = map.get(item);
			map.put(item, counter + 1);
		} else {
			map.put(item, 1);
		}
	}
	
	void removeFromTheMap(TreeMap<Integer, Integer> map, Integer item) {
		if (map.containsKey(item)) {
			int counter = map.get(item);
			if (counter == 1) {
				map.remove(item);
				return;
			}
			map.put(item, counter - 1);
		}
	}
	
	static void prettyPrint(String prefix, Map<Integer, Integer> map) {
		System.out.print(prefix);
		System.out.print("[");
		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			String val = entry.getKey() + " ";
			for (int i = 0; i < entry.getValue(); i++) {
				System.out.print(val);
			}
		}
		System.out.println("]");
	}

	public static void main(String[] args) {
		MedianCounter counter = new MedianCounter();
		Random rand = new Random();
		
		for (int i = 0; i < 1000; i++) {
			int item = rand.nextInt(1000);
			int median = counter.addItem(item);
			System.out.println("item = " + item);
			System.out.println("median = " + median);
			Integer[] items = counter.items.toArray(new Integer[0]);
			System.out.println(Arrays.deepToString(items));
			Arrays.sort(items);
			System.out.println("Sorted: " + Arrays.deepToString(items));

			int trueMedian;
			if (items.length % 2 == 1) {
				trueMedian = items[Math.max(0, (items.length / 2))];
				System.out.println("True median: " + trueMedian);
			} else {
				trueMedian = items[Math.max(0, (items.length / 2) - 1)];
				System.out.println("True median-ish: " + trueMedian);
			}
			
			prettyPrint("lesser: ", counter.lesserItems);
			prettyPrint("bigger: ", counter.biggerItems);

			assert median == trueMedian;
			System.out.println(median == trueMedian);
			System.out.println();
		}
	}
}
