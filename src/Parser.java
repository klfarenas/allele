import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
	private Path fFilePath;
	private static Charset ENCODING = StandardCharsets.UTF_8;

	private String name, acodon, isotype, sequence;
	private Float fenergy;

	public Parser(String fileName) {
		fFilePath = Paths.get(fileName);
	}
	
	public TrnaDatabase parseSpecies() throws IOException {
		TrnaDatabase db = new TrnaDatabase();
//		int i=0;

		try (Scanner scanner = new Scanner(fFilePath, ENCODING.name())) {
			while (scanner.hasNextLine()) {
				Species temp = processLineForSpecies(scanner.nextLine());
				if (temp != null) {
					db.insert(temp);
//					System.out.println(db.getSelectedNode(i++).toString());
				}
			}
		}
		
		return db;
	}
	
	protected Species processLineForSpecies(String line) {
		Species s;
		String name, nickname, kingdom, phylum, classs, order, family, genus;
		
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter(",");
		if (scanner.hasNext()) {
			// assumes the line has a certain structure
			name = scanner.next();
			nickname = scanner.next();
			kingdom = scanner.next();
			phylum = scanner.next();
			classs = scanner.next();
			order = scanner.next();
			family = scanner.next();
			genus = scanner.next();
			ArrayList<Trna> trnaList =  new ArrayList<Trna>();
//			System.out.println(name + nickname + kingdom + phylum + classs + order + family + genus);
			s = new Species(name, nickname, kingdom, phylum, classs, order, family, genus, trnaList, null);
			return s;
		} else {
			System.out.println("Empty or invalid line. Unable to process.");
			return null;
		}
	}
	
	public ArrayList<Trna> parseTrna() throws IOException {
		ArrayList<Trna> trnaList =  new ArrayList<Trna>();
		
		// let user pick format
		
		trnaList = new ArrayList<Trna>(processByThrees());
		
		return trnaList;
	}

	public ArrayList<Trna> processByThrees() throws IOException {
		ArrayList<Trna> trnaList = new ArrayList<Trna>();

		try (Scanner scanner = new Scanner(fFilePath, ENCODING.name())) {
			while (scanner.hasNextLine()) {
				processLine1(scanner.nextLine());
				processLine2(scanner.nextLine());
				Trna temp = processLine3(scanner.nextLine());
				if (temp != null) {
					trnaList.add(temp);
				}
			}
		}

		return trnaList;
	}

	protected void processLine1(String line) {
		// use a second Scanner to parse the content of each line
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter(" ");
		if (scanner.hasNext()) {
			// assumes the line has a certain structure
			String temp = scanner.next();
			temp = temp.substring(1);
			this.name = temp;
			scanner.next();
			scanner.next();
			this.isotype = scanner.next();
			temp = scanner.next();
			temp = temp.substring(1, temp.length() - 1);
			this.acodon = temp;
		} else {
			System.out.println("Empty or invalid line. Unable to process.");
		}
	}

	protected void processLine2(String line) {
		// use a second Scanner to parse the content of each line
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter(" ");
		if (scanner.hasNext()) {
			// assumes the line has a certain structure
			this.sequence = scanner.next();
		} else {
			System.out.println("Empty or invalid line. Unable to process.");
		}
	}

	protected Trna processLine3(String line) {
		// use a second Scanner to parse the content of each line
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter(" ");
		if (scanner.hasNext()) {
			// assumes the line has a certain structure
			scanner.next();
			String temp = scanner.next();
			temp = temp.substring(1, temp.length() - 1);
			this.fenergy = Float.parseFloat(temp);

			Trna t = new Trna(this.name, this.acodon, this.isotype,
					this.sequence, this.fenergy);
			return t;
		} else {
			System.out.println("Empty or invalid line. Unable to process.");
			return null;
		}
	}

	public static void main(String args[]) throws IOException {
		String inputFile = "C:\\Temp\\test2.csv";
		Parser parser = new Parser(inputFile);
		
		
//		ArrayList<Trna> trnaList = new ArrayList<Trna>(parser.parseTrna());
//
//		Species spec = new Species("test", "test", "test", "test", "test",
//				"test", "test", "test", trnaList, null);
//		System.out.println(spec.showTrnaList());
		
		TrnaDatabase db = parser.parseSpecies();
		Species n = db.listIterator();
		while(n != null){
			System.out.println(n.toString());
			n = db.listIterator();
		}
	}

}