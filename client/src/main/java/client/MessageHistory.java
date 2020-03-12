package client;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MessageHistory {

    private static File file;
    private static FileWriter fileWriter;
    private static BufferedWriter bufferedWriter;

    public static void fileConnect(String nickname) throws IOException {
        file = new File(String.format("client/src/main/resources/history_%s.txt", nickname));
        fileWriter = new FileWriter(file, true);
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    public static void fileDisconnect() throws IOException {
        bufferedWriter.close();
        fileWriter.close();
    }

    public static void loadMessageHistory(Controller controller) throws IOException {
        List<String> list = new ArrayList<>();
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    list.add(line);
                }
                if (list.size() >= 100) {
                    for (int i = list.size() - 100; i < list.size(); i++) {
                        controller.textArea.appendText(list.get(i) + "\n");
                    }
                    return;
                }
                for (String s : list) {
                    controller.textArea.appendText(s + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public static void addMessageToFile(String s) throws IOException {
        bufferedWriter.write(s + "\n");
        bufferedWriter.flush();
    }
}
