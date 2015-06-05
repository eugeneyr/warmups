package info.lynxnet.warmups;

import java.io.*;
import java.util.*;

/**
 *
 */
public class WordTransformer {

    public List<String> findPath(String start, String end, List<String> dictionary) {
        if (start == null || end == null || start.length() == 0 || end.length() == 0
                || dictionary == null || dictionary.size() == 0 || start.length() != end.length()) {
            return Collections.EMPTY_LIST;
        }
        List<String> subd = new LinkedList<String>();
        for (String s : dictionary) {
            if (s.length() == start.length()) {
                subd.add(s);
            }
        }
        if (subd.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, String> from = new HashMap<String, String>();
        Map<String, State> states = new HashMap<String, State>();
        List<String> result = new LinkedList<String>();
        LinkedList<String> queue = new LinkedList<String>();
        String current;
        queue.add(start);
        do {
            if ((current = queue.poll()) == null) {
                // the path does not exist, exiting the loop
                break;
            }
            if (current.equals(end)) {
                // we found the path, exiting the loop
                break;
            }
            states.put(current, State.VISITING);
            List<String> neighbors = getUnvisitedNeighbors(current, states, subd);
            for (String neighbor : neighbors) {
                from.put(neighbor, current);
                queue.add(neighbor);
                states.put(neighbor, State.NEW);
            }
            states.put(current, State.VISITED);
        } while (true);
        if (end.equals(current)) {
            do {
                // we reached the end, reconstructing the path
                result.add(0, current);
                current = from.get(current);
            }
            while (current != null);
        }
        return result;
    }

    public List<String> findPaths(String start, String end, List<String> dictionary) {
        if (start == null || end == null || start.length() == 0 || end.length() == 0
                || dictionary == null || dictionary.size() == 0 || start.length() != end.length()) {
            return Collections.EMPTY_LIST;
        }
        List<String> subd = new LinkedList<String>();
        for (String s : dictionary) {
            if (s.length() == start.length()) {
                subd.add(s);
            }
        }
        if (subd.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, String> from = new HashMap<String, String>();
        Map<String, State> states = new HashMap<String, State>();
        List<String> result = new LinkedList<String>();
        LinkedList<String> queue = new LinkedList<String>();
        String current;
        queue.add(start);
        do {
            if ((current = queue.poll()) == null) {
                break;
            }
            states.put(current, State.VISITING);
            List<String> neighbors = getUnvisitedNeighbors(current, states, subd);
            for (String neighbor : neighbors) {
                from.put(neighbor, current);
                queue.add(neighbor);
                states.put(neighbor, State.NEW);
            }
            states.put(current, State.VISITED);
        } while (true);
        current = end;
        do {
            // we reached the end, reconstructing the path
            result.add(0, current);
            current = from.get(current);
        }
        while (current != null);
        return result;
    }

    static class WeightedWord implements Comparable {
        String word;
        Integer weight;

        WeightedWord(String word) {
            this.word = word;
        }

        WeightedWord(String word, Integer weight) {
            this.word = word;
            this.weight = weight;
        }

        public int compareTo(Object o) {
            WeightedWord ww = (WeightedWord) o;
            if (ww.word.equals(word)) {
                return 0;
            }
            if (ww.weight == null) {
                return -1;
            }
            if (weight != null) {
                return weight - ww.weight;
            }
            return 1;
        }

        @Override
        public boolean equals(Object obj) {
            WeightedWord ww = (WeightedWord) obj;
            return ww.word.equals(word);
        }

        @Override
        public int hashCode() {
            return word != null ? word.hashCode() : 0;
        }
    }

    public List<String> findShortestPath(String start, String end, List<String> dictionary) {
        if (start == null || end == null || start.length() == 0 || end.length() == 0
                || dictionary == null || dictionary.size() == 0 || start.length() != end.length()) {
            return Collections.EMPTY_LIST;
        }
        List<String> subd = new LinkedList<String>();
        for (String s : dictionary) {
            if (s.length() == start.length()) {
                subd.add(s);
            }
        }
        if (subd.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Map<String, String> from = new HashMap<String, String>();
        Map<String, State> states = new HashMap<String, State>();
        Map<String, WeightedWord> weights = new HashMap<String, WeightedWord>();
        List<String> result = new LinkedList<String>();

        PriorityQueue<WeightedWord> queue = new PriorityQueue<WeightedWord>();

        for (String word : subd) {
            WeightedWord weightedWord = new WeightedWord(word, word.equals(start) ? 0 : null);
            queue.add(weightedWord);
            weights.put(word, weightedWord);
        }

        while (!queue.isEmpty()) {
            WeightedWord current = queue.remove();
            if (current.weight == null) {
                // we came across an unreachable word
                System.out.println("UNREACHABLE: " + current.word);
                break;
            }
            states.put(current.word, State.VISITED);
            List<String> neighbors = getUnvisitedNeighbors(current.word, states, subd);
            for (String neighbor : neighbors) {
                WeightedWord weightedWord = weights.get(neighbor);
                // relaxation
                if (weightedWord.weight == null || weightedWord.weight > current.weight + 1) {
                    weightedWord.weight = current.weight + 1;
                    from.put(weightedWord.word, current.word);
                    if (queue.remove(weightedWord)) {
                        queue.add(weightedWord);
                    }
                }
            }
        }

        String current = end;
        do {
            // we reached the end, reconstructing the path
            result.add(0, current);
            current = from.get(current);
        }
        while (current != null);
        if (!queue.isEmpty()) {
            while (!queue.isEmpty()) {
                WeightedWord ww = queue.remove();
                System.out.println("UNREACHABLE: " + ww.word);
            }
        }
        return result;
    }

    public List<String> getUnvisitedNeighbors(String word, Map<String, State> markers, List<String> dictionary) {
        List<String> result = new LinkedList<String>();
        for (String w : dictionary) {
            if (distance(w, word) == 1) {
                State state = markers.get(w);
                if (state == null) {
                    result.add(w);
                }
            }
        }
        return result;
    }

    static int distance(String w1, String w2) {
        if (w1 == null || w2 == null) {
            return -1;
        }
        if (w1.length() != w2.length()) {
            return -1;
        }
        int count = 0;
        for (int i = 0; i < w1.length(); i++) {
            count += (w1.charAt(i) == w2.charAt(i)) ? 0 : 1;
        }
        return count;
    }

    public List<String> loadDictionary(String fileName) {
        try {
            List<String> result = new LinkedList<String>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String s;
            do {
                s = reader.readLine();
                if (s != null) {
                    s = s.toLowerCase().trim();
                    if (s.length() > 0) {
                        result.add(s);
                    }
                }
            } while (s != null);
            return result;
        } catch (IOException e) {
            return Collections.EMPTY_LIST;
        }
    }

    public static void main(String[] args) {
        WordTransformer transformer = new WordTransformer();
        List<String> dictionary = transformer.loadDictionary(args[0]);
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the start word:");
            String start = scanner.next();
            System.out.println("Enter the end word:");
            String end = scanner.next();
            List<String> result = transformer.findShortestPath(start, end, dictionary);
            for (String word : result) {
                System.out.println(word);
            }
        } while (true);
    }
}
