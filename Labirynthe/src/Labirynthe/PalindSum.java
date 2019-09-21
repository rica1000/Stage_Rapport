package Labirynthe;

import java.util.ArrayList;

public class PalindSum {
	private String number;
	private ArrayList<Integer> pal; 
    public PalindSum() {
    	number = "61655151";
    	pal = new ArrayList<Integer>();
    	algo();
    }
	private void algo() {
		int integer = Integer.parseInt(number);
		int reste = 0;
		while((integer/10) != 0 && !palindrome(integer)){
			for(int i = (Integer.toString(integer).length())/2-1;i>=0 && !palindrome(integer);i--) {
				String string = Integer.toString(integer);
				int t = string.length();
				int opi = t-i-1;
				reste += Math.abs(string.charAt(i)-string.charAt(opi))*(int) Math.pow(10, i);
				int tmp = integer;
				integer -= Math.abs(string.charAt(i)-string.charAt(opi))*(int) Math.pow(10, i);
				System.out.println(integer+"   "+reste);
				if(integer != tmp) i = (Integer.toString(integer).length())/2;
			}
			pal.add(integer);
			integer=reste;
			reste = 0;
		}
		pal.add(integer);
	}
	private boolean palindrome(int integer) {
		boolean b = true;
		for(int i = Integer.toString(integer).length()/2-1;i>=0;i--) {
			String string = Integer.toString(integer);
			int t = string.length();
			int opi = t-i-1;
			if(string.charAt(i) != string.charAt(opi)) b = false;
		}
		
		return b;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PalindSum p = new PalindSum();
		System.out.println(p.getPal());
	}
	public ArrayList<Integer> getPal() {
		return pal;
	}
	public void setPal(ArrayList<Integer> pal) {
		this.pal = pal;
	}

}
