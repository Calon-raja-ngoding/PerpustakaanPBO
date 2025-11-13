package src.frontend; // Sesuai dengan gambar 232382.png

import src.backend.Kategori; // Import Kategori dari package backend
import java.util.ArrayList; // Tidak ada di gambar, tapi dibutuhkan

public class TestBackend {
    
    public static void main(String[] args) {
        
        // Buat objek Kategori
        Kategori kat1 = new Kategori("Novel", "Koleksi buku novel");
        Kategori kat2 = new Kategori("Referensi", "Buku referensi ilmiah");
        Kategori kat3 = new Kategori("Komik", "Komik anak-anak");
        
        // test insert
        kat1.save();
        kat2.save();
        kat3.save();
        
        // test update
        kat2.setKeterangan("Koleksi buku referensi ilmiah");
        kat2.save();
        
        // test delete (hapus kat3)
        kat3.delete();
        
        // test select all
        System.out.println("\n--- TEST SELECT ALL ---");
        // Gunakan objek Kategori kosong untuk memanggil method static-like getAll()
        for (Kategori k : new Kategori().getAll()) { 
            System.out.println("Nama: " + k.getNama() + ", Ket: " + k.getKeterangan());
        }
        
        // test search
        System.out.println("\n--- TEST SEARCH (keyword: ilmiah) ---");
        // Gunakan objek Kategori kosong untuk memanggil method static-like search()
        for (Kategori k : new Kategori().search("ilmiah")) { 
            System.out.println("Nama: " + k.getNama() + ", Ket: " + k.getKeterangan());
        }
    }
}