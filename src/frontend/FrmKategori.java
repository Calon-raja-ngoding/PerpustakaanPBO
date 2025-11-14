package src.frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import src.backend.Kategori;

public class FrmKategori extends JFrame {

    private JTextField txtIdKategori = new JTextField();
    private JTextField txtNamaKategori = new JTextField();
    private JTextField txtKeterangan = new JTextField();

    private JButton btnSimpan = new JButton("Simpan");
    private JButton btnHapus = new JButton("Hapus");
    private JButton btnTambahBaru = new JButton("Tambah Baru");
    private JButton btnCari = new JButton("Cari");

    private JTextField txtCari = new JTextField();

    private JTable tblKategori;
    private DefaultTableModel tblModel;

    public FrmKategori() {
        setTitle("Form Kategori");
        setSize(650, 450);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ---------- LABEL & TEXTFIELD ----------
        addLabel("ID Kategori", 20, 20);
        addTextField(txtIdKategori, 120, 20);

        addLabel("Nama Kategori", 20, 60);
        addTextField(txtNamaKategori, 120, 60);

        addLabel("Keterangan", 20, 100);
        addTextField(txtKeterangan, 120, 100);

        // ---------- BUTTON ----------
        btnSimpan.setBounds(20, 140, 100, 25);
        add(btnSimpan);

        btnHapus.setBounds(130, 140, 100, 25);
        add(btnHapus);

        btnTambahBaru.setBounds(240, 140, 120, 25);
        add(btnTambahBaru);

        txtCari.setBounds(370, 140, 150, 25);
        add(txtCari);

        btnCari.setBounds(530, 140, 80, 25);
        add(btnCari);

        // ---------- TABLE ----------
        String[] columnNames = { "ID", "Nama Kategori", "Keterangan" };
        tblModel = new DefaultTableModel(columnNames, 0);
        tblKategori = new JTable(tblModel);
        
        JScrollPane scrollPane = new JScrollPane(tblKategori);
        scrollPane.setBounds(20, 180, 590, 200);
        add(scrollPane);

        // LOAD DATA
        tampilkanData();

        // EVENTS
        btnSimpan.addActionListener(e -> simpanKategori());
        btnHapus.addActionListener(e -> hapusKategori());
        btnTambahBaru.addActionListener(e -> kosongkanForm());
        btnCari.addActionListener(e -> cariKategori());

        // EVENT KLIK TABEL
        tblKategori.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblKategori.getSelectedRow();
                txtIdKategori.setText(tblModel.getValueAt(row, 0).toString());
                txtNamaKategori.setText(tblModel.getValueAt(row, 1).toString());
                txtKeterangan.setText(tblModel.getValueAt(row, 2).toString());
            }
        });
    }

    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 100, 25);
        add(label);
    }

    private void addTextField(JTextField txt, int x, int y) {
        txt.setBounds(x, y, 200, 25);
        add(txt);
    }

    // ----------------- METHOD CRUD -----------------

    private void tampilkanData() {
        tblModel.setRowCount(0);
        for (Kategori k : new Kategori().getAll()) {
            tblModel.addRow(new Object[]{
                k.getIdKategori(),
                k.getNama(),
                k.getKeterangan()
            });
        }
    }

    private void simpanKategori() {
        Kategori k = new Kategori();
        k.setIdKategori(Integer.parseInt(txtIdKategori.getText()));
        k.setNama(txtNamaKategori.getText());
        k.setKeterangan(txtKeterangan.getText());
        k.save();
        tampilkanData();
        kosongkanForm();
    }

    private void hapusKategori() {
        Kategori k = new Kategori();
        k.setIdKategori(Integer.parseInt(txtIdKategori.getText()));
        k.delete();
        tampilkanData();
        kosongkanForm();
    }

    private void kosongkanForm() {
        txtIdKategori.setText("0");
        txtNamaKategori.setText("");
        txtKeterangan.setText("");
    }

    private void cariKategori() {
        String keyword = txtCari.getText();
        tblModel.setRowCount(0);

        for (Kategori k : new Kategori().search(keyword)) {
            tblModel.addRow(new Object[]{
                k.getIdKategori(),
                k.getNama(),
                k.getKeterangan()
            });
        }
    }

    // ------------ MAIN ------------
    public static void main(String[] args) {
        new FrmKategori().setVisible(true);
    }
}
