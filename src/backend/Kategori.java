package src.backend;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Kategori {
    private int idKategori;
    private String nama, keterangan;

    // --- Getters ---
    public int getIdKategori () {
        return idKategori;
    }

    public void setIdKategori (int newIdKategori) {
        this.idKategori = newIdKategori;
    }

    public String getNama () {
        return nama;
    }

    public void setNama (String newNama) {
        this.nama = newNama;
    }

    public String getKeterangan () {
        return keterangan;
    }

    public void setKeterangan (String setKeterangan) {
        this.keterangan = setKeterangan;
    }
    
    // --- Constructors ---
    public Kategori () {}

    public Kategori (String nama, String keterangan) {
        this.nama = nama;
        this.keterangan = keterangan;
    }

    // =======================================================
    // --- DATABASE LOGIC (DAO/CONTROLLER METHODS) ---
    // =======================================================
    
    // Method getById(int id) - Sudah ada dari input Anda
    public Kategori getById(int id) {
        Kategori kat = new Kategori(); 
        ResultSet rs = null;
        
        String query = "SELECT idkategori, nama, keterangan FROM kategori "
                     + "WHERE idkategori = " + id;
        
        rs = DBHelper.selectQuery(query);

        try {
            if (rs.next()) {
                kat.setIdKategori(rs.getInt("idkategori"));
                kat.setNama(rs.getString("nama"));
                kat.setKeterangan(rs.getString("keterangan"));
            }
            
            if (rs != null) rs.close(); 
            
        } catch (SQLException e) {
            System.out.println("Error saat mengambil data Kategori berdasarkan ID: " + e.getMessage());
            e.printStackTrace();
            kat = new Kategori(); 
        }

        return kat;
    }

    // Method save() - Menggabungkan INSERT dan UPDATE
    public void save() {
        // Cek ID: jika ID 0, maka INSERT. Jika > 0, maka UPDATE.
        // Di sini kita cek apakah data dengan ID ini ada, namun karena ini
        // ada di kelas Kategori, kita asumsikan jika idKategori == 0 itu adalah INSERT baru.
        
        if (this.idKategori == 0) { // Logika INSERT
            String sql = "INSERT INTO kategori (nama, keterangan) VALUES ("
                    + "'" + this.nama + "', "
                    + "'" + this.keterangan + "')";
            
            // Panggil DBHelper.insertQueryGetId() untuk INSERT dan ambil ID yang baru dibuat
            this.idKategori = DBHelper.insertQueryGetId(sql);
            System.out.println("✅ Data INSERT berhasil, ID: " + this.idKategori);

        } else { // Logika UPDATE
            String sql = "UPDATE kategori SET "
                    + "nama = '" + this.nama + "', "
                    + "keterangan = '" + this.keterangan + "' "
                    + "WHERE idkategori = " + this.idKategori;
            
            DBHelper.executeQuery(sql);
            System.out.println("✅ Data UPDATE ID " + this.idKategori + " berhasil.");
        }
    }
    
    // Method delete()
    public void delete() {
        String sql = "DELETE FROM kategori WHERE idkategori = " + this.idKategori;
        DBHelper.executeQuery(sql);
        System.out.println("✅ Data DELETE ID " + this.idKategori + " berhasil.");
    }

    // Method getAll()
    public ArrayList<Kategori> getAll() {
        ArrayList<Kategori> ListKategori = new ArrayList<>();
        ResultSet rs = DBHelper.selectQuery("SELECT idkategori, nama, keterangan FROM kategori");

        try {
            while (rs.next()) {
                Kategori kat = new Kategori();
                kat.setIdKategori(rs.getInt("idkategori"));
                kat.setNama(rs.getString("nama"));
                kat.setKeterangan(rs.getString("keterangan"));
                
                ListKategori.add(kat);
            }
            if (rs != null) rs.close(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ListKategori;
    }
    
    // Method search(String keyword)
    public ArrayList<Kategori> search(String keyword) {
        ArrayList<Kategori> ListKategori = new ArrayList<>();
        
        // Menggunakan ILIKE untuk case-insensitive search di PostgreSQL
        String sql = "SELECT idkategori, nama, keterangan FROM kategori WHERE "
                + "nama ILIKE '%" + keyword + "%' " 
                + "OR keterangan ILIKE '%" + keyword + "%'";
        
        ResultSet rs = DBHelper.selectQuery(sql);

        try {
            while (rs.next()) {
                Kategori kat = new Kategori();
                kat.setIdKategori(rs.getInt("idkategori"));
                kat.setNama(rs.getString("nama"));
                kat.setKeterangan(rs.getString("keterangan"));
                
                ListKategori.add(kat);
            }
            if (rs != null) rs.close(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ListKategori;
    }
}