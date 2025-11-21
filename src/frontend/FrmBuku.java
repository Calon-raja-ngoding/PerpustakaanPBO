package src.frontend;

// Preview image (uploaded by user): /mnt/data/4d96f291-827c-4d02-b6cb-4be8334dbf70.png

import src.backend.Buku;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.sql.ResultSet;

public class FrmBuku extends JFrame {
    private JTextField txtIdBuku;
    private JComboBox<ComboItem> cmbKategori;
    private JTextField txtJudul;
    private JTextField txtPenerbit;
    private JTextField txtPenulis;
    private JButton btnSimpan, btnHapus, btnTambahBaru, btnCari;
    private JTextField txtCari;
    private JTable tblBuku;
    private DefaultTableModel model;

    public FrmBuku() {
        initComponent();
        loadKategori();
        loadTable();
    }

    private void initComponent() {
        setTitle("Form Buku");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelForm = new JPanel();
        panelForm.setLayout(null);
        panelForm.setPreferredSize(new Dimension(700, 220));

        JLabel lblId = new JLabel("ID Buku");
        lblId.setBounds(10, 10, 80, 25);
        panelForm.add(lblId);

        txtIdBuku = new JTextField();
        txtIdBuku.setBounds(100, 10, 60, 25);
        txtIdBuku.setEnabled(false);
        panelForm.add(txtIdBuku);

        JLabel lblKategori = new JLabel("Kategori");
        lblKategori.setBounds(10, 45, 80, 25);
        panelForm.add(lblKategori);

        cmbKategori = new JComboBox<>();
        cmbKategori.setBounds(100, 45, 200, 25);
        panelForm.add(cmbKategori);

        JLabel lblJudul = new JLabel("Judul");
        lblJudul.setBounds(10, 80, 80, 25);
        panelForm.add(lblJudul);

        txtJudul = new JTextField();
        txtJudul.setBounds(100, 80, 520, 25);
        panelForm.add(txtJudul);

        JLabel lblPenerbit = new JLabel("Penerbit");
        lblPenerbit.setBounds(10, 115, 80, 25);
        panelForm.add(lblPenerbit);

        txtPenerbit = new JTextField();
        txtPenerbit.setBounds(100, 115, 200, 25);
        panelForm.add(txtPenerbit);

        JLabel lblPenulis = new JLabel("Penulis");
        lblPenulis.setBounds(330, 115, 80, 25);
        panelForm.add(lblPenulis);

        txtPenulis = new JTextField();
        txtPenulis.setBounds(400, 115, 220, 25);
        panelForm.add(txtPenulis);

        btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(10, 155, 90, 30);
        panelForm.add(btnSimpan);

        btnTambahBaru = new JButton("Tambah Baru");
        btnTambahBaru.setBounds(110, 155, 120, 30);
        panelForm.add(btnTambahBaru);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(240, 155, 90, 30);
        panelForm.add(btnHapus);

        txtCari = new JTextField();
        txtCari.setBounds(350, 155, 200, 30);
        panelForm.add(txtCari);

        btnCari = new JButton("Cari");
        btnCari.setBounds(560, 155, 90, 30);
        panelForm.add(btnCari);

        add(panelForm, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new Object[]{"ID Buku", "Kategori", "Judul", "Penerbit", "Penulis"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblBuku = new JTable(model);
        JScrollPane scroll = new JScrollPane(tblBuku);
        scroll.setPreferredSize(new Dimension(700, 260));
        add(scroll, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Events
        btnTambahBaru.addActionListener(e -> clearForm());

        btnSimpan.addActionListener(e -> {
            simpanData();
            loadTable();
            clearForm();
        });

        btnHapus.addActionListener(e -> {
            hapusData();
            loadTable();
            clearForm();
        });

        btnCari.addActionListener(e -> {
            cariData(txtCari.getText().trim());
        });

        tblBuku.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tblBuku.getSelectedRow();
                if (row != -1) {
                    txtIdBuku.setText(model.getValueAt(row, 0).toString());
                    // select kategori in combobox by name
                    String kategoriName = model.getValueAt(row, 1).toString();
                    selectKategoriByName(kategoriName);
                    txtJudul.setText(model.getValueAt(row, 2).toString());
                    txtPenerbit.setText(model.getValueAt(row, 3).toString());
                    txtPenulis.setText(model.getValueAt(row, 4).toString());
                }
            }
        });
    }

