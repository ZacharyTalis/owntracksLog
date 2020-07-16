package ZacharyTalis.ZacharyTalis;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class Main {

    private static String filename = "owntracksLog.txt";

    public static void main(String[] args) throws Exception
    {
        ///// WRITE TO owntracksLog.txt /////
        String[] listData = readFileAsString();
        HashMap<String, String> data = new HashMap<>();
        String dataPointID;
        for (String dataPoint : listData) {
            if (dataPoint.length() >= 33) {
                dataPointID = dataPoint.substring(7, 32);
                data.put(dataPointID, "{\"id\"" + dataPoint);
            }
        }
        Path file = Paths.get(filename);
        Files.write(file, data.values(), StandardCharsets.UTF_8);
        ///// WRITE TO owntracksLog.txt /////


        ///// COLLECT coordsLat /////
        List<String> oldData = readFileStrings();
        List<String> coordsLat = new ArrayList<>();
        List<Integer> toRemove = new ArrayList<>();
        for (String line : oldData) {
            if (line.contains("]}},")) {
                line = line.substring(line.indexOf("[\"")).trim();
                line = line.substring(line.indexOf("\",\"")+3).trim();
                line = line.substring(0, line.indexOf("\"")).trim();
                coordsLat.add(line);
            } else { toRemove.add(oldData.indexOf(line)); }
        }
        ///// COLLECT coordsLat /////

        ///// COLLECT coordsLon /////
        List<String> coordsLon = new ArrayList<>();
        for (String line : oldData) {
            if (line.contains("]}},")) {
                line = line.substring(line.indexOf("[\"")+2).trim();
                line = line.substring(0, line.indexOf("\"")).trim();
                coordsLon.add(line);
            } else { if (!toRemove.contains(oldData.indexOf(line)))
            { toRemove.add(oldData.indexOf(line)); } }
        }
        ///// COLLECT coordsLon /////


        ///// COLLECT datesDay /////
        List<String> datesDay = new ArrayList<>();
        for (String line : oldData) {
            if (line.contains("\"created_at\":\"")) {
                line = line.substring(line.indexOf("\"created_at\":\"")+14)
                        .trim();
                line = line.substring(0, line.indexOf("T")).trim();
                datesDay.add(line);
            } else { if (!toRemove.contains(oldData.indexOf(line)))
            { toRemove.add(oldData.indexOf(line)); } }
        }
        ///// COLLECT datesDay /////


        ///// COLLECT datesTime /////
        List<String> datesTime = new ArrayList<>();
        for (String line : oldData) {
            if (line.contains("\"created_at\":\"")) {
                line = line.substring(line.indexOf("\"created_at\":\"")+14)
                        .trim();
                line = line.substring(0, line.indexOf("Z\",\"")).trim();
                line = line.substring(line.indexOf("T")+1).trim();
                datesTime.add(line);
            } else { if (!toRemove.contains(oldData.indexOf(line)))
            { toRemove.add(oldData.indexOf(line)); } }
        }
        ///// COLLECT datesTime /////


        ///// DELETE bad entries /////
        for (Integer i : toRemove) {
            oldData.set(i, "REMOVE");
        } oldData.removeAll(Collections.singletonList("REMOVE"));
        ///// DELETE bad entries /////


        ///// MERGE coordsLat, coordsLon, datesDay, datesTime /////
        List<String> newData = new ArrayList<>();
        int i = 0;
        while (i < oldData.size()) {
            if (oldData.get(i).length() >= 5) {
                newData.add(coordsLat.get(i) + "," + coordsLon.get(i) + "," +
                        datesDay.get(i) + "," + datesTime.get(i));
                i++;
            }
        }
        newData.add(0,"Latitude,Longitude,Date,Time");
        filename = "owntracksLog.csv";
        file = Paths.get(filename);
        Files.write(file, newData, StandardCharsets.UTF_8);
        ///// MERGE coordsLat, coordsLon, datesDay, datesTime /////
    }
    private static String[] readFileAsString() throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(filename)))
                .replaceAll("\n", "").replaceAll
                        ("\r","").split("\\{\"id\"");
    }
    private static List<String> readFileStrings() throws Exception
    {
        String readFilename = "owntracksLog.txt";
        return Files.readAllLines(Paths.get(readFilename));
    }
}
