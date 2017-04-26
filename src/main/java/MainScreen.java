import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by sylentbv on 4/25/2017.
 */
public class MainScreen extends JFrame implements WindowListener{
    private JPanel rootPanel;
    private JComboBox mediaTypeCB;
    private JButton addNewButton;
    private JTextField searchTextTF;
    private JComboBox searchGenreCB;
    private JComboBox searchMediaTypeCB;
    private JButton searchButton;
    private JTable resultsJT;
    private JButton editButton;
    private JButton deleteButton;
    private JPanel resultsPanel;

    dbAccess mainDB;

    MainScreen(){
        //set up window
        setContentPane(rootPanel);
        pack();
        setTitle("Media Tracking Center");
        addWindowListener(this);

        setSize(new Dimension(500, 400));
        setLocation(500,200);

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainDB = new dbAccess();

        addListeners();
    }

    private void addListeners() {
        addNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //call window for appropriate selected media type
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //populate jtable with results
            }
        });
    }

    @Override
    public void windowClosing(WindowEvent e) {
        mainDB.shutdown();
        System.out.println("closing");
    }

    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
}
