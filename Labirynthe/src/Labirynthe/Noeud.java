package Labirynthe;
public class Noeud implements Comparable{
	private String label;
	private int x,y,cost;
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public void setX(int x) {
		this.x = x;
	}

	public Noeud(String string) {
		label = string;
		x=0;
		y=0;
	}

	public int getX() {
		
		return x;
	}

	@Override
	public int compareTo(Object o) {
		Noeud noeud = (Noeud)o;
		return cost-noeud.cost;
	}
	public String toString() {
		String ch = "["+x+","+y+"]";
		return ch;
	}
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o == null) return false;
		if(getClass() != o.getClass()) return false;
		Noeud noeud = (Noeud)o;
		if(!noeud.label.equals(label)) return false;
		return true;
	}

}
