package admin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import custom_classes.DBQueries;
import custom_classes.Results;
import custom_classes.Theme;

import custom_components.RoundedCornerPanel;
import custom_components.WButton;
import custom_components.WTable;
import custom_components.WTextField;
import custom_components.GlassPanePopup.GlassPane;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;

public class SearchEmployeePage extends RoundedCornerPanel {

    JLabel txtTitle;
    WButton btnClose, btnSelectEmp;
    JComboBox<String> filterBox;
    WTextField txtInput;
    WTable tableEmps;
    DefaultTableModel tableModel;
    SelectedEmployee selectedEmployee;

    String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

    public SearchEmployeePage(SelectedEmployee selectedEmployee) {
        init();
        grabFocus();
        this.selectedEmployee = selectedEmployee;
    }

    public interface SelectedEmployee {
        void getSelectedEmployee(String id, String name);
    }

    private void init() {
        setPreferredSize(new Dimension((int) Theme.FRAME_SIZE.getWidth() - 200, 500));
        setMinimumSize(getPreferredSize());
        setBackground(Theme.TRANSPARENT_COLOR);
        setCornerRadius(0);
        setBgColor(Theme.BG_COLOR);
        setLayout(new MigLayout("fillx, insets 20, gapy 10, gapx 0"));

        // Title
        txtTitle = new JLabel("Search Employee");
        txtTitle.setFont(Theme.poppinsSemiboldTitleFont);
        txtTitle.setForeground(Theme.MUTED_TEXT_COLOR);
        add(txtTitle, "top, left");

        // Close Button
        btnClose = new WButton();
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
        Icon icon = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 28, Theme.PRIMARY);
        btnClose.setIcon(icon);
        btnClose.setCornerRadius(128);
        btnClose.setBorder(new EmptyBorder(5, 5, 5, 5));
        btnClose.setBgColor(Theme.TRANSPARENT_COLOR);
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlassPane.closePopupLast();
            }
        });
        add(btnClose, "right, gapbottom 10, wrap");

        // Filter
        filterBox = new JComboBox<>();
        filterBox.addItem("Name");
        filterBox.addItem("Email");
        filterBox.setLightWeightPopupEnabled(false);
        add(filterBox, "cell 0 1, height 40, width 10%");

        // Searching Field
        txtInput = new WTextField();
        txtInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchEmps();
            }
        });
        add(txtInput, "cell 0 1,, width 60%, gap 0, height 40");

        // Search Employee Button
        btnSelectEmp = new WButton("Search");
        btnSelectEmp.setBgColor(Theme.PRIMARY);
        btnSelectEmp.setFont(Theme.latoFont);
        btnSelectEmp.setForeground(Color.WHITE);
        btnSelectEmp.setCornerRadius(0);
        btnSelectEmp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchEmps();
            }
        });
        add(btnSelectEmp, "height 40, width 30%, gapx 0, wrap");

        // Searched Employees Table
        tableEmps = new WTable();
        tableEmps.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane tableContainer = new JScrollPane(tableEmps);
        add(tableContainer, "width 100%, span");
        String cols[] = { "ID", "Name", "Email" };
        tableModel = new DefaultTableModel(cols, 0);
        tableEmps.setModel(tableModel);
        tableEmps.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tableEmps.getSelectedRow() != -1) {
                    int selection = JOptionPane.showConfirmDialog(getParent(),
                            "Are you sure you want to select this Employee?",
                            "Select Employee", JOptionPane.YES_NO_OPTION);
                    if (selection == 0) {
                        // User Selected YES
                        String id = tableEmps.getValueAt(tableEmps.getSelectedRow(), 0).toString();
                        String name = tableEmps.getValueAt(tableEmps.getSelectedRow(), 1).toString();
                        selectedEmployee.getSelectedEmployee(id, name);
                        GlassPane.closePopupLast();
                    }
                }
            }

        });
    }

    // To Search Employee in the Database from selected filter and input
    private void searchEmps() {
        String input = txtInput.getText();

        if (input.equals("")) {
            JOptionPane.showMessageDialog(getParent(), "Please insert value to be searched");
            return;
        }

        String filter = filterBox.getSelectedItem().toString();

        // Validating Email
        if (filter.equals("Email")) {
            Matcher matcher = Pattern.compile(regex).matcher(input);
            if (!matcher.matches()) {
                JOptionPane.showMessageDialog(getParent(), "Please Enter valid Email");
                return;
            }
        }

        // Reset Table
        tableModel.setRowCount(0);

        // Sending Request
        DBQueries.getUnAssignedEmps(input, filter, (queryStatus, details) -> {
            if (queryStatus == Results.ERROR) {
                JOptionPane.showMessageDialog(getParent(), "Failed to Communicate with Server");
                return;
            } else if (queryStatus == Results.FAILED) {
                JOptionPane.showMessageDialog(getParent(), "Failed to Execute Query, please try again");
                return;
            } else if (queryStatus == Results.NO_RECORD_FOUND) {
                JOptionPane.showMessageDialog(getParent(), "No Record Found");
                return;
            }

            Vector<Object> data = new Vector<>();
            data.add(details.getEmpID());
            data.add(details.getEmpName());
            data.add(details.getEmpEmail());
            tableModel.addRow(data);
        });
    }

}
