# 📝 Aplikasi To-Do List

<div align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/JavaFX-8A2BE2?style=for-the-badge&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white"/>
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white"/>
</div>

## 🌟 Gambaran Umum
Aplikasi To-Do List ini adalah aplikasi berbasis JavaFX yang kaya fitur untuk membantu pengguna mengelola tugas dan aktivitas harian mereka. Aplikasi ini menyediakan antarmuka yang ramah pengguna untuk membuat, mengatur, dan melacak tugas dengan berbagai fitur seperti kategorisasi, pengaturan prioritas, dan manajemen tenggat waktu.

## ✨ Fitur Utama
- 🔐 **Autentikasi Pengguna**: Sistem login dan registrasi yang aman
- ✅ **Manajemen Tugas**: Buat, edit, hapus, dan tandai tugas sebagai selesai
- 📂 **Kategorisasi**: Atur tugas ke dalam kategori berbeda
- 🚩 **Level Prioritas**: Tetapkan level prioritas untuk tugas
- 📅 **Pelacakan Tenggat Waktu**: Atur dan lacak tenggat waktu untuk tugas
- 🔍 **Pencarian & Filter**: Temukan tugas dengan cepat menggunakan opsi pencarian dan filter
- 🔔 **Pengingat**: Dapatkan notifikasi untuk tugas yang akan datang
- 🎨 **Kustomisasi UI**: Sesuaikan ukuran font dan skala UI sesuai preferensi Anda
- 🔄 **Manajemen Sesi**: Login otomatis untuk pengguna yang kembali

## 🛠️ Teknologi yang Digunakan
| Teknologi | Kegunaan |
|-----------|----------|
| **Java 24** | Bahasa pemrograman utama |
| **JavaFX 17** | Framework UI |
| **SQLite** | Database untuk menyimpan tugas, kategori, dan informasi pengguna |
| **Maven** | Manajemen dependensi dan alat build |
| **FXML** | Layout UI berbasis XML |
| **CSS** | Styling untuk aplikasi |
| **Gson** | Pemrosesan JSON |
| **ControlsFX, FormsFX, BootstrapFX** | Komponen UI yang ditingkatkan |

## 📁 Struktur Proyek
```
ProjectUpdate/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── org/example/project/
│   │   │   │   ├── Controller/       # Pengontrol UI
│   │   │   │   ├── Model/            # Model data
│   │   │   │   ├── Util/             # Kelas utilitas
│   │   │   │   └── Main.java         # Titik masuk aplikasi
│   │   ├── resources/
│   │   │   ├── css/                  # Stylesheet
│   │   │   ├── fxml/                 # File layout UI
├── data.db                           # Database SQLite
├── session.ser                       # Data sesi yang diserialisasi
└── pom.xml                           # Konfigurasi Maven
```

## 📥 Instalasi
1. Pastikan Anda memiliki Java 24 atau yang lebih baru terinstal di sistem Anda
2. Clone repositori ini
3. Navigasi ke direktori proyek
4. Build proyek menggunakan Maven:
   ```
   mvn clean install
   ```
5. Jalankan aplikasi:
   ```
   mvn javafx:run
   ```

## 📚 Cara Penggunaan
1. 🔑 **Login/Register**: Mulai dengan membuat akun atau login
2. 📊 **Dashboard**: Lihat semua tugas Anda yang diatur berdasarkan kategori dan prioritas
3. ➕ **Tambah Tugas**: Klik tombol "+" untuk menambahkan tugas baru
4. 📝 **Edit Tugas**: Klik pada tugas untuk mengedit detailnya
5. ✓ **Selesaikan Tugas**: Centang kotak untuk menandai tugas sebagai selesai
6. 🗑️ **Hapus Tugas**: Gunakan tombol hapus untuk menghapus tugas
7. 📋 **Kelola Kategori**: Buat dan edit kategori dari dashboard
8. 🔍 **Filter Tugas**: Gunakan opsi filter untuk melihat tugas tertentu
9. 🔎 **Pencarian**: Gunakan bilah pencarian untuk menemukan tugas berdasarkan nama

## 📸 Tangkapan Layar
(Tangkapan layar akan ditampilkan di sini)

## 🚀 Pengembangan Masa Depan
- ☁️ Sinkronisasi cloud
- 📱 Aplikasi mobile
- 👥 Manajemen tugas kolaboratif
- 📊 Statistik dan pelaporan lanjutan
- 📆 Integrasi kalender

## 👨‍💻 Kontributor
71231022 - Laurensius Rio Darryl
71231039 - Hansel Ivano Supratman
71231047 - Nengah Yesi Putri

## 📄 Lisensi
Proyek ini dilisensikan di bawah Lisensi MIT - lihat file LICENSE untuk detailnya.
