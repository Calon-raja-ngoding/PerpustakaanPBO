package src.backend;
import java.util.ArrayList;
import java.sql.*;

public class Anggota {
    private int idAnggota;
    private String nama, alamat, telepon;

    public int getIdAnggota () {
        return idAnggota;
    }

    public void setIdAnggota (int newIdAnggota) {
        this.idAnggota = newIdAnggota;
    }

    public String getNama () {
        return nama;
    }

    public void setNama (String newNama) {
        this.nama = newNama;
    }

    public String getAlamat () {
        return alamat;
    }

    public void setAlamat (String newAlamat) {
        this.alamat = newAlamat;
    }

    public String getTelepon () {
        return telepon;
    }

    public void setTelepon (String newTelepon) {
        this.telepon = newTelepon;
    }

    public Anggota () {}
    public Anggota (String nama, String alamat, String telepon) {
        this.nama = nama;
        this.alamat = alamat;
        this.telepon = telepon;
    }

    public Anggota getById(int id) {
        Anggota agt = new Anggota();
        ResultSet rs = DBHelper.selectQuery("SELECT * FROM anggota "
                + " WHERE idanggota = '" + id + "'");

        try
        {
            while(rs.next())
            {
                agt = new Anggota();
                agt.setIdAnggota(rs.getInt("idanggota"));
                agt.setNama(rs.getString("nama"));
                agt.setAlamat(rs.getString("alamat"));
                agt.setTelepon(rs.getString("telepon"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return agt;
    }

    public ArrayList<Anggota> getAll(){
        ArrayList<Anggota> listAnggota = new ArrayList<>();
        
        ResultSet rs = DBHelper.selectQuery("SELECT * FROM anggota");

        try
        {
            while (rs.next())
            {
                Anggota agt = new Anggota();
                agt.setIdAnggota(rs.getInt("idanggota"));
                agt.setNama(rs.getString("nama"));
                agt.setAlamat(rs.getString("alamat"));
                agt.setTelepon(rs.getString("telepon"));
                
                listAnggota.add(agt);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return listAnggota;
    }

    public ArrayList<Anggota> search(String keyword) {
        ArrayList<Anggota> listAnggota = new ArrayList<>();

        String sql = "SELECT * FROM anggota WHERE "
                + " nama LIKE '%" + keyword + "%'"
                + " OR alamat LIKE '%" + keyword + "%'"
                + " OR telepon LIKE '%" + keyword + "%'"; 

        ResultSet rs = DBHelper.selectQuery(sql);

        try
        {
            while(rs.next())
            {
                Anggota agt = new Anggota();
                agt.setIdAnggota(rs.getInt("idanggota")); 
                agt.setNama(rs.getString("nama"));
                agt.setAlamat(rs.getString("alamat"));
                agt.setTelepon(rs.getString("telepon"));

                listAnggota.add(agt);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return listAnggota;
    }

    public void save() {
        if (getById(idAnggota).getIdAnggota() == 0) {
            String sql = "INSERT INTO anggota (nama, alamat, telepon) VALUES ("
                    + "'" + this.nama + "', "
                    + "'" + this.alamat + "', "
                    + "'" + this.telepon + "'"
                    + ")";
            this.idAnggota = DBHelper.insertQueryGetId(sql);
        } else {
            String sql = "UPDATE anggota SET "
                    + "alamat = '" + this.alamat + "', "
                    + "telepon = '" + this.telepon + "', "
                    + "nama = '" + this.nama + "' "
                    + "WHERE idanggota = " + this.idAnggota;

            DBHelper.executeQuery(sql);
        }
    }

    public void delete() {
        String sql = "DELETE FROM anggota WHERE idanggota = " + this.idAnggota;
        DBHelper.executeQuery(sql);
    }
}