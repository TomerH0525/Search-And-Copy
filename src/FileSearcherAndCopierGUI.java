import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;

public class FileSearcherAndCopierGUI {

    private JFrame mainFrame;
    private JTextArea searchTextArea;
    private JButton button_StartSearching;
    private String directoryPath;
    private String DestinationPath;
    private JTextField sourceDirectoryPath2;
    private JLabel sourceDirectoryPath2Label;
    private JTextField destinationDirectoryPath2;
    private JLabel destinationDirectoryPath2Label;
    private JCheckBox flag_SearchDuplicates;
    private JLabel label_SearchDuplicates;
    private JCheckBox flag_ExactMatch;
    private JLabel label_ExactMatch;
    private JButton helpButton;

    public FileSearcherAndCopierGUI() {

        //init main frame
        mainFrame = new JFrame("Search and copy files");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setPreferredSize(new Dimension(500, 400));
        mainFrame.pack();
        mainFrame.setResizable(Boolean.FALSE);


        //main frame head (application settings bar)
        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        helpButton = new JButton("help");
        helpButton.setPreferredSize(new Dimension(55,20));
        helpButton.setFont(new Font(helpButton.getFont().getName(),Font.BOLD,10));
        helpButton.addActionListener(actionListenenr -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "to begin searching files to copy please provide the next things: \n " +
                    "1.Source directory to search the files \n " +
                    "2.Destination directory to copy and paste the found files \n " +
                    "3.the strings to search in source directory"
            ,"Help",JOptionPane.QUESTION_MESSAGE);
        });

        panel4.add(helpButton);
        mainFrame.add(BorderLayout.NORTH,panel4);


        //main frame body
        searchTextArea = new JTextArea(8, 20);
        JScrollPane scrollPane = new JScrollPane(searchTextArea);

        flag_SearchDuplicates = new JCheckBox();
        label_SearchDuplicates = new JLabel("Search duplicates?");
        label_SearchDuplicates.setLabelFor(flag_SearchDuplicates);

        flag_ExactMatch = new JCheckBox();
        label_ExactMatch = new JLabel("exact match only?");
        label_ExactMatch.setLabelFor(flag_ExactMatch);

        sourceDirectoryPath2 = new JTextField();
        sourceDirectoryPath2.setColumns(20);
        sourceDirectoryPath2Label = new JLabel();
        sourceDirectoryPath2Label.setText("Directory to search in : ");
        sourceDirectoryPath2Label.setLabelFor(sourceDirectoryPath2);

        destinationDirectoryPath2 = new JTextField();
        destinationDirectoryPath2.setColumns(20);
        destinationDirectoryPath2Label = new JLabel();
        destinationDirectoryPath2Label.setText("Destination folder to copy to : ");
        destinationDirectoryPath2Label.setLabelFor(destinationDirectoryPath2);

        button_StartSearching = new JButton("Search and Copy");
        button_StartSearching.addActionListener(actionListener -> {
                directoryPath = sourceDirectoryPath2.getText().replaceAll("\\\\", "\\\\\\\\").toString();
                DestinationPath = destinationDirectoryPath2.getText().replaceAll("\\\\", "\\\\\\\\").toString();
                System.out.println("sourceDirectoryPath = " + directoryPath);
                System.out.println("destinationDirectoryPath = " + DestinationPath);
                searchAndCopyFiles();
            });


        JPanel firstPanel = new JPanel(new FlowLayout());

        firstPanel.add(scrollPane);

        JPanel flagsPanel = new JPanel(new GridLayout(0,2,5,5));

        flagsPanel.add(label_SearchDuplicates);
        flagsPanel.add(flag_SearchDuplicates);
        flagsPanel.add(label_ExactMatch);
        flagsPanel.add(flag_ExactMatch);


        firstPanel.add(flagsPanel);


        JPanel directoryPanel = new JPanel(new GridLayout(0,2,5,0));

        directoryPanel.add(sourceDirectoryPath2Label);
        directoryPanel.add(sourceDirectoryPath2);

        directoryPanel.add(destinationDirectoryPath2Label);
        directoryPanel.add(destinationDirectoryPath2);

        firstPanel.add(directoryPanel);
        firstPanel.add(button_StartSearching);
        firstPanel.add(directoryPanel);

        mainFrame.add(firstPanel);
        searchTextArea.grabFocus();
        mainFrame.setVisible(true);

    }

    private void searchAndCopyFiles() {
        String[] initialSearchStrings = searchTextArea.getText().split("\n");
        ArrayList<String> searchStrings = new ArrayList<>(Arrays.asList(initialSearchStrings));

        //creating FileFilter to use instead of iterating normaly...
        //testing performance and speed
        FileFilter fileFilter = file -> {
            System.out.println(flag_SearchDuplicates.isSelected());
            if (file.isFile() && !searchStrings.isEmpty() && file.getName().toLowerCase().endsWith(".txt")) {

                for (int i = 0; i < searchStrings.size(); i++) {
//                    if (exactMatch)
                    if (file.getName().contains(searchStrings.get(i))) {

                            if (!flag_SearchDuplicates.isSelected()) {
                                searchStrings.remove(i);
                            }

                        return true;
                    }
                }
            }
            return false;
        };


        File sourceDirectory = new File(directoryPath);
        File destinationDirectory = new File(DestinationPath);

        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
            JOptionPane.showMessageDialog(mainFrame, "Invalid source directory path.");
            return;
        }

        if (!destinationDirectory.exists()) {
            if (!destinationDirectory.mkdirs()) {
                JOptionPane.showMessageDialog(mainFrame, "Failed to create destination directory.");
                return;
            }
        } else if (!destinationDirectory.isDirectory()) {
            JOptionPane.showMessageDialog(mainFrame, "Destination path is not a directory.");
            return;
        }

        File[] files = sourceDirectory.listFiles();

        File[] files2 = sourceDirectory.listFiles(fileFilter);
        System.out.println(Arrays.toString(files2));

        if (files != null && files.length > 0) {
            for (File file : files) {
                String filename = file.getName();

                if (filename.toLowerCase().endsWith(".txt")) { //check if the file ends with txt extension doesn't matter if its capital or not.
                    for (int i = 0; i < searchStrings.size(); i++) {
                        String searchString = searchStrings.get(i);

                        if (filename.contains(searchString)) {
                            try {
                                File destinationFile = new File(destinationDirectory, filename);
                                Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                System.out.println("Copied " + filename + " to " + destinationFile.getAbsolutePath());
                                searchStrings.remove(i); // Remove the matched string found in the directory
                                i--; // because of file removal adjust index to match the remaining number of strings to search
                                break;
                            } catch (IOException ex) {
                                System.err.println("Error copying file: " + ex.getMessage());
                            }
                        }
                    }
                    if (searchStrings.isEmpty()) { // Check if all search strings have been matched
                        JOptionPane.showMessageDialog(mainFrame, "All search strings have been matched and copied.");
                        return; // Exit the function early
                    }
                }
            }
            if (!searchStrings.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "No files found with the requested strings : \n" +
                        searchStrings.toString());
            }

        } else {
            JOptionPane.showMessageDialog(mainFrame, "No files found.");
        }
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("metal");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            System.err.println("tried to set application look and feel and failed with the following exception -> "+e);
        }

        SwingUtilities.invokeLater(FileSearcherAndCopierGUI::new);

    }
}
