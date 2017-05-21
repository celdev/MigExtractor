import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TranslatedComparator {

    private static final String NEW_FILENAME = "1translated.txt";
    private static final String OLD_FILENAME = "Innan29mars/1translated.txt";

    private static final String HEADER = "Type," +
            "First time/Extension," +
            "Swedish person type," +
            "Move to type," +
            "Country," +
            "Online/Paper," +
            "Asked to send documents," +
            "Lived Together," +
            "Lived together more than 2 years," +
            "Have proof of live together more than 2 years," +
            "<29MarsAverage," +
            "<29MarsLow," +
            "<29MarsHigh," +
            "<18AprilAverage," +
            "<18AprilLow," +
            "<18AprilHigh" +
            ">18AprilAverage," +
            ">18AprilLow," +
            ">18AprilHigh";

    public static void main(String[] args) {
        ArrayList<String> newData = new ArrayList<>(6000);
        ArrayList<String> oldData = new ArrayList<>(6000);

        ArrayList<String> result = new ArrayList<>(6000);

        readAndAddLines(NEW_FILENAME, newData);
        readAndAddLines(OLD_FILENAME, oldData);

        result.add(HEADER);
        for(int i = 1; i < newData.size() || i == 15; i++) {
            String newLine = newData.get(i);
            String oldLine = oldData.get(i);
            String[] oldSplit = oldLine.split(",", -1);
            result.add(newLine + "," + oldSplit[10] + "," + oldSplit[11] + "," + oldSplit[12]);
        }
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("result.txt")));
            result.forEach(s -> {
                try {
                    bufferedWriter.write(s + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        //String line = "Live together with someone in sweden,First time,Swedish citizen,Wife Husband Partner,Afghanistan,Online,Not asked,Lived together,More than 2 year,Have proof,16-22,16,22";
//        String line = "Live together with someone in sweden,First time,Swedish citizen,Wife Husband Partner,Afghanistan,Online,Asked to send documents,not lived together,,,N/A,,";
//        String[] split = line.split(",", -1);
//        for(int i = 0; i < split.length; i++) {
//            if (split[i].equalsIgnoreCase("")) {
//                System.out.println("i = " + i + " = empty");
//            } else {
//                System.out.println("i = " + i + " value = " + split[i]);
//            }
//        }
//    }

    private static void readAndAddLines(String filename, List<String> collection) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filename)));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                collection.add(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
