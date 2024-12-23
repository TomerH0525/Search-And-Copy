import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;

public class FileSearcherAndCopierGUI {

    private JFrame frame;
    private JTextArea searchStringsTextArea;
    private JButton searchAndCopyButton;
    private String sourceDirectoryPath;
    private String destinationDirectoryPath;
    private JTextField sourceDirectoryPath2;
    private JLabel sourceDirectoryPath2Label;
    private JTextField destinationDirectoryPath2;
    private JLabel destinationDirectoryPath2Label;

//    public FileSearcherAndCopierGUI() {
//        frame = new JFrame("File Searcher and Copier");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 200);
//
//        JPanel panel = new JPanel(new BorderLayout());
//
//        searchStringsTextArea = new JTextArea(5, 20);
//        JScrollPane scrollPane = new JScrollPane(searchStringsTextArea);
//        panel.add(scrollPane, BorderLayout.CENTER);
//
//        searchAndCopyButton = new JButton("Search and Copy");
//        searchAndCopyButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                searchAndCopyFiles();
//            }
//        });
//        panel.add(searchAndCopyButton, BorderLayout.SOUTH);
//
//        frame.add(panel);
//        frame.setVisible(true);
//
//        // Set default paths (you can modify these)
//        sourceDirectoryPath = "\\\\iis2\\priority\\system\\load\\LOGRESERVEDIN\\ShipApiFiles";
//        destinationDirectoryPath = "C:\\Users\\tomer_h\\Desktop\\test";
//    }
//
//    private void searchAndCopyFiles() {
//        String[] searchStrings = searchStringsTextArea.getText().split("\n");
//        Arrays.stream(searchStrings).forEach((string ) -> System.out.println("this is the string = "+string));
//
//        File sourceDirectory = new File(sourceDirectoryPath);
//        File destinationDirectory = new File(destinationDirectoryPath);
//
//        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
//            JOptionPane.showMessageDialog(frame, "Invalid source directory path.");
//            return;
//        }
//
//        if (!destinationDirectory.exists()) {
//            if (!destinationDirectory.mkdirs()) {
//                JOptionPane.showMessageDialog(frame, "Failed to create destination directory.");
//                return;
//            }
//        } else if (!destinationDirectory.isDirectory()) {
//            JOptionPane.showMessageDialog(frame, "Destination path is not a directory.");
//            return;
//        }
//
//        for (String searchString : searchStrings) {
//            System.out.println("this is the searched string = " +searchString);
//            searchAndCopyForString(searchString, sourceDirectory, destinationDirectory);
//        }
//    }
//
//    private void searchAndCopyForString(String searchString, File sourceDirectory, File destinationDirectory) {
//
//        File[] files = sourceDirectory.listFiles((dir, name) -> (name.contains(searchString) && name.endsWith(".txt")));
//
//        if (files != null && files.length > 0) {
//            System.out.println("Found files for \"" + searchString + "\":");
//            for (File file : files) {
//                System.out.println(file.getAbsolutePath());
//
//                try {
//                    File destinationFile = new File(destinationDirectory, file.getName());
//                    Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                    System.out.println("Copied " + file.getName() + " to " + destinationFile.getAbsolutePath());
//                } catch (IOException ex) {
//                    System.err.println("Error copying file: " + ex.getMessage());
//                }
//            }
//        } else {
//            System.out.println("No files found for \"" + searchString + "\".");
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new FileSearcherAndCopierGUI());
//    }
//}

