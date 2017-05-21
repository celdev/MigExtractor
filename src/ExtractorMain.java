import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ExtractorMain {

    public static final String BASE_URL = "https://www.migrationsverket.se/4.2d998ffc151ac3871595b01/10.2d998ffc151ac3871595b0c.js?history=";

    private final String[] kindOfVisa = {"1"};                      //q0    1 = live together with someone in sweden

    private final String[] firstTimeOrExtention = {"1"};            //q1    1 = first time

    private final String[] onlineOrPaper = {"1", "2"};              //q2    1 = online 2 = papaer

    private List<CountryCodeEnum> countryOfResident;                   //q3    country

    private final String[] sendDocumentToMig = {"1", "2"};          //q4    1 = behövt sända 2 = inte sänt

    private final String[] personInSwedenKind =
            {"1", /*"2", "3", /*"4", "5", "6" /*, "7"*/ };              //q5
    /*  1   =   Swedish citizen
        2   =   EU Citizen
        3   =   PUT
        4   =   Student         //not active
        5   =   own company     //not active
        6   =   working         //not active
        7   =   TUT             //not active
    * */

    private final String[] moveToPerson = {"1" /*, "2", "3", "4" */};         //q6
    /*  1   =   significant other
        2   =   parent
        3   =   child
        4   =   close relative
    * */

    private final String[] livedTogether = {"1", "2"};                        //q8    1 = lived together  2 = no

    private final String[] moreThan2Year = {"1", "2"};                        //q9    1 = mer än 2 år, 2 = mindre

    private final String[] haveDocumentOfMoreThan2Year = {"1", "2"};          //q10   1 = ja 2 = nej


    public static void main(String[] args) {
        new ExtractorMain();
    }

    private ExtractorMain() {
        List<String> urls = new ArrayList<>(1000);
        fillCountryList();
        long start = System.currentTimeMillis();
        for(int i = 0; i < kindOfVisa.length; i++) {
            String q0 = "q0:" + kindOfVisa[i];
            for(int a = 0; a < firstTimeOrExtention.length; a++) {
                String q1 = "q1:" + firstTimeOrExtention[a];
                for(int b = 0; b < personInSwedenKind.length; b++) {
                    String q5 = "q5:" + personInSwedenKind[b];
                    for (int c = 0; c < moveToPerson.length; c++) {
                        String q6 = "q6:" + moveToPerson[c];
                        for (int d = 0; d < countryOfResident.size(); d++) {
                            CountryCodeEnum temp = countryOfResident.get(d);
                            String q3 = "q3:" + temp.getCountryCode();
                            String countryName = temp.getCountryNameInEnglish();
                            for (int e = 0; e < onlineOrPaper.length; e++) {
                                String q2 = "q2:" + onlineOrPaper[e];
                                for (int f = 0; f < sendDocumentToMig.length; f++) {
                                    String q4 = "q4:" + sendDocumentToMig[f];
                                    for (int g = 0; g < livedTogether.length; g++) {
                                        String q8 = "q8:" + livedTogether[g];
                                        String q9 = "";
                                        String q10 = "";
                                        for(int h = 0; h < moreThan2Year.length; h++) {
                                            q9 = "q9:" + moreThan2Year[h];
                                            for(int j = 0; j < haveDocumentOfMoreThan2Year.length; j++) {
                                                q10 = "q10:" + haveDocumentOfMoreThan2Year[j];
                                                String url = BASE_URL + q0 + "," + q1 + "," + q5 + "," + q6 + "," + q3 + "," +
                                                        q2 + "," + q4 + "," + q8;
                                                if (!q9.isEmpty() && !q8.equals("q8:2")) {
                                                    url += "," + q9;
                                                    if (!q10.isEmpty() && !q9.equals("q9:2")) {
                                                        url += "," + q10;
                                                    }
                                                }
                                                urls.add(url);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        long end = System.currentTimeMillis();
        long time = end - start;
        System.out.println("Found " + urls.size() + " urls in " + time + " ms");
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("urls_only_swe.txt")));
            urls.forEach(s -> {
                try {
                    bufferedWriter.write(s + "\n");
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


    private void fillCountryList() {
        countryOfResident = Arrays.asList(CountryCodeEnum.values());
    }

}
