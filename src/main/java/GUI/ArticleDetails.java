package GUI;

import SQLhandling.Selector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

public class ArticleDetails
{
    private MainWindow MainInstance;
    private JFrame frame;
    private JFrame parentFrame;
    private ArrayList<String> ArticleValues;
    private ArticlePanel ArticlePanelInstance;

    private JButton updateArticleButton;
    private JButton deleteArticleButton;
    private JLabel titleLabel;
    private JLabel authorLabel;
    private JLabel specializationLabel;
    private JLabel redactorLabel;
    private JPanel ArticleDetailsPanel;
    private JLabel stateLabel;
    private JButton viewArticleButton;
    private JTable ReviewsTable;
    private DefaultTableModel ReviewsTableModel;

    private final Logger logger = LogManager.getLogger(UserDetails.class);
    private static final Object[] confirmOptions = {"     Tak     ","     Nie     "};


    public ArticleDetails(JFrame mainWindowFrame, ArrayList<String> articleValues, MainWindow mainInstance, ArticlePanel articlePanelInstance)
    {
        parentFrame = mainWindowFrame;
        ArticleValues = articleValues;
        MainInstance = mainInstance;
        ArticlePanelInstance = articlePanelInstance;

        frame = new JFrame("Szczegóły artykułu");
        frame.setContentPane(this.ArticleDetailsPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(parentFrame);
        parentFrame.setEnabled(false);
        UpdateReviewsTable();

        frame.addWindowListener(new WindowAdapter()     // closing window - activating main window again
        {
            @Override
            public void windowClosing(WindowEvent we)
            {
                frame.dispose();
                parentFrame.setEnabled(true);
                parentFrame.setVisible(true);
            }
        });

        titleLabel.setText(articleValues.get(0));
        authorLabel.setText(articleValues.get(1));
        if(articleValues.get(2)!=null)  specializationLabel.setText(articleValues.get(2));
        if(articleValues.get(3)!=null)  redactorLabel.setText(articleValues.get(3));
        if(articleValues.get(4)!=null)  stateLabel.setText(articleValues.get(4));

        viewArticleButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // TODO
                // podgłąd artykułu
            }
        });

        updateArticleButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // TODO
                // wgranie nowego pliku artykułu
            }
        });

        deleteArticleButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // TODO
                // usunięcie artykułu (tylko jeżeli ma stan inny niż accepted)
            }
        });

        ReviewsTable.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent evt)
            {
                int row = ReviewsTable.convertRowIndexToModel(ReviewsTable.rowAtPoint(evt.getPoint()));

                ArrayList<String> ReviewValues = Selector.select("" +
                        "SELECT R.title, UR.login, R.Rating\n" +
                        "FROM review R, sysuser UR\n" +
                        "WHERE R.ReviewerID = UR.userID AND R.articleID = '" + ArticleValues.get(5) + "'" +
                        "LIMIT " + row + ", 1;").get(0);

                String temp = "";

                for(String s : ReviewValues)
                    temp+=" "+s;

                JOptionPane.showMessageDialog(frame, temp, "Recenzja:", JOptionPane.PLAIN_MESSAGE);
            }
        });
    }

    void UpdateReviewsTable()
    {
        ReviewsTableModel.setNumRows(0);

        ArrayList<ArrayList<String>> ReviewsList = Selector.select("" +
                "SELECT R.title, UR.login, R.Rating\n" +
                "FROM review R, sysuser UR\n" +
                "WHERE R.ReviewerID = UR.userID AND R.articleID = '" + ArticleValues.get(5) + "';");

        for(ArrayList<String> iter : ReviewsList)
            ReviewsTableModel.addRow(new Vector<String>(iter));
    }

    private void createUIComponents()
    {
        ReviewsTable = new JTable();
        String[] ReviewsTableColumns = {"Tytuł recenzji", "Autor", "Ocena"};
        ReviewsTable.setModel(new DefaultTableModel(ReviewsTableColumns,0));
        ReviewsTableModel = (DefaultTableModel) ReviewsTable.getModel();
        ReviewsTable.setDefaultEditor(Object.class, null);
        ReviewsTable.setAutoCreateRowSorter(true);
    }
}
