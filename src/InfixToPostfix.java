import java.util.*;

public class InfixToPostfix {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Polinomu giriniz:");
		String pol = sc.nextLine();
		System.out.println("x degerini giriniz: ");
		int x = sc.nextInt();
		sc.close();
		String pol1 = "(3x^2 + 2x) * (5x^3 + 2)/((4x^15 + 3x^4 - 12x^2 + 2) + (4x))";
		String pol2 = "((3x^2 + 2x) + (2x^2 -5)) * (2x^2) / (2x^1 + 5 -8 + 3x^2) + ( 3x^4 - 2x^4) ";
		String pol3 = "((3x) + (2x + 5)) * (1) / (1) + ( 5 ) - ( 8x^10 - 10x^1 + 3x^4 ) / ((8x^10 ) + (2x))";
		String pol4 = "( ((-5x^4 + 15) - (2x^4 - 3x^3) - (3)) - (-5x^2))";
		String pol5 = "(3x^4  -8 + x^6  -4 ) * (-1) + (-5x^5  +5x^5 -1)";
		//test case olarak deneyebilirsiniz :D
		System.out.println(hesapla(pol,x));
	}
	private static double hesapla (String s, int x) {
		s = s.replaceAll("\\s+","");
		ArrayList<String> polynoms = ayir(s);
		LinkedList<Node>[] linkedlist = linklistOlustur(polynoms,x);
		int[] polSums = polHesapla(linkedlist);
		String infix = abcdYaz(s,polynoms);
		String postfix = postfix(infix);
		String postWithSums = cevaplariYaz(postfix, polSums);
		Double cevap = postfixHesapla(postWithSums,polSums);
		return cevap;
	}
	private static ArrayList<String> ayir(String s) {
		ArrayList<String> polinoms = new ArrayList<>();
		boolean parantez = true;
		String pol = "";
		for(int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			if(c == '(')
				parantez = true;
			else if(c == ')') {
				parantez = false;
				pol = pol.replaceAll("\\s+","");
				if (!pol.equals(""))
					polinoms.add(pol);
				pol = "";
			}
			else if (parantez)
				pol += c;
		}
		return polinoms;
	}
	private static LinkedList<Node>[] linklistOlustur(ArrayList<String> pols, int x) {
		LinkedList<Node>[] linkedlist = new LinkedList[pols.size()];
		for(int i=0; i<pols.size(); i++)
		linkedlist[i] = new LinkedList<>();
		for(int i=0; i<pols.size(); i++) {
			int[][] nodes = parcala(pols.get(i));
			for(int j=0; j<nodes.length; j++) {
				Node node = new Node(nodes[j][1], x, nodes[j][0]);
				linkedlist[i].add(node);
			}
		}
		return linkedlist;
	}
	private static int[][] parcala(String s) {
		int[][] terimler = new int[20][2];
		//bir polinom max 20 terim tutabilir dedim.
		//gerekirse arttirabiliriz.
		int counter = 0;
		String kiminTerimi="-5";
		String terimKatsayisi="";
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			if(c >= '0' && c <= '9'){
				if(kiminTerimi.equals("-5"))
					terimKatsayisi += c;
				else
					kiminTerimi += (c - '0');
			} else if(c == 'x'){
				if(terimKatsayisi.equals(""))
					terimKatsayisi = "1";
				if(terimKatsayisi.equals("-"))
					terimKatsayisi = "-1";
				kiminTerimi = "1";
			} else if(c == '^'){
					kiminTerimi = "";
			} else if(c == '*' || c == ' '){

			} else if(c == '+'){
					katsayilariGuncelle(terimKatsayisi, kiminTerimi, terimler, counter);
					counter++;
					kiminTerimi = "-5";
					terimKatsayisi = "";
			} else if(c == '-'){
					katsayilariGuncelle(terimKatsayisi, kiminTerimi, terimler, counter);
					counter++;
					kiminTerimi = "-5";
					terimKatsayisi = "-";
			}
		}
		katsayilariGuncelle(terimKatsayisi, kiminTerimi, terimler, counter);
		counter++;
		return terimler;
	}
	private static void katsayilariGuncelle(String katsayi, String kiminTerimi, int[][] dizi, int counter) {
		if(kiminTerimi.equals("-5"))
		kiminTerimi="0";
		if(!katsayi.equals("")){
			dizi[counter][0] = Integer.parseInt(kiminTerimi);
			dizi[counter][1] = Integer.parseInt(katsayi);
		}
	}
	private static int[] polHesapla(LinkedList<Node>[] list) {
		int[] toplamlar = new int[list.length];
		for(int i=0; i<list.length; i++) {
			int sum = 0;
			for(int j=0; j<list[i].size(); j++)
				sum += list[i].get(j).cevap;
			toplamlar[i] = sum;
		}
		return toplamlar;
	}
	private static String abcdYaz(String s, ArrayList<String> pols) {
		int a = 'a';
		for(int i=0; i<pols.size(); i++) {
			s = s.substring(0, s.indexOf(pols.get(i))) + ""+(char)(a) + s.substring(s.indexOf(pols.get(i))+pols.get(i).length());
			a++;
		}
		return s;
	}
	private static String postfix ( String s ) {
		String yeni = "";
		Stack<Character> stack = new Stack<>();
		for(int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 'a' && c <= 'z')
				yeni += c;
			else if (c == '(')
				stack.push(c);
			else if (c == ')') {
				while (!stack.isEmpty() && stack.peek() != '(')
					yeni += stack.pop();

				if (!stack.isEmpty() && stack.peek() != '(')
					return "hata";
				else
					stack.pop();
			}
			else {
				while (!stack.isEmpty() && operator(c) <= operator(stack.peek()))
					yeni += stack.pop();
				stack.push(c);
			}
		}
		while (!stack.isEmpty())
			yeni += stack.pop();

		for(int i=0; i<yeni.length()-1; i++)
			if(Character.isLetter(yeni.charAt(i)) && Character.isLetter(yeni.charAt(i+1)))
				yeni = yeni.substring(0, i+1) + " " + yeni.substring(i+1);
		return yeni;
	}
	private static int operator(char c) {
		if (c == '+' || c == '-') return 1;
		else if (c == '*' || c == '/') return 2;
		else return -1;
	}
	private static String cevaplariYaz(String s, int[] sum) {
		int j = 0;
		for(int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			if(c >= 'a' && c <= 'z') {
				String eski = "" + c;
				String yeni = "" + sum[j];
				if(sum[j] < 0)
				yeni = "!" + yeni.substring(1);
				s = s.replaceFirst(eski, yeni);
				j++;
			}
		}
		return s;
	}
	private static Double postfixHesapla(String s, int[] sum) {
		Stack<Double> stack = new Stack<>();
		String deger = "";
		double result = 0;
		for(int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isDigit(c))
				deger += c;
			else if(c == '*' || c == '/' || c == '+' || c == '-'){
				if(!deger.equals("")) {
					stack.push(Double.parseDouble(deger));
					deger = "";
				}
				double a = stack.pop();
				double b = stack.pop();
				result = evaluate(a,b,c);
				stack.push(result);
			}
			else if(c == ' ') {
				if(!deger.equals("")) {
					stack.push(Double.parseDouble(deger));
					deger = "";
				}
			}
			else if(c == '!')
				deger += '-';
		}
		result = stack.pop();
		return result;
	}
	private static double evaluate(double a, double b, char c) {
		if(c == '+') return a+b;
		else if(c == '-') return b-a;
		else if(c == '*') return a*b;
		else if(c == '/') return b/a;
		else return 0;
	}


	public static class Node {
		int katsayi;
		int x;
		int us;
		int cevap;
		Node(int k, int xx, int u) {
			katsayi = k;
			x = xx;
			us = u;
			cevap = (int)Math.pow(xx, u);
			cevap *= katsayi;
		}
		public String toString() {
			return "" + katsayi + "x^"+us + "=" + cevap;
		}
	}
}
