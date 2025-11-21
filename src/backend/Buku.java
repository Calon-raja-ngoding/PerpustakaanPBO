package src.backend;

import java.util.ArrayList;
import java.sql.*;

public class Buku {
    private int idBuku;
    private int idKategori;
    private String judul, penerbit, penulis;

    // === GETTER SETTER ===
    public int getIdBuku() {
        return idBuku;
    }

    public void setIdBuku(int idBuku) {
        this.idBuku = idBuku;
    }

    public int getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(int idKategori) {
        this.idKategori = idKategori;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    // === KONSTRUKTOR ===
    public Buku() {}

    public Buku(int idKategori, String judul, String penerbit, String penulis) {
        this.idKategori = idKategori;
        this.judul = judul;
        this.penerbit = penerbit;
        this.penulis = penulis;
    }

    // === GET BY ID ===
    public Buku getById(int id) {
        Buku bk = new Buku();
        ResultSet rs = DBHelper.selectQuery(
                "SELECT * FROM buku WHERE idbuku = " + id);

        try {
            while (rs.next()) {
                bk = new Buku();
                bk.setIdBuku(rs.getInt("idbuku"));
                bk.setIdKategori(rs.getInt("idkategori"));
                bk.setJudul(rs.getString("judul"));
                bk.setPenerbit(rs.getString("penerbit"));
                bk.setPenulis(rs.getString("penulis"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bk;
    }

    // === GET ALL ===
    public ArrayList<Buku> getAll() {
        ArrayList<Buku> list = new ArrayList<>();

        ResultSet rs = DBHelper.selectQuery("SELECT * FROM buku");

        try {
            while (rs.next()) {
                Buku bk = new Buku();
                bk.setIdBuku(rs.getInt("idbuku"));
                bk.setIdKategori(rs.getInt("idkategori"));
                bk.setJudul(rs.getString("judul"));
                bk.setPenerbit(rs.getString("penerbit"));
                bk.setPenulis(rs.getString("penulis"));

                list.add(bk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // === SEARCH ===
    public ArrayList<Buku> search(String keyword) {
        ArrayList<Buku> list = new ArrayList<>();

        String sql = "SELECT * FROM buku WHERE "
                + "judul LIKE '%" + keyword + "%' "
                + "OR penerbit LIKE '%" + keyword + "%' "
                + "OR penulis LIKE '%" + keyword + "%'";

        ResultSet rs = DBHelper.selectQuery(sql);

        try {
            while (rs.next()) {
                Buku bk = new Buku();
                bk.setIdBuku(rs.getInt("idbuku"));
                bk.setIdKategori(rs.getInt("idkategori"));
                bk.setJudul(rs.getString("judul"));
                bk.setPenerbit(rs.getString("penerbit"));
                bk.setPenulis(rs.getString("penulis"));

                list.add(bk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // === SAVE ===
    public void save() {
        if (getById(idBuku).getIdBuku() == 0) {
            String sql = "INSERT INTO buku (idkategori, judul, penerbit, penulis) VALUES ("
                    + this.idKategori + ", "
                    + "'" + this.judul + "', "
                    + "'" + this.penerbit + "', "
                    + "'" + this.penulis + "')";
            this.idBuku = DBHelper.insertQueryGetId(sql);
        } else {
            String sql = "UPDATE buku SET "
                    + "idkategori = " + this.idKategori + ", "
                    + "judul = '" + this.judul + "', "
                    + "penerbit = '" + this.penerbit + "', "
                    + "penulis = '" + this.penulis + "' "
                    + "WHERE idbuku = " + this.idBuku;

            DBHelper.executeQuery(sql);
        }
    }

    // === DELETE ===
    public void delete() {
        String sql = "DELETE FROM buku WHERE idbuku = " + this.idBuku;
        DBHelper.executeQuery(sql);
    }
}