public FileSearcherAndCopierGUI() {
    frame = new JFrame("File Searcher and Copier");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setMinimumSize(new Dimension(500,350));
    frame.setPreferredSize(new Dimension(500,400));
    frame.setMaximumSize(new Dimension(550, 400));
    frame.setResizable(Boolean.FALSE);

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setMinimumSize(new Dimension(500,350));
    panel.setPreferredSize(new Dimension(500,400));
    panel.setMaximumSize(new Dimension(550, 400));


    FlowLayout flowlayout = new FlowLayout();
    JPanel panel4 = new JPanel(new FlowLayout());

    searchStringsTextArea = new JTextArea(5, 20);
    JScrollPane scrollPane = new JScrollPane(searchStringsTextArea);
    JTextPane scrollpaneLabel = new JTextPane();
    scrollpaneLabel.setText("to begin searching files to copy please provide the next things: \n 1.Source directory to search the files \n 2.Destination directory to copy and paste the found files \n 3.the strings to search in source directory");
    panel4.add(scrollpaneLabel);
    panel4.add(scrollPane);


    JPanel panel2 = new JPanel();
    panel2.setLayout(new FlowLayout());


    sourceDirectoryPath2 = new JTextField();
    sourceDirectoryPath2.setColumns(20);
    sourceDirectoryPath2Label = new JLabel("Directory to search in : ");
    sourceDirectoryPath2Label.setText("Directory to search in : ");

    sourceDirectoryPath2Label.setLabelFor(sourceDirectoryPath2);

    panel2.add(sourceDirectoryPath2Label);
    panel2.add(sourceDirectoryPath2);


    destinationDirectoryPath2 = new JTextField();
    destinationDirectoryPath2.setColumns(20);;
    destinationDirectoryPath2Label = new JLabel("Directory to search in : ");
    destinationDirectoryPath2Label.setText("Destination folder to copy to : ");

    destinationDirectoryPath2Label.setLabelFor(destinationDirectoryPath2);

    panel2.add(destinationDirectoryPath2Label);
    panel2.add(destinationDirectoryPath2);


    panel4.add(panel2);


    searchAndCopyButton = new JButton("Search and Copy");
    panel4.add(searchAndCopyButton);
    searchAndCopyButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            sourceDirectoryPath = sourceDirectoryPath2.getText().replaceAll("\\\\", "\\\\\\\\").toString();
            destinationDirectoryPath = destinationDirectoryPath2.getText().replaceAll("\\\\","\\\\\\\\").toString();
            System.out.println("sourceDirectoryPath = "+sourceDirectoryPath);
            System.out.println("destinationDirectoryPath = "+destinationDirectoryPath);
            searchAndCopyFiles();
        }
    });
    panel.add(panel4, BorderLayout.PAGE_START);
//    panel.add(panel3,BorderLayout.CENTER);

    frame.add(panel);
    frame.setVisible(true);

    // Set default paths (you can modify these)

}

private void searchAndCopyFiles() {
    String[] initialSearchStrings = searchStringsTextArea.getText().split("\n");
    ArrayList<String> searchStrings = new ArrayList<>(Arrays.asList(initialSearchStrings));

    File sourceDirectory = new File(sourceDirectoryPath);
    File destinationDirectory = new File(destinationDirectoryPath);

    if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
        JOptionPane.showMessageDialog(frame, "Invalid source directory path.");
        return;
    }

    if (!destinationDirectory.exists()) {
        if (!destinationDirectory.mkdirs()) {
            JOptionPane.showMessageDialog(frame, "Failed to create destination directory.");
            return;
        }
    } else if (!destinationDirectory.isDirectory()) {
        JOptionPane.showMessageDialog(frame, "Destination path is not a directory.");
        return;
    }

    File[] files = sourceDirectory.listFiles();

    if (files != null && files.length > 0) {
        for (File file : files) {
            if (file.getName().endsWith(".txt")) {
                for (int i = 0; i < searchStrings.size(); i++) {
                    String searchString = searchStrings.get(i);
                    if (file.getName().contains(searchString)) {
                        try {
                            File destinationFile = new File(destinationDirectory, file.getName());
                            Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("Copied " + file.getName() + " to " + destinationFile.getAbsolutePath());
                            searchStrings.remove(i); // Remove the matched string found in the directory
                            i--; // because of file removal adjust index to match the remaining number of strings to search
                            break;
                        } catch (IOException ex) {
                            System.err.println("Error copying file: " + ex.getMessage());
                        }
                    }
                }
                if (searchStrings.isEmpty()) { // Check if all search strings have been matched
                    JOptionPane.showMessageDialog(frame,"All search strings have been matched and copied.");
                    return; // Exit the function early
                }
            }
        }
    } else {
        System.out.println("No files found.");
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FileSearcherAndCopierGUI());
    }
}