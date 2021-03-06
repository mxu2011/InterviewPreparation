import java.util.ArrayList;
import java.util.Collections;

public class TallestPeopleTower {
	private static ArrayList<Person> pickLongerSeq(
		ArrayList<Person> s1, ArrayList<Person> s2) {
		if (s1 == null) {
			return s2;
		} 
		if (s2 == null) {
			return s1;
		}
		if (s1.size() < s2.size()) {
			return s2;
		} else {
			return s1;
		}
	}

	//User recursion to build towers
	public static void buildTower(ArrayList<Person> circus,
			ArrayList<ArrayList<Person>> res, int depth) {
		if (depth >= circus.size())
			return;
		ArrayList<Person> tmpRes = null;
		for (int i = 0; i < depth; i++) { 
			if (circus.get(i).isBefore(circus.get(depth))){
				tmpRes = pickLongerSeq(tmpRes, res.get(i));
			}
		}
		ArrayList<Person> newRes = new ArrayList<Person>();
		if (tmpRes != null) {
			newRes.addAll(tmpRes);
		}
		newRes.add(circus.get(depth));
		res.add(newRes);
		buildTower(circus, res, depth + 1);
	}
	public static ArrayList<Person> tower(ArrayList<Person> circus){
		ArrayList<Person> res = new ArrayList<>();
		if (circus == null || circus.isEmpty())
			return res;
		Collections.sort(circus);

		//Start from depth 0
		int depth = 0;
		// tallest person tower until the current person
		// (longest increasing subsequence until cur index)
		ArrayList<ArrayList<Person>> tmpRes = new ArrayList<ArrayList<Person>>();

		//Basiclly build a series of towers
		buildTower(circus, tmpRes, depth);

		//compare towers and pick the longest tower
		for (int i = 0; i < tmpRes.size(); i++) {
			res = pickLongerSeq(res, tmpRes.get(i));
		}
		return res;
	}
	
	public static void main(String[] args) {
		//test1();
		//test2();
		test3();
	}

	private static void test1() {
		Person p1 = new Person(0, 65, 100);
		Person p2 = new Person(1, 70, 150);
		Person p3 = new Person(2, 56, 90);
		Person p4 = new Person(3, 75, 190);
		Person p5 = new Person(4, 60, 95);
		Person p6 = new Person(5, 68, 110);
		ArrayList<Person> circus = new ArrayList<Person>();
		circus.add(p1);
		circus.add(p2);
		circus.add(p3);
		circus.add(p4);
		circus.add(p5);
		circus.add(p6);
		System.out.println("Test case 1: ");
		System.out.println("Circus members: ");
		for (int i = 0; i < circus.size(); i++) {
			System.out.print(circus.get(i).GetId() + "(" + circus.get(i).GetHeight() +
					", " + circus.get(i).GetWeight() + ")->");
		}
		ArrayList<Person> res = tower(circus);
		System.out.println();
		System.out.println("The tallest tower member: ");
		for (int i = 0; i < res.size(); i++) {
			System.out.print(res.get(i).GetId() + "(" + res.get(i).GetHeight() +
					", " + res.get(i).GetWeight() + ")->");
		}
	}

	private static void test2() {
		Person p1 = new Person(0, 3, 2);
		Person p2 = new Person(1, 5, 9);
		Person p3 = new Person(2, 6, 7);
		Person p4 = new Person(3, 7, 8);

		ArrayList<Person> circus = new ArrayList<Person>();
		circus.add(p1);
		circus.add(p2);
		circus.add(p3);
		circus.add(p4);

		System.out.println("Test case 2: ");
		System.out.println("Circus members: ");
		for (int i = 0; i < circus.size(); i++) {
			System.out.print(circus.get(i).GetId() + "(" + circus.get(i).GetHeight() +
					", " + circus.get(i).GetWeight() + ")->");
		}
		ArrayList<Person> res = tower(circus);
		System.out.println();
		System.out.println("The tallest tower member: ");
		for (int i = 0; i < res.size(); i++) {
			System.out.print(res.get(i).GetId() + "(" + res.get(i).GetHeight() +
					", " + res.get(i).GetWeight() + ")->");
		}
	}

	private static void test3() {
		Person p1 = new Person(0, 3, 2);
		Person p2 = new Person(1, 8, 4);
		Person p3 = new Person(2, 6, 7);
		Person p4 = new Person(3, 9, 5);

		ArrayList<Person> circus = new ArrayList<Person>();
		circus.add(p1);
		circus.add(p2);
		circus.add(p3);
		circus.add(p4);

		System.out.println("Test case 2: ");
		System.out.println("Circus members: ");
		for (int i = 0; i < circus.size(); i++) {
			System.out.print(circus.get(i).GetId() + "(" + circus.get(i).GetHeight() +
					", " + circus.get(i).GetWeight() + ")->");
		}
		ArrayList<Person> res = tower(circus);
		System.out.println();
		System.out.println("The tallest tower member: ");
		for (int i = 0; i < res.size(); i++) {
			System.out.print(res.get(i).GetId() + "(" + res.get(i).GetHeight() +
					", " + res.get(i).GetWeight() + ")->");
		}
	}
}

class Person implements Comparable<Object> {
	private int id;
	private int height;
	private int weight;
	Person(int i, int h, int w) {
		id = i;
		height = h;
		weight = w;
	}
	
	public int GetId() {
		return id;
	}
	
	public int GetHeight() {
		return height;
	}
	
	public int GetWeight() {
		return weight;
	}
	
	public int compareTo(Object s) {
		Person second = (Person)s;
		if (this.GetHeight() != second.GetHeight()) {
			return ((Integer)this.GetHeight()).compareTo(second.GetHeight());
		} else {
			return ((Integer)this.GetWeight()).compareTo(second.GetWeight());
		}
	}
	
	public boolean isBefore(Person p) {
		if (this.GetHeight() < p.GetHeight() && 
				this.GetWeight() < p.GetWeight()) {
			return true;
		} else {
			return false;
		}
	}
}