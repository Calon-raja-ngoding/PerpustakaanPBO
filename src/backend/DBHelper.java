package src.backend;

import java.sql.*;

public class DBHelper {
    
    // Objek koneksi statis agar bisa diakses di seluruh kelas
    private static Connection koneksi;

    /**
     * Membuka koneksi ke database PostgreSQL jika belum terbuka.
     */
    public static void bukaKoneksi() {
    if (koneksi == null) {
        try {
            // register driver manual biar gak "no suitable driver"
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/dbperpus";
            String user = "root";
            String password = "";

            koneksi = DriverManager.getConnection(url, user, password);
            System.out.println("Koneksi berhasil!");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL tidak ditemukan!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error koneksi!");
            e.printStackTrace();
        }
    }
}

    /**
     * Menutup koneksi database jika sedang terbuka.
     */
    public static void tutupKoneksi() {
        if (koneksi != null) {
            try {
                koneksi.close();
                koneksi = null; // Set kembali ke null
                System.out.println("Koneksi PostgreSQL berhasil ditutup.");
            } catch (SQLException e) {
                System.out.println("Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }
    
    public static int insertQueryGetId(String query) {
        bukaKoneksi();
            int num = 0;
            int result = -1;
                try {
                    Statement stmt = koneksi.createStatement();
                    num = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next())
                    {
                    result = rs.getInt(1);
                    }
                    rs.close();
                    stmt.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    result = -1;
                }
            return result;
        }

    //------------------------------------------------------------------
    
    /**
     * Menjalankan query DML (INSERT, UPDATE, DELETE).
     * @param query SQL DML statement
     * @return true jika operasi berhasil, false jika terjadi error.
     */
    public static boolean executeQuery(String query) {
        bukaKoneksi();
        boolean result = false;
        Statement stmt = null; // Deklarasi Statement
        
        try {
            stmt = koneksi.createStatement();
            stmt.executeUpdate(query);
            result = true;
        } catch (SQLException e) {
            System.out.println("Error saat menjalankan query: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Pastikan Statement ditutup untuk mencegah resource leak
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Menjalankan query DQL (SELECT).
     * @param query SQL SELECT statement
     * @return ResultSet yang berisi hasil query, atau null jika terjadi error.
     */
    public static ResultSet selectQuery(String query) {
        bukaKoneksi();
        ResultSet rs = null;
        Statement stmt = null; // Deklarasi Statement
        
        try {
            stmt = koneksi.createStatement();
            rs = stmt.executeQuery(query); // ResultSet dikembalikan, Statement tidak ditutup di sini
        } catch (SQLException e) {
            System.out.println("Error saat menjalankan select query: " + e.getMessage());
            e.printStackTrace();
            // Penting: Jika error, ResultSet dan Statement harus null
            rs = null; 
        }
        
        // Catatan: Statement TIDAK ditutup di sini, harus ditutup di kelas pemanggil 
        // setelah ResultSet selesai diproses. Ini adalah trade-off dalam pattern DBHelper sederhana.
        return rs; 
    }

    //------------------------------------------------------------------
}