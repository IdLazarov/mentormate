import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MainTest {

	private static final String String = null;

	public static void main(String[] args) throws Exception {

		Scanner input = new Scanner(System.in);

		String employeesData;
		String definitionData;

		System.out.println("Please, insert path to the file:");
		employeesData = (input.nextLine());
		Object objectEmployees = new JSONParser().parse(new FileReader(employeesData));

		System.out.println("Please, insert path to the definition file:");
		definitionData = (input.nextLine());
		Object objectDefinition = new JSONParser().parse(new FileReader(definitionData));

		System.out.println(objectEmployees);
		// print data
		System.out.println(objectDefinition);

		JSONArray jsonEmployees = (JSONArray) objectEmployees;

		JSONObject jsonDefinition = (JSONObject) objectDefinition;

		String topPerformersThreshold = String.valueOf(jsonDefinition.get("topPerformersThreshold"));
		String useExprienceMultiplier = String.valueOf(jsonDefinition.get("useExprienceMultiplier"));
		String periodLimit = String.valueOf(jsonDefinition.get("periodLimit"));
		Boolean firstIteration = true;

//		JSONArray filteredJsonObjects = new JSONArray();

		for (int i = 0; i < jsonEmployees.size(); i++) {
			JSONObject obj = (JSONObject) jsonEmployees.get(i);

			String name = (String) obj.get("name");
			Long totalSales = (Long) obj.get("totalSales");
			Long salesPeriod = (Long) obj.get("salesPeriod");
			Double experienceMultiplier = (Double) obj.get("experienceMultiplier");
			System.out.println(name + " ," + totalSales + " ," + salesPeriod + " ," + experienceMultiplier + ".");
			Double resultCalculation  = calculateScore(Boolean.valueOf(useExprienceMultiplier),
					totalSales, salesPeriod, experienceMultiplier);
			if (Long.valueOf(periodLimit) >= salesPeriod && resultCalculation > Double.parseDouble(topPerformersThreshold)) {
				writeDataToCsv(name, resultCalculation, firstIteration);
				firstIteration = false;
			}

		}

	

	}

	private static void writeDataToCsv(String name, Double calculateScore, Boolean firstIteration) throws IOException {
		File file = new File("CvsFiltered.csv");
		BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
		try {
			if (firstIteration) {
				out.write("\n Name	,Score");
			}
			out.write("\n" + name + " ," + String.valueOf((calculateScore)));
			System.out.println("Successfully writen JSON object to File " + file.getName());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}

//	public static void writeDataToCsv(JSONArray data) throws IOException {
//
//    	File file = new File("D:\\Softuerni tehnologii\\GraphProject+ver2\\Test\\CvsFiltered.csv");
//    	FileWriter csvWriter = new FileWriter(file);  
//    	try {
//    		csvWriter.write(data.toJSONString());
//    		System.out.println("Successfully writen JSON objects to File " + file.getName());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			csvWriter.flush();
//			csvWriter.close();
//		}
//
//    }

	private static Double calculateScore(Boolean useExprienceMultiplier, Long totalSales, Long salesPeriod,
			Double experienceMultiplier) {
		if (useExprienceMultiplier == true) {
			return totalSales / salesPeriod * experienceMultiplier;
		} else {
			return (double) (totalSales / salesPeriod);
		}
	}

}