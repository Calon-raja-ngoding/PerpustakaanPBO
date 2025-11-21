package src.frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import src.backend.Kategori;
import java.util.ArrayList;

public class FrmKategori extends JFrame {

    private JTextField txtIdKategori;
    private JTextField txtNama;
    private JTextField txtKeterangan;

    private JButton btnSimpan;
    private JButton btnHapus;
    private JButton btnTambahBaru;
    private JTextField txtCari;
    private JButton btnCari;

    private JTable tblKategori;
    private DefaultTableModel tableModel;

    private Kategori kategoriObj;

    public FrmKategori() {
        kategoriObj = new Kategori();

        setTitle("Form Kategori Buku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); 

        JPanel panelInput = new JPanel(new GridBagLayout());
        setupPanelInput(panelInput);
        add(panelInput, BorderLayout.NORTH);

        JPanel panelKontrol = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setupPanelKontrol(panelKontrol);
        add(panelKontrol, BorderLayout.CENTER);

        setupTable();
        JScrollPane scrollPane = new JScrollPane(tblKategori);
        scrollPane.setPreferredSize(new Dimension(480, 150)); 
        add(scrollPane, BorderLayout.SOUTH); 

        setupActionListeners();

        loadTableData();

        pack(); 
        setLocationRelativeTo(null);
    }

    private void setupPanelInput(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("ID Kategori"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtIdKategori = new JTextField(15);
        txtIdKategori.setText("");
        txtIdKategori.setEnabled(false); 
        panel.add(txtIdKategori, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Nama Kategori"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNama = new JTextField(20);
        txtNama.setText("");
        panel.add(txtNama, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Keterangan"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtKeterangan = new JTextField(20);
        txtKeterangan.setText("");
        panel.add(txtKeterangan, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST; 
        btnSimpan = new JButton("Simpan");
        panel.add(btnSimpan, gbc);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void setupPanelKontrol(JPanel panel) {
        btnTambahBaru = new JButton("Tambah Baru");
        panel.add(btnTambahBaru);

        btnHapus = new JButton("Hapus");
        panel.add(btnHapus);

        panel.add(Box.createRigidArea(new Dimension(50, 0))); 

        txtCari = new JTextField(10);
        txtCari.setText("");
        panel.add(txtCari);

        btnCari = new JButton("Cari");
        panel.add(btnCari);
    }

    private void setupTable() {
        String[] columnNames = {"ID Kategori", "Nama", "Keterangan"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblKategori = new JTable(tableModel);
    }

    private void setupActionListeners() {
        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simpanKategori();
            }
        });

        btnTambahBaru.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusKategori();
            }
        });

        btnCari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cariKategori();
            }
        });

        tblKategori.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editKategori();
                }
            }
        });
    }

    private void simpanKategori() {
        String nama = txtNama.getText().trim();
        String keterangan = txtKeterangan.getText().trim();

        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama kategori tidak boleh kosong!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            txtNama.requestFocus();
            return;
        }

        try {
            String idKategori = txtIdKategori.getText().trim();
            
            if (idKategori.isEmpty()) {
                kategoriObj.setIdKategori(0);
                kategoriObj.setNama(nama);
                kategoriObj.setKeterangan(keterangan);
                kategoriObj.save();
                
                JOptionPane.showMessageDialog(this, "Data kategori berhasil ditambahkan!", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int id = Integer.parseInt(idKategori);
                kategoriObj.setIdKategori(id);
                kategoriObj.setNama(nama);
                kategoriObj.setKeterangan(keterangan);
                kategoriObj.save();
                
                JOptionPane.showMessageDialog(this, "Data kategori berhasil diperbarui!", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
            
            clearForm();
            loadTableData();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID harus berupa angka!", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusKategori() {
        int selectedRow = tblKategori.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idKategori = (int) tblKategori.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin ingin menghapus kategori ini?", "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            kategoriObj.setIdKategori(idKategori);
            kategoriObj.delete();
            
            JOptionPane.showMessageDialog(this, "Data kategori berhasil dihapus!", 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadTableData();
        }
    }

    private void cariKategori() {
        String keyword = txtCari.getText().trim();
        if (keyword.isEmpty()) {
            loadTableData();
            return;
        }

        ArrayList<Kategori> hasilCari = kategoriObj.search(keyword);
        updateTable(hasilCari);
        
        if (hasilCari.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan!", 
                "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editKategori() {
        int selectedRow = tblKategori.getSelectedRow();
        if (selectedRow == -1) return;

        txtIdKategori.setText(tblKategori.getValueAt(selectedRow, 0).toString());
        txtNama.setText(tblKategori.getValueAt(selectedRow, 1).toString());
        txtKeterangan.setText(tblKategori.getValueAt(selectedRow, 2).toString());
    }

    private void clearForm() {
        txtIdKategori.setText("");
        txtNama.setText("");
        txtKeterangan.setText("");
        txtCari.setText("");
        txtNama.requestFocus();
    }

    private void loadTableData() {
        ArrayList<Kategori> semuaKategori = kategoriObj.getAll();
        updateTable(semuaKategori);
    }

    private void updateTable(ArrayList<Kategori> data) {
        tableModel.setRowCount(0);
        
        for (Kategori kat : data) {
            Object[] row = {
                kat.getIdKategori(),
                kat.getNama(),
                kat.getKeterangan()
            };
            tableModel.addRow(row);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FrmKategori().setVisible(true);
            }
        });
    }
}