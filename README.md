# 📄 CV Rehberi – Özgeçmiş Analiz

**CV Rehberi**, PDF özgeçmişinizi yükleyip hedef role göre ATS uyumluluğu, güçlü/zayıf yönler ve iyileştirme önerileri alabileceğiniz, **Kotlin Multiplatform** ile yazılmış bir masaüstü ve mobil uygulamadır. Analiz, **Groq** üzerinden **Llama 3.3 70B** modeli ile yapılır.

---

## ✨ Özellikler

- **PDF yükleme** – Özgeçmişinizi PDF olarak yükleyin; metin otomatik çıkarılır.
- **Hedef rol** – Analizi “Yazılım Geliştirici”, “Ürün Müdürü” gibi bir hedef role göre yapın.
- **ATS skoru** – Parsing, anahtar kelime eşleşmesi, yapı, okunabilirlik ve etki skorları (0–100).
- **Profil özeti** – İsim, tespit edilen rol, deneyim yılı, beceriler ve eğitim.
- **Güçlü / zayıf yönler** – Maddeler halinde özet ve açıklamalar.
- **İyileştirme önerileri** – CV’yi güçlendirmek için somut öneriler.
- **Eksik anahtar kelimeler** – Hedef role göre eklenmesi önerilen kelimeler.
- **Geçmiş** – Daha önce yaptığınız analizleri listeleyip tekrar inceleyebilirsiniz.

---

## 🛠 Teknoloji

| Alan | Kullanılan teknoloji |
|------|----------------------|
| **UI** | Compose Multiplatform, Material 3 |
| **Dil** | Kotlin 2.3 |
| **Platformlar** | Android, iOS, JVM (Desktop), JS, WasmJS |
| **Ağ** | Ktor Client |
| **AI** | Groq API (Llama 3.3 70B Versatile) |
| **PDF** | Platforma özel (Android: PDFBox Android, JVM: PDFBox, vb.) |

---

## 📋 Gereksinimler

- **JDK 17** (veya projede tanımlı hedef)
- **Android Studio** veya **IntelliJ IDEA** (Android / JVM geliştirme için)
- **Xcode** (iOS derlemesi için, yalnızca macOS)
- **Groq API anahtarı** – [Groq Console](https://console.groq.com/) üzerinden ücretsiz alabilirsiniz.

---

## 🚀 Kurulum ve Çalıştırma

### 1. Projeyi klonlayın

```bash
git clone https://github.com/KULLANICI_ADINIZ/OzgecmisAnaliz.git
cd OzgecmisAnaliz
```

### 2. Groq API anahtarını ayarlayın

**Android**

- Proje kökünde `local.properties` dosyası oluşturun (yoksa) ve ekleyin:
  ```properties
  GROQ_API_KEY=your_groq_api_key_here
  ```

**JVM / Web (JS, WasmJS)**

- Ortam değişkeni olarak verin:
  ```bash
  export GROQ_API_KEY=your_groq_api_key_here
  ```

**iOS**

- `iosApp/iosApp/Info.plist` içinde `GROQ_API_KEY` değerini kendi anahtarınızla değiştirin. Bu dosyayı gerçek anahtar ile **commit etmeyin**.

### 3. Platforma göre çalıştırma

```bash
# JVM (Desktop) – macOS / Windows / Linux
./gradlew :composeApp:run

# Android
./gradlew :composeApp:installDebug

# Web (JS)
./gradlew :composeApp:jsBrowserDevelopmentRun

# Web (WasmJS)
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

iOS için Xcode ile `iosApp` projesini açıp derleyebilirsiniz.

---

## 📁 Proje yapısı (kısa)

```
composeApp/src/
├── commonMain/kotlin/.../
│   ├── domain/model/       # CvAnalysis, AtsScore, ExtractedProfile, vb.
│   ├── data/               # GroqRepository, network DTO’ları, PDF extractor interface
│   ├── presentation/       # Landing, Upload, Analysis, History ekranları
│   │   ├── ui/theme/       # Renk, tipografi, spacing, Material 3 tema
│   │   └── ui/components/  # Ortak UI bileşenleri
│   └── App.kt
├── androidMain/            # Android-specific (PDFBox, BuildConfig, vb.)
├── iosMain/
├── jvmMain/                # Desktop giriş noktası
├── jsMain/ & wasmJsMain/   # Web
└── webMain/                # Web kaynakları (HTML, CSS)
```

---

## 🔒 Gizlilik ve API anahtarı

- Analiz için **Groq API** kullanılır; CV metni API’ye gönderilir.
- API anahtarınızı **asla** repoya commit etmeyin; `local.properties` ve `.env` dosyalarınızı `.gitignore`’da tutun.

---

**CV Rehberi** – Özgeçmişinizi hedef role göre analiz edin, ATS skorunuzu ve iyileştirme önerilerinizi görün.
