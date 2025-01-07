import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
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
    private JCheckBox searchDuplicates;


    public FileSearcherAndCopierGUI() {
        frame = new JFrame("File Searcher and Copier");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(500, 375));
        frame.setResizable(Boolean.FALSE);

        JPanel firstPanel = new JPanel();

        searchStringsTextArea = new JTextArea(8, 20);
        JScrollPane scrollPane = new JScrollPane(searchStringsTextArea);
        JTextPane scrollpaneLabel = new JTextPane();
        scrollpaneLabel.setText("to begin searching files to copy please provide the next things: \n 1.Source directory to search the files \n 2.Destination directory to copy and paste the found files \n 3.the strings to search in source directory");
        firstPanel.add(scrollpaneLabel);
        firstPanel.add(scrollPane);

        JPanel directoryPanel = new JPanel();

        sourceDirectoryPath2 = new JTextField();
        sourceDirectoryPath2.setColumns(20);
        sourceDirectoryPath2Label = new JLabel("Directory to search in : ");
        sourceDirectoryPath2Label.setText("Directory to search in : ");

        sourceDirectoryPath2Label.setLabelFor(sourceDirectoryPath2);

        JPanel directorySourcePanel = new JPanel();
        directorySourcePanel.add(sourceDirectoryPath2Label);
        directorySourcePanel.add(sourceDirectoryPath2);

        firstPanel.add(directorySourcePanel);

        destinationDirectoryPath2 = new JTextField();
        destinationDirectoryPath2.setColumns(20);
        destinationDirectoryPath2Label = new JLabel("Directory to search in : ");
        destinationDirectoryPath2Label.setText("Destination folder to copy to : ");

        destinationDirectoryPath2Label.setLabelFor(destinationDirectoryPath2);

        JPanel directoryDestinationPanel = new JPanel();
        directoryDestinationPanel.add(destinationDirectoryPath2Label);
        directoryDestinationPanel.add(destinationDirectoryPath2);

        firstPanel.add(directoryDestinationPanel);

        searchAndCopyButton = new JButton("Search and Copy");
        firstPanel.add(searchAndCopyButton);
        searchAndCopyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sourceDirectoryPath = sourceDirectoryPath2.getText().replaceAll("\\\\", "\\\\\\\\").toString();
                destinationDirectoryPath = destinationDirectoryPath2.getText().replaceAll("\\\\", "\\\\\\\\").toString();
                System.out.println("sourceDirectoryPath = " + sourceDirectoryPath);
                System.out.println("destinationDirectoryPath = " + destinationDirectoryPath);
                searchAndCopyFiles();
            }
        });

        firstPanel.add(directoryPanel);
        frame.add(firstPanel);
        frame.setVisible(true);

    }

    private void searchAndCopyFiles() {
        String[] initialSearchStrings = searchStringsTextArea.getText().split("\n");
        ArrayList<String> searchStrings = new ArrayList<>(Arrays.asList(initialSearchStrings));

        //creating FileFilter to use instead of iterating normaly...
        //testing performance and speed
        FileFilter fileFilter = file -> {
            if (file.isFile() && !searchStrings.isEmpty() && !file.getName().toLowerCase().endsWith(".txt")) {

                for (int i = 0; i < searchStrings.size(); i++) {
                    if (file.getName().contains(searchStrings.get(i))) {
//
//                            if (!searchDuplicates) {
//                                searchStrings.remove(i);
//                            }
                        return true;
                    }
                }
            }
            return false;
        };


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
                        JOptionPane.showMessageDialog(frame, "All search strings have been matched and copied.");
                        return; // Exit the function early
                    }
                }
            }
            if (!searchStrings.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No files found with the requested strings : \n" +
                        searchStrings.toString());
            }

        } else {
            JOptionPane.showMessageDialog(frame, "No files found.");
        }
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        SwingUtilities.invokeLater(() -> new FileSearcherAndCopierGUI());

    }
}
