package registrationform;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class Registrationform extends JFrame {

    private final JTextField txtSubjectName;
    private final JTextField txtSksCredit;
    private final JTable tblSubjects;
    private final JButton btnSave;
    private final JButton btnDelete;
    private final DefaultTableModel tableModel;
    private final JLabel lblFormRegistration;
    private final JLabel lblTotalSks;
    private final Set<String> subjectNamesSet;
    private int totalSks;

   public Registrationform() {
        setSize(800, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // inisialiasi component
        lblFormRegistration = new JLabel("Form Registration");
        lblFormRegistration.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel lblSubjectName = new JLabel("Subject name");
        txtSubjectName = new JTextField(15);

        JLabel lblSksCredit = new JLabel("SKS credit      ");
        txtSksCredit = new JTextField(15);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Subject Name");
        tableModel.addColumn("SKS");

        tblSubjects = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblSubjects.setBackground(new Color(238, 238, 238));
        tblSubjects.setSelectionBackground(new Color(144, 202, 249));
        tblSubjects.getTableHeader().setReorderingAllowed(false);

        btnSave = new JButton("Save");
        btnDelete = new JButton("Delete");

        lblTotalSks = new JLabel("Total SKS: 0");
        lblTotalSks.setFont(new Font("Arial", Font.BOLD, 14));

        // Set layout frame ket borderlayout
        setLayout(new BorderLayout());

        // Panel untuk judul  form registration
        JPanel panelForm = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelForm.add(lblFormRegistration);
        add(panelForm, BorderLayout.NORTH);

        // Panel untuk memasukan inputan
        JPanel panelInputs = new JPanel(new GridLayout(3, 2, 5, 5));
        panelInputs.add(lblSubjectName);
        panelInputs.add(txtSubjectName);
        panelInputs.add(lblSksCredit);
        panelInputs.add(txtSksCredit);
        add(panelInputs, BorderLayout.WEST);

        // Panel untuk button 
        panelInputs.add(btnSave);
        panelInputs.add(btnDelete);

        // Panel untuk tabel 
        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.add(new JScrollPane(tblSubjects), BorderLayout.CENTER);
       
        add(panelTable, BorderLayout.CENTER);
        
       // Panel untuk total sks
        JPanel panelTotalSKS = new JPanel();
        panelTotalSKS.setLayout(new FlowLayout( FlowLayout.CENTER));
        panelTotalSKS.add(lblTotalSks);
        add(panelTotalSKS, BorderLayout.SOUTH);      

        // inisialiasi listener
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();
            }
        });

        setVisible(true);

        totalSks = 0;
        subjectNamesSet = new HashSet<>();
    }

    private void saveData() {
        //validasi data
        String subjectName = txtSubjectName.getText().trim();
        String sksCreditString = txtSksCredit.getText().trim();

        if (subjectName.isEmpty() || sksCreditString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data harus terisi semua.");
            return;
        }

        try {
            int sksCredit = Integer.parseInt(sksCreditString);
            if (sksCredit <= 0) {
                JOptionPane.showMessageDialog(this, "SKS harus bilangan bulat positif.");
                return;
            }

            if (subjectNamesSet.contains(subjectName.toUpperCase())) {
                JOptionPane.showMessageDialog(this, "Data sudah tersedia.");
                return;
            }

            if (totalSks + sksCredit > 20) {
                JOptionPane.showMessageDialog(this, "SKS tidak boleh lebih dari 20.");
                return;
            }

            Object[] rowData = {subjectName, sksCredit};
            tableModel.addRow(rowData);

            subjectNamesSet.add(subjectName.toUpperCase());
            totalSks += sksCredit;
            lblTotalSks.setText("Total SKS: " + totalSks);

            txtSubjectName.setText("");
            txtSksCredit.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Format tidak valid.");
        }
    }

    private void deleteData() {
        int selectedRow = tblSubjects.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Tidak ada data yang dipilih untuk dihapus.");
            return;
        }

        int sksCreditToDelete = (int) tblSubjects.getValueAt(selectedRow, 1);

        subjectNamesSet.remove(tblSubjects.getValueAt(selectedRow, 0).toString().toUpperCase());
        totalSks -= sksCreditToDelete;
        lblTotalSks.setText("Total SKS: " + totalSks);

        tableModel.removeRow(selectedRow);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Registrationform::new);
    }
}
