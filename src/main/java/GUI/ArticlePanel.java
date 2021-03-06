package GUI;

import SQLhandling.Selector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

public class ArticlePanel
{
    private JTable ArticleTable;
    private JTextField ArticleSearchField;
    private DefaultTableModel ArticleTableModel;
    private JFrame MainWindowFrame;
    private MainWindow MainInstance;
    private ArticlePanel ArticlePanelInstance = this;

    ArticlePanel(JTable articleTable, JTextField articleSearchField, DefaultTableModel articleTableModel, JFrame frame, MainWindow mainInstance)
    {
        ArticleTable = articleTable;
        ArticleSearchField = articleSearchField;
        ArticleTableModel = articleTableModel;
        MainWindowFrame = frame;
        MainInstance = mainInstance;
    }

    KeyListener ArticleSearchFieldListener = new KeyListener()
    {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e)
        {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) ArticleTable.getModel());
            sorter.setRowFilter(RowFilter.regexFilter(ArticleSearchField.getText()));
            ArticleTable.setRowSorter(sorter);
        }
    };

    ActionListener AddArticleButtonListener =  new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            // TODO
            // dodawanie artykułu - dodanie do bazy i na serwer
        }
    };

    MouseAdapter ArticleTableListener = new MouseAdapter()
    {
        @Override
        public void mouseClicked(MouseEvent evt)
        {
            int row = ArticleTable.convertRowIndexToModel(ArticleTable.rowAtPoint(evt.getPoint()));
            ArrayList<String> ArticleValues = Selector.select(
                    "SELECT A.title AS tytul, UA.login AS autor, S.name AS specjalizacja, UR.login AS redaktor, A.state as stan, A.articleID\n" +
                    "FROM Article A\n" +
                    "JOIN sysuser UA ON A.AuthorID = UA.userID\n" +
                    "LEFT JOIN specialization S ON S.specializationID = A.specializationID \n" +
                    "LEFT JOIN sysuser UR ON A.RedactorID = UR.userID\n" +
                    "LIMIT " + row + ", 1").get(0);

            new ArticleDetails(MainWindowFrame, ArticleValues, MainInstance, ArticlePanelInstance);

            /*
            String message = "";
            for(String Value : ArticleValues)
                message+= Value + " ";
            JOptionPane.showMessageDialog(null, message, "Błąd", JOptionPane.PLAIN_MESSAGE);*/
        }
    };

    void UpdateArticleTable()
    {
        ArticleTableModel.setNumRows(0);

        ArrayList<ArrayList<String>> ArticleList = Selector.select("SELECT S.login AS author, A.title, SR.login as redactor, SP.name as specialization\n" +
                "FROM Article A\n" +
                "LEFT JOIN Sysuser S ON A.AuthorID = S.userID\n" +
                "LEFT JOIN sysuser SR ON A.RedactorID = SR.userID\n" +
                "LEFT JOIN specialization SP ON  A.specializationID = SP.specializationID\n" +
                "WHERE S.userID = '" + CurrentUser.getID() + "';");

        for(ArrayList<String> iter : ArticleList)
            ArticleTableModel.addRow(new Vector<String>(iter));
    }
}
