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
![Screenshot 2025-06-01 175211](https://github.com/user-attachments/assets/2392eb50-4a99-4e57-9eb5-976d8edb2608) ![Screenshot 2025-06-01 175219](https://github.com/user-attachments/assets/d0ff7d1b-d49b-4126-ad96-067c439eb6b4) <br>
![Screenshot 2025-06-01 175246](https://github.com/user-attachments/assets/ca61927a-6dc1-4fff-84a0-cd5a5374a890) ![Screenshot 2025-06-01 175335](https://github.com/user-attachments/assets/bf16aaf8-fd93-4548-b477-0bd261046a65)

## 🚀 Pengembangan Masa Depan
- ☁️ Sinkronisasi cloud
- 📱 Aplikasi mobile
- 👥 Manajemen tugas kolaboratif
- 📊 Statistik dan pelaporan lanjutan
- 📆 Integrasi kalender

## 👨‍💻 Kontributor
👨 71231022 - Laurensius Rio Darryl <br>
👨 71231039 - Hansel Ivano Supratman <br>
👩 71231047 - Nengah Yesi Putri

## 🔑 Account Default
Username: admin <br>
Password: Admin123!
