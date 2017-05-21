import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoExtractor {

    private static final String ROW_ID = "svid12_2d998ffc151ac3871595b10";
    private static final Pattern regex = Pattern.compile("(\\d){1,2}-(\\d){1,2}");
    private static ArrayList<String> info = new ArrayList<>();

    private static final String URL_FILENAME = "urls_only_swe1.txt";
    private static final String RESULT_FILENAME = "extract_swe1.txt";

    public static void main(String[] args) {
        String fileName = URL_FILENAME;
        if (args.length > 0) {
            fileName = args[0];
        }
        Counter counter = new Counter();
        List<String> list = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }
            bufferedReader.close();
            System.out.println("starting extraction...");
            System.out.println("using " + list.size() + " urls");
            list.forEach(url -> {
                counter.inc();
                long start = System.currentTimeMillis();
                System.out.print("Extracting url + " + counter.getCount() + "/" + list.size());
                extractInfo(url);
                long time = System.currentTimeMillis() - start;
                if (time > 3000) {
                    int sleep = counter.getLongSleeperCount() % 3 == 0 ? -1 : 15;
                    if (sleep == -1) {
                        System.out.println("\nPaused because too long time to extract information\nwrite continue to continue");
                        Scanner scanner = new Scanner(System.in);
                        String input = "";
                        while (true) {
                            if ((input = scanner.nextLine()) != null) {
                                if (input.equalsIgnoreCase("continue")) {
                                    break;
                                } else if (input.equalsIgnoreCase("stop")) {
                                    doStop();
                                }
                            }
                        }
                    } else {
                        System.out.print("\nSleeping for " + sleep + " seconds because time to extract more than 3 seconds...");
                        counter.incLongSleeperCount();
                        for(int i = sleep; i > 0; i--) {
                            try {
                                System.out.print(i + ", ");
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println("");
                    }
                }
                try {
                    int sleep = new Random().nextInt(100);
                    System.out.print(" + time to extract " + time + "ms" + "- sleeping " + sleep + "ms +\n");
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        doWrite();
    }

    private static void doWrite() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(RESULT_FILENAME)));
            info.forEach(info -> {
                try {
                    writer.write(info + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void doStop() {
        doWrite();
        System.exit(2);
    }

    private static String extractInfo(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            //System.out.println("\n==========URL " + urlStr + " result==============\n");
            while ((line = reader.readLine()) != null) {
                if (line.contains(ROW_ID)) {
                    Matcher matcher = regex.matcher(line);
                    String result;
                    if (matcher.find()) {
                        result = matcher.group();
                    } else {
                        result = "N/A";
                    }
                    urlStr = urlStr.replace(ExtractorMain.BASE_URL, "");
                    info.add(urlStr + appendMissingComma(urlStr) + "#" + result);
                }
            }
            //System.out.println("===========END OF RESULT================ \n\n");
            is.close();
        } catch (Exception e) {
            System.out.println("Error parsing url " + urlStr);
            e.printStackTrace();
        }
        return null;
    }

    private static String appendMissingComma(String url) {
        if (!url.contains("q9")) {
            return ",,";
        } else if (!url.contains("q10")) {
            return ",";
        }
        return "";
    }

    private static class Counter {
        private int counter = 0;
        private int longSleeperCount = 0;
        private void inc(){
            counter++;
        }
        private int getCount() {
            return counter;
        }

        private void incLongSleeperCount() {
            longSleeperCount++;
        }

        public int getLongSleeperCount() {
            return longSleeperCount;
        }
    }
}
