import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator {

    private static String HEADER = "Type,First time/Extension,Swedish person type,Move to type,Country,Online/Paper,Asked to send documents,Lived Together,Lived together more than 2 years,Have proof of live together more than 2 years,Average,First,Second";

    private static String[] q0 = {"Live together with someone in sweden"};

    private static String[] q1 = {"First time"};

    private static String[] q2 = {"Online", "Paper"};

    /*  kolla denna*/
    private static String[] q4 = {"Asked to send documents", "Not asked"};

    private static String[] q5 = {"Swedish citizen"};

    private static String[] q6 = {"Wife Husband Partner"};

    private static String[] q8 = {"Lived together", "not lived together"};

    private static String[] q9 = {"More than 2 year", "less than 2 year"};

    private static String[] q10 = {"Have proof", "Do not have proof"};

    private static Pattern countryPattern = Pattern.compile("(\\d[A-Ö]{2})");

    private static Map<String, String> countryMap = new HashMap<>(CountryCodeEnum.values().length);

    public static void main(String[] args) {
        String fileName = "extract.txt";
        if (args.length > 0) {
            fileName = args[0];
        }
        buildCountryMap();
        List<String> list = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                String temp = cleanQ0(cleanQ1(cleanQ2(cleanQ3(cleanQ4(cleanQ5(cleanQ6(cleanQ8(cleanQ9(cleanQ10(line))))))))));
                temp = temp.replace("#", ",");
                //System.out.println(temp);
                list.add(temp);
            }

            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("1translated.txt")));
            bufferedWriter.write(HEADER + "\n");
            list.forEach(line -> {
                try {
                    bufferedWriter.write(translateWait(line) + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String translateWait(String line) {
        String[] split = line.split(",");
        if (split[split.length-1].equalsIgnoreCase("N/A")) {
            return line+",,";
        } else {
            String wait = split[split.length - 1];
            String[] intSplit = wait.split("-");
            try {
                int first = Integer.parseInt(intSplit[0]);
                int second = Integer.parseInt(intSplit[1]);
                return line + "," + first + "," + second;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return line + ",e,e";
            }
        }
    }

    private static void buildCountryMap() {
        for (CountryCodeEnum countryCodeEnum : CountryCodeEnum.values()) {
            countryMap.put(countryCodeEnum.getCountryCode(), countryCodeEnum.getCountryNameInEnglish());
        }
    }

    private static String cleanQ0(String line) {
        return line.replace("q0:1", q0[0]);
    }

    private static String cleanQ1(String line) {
        return line.replace("q1:1", q1[0]);
    }

    private static String cleanQ2(String line) {
        return line.replace("q2:1", q2[0]).replace("q2:2", q2[1]);
    }

    private static String cleanQ3(String line) {
        Matcher matcher = countryPattern.matcher(line);
        if (matcher.find()) {
            String countryCode = matcher.group();
            String countryName = countryMap.get(countryCode);
            if (countryCode == null || countryCode.isEmpty()) {
                System.out.println("Error translating countrycode " + countryCode + " in line " + line);
            } else {
                return line.replace("q3:" + countryCode, countryName);
            }
        }
        System.out.println("Error matching country code in line " + line);
        return line;
    }

    private static String cleanQ4(String line) {
        return line.replace("q4:1", q4[0]).replace("q4:2", q4[1]);
    }

    private static String cleanQ5(String line) {
        return line.replace("q5:1", q5[0]);
    }

    private static String cleanQ6(String line) {
        return line.replace("q6:1", q6[0]);
    }

    private static String cleanQ8(String line) {
        return line.replace("q8:1", q8[0]).replace("q8:2", q8[1]);
    }

    private static String cleanQ9(String line) {
        return line.replace("q9:1", q9[0]).replace("q9:2", q9[1]);
    }

    private static String cleanQ10(String line) {
        return line.replace("q10:1", q10[0]).replace("q10:2", q10[1]);
    }
}
