import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Generate_VehicleRegistration_UK {
    public static void main(String[] args) {
        String filePath = "vehicles.csv";
        List<String[]> vehicles = readCSV(filePath);

        int total_GeneratedReg = 0;
        int total_FailedReg = 0;

        Map<String, Integer> areaCount = new HashMap<>();
        List<String[]> failedRegList = new ArrayList<>();

        for (String[] vehicle : vehicles) {
            String regArea = vehicle[4];
            String dateOfManufacture = vehicle[2];

            String areaCode = generateAreaCode(regArea);
            if (areaCode == null) {
                total_FailedReg++;
                System.out.println("Failed to generate registration number for vehicle: " + vehicle[0] + " (Area: "
                        + regArea + ")");
                failedRegList.add(vehicle);
                continue;
            }

            String ageIdentifier = generateAgeIdentifier(dateOfManufacture);
            String randomLetter = generateRandomLetters();
            String regNumber = areaCode + " " + ageIdentifier + " " + randomLetter;
            System.out.println("Generated Registration Number for " + vehicle[0] + " (" + regArea + "): " + regNumber);
            total_GeneratedReg++;
            areaCount.put(regArea, areaCount.getOrDefault(regArea, 0) + 1);

        }

        System.out.println("Total registration numbers generated: " + total_GeneratedReg);
        System.out.println("Total failed registration numbers: " + total_FailedReg);
        System.out.println("Registration numbers generated per area:");

        for (Map.Entry<String, Integer> entry : areaCount.entrySet()) {
            System.out.println("Area: " + entry.getKey() + ", Count: " + entry.getValue());
        }

        // for (String[] Vehicle : failedRegList) {
        // System.out.println(
        // "Failed to generate vehicle registration for " + Vehicle[0] + " " + "AreaCode
        // " + Vehicle[4]);
        // }
    }

    public static String generateRandomLetters() {
        Random random = new Random();
        StringBuilder letters = new StringBuilder();
        String notAllowed = "IKMY";
        while (letters.length() < 3) {
            char letter = (char) (random.nextInt(26) + 'A');
            if (!notAllowed.contains(String.valueOf(letter))) {
                letters.append(letter);
            }
        }
        return letters.toString();
    }

    public static String generateAgeIdentifier(String dateOfManufacture) {
        String[] dateParts = dateOfManufacture.split("-");
        int year = Integer.parseInt(dateParts[2]);
        int month = Integer.parseInt(dateParts[1]);
        if (month >= 3 && month <= 8) {
            return String.valueOf(year % 100);
        } else {
            return String.valueOf((year % 100) + 50);
        }

    }

    public static String generateAreaCode(String regArea) {
        regArea = regArea.toLowerCase();
        switch (regArea) {
            case "birmingham":
                return "C" + generateRandomLetter('A', 'C');
            case "cardiff":
                return "C" + generateRandomLetter('L', 'Z');
            case "swansea":
                return "C" + generateRandomLetter('A', 'K');
            default:
                return null;
        }

    }

    public static char generateRandomLetter(char start, char end) {
        double randomValue = Math.random();
        int range = (end - start + 1);
        int randomOffset = (int) (randomValue * range);
        char randomLetter = (char) (start + randomOffset);
        return randomLetter;
    }

    public static List<String[]> readCSV(String filePath) {
        List<String[]> vehicles = new ArrayList<>();
        String line;
        try (FileReader fs = new FileReader(filePath)) {
            BufferedReader br = new BufferedReader(fs);
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                vehicles.add(values);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

}