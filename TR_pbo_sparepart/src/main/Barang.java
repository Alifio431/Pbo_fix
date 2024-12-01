package main;

public class Barang {
    private int idBarang;
    private String namaBarang;
    private String merk;
    private String kategori;
    private double harga;
    private int stok;
    private String deskripsiBarang;

    public Barang(int idBarang, String namaBarang, String merk, String kategori, double harga, int stok, String deskripsiBarang) {
        this.idBarang = idBarang;
        this.namaBarang = namaBarang;
        this.merk = merk;
        this.kategori = kategori;
        this.harga = harga;
        this.stok = stok;
        this.deskripsiBarang = deskripsiBarang;
    }

    // Getter dan Setter
    public int getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(int idBarang) {
        this.idBarang = idBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getDeskripsiBarang() {
        return deskripsiBarang;
    }

    public void setDeskripsiBarang(String deskripsiBarang) {
        this.deskripsiBarang = deskripsiBarang;
    }
}
