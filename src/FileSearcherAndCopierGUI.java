import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;

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
    public HashMap<String, Integer> searchStrings = new HashMap<>();


    public FileSearcherAndCopierGUI() {

        List<Image> icons = new ArrayList<Image>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Project-IconX16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Project-IconX32.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Project-IconX64.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Project-IconX128.png")));
        //init main frame
        mainFrame = new JFrame("Search and copy files");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setPreferredSize(new Dimension(500, 400));
        mainFrame.setIconImages(icons); //setting icon to frame
        mainFrame.pack();
        mainFrame.setResizable(Boolean.FALSE);



        //main frame head (application settings bar)
        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        helpButton = new JButton("HELP");
        helpButton.setPreferredSize(new Dimension(60, 20));
        helpButton.setFont(new Font(helpButton.getFont().getName(), Font.BOLD, 10));
        helpButton.addActionListener(actionListenenr -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "To begin searching files to copy please provide the next things: \n " +
                            "1.Source directory to search the files \n " +
                            "2.Destination directory to copy and paste the found files \n " +
                            "3.the strings to search in source directory"
                    , "Help", JOptionPane.QUESTION_MESSAGE);
        });

        panel4.add(helpButton);
        mainFrame.add(BorderLayout.NORTH, panel4);


        //main frame body
        searchTextArea = new JTextArea(12, 20);
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

        JPanel flagsPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        flagsPanel.add(label_SearchDuplicates);
        flagsPanel.add(flag_SearchDuplicates);
        flagsPanel.add(label_ExactMatch);
        flagsPanel.add(flag_ExactMatch);

        firstPanel.add(flagsPanel);

        JPanel directoryPanel = new JPanel(new GridLayout(0, 2, 5, 0));

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

        for (String string : searchTextArea.getText().split("\n")) {
            searchStrings.put(string, 0);
        }

            FileFilter fileFilter = file -> {
                if (file.isFile() && !searchStrings.isEmpty() && file.getName().toLowerCase().endsWith(".txt")) {
                    for (String searchString : searchStrings.keySet()) {
//                    if (exactMatch)
                        if (file.getName().contains(searchString)) {
                            searchStrings.put(searchString, (searchStrings.get(searchString)+1));

                            if (!flag_SearchDuplicates.isSelected()) {
                                System.out.println("found first match! for the string = "+searchString);
                                searchStrings.remove(searchString);

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

            ArrayList<String> foundFiles = new ArrayList<String>();

            if (files2 != null && files2.length > 0) {
                for (File file : files2) {

                    System.out.println("starting to move found files _FILTERED_ files");
                    try {
                        File destinationFile = new File(destinationDirectory, file.getName());
                        Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (searchStrings.isEmpty()) { // Check if all search strings have been matched
                    JOptionPane.showMessageDialog(mainFrame, "All search strings have been matched and copied.");
                    System.out.println(searchStrings.toString());
                } else{
                    System.out.println("Searched string with the number of times it was found : ");
                    searchStrings.forEach((k,v) -> {
                        System.out.println("String: '"+k+"' , found: "+v+" times..");
                    });
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "No files found with the requested strings : \n" +
                        searchStrings.keySet());
            }

//        } else {
//            JOptionPane.showMessageDialog(mainFrame, "No files found.");
//        }
        }

        public static void main (String[]args){

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                     UnsupportedLookAndFeelException e) {
                System.err.println("tried to set application look and feel and failed with the following exception -> " + e);
            }

            SwingUtilities.invokeLater(FileSearcherAndCopierGUI::new);

        }
    }

