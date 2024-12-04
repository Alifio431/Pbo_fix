-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 04, 2024 at 07:05 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tr_pbo`
--

-- --------------------------------------------------------

--
-- Table structure for table `akun`
--

CREATE TABLE `akun` (
  `id_akun` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `permission` enum('admin','user') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `akun`
--

INSERT INTO `akun` (`id_akun`, `username`, `password`, `permission`) VALUES
(1, 'alifio', '6969', 'admin'),
(2, 'farahan', '123', 'user'),
(3, 'farhan', '1212', 'user'),
(4, 'master', '1212', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `data_barang`
--

CREATE TABLE `data_barang` (
  `id_barang` int(11) NOT NULL,
  `nama_barang` varchar(100) NOT NULL,
  `merk` varchar(50) NOT NULL,
  `kategori` varchar(50) NOT NULL,
  `harga` decimal(10,2) NOT NULL,
  `stok` int(11) NOT NULL,
  `deskripsi_barang` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `data_barang`
--

INSERT INTO `data_barang` (`id_barang`, `nama_barang`, `merk`, `kategori`, `harga`, `stok`, `deskripsi_barang`) VALUES
(2, 'Piston', 'Alphine', 'Mesin dan Komponen Mesin', 500000.00, 86, 'Piston untuk mesin kendaraan, berkualitas tinggi.'),
(3, 'Camshaft', 'Dunloop', 'Mesin dan Komponen Mesin', 1200000.00, 47, 'Camshaft untuk performa mesin yang optimal.'),
(4, 'Crankshaft', 'Bosch', 'Mesin dan Komponen Mesin', 2000000.00, 30, 'Crankshaft untuk meningkatkan tenaga mesin.'),
(5, 'Blok Mesin', 'Mitsubishi', 'Mesin dan Komponen Mesin', 3500000.00, 20, 'Blok mesin dengan material tahan lama.'),
(6, 'Kopling', 'ZF', 'Sistem Transmisi', 450000.00, 200, 'Kopling untuk sistem transmisi manual kendaraan.'),
(7, 'Gearbox', 'Sachs', 'Sistem Transmisi', 2500000.00, 40, 'Gearbox dengan kualitas terbaik untuk kendaraan.'),
(8, 'Poros Transmisi', 'Bosch', 'Sistem Transmisi', 1200000.00, 80, 'Poros transmisi untuk kendaraan komersial.'),
(9, 'Drive Shaft', 'SKF', 'Sistem Transmisi', 1000000.00, 100, 'Drive shaft untuk transmisi kendaraan yang andal.'),
(10, 'Shockbreaker', 'KYB', 'Sistem Suspensi dan Kemudi', 750000.00, 150, 'Shockbreaker untuk kenyamanan berkendara.'),
(11, 'Ball Joint', 'TRW', 'Sistem Suspensi dan Kemudi', 350000.00, 200, 'Ball joint untuk perbaikan sistem kemudi.'),
(12, 'Tie Rod', 'MOOG', 'Sistem Suspensi dan Kemudi', 500000.00, 180, 'Tie rod untuk kontrol kemudi yang stabil.'),
(13, 'Bearing Roda', 'NSK', 'Sistem Suspensi dan Kemudi', 250000.00, 300, 'Bearing roda untuk pengendalian yang lebih halus.'),
(14, 'Kampas Rem', 'Brembo', 'Sistem Rem', 350000.00, 250, 'Kampas rem berkualitas untuk performa maksimal.'),
(15, 'Cakram Rem', 'Ferodo', 'Sistem Rem', 1200000.00, 150, 'Cakram rem yang tahan lama dan aman digunakan.'),
(16, 'Master Rem', 'ATE', 'Sistem Rem', 900000.00, 200, 'Master rem untuk kinerja sistem rem yang efisien.'),
(17, 'Kaliper', 'Wilwood', 'Sistem Rem', 1800000.00, 100, 'Kaliper rem untuk pengereman yang lebih responsif.'),
(18, 'Aki', 'Exide', 'Sistem Elektrikal', 800000.00, 120, 'Aki kendaraan dengan daya tahan tinggi.'),
(19, 'Alternator', 'Bosch', 'Sistem Elektrikal', 1500000.00, 80, 'Alternator untuk pengisian daya kendaraan.'),
(20, 'Kabel Busi', 'NGK', 'Sistem Elektrikal', 100000.00, 300, 'Kabel busi untuk sistem pengapian yang efektif.'),
(21, 'Relay', 'Omron', 'Sistem Elektrikal', 200000.00, 400, 'Relay untuk kontrol sistem elektrikal kendaraan.'),
(22, 'Radiator', 'Denso', 'Sistem Pendingin dan Pelumas', 2000000.00, 50, 'Radiator untuk sistem pendingin mesin kendaraan.'),
(23, 'Kipas Pendingin', 'Maradyne', 'Sistem Pendingin dan Pelumas', 700000.00, 100, 'Kipas pendingin untuk menjaga suhu mesin.'),
(24, 'Thermostat', 'Stant', 'Sistem Pendingin dan Pelumas', 300000.00, 150, 'Thermostat untuk pengaturan suhu mesin yang optimal.'),
(25, 'Oli Mesin', 'Castrol', 'Sistem Pendingin dan Pelumas', 400000.00, 200, 'Oli mesin untuk pelumasan dan perlindungan mesin.'),
(26, 'Spion', 'Alphine', 'Body dan Interior', 350000.00, 180, 'Spion untuk kendaraan dengan desain aerodinamis.'),
(27, 'Lampu Utama', 'Philips', 'Body dan Interior', 500000.00, 250, 'Lampu utama berkualitas untuk visibilitas optimal.'),
(28, 'Dashboard', 'Sony', 'Body dan Interior', 2500000.00, 70, 'Dashboard mobil dengan fitur canggih dan tampilan modern.'),
(29, 'Jok Kendaraan', 'Recaro', 'Body dan Interior', 1500000.00, 100, 'Jok kendaraan untuk kenyamanan berkendara.'),
(30, 'Ban Tubeless', 'Michelin', 'Ban dan Velg', 600000.00, 150, 'Ban tubeless dengan ketahanan tinggi untuk segala medan.'),
(31, 'Velg Racing', 'OZ Racing', 'Ban dan Velg', 2000000.00, 120, 'Velg racing untuk tampilan dan performa kendaraan.'),
(32, 'Pentil Ban', 'Schrader', 'Ban dan Velg', 50000.00, 500, 'Pentil ban yang kuat dan tahan lama.'),
(33, 'Ban Cadangan', 'Bridgestone', 'Ban dan Velg', 800000.00, 200, 'Ban cadangan untuk kendaraan off-road dan perkotaan.'),
(34, 'Pompa Bensin', 'Bosch', 'Sistem Bahan Bakar', 1200000.00, 100, 'Pompa bensin untuk kendaraan berbahan bakar bensin.'),
(35, 'Filter Bahan Bakar', 'MANN', 'Sistem Bahan Bakar', 350000.00, 250, 'Filter bahan bakar untuk menjaga sistem tetap bersih.'),
(36, 'Injektor', 'Delphi', 'Sistem Bahan Bakar', 900000.00, 150, 'Injektor untuk pengiriman bahan bakar yang efisien.'),
(37, 'Karburator', 'Zenith', 'Sistem Bahan Bakar', 1500000.00, 50, 'Karburator untuk kendaraan berbahan bakar campuran.'),
(38, 'Knalpot Racing', 'HKS', 'Aksesori dan Performa', 2000000.00, 100, 'Knalpot racing untuk performa mesin yang lebih baik.'),
(39, 'Stiker Kendaraan', '3M', 'Aksesori dan Performa', 50000.00, 500, 'Stiker kendaraan untuk modifikasi dan estetika.'),
(40, 'Handle Rem', 'Brembo', 'Aksesori dan Performa', 600000.00, 150, 'Handle rem berkualitas untuk kendaraan performa tinggi.'),
(41, 'Tutup Radiator', 'Mishimoto', 'Aksesori dan Performa', 250000.00, 300, 'Tutup radiator untuk meningkatkan efisiensi sistem pendingin.'),
(43, 'asd', 'asd', 'asd', 123.00, 113, 'asd'),
(44, 'asd', 'asd', 'asd', 123.00, 113, 'asd'),
(45, 'qwe', 'qwe', 'qwe', 123.00, 113, 'qwe'),
(46, 'Ban dalam ', 'Esparco', 'Ban dan Roda', 150000.00, 50, 'Ban dalam anti paku anti bocor garansi seumur hidup selama tidak kecelakaan');

-- --------------------------------------------------------

--
-- Table structure for table `history_transaksi`
--

CREATE TABLE `history_transaksi` (
  `id_penjualan` int(11) NOT NULL,
  `id_akun` int(11) NOT NULL,
  `id_barang` int(11) NOT NULL,
  `nama_barang` varchar(100) NOT NULL,
  `merk` varchar(50) NOT NULL,
  `harga` decimal(10,2) NOT NULL,
  `quantity` int(11) NOT NULL,
  `total_harga` decimal(15,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `laporan_barang`
--

CREATE TABLE `laporan_barang` (
  `id_laporan` int(11) NOT NULL,
  `id_barang` int(11) NOT NULL,
  `stok_barang` int(11) NOT NULL,
  `barang_masuk` int(11) NOT NULL,
  `barang_keluar` int(11) NOT NULL,
  `jumlah_transaksi` decimal(15,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `akun`
--
ALTER TABLE `akun`
  ADD PRIMARY KEY (`id_akun`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `data_barang`
--
ALTER TABLE `data_barang`
  ADD PRIMARY KEY (`id_barang`);

--
-- Indexes for table `history_transaksi`
--
ALTER TABLE `history_transaksi`
  ADD PRIMARY KEY (`id_penjualan`),
  ADD KEY `id_barang` (`id_barang`),
  ADD KEY `id_akun` (`id_akun`);

--
-- Indexes for table `laporan_barang`
--
ALTER TABLE `laporan_barang`
  ADD PRIMARY KEY (`id_laporan`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `akun`
--
ALTER TABLE `akun`
  MODIFY `id_akun` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `data_barang`
--
ALTER TABLE `data_barang`
  MODIFY `id_barang` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `history_transaksi`
--
ALTER TABLE `history_transaksi`
  MODIFY `id_penjualan` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `laporan_barang`
--
ALTER TABLE `laporan_barang`
  MODIFY `id_laporan` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `history_transaksi`
--
ALTER TABLE `history_transaksi`
  ADD CONSTRAINT `history_transaksi_ibfk_1` FOREIGN KEY (`id_akun`) REFERENCES `akun` (`id_akun`),
  ADD CONSTRAINT `history_transaksi_ibfk_2` FOREIGN KEY (`id_barang`) REFERENCES `data_barang` (`id_barang`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