    private void clearForm() {
        txtIdBuku.setText("");
        if (cmbKategori.getItemCount() > 0) cmbKategori.setSelectedIndex(0);
        txtJudul.setText("");
        txtPenerbit.setText("");
        txtPenulis.setText("");
        txtCari.setText("");
    }

    private void loadKategori() {
        cmbKategori.removeAllItems();
        try {
            ResultSet rs = src.backend.DBHelper.selectQuery("SELECT * FROM kategori");
            while (rs.next()) {
                int id = rs.getInt("idkategori");
                String nama = rs.getString("nama");
                cmbKategori.addItem(new ComboItem(id, nama));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat kategori: " + e.getMessage());
        }
    }

    private void selectKategoriByName(String name) {
        for (int i = 0; i < cmbKategori.getItemCount(); i++) {
            ComboItem it = cmbKategori.getItemAt(i);
            if (it.getName().equals(name)) {
                cmbKategori.setSelectedIndex(i);
                return;
            }
        }
    }

    private void loadTable() {
        model.setRowCount(0);
        try {
            ArrayList<Buku> list = new Buku().getAll();
            for (Buku b : list) {
                // get kategori name
                String kategoriName = "";
                try {
                    ResultSet rs = src.backend.DBHelper.selectQuery("SELECT nama FROM kategori WHERE idkategori = " + b.getIdKategori());
                    if (rs.next()) kategoriName = rs.getString("nama");
                } catch (Exception ex) { /* ignore */ }

                model.addRow(new Object[]{b.getIdBuku(), kategoriName, b.getJudul(), b.getPenerbit(), b.getPenulis()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cariData(String keyword) {
        model.setRowCount(0);
        ArrayList<Buku> list = new Buku().search(keyword);
        for (Buku b : list) {
            String kategoriName = "";
            try {
                ResultSet rs = src.backend.DBHelper.selectQuery("SELECT nama FROM kategori WHERE idkategori = " + b.getIdKategori());
                if (rs.next()) kategoriName = rs.getString("nama");
            } catch (Exception ex) { }
            model.addRow(new Object[]{b.getIdBuku(), kategoriName, b.getJudul(), b.getPenerbit(), b.getPenulis()});
        }
    }

    private void simpanData() {
        try {
            Buku b = new Buku();
            if (!txtIdBuku.getText().isEmpty()) b.setIdBuku(Integer.parseInt(txtIdBuku.getText()));

            ComboItem ci = (ComboItem) cmbKategori.getSelectedItem();
            if (ci != null) b.setIdKategori(ci.getId());
            b.setJudul(txtJudul.getText().trim());
            b.setPenerbit(txtPenerbit.getText().trim());
            b.setPenulis(txtPenulis.getText().trim());

            b.save();
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + e.getMessage());
        }
    }

    private void hapusData() {
        try {
            if (txtIdBuku.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus.");
                return;
            }
            int id = Integer.parseInt(txtIdBuku.getText());
            Buku b = new Buku().getById(id);
            if (b.getIdBuku() == 0) {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                b.delete();
                JOptionPane.showMessageDialog(this, "Data dihapus.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menghapus: " + e.getMessage());
        }
    }

    // Simple helper class to keep id/name in combobox
    private class ComboItem {
        private int id;
        private String name;

        public ComboItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() { return id; }
        public String getName() { return name; }

        @Override
        public String toString() { return name; }
    }

    // main untuk testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmBuku());
    }
}
