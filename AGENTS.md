# Studio Portfolio - Implementation Plan

Dokumen ini adalah sumber kebenaran untuk implementasi desain Figma ke project Compose Multiplatform Web (wasmJs) di repository ini. Baca dokumen ini sebelum mengubah kode.

## 1. Tujuan

Membangun portfolio Andreas Manatar Lumban Gaol sebagai aplikasi Compose Multiplatform untuk web (wasmJs), dengan:

- Halaman Home, Work, About (Experience/Education), Contact, dan 3 halaman Project Detail.
- Bahasa EN/ID dan tema Light/Dark yang bisa diganti dari UI dan disimpan di settings.
- Layout responsif: Compact (mobile) dan Expanded (desktop).
- Navigasi yang terhubung dengan browser history, sehingga tombol Back/Forward browser bekerja.
- Semua gambar (logo skill, avatar, ikon sosial) diambil dari Compose Resources.
- Tampilan mengikuti desain Figma "Portfolio M3 Expressive".

## 2. Sumber desain

File Figma: `Portfolio M3 Expressive`
File key: `v1SRk1ZLX7T3h6alrBGYG8`

Halaman:

- `Design System` (`0:1`): token warna, tipografi, shape, komponen.
- `Desktop (1440)` (`4:2`): layout Expanded.
- `Mobile (390)` (`4:3`): layout Compact.

Frame referensi utama:

| Halaman | Desktop | Mobile |
|---|---|---|
| Home | `13:66` (EN Dark), `25:5307` (EN Light), `25:7686` (ID Dark) | `16:66`, `25:8158`, `25:8311` |
| Work | `23:2032`, `25:5389`, `25:7768` | `16:115`, `25:6532`, `24:2364` |
| About Experience | `23:2367`, `25:5533`, `24:1981` | `16:195`, `25:6729`, `24:2574` |
| About Education | `25:4493`, `25:5654`, `25:4678` | `25:4879`, `25:6824`, `25:5022` |
| Contact | `23:2510`, `25:5775`, `24:2170` | `16:276`, `25:6918`, `24:2719` |
| Project Detail AGallery | `18:328` | `18:881` |
| Project Detail TomaVue | `23:1132` | `23:1342` |
| Project Detail Caritas Kopi | `23:1198` | `23:1364` |

Catatan penting: Figma menyimpan 3 section presentasi (EN Dark, EN Light, ID Dark). Itu bukan 3 aplikasi terpisah. Aplikasi hanya punya satu runtime dengan toggle bahasa dan tema. Section tersebut dipakai sebagai referensi visual per kombinasi.

Token desain yang sudah ada di project:

- `Color.kt`: LightColors dan DarkColors, sumbernya collection "M3 Color" di Figma.
- `Type.kt`: Google Sans Flex, ukuran dan weight dari text style Figma.
- `Shape.kt`: radius dari collection "M3 Shape".

## 3. Dependency dan versi

Semua versi di bawah adalah versi terbaru yang tersedia saat plan ini dibuat. Untuk artifact Compose/JetBrains, ketersediaan varian `wasmJs` sudah dicek lewat Gradle module metadata dan klibs.io.

| Kebutuhan | Artifact | Versi | Catatan |
|---|---|---|---|
| Kotlin | `org.jetbrains.kotlin.multiplatform` | `2.4.21` | Versi stable terbaru. `2.5.0-Beta2` masih beta. |
| Compose Multiplatform | `org.jetbrains.compose` | `1.13.0-beta02` | Beta terbaru. Diperlukan untuk API Material 3 Expressive. |
| Compose compiler | `org.jetbrains.kotlin.plugin.compose` | `2.4.21` | Mengikuti versi Kotlin. |
| Material 3 | `org.jetbrains.compose.material3:material3` | `1.13.0-beta02` | Sama dengan versi Compose. |
| Adaptive | `org.jetbrains.compose.material3.adaptive:adaptive` | `1.3.0` | Varian `wasmJs` tersedia. Dipakai untuk `currentWindowAdaptiveInfo()`. |
| Navigation | `org.jetbrains.androidx.navigation:navigation-compose` | `2.10.0` | Varian `wasmJs` tersedia. Browser history via `bindToBrowserNavigation()`. |
| Lifecycle | `org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose`, `lifecycle-runtime-compose` | `2.11.0` | Sudah dipakai project. |
| Settings | `com.russhwolf:multiplatform-settings-no-arg` | `1.3.0` | Varian `wasmJs` tersedia. Backend web memakai `localStorage`. |
| Serialization | `org.jetbrains.kotlinx:kotlinx-serialization-json` + plugin | `1.12.0` / Kotlin `2.4.21` | Untuk type-safe navigation dan data. |
| Coroutines | `org.jetbrains.kotlinx:kotlinx-coroutines-core` | `1.11.0` | Varian `wasmJs` tersedia. |
| Browser DOM | `org.jetbrains.kotlinx:kotlinx-browser` | `0.5.0` | Untuk `document`, `window`, `history` di wasmJs. |
| Ikon | `org.jetbrains.compose.material:material-icons-extended` | `1.7.3` | Varian `wasmJs` tersedia. |

Dependency yang dihapus: target `js`. Sesuai keputusan, project hanya membangun `wasmJs`. Source set `jsMain` dan konfigurasi `js { }` dihapus. Browser history tetap didukung lewat Navigation Compose.

## 4. Arsitektur

```
shared/src/commonMain/kotlin/id/andreasmlbngaol/studio/
  App.kt                         # root composable, theme, nav host, scaffold
  theme/                         # Color.kt, Theme.kt, Type.kt, Shape.kt, Dimens.kt
  data/
    Models.kt                    # Project, ExperienceItem, EducationItem, Skill, SkillGroup
    PortfolioContent.kt          # semua copy EN/ID dan data project
    Strings.kt                   # typed strings per bahasa
  navigation/
    Routes.kt                    # @Serializable routes
    AppNavHost.kt                # NavHost + transisi
  state/
    AppSettings.kt               # Settings + State tema/bahasa
    AppConfig.kt                 # CompositionLocal
  ui/
    components/                  # Logo, Header, NavBar, LanguageToggle, ThemeToggle,
                                 # ConnectedNavGroup, SectionHeader, ProjectCard,
                                 # SkillChip, SocialIcons, Footer, FormField, dll.
    screens/                     # HomeScreen, WorkScreen, AboutScreen, ContactScreen,
                                 # ProjectDetailScreen
    layout/                      # AdaptivePortfolioScaffold
  util/                          # format, validasi, url helper

webApp/src/wasmJsMain/kotlin/id/andreasmlbngaol/studio/
  main.kt                        # ComposeViewport + bindToBrowserNavigation()
webApp/src/wasmJsMain/resources/
  index.html
  styles.css

functions/api/contact.ts         # Cloudflare Pages Function untuk form kontak
wrangler.toml                    # konfigurasi Cloudflare Pages
```

Prinsip:

- Semua UI dan state ada di `commonMain` agar mudah dites dan dipindah ke target lain nanti.
- Hanya entry point web dan browser binding yang ada di `wasmJsMain`.
- Tidak memakai API browser langsung dari `commonMain`.
- Tidak ada hardcoded copy di composable. Semua teks lewat `Strings` dan `PortfolioContent`.

## 5. State dan settings

`AppSettings` menyimpan:

- `themeMode`: `System`, `Light`, `Dark`. Default `System`.
- `language`: `EN`, `ID`. Default `EN`.

Implementasi:

- `Settings` dari `multiplatform-settings-no-arg`.
- Nilai awal dibaca dari settings. Jika belum ada, pakai default.
- Setiap perubahan langsung ditulis ke settings.
- `AppConfig` (CompositionLocal) membawa `strings`, `language`, `themeMode`, dan callback toggle.
- Tema mengikuti `isSystemInDarkTheme()` jika `themeMode == System`.
- Tidak ada flash tema: root `PortfolioTheme` langsung memakai nilai dari settings.

## 6. Navigasi dan browser history

Route type-safe dengan kotlinx.serialization:

```kotlin
@Serializable @SerialName("home") data object HomeRoute
@Serializable @SerialName("work") data object WorkRoute
@Serializable @SerialName("about") data object AboutRoute
@Serializable @SerialName("contact") data object ContactRoute
@Serializable @SerialName("project") data class ProjectRoute(val slug: String)
```

URL yang dihasilkan:

- `#home`
- `#work`
- `#about`
- `#contact`
- `#project/agallery`

Browser binding:

```kotlin
ComposeViewport(document.body!!) {
    App(onNavHostReady = { it.bindToBrowserNavigation() })
}
```

Dengan ini:

- Tombol Back/Forward browser berpindah route.
- Refresh halaman tetap di route yang sama.
- URL bisa dibuka langsung.
- `NavHost` tetap menjadi sumber kebenaran back stack.

Transisi: fade/dissolve 420ms, mengikuti prototype Figma. Back dari project detail kembali ke Work.

## 7. Layout responsif

Breakpoint:

- Compact: lebar < 600dp.
- Expanded: lebar >= 600dp.
- Header nav: lebar >= 960dp. Di bawah 960dp, connected nav group disembunyikan dan bottom navigation yang dipakai. Layout konten tetap mengikuti Compact/Expanded.
- Tabel Work memakai baris gaya Compact di bawah 960dp supaya kolom tidak terpotong.
- Di Compact, top bar auto-hide saat scroll ke bawah dan muncul lagi saat scroll ke atas. Project detail selalu menampilkan top bar karena berisi tombol Back.

Header dan bottom navigation dirender di `App.kt`, di luar `NavHost`, supaya tidak ikut fade saat pindah halaman. `NavHost` berada di atas `Surface(background)` yang opaque supaya tidak ada kedip putih saat transisi.

Compact:

- Header atas: logo Studio, nama "Studio", toggle bahasa EN/ID, toggle tema.
- Bottom navigation: Home, Work, About, Contact, memakai `NavigationBar`/`ShortNavigationBar`.
- Padding konten 20dp, lebar konten maksimal 600dp.
- Home selected work: kartu tanpa gambar, hanya judul, deskripsi, tombol Details/Code.
- Work: filter chip All, Android, Backend, AI, Web, Full stack.
- About: tab Experience/Education.
- Contact: form full width.
- Project detail: satu kolom, metadata ditumpuk.

Expanded:

- Header atas: logo Studio, connected button group nav (Home, Work, About, Contact), connected language group (EN/ID), theme icon button, tombol Hire me.
- Konten maksimal 1312dp, padding horizontal 64dp.
- Home selected work: 3 kartu dengan blok media/placeholder.
- Work: highlight 3 kartu + tabel 9 project. Tanpa filter chip, mengikuti desktop Figma.
- About: bio di kiri, tab Experience/Education di kanan; skills dalam baris label + chips.
- Contact: kolom kiri copy dan detail, kolom kanan form.
- Project detail: layout dua kolom untuk overview dan results.

## 8. Halaman dan fungsionalitas

### Home

- Hero: "Hi, I'm" + "Andreas", subtitle, shape collage dengan avatar.
- CTA: View work (ke Work), Download CV (placeholder sampai link diberikan).
- Selected work: 3 project (AGallery, TomaVue, Caritas Kopi).
  - Compact: tanpa media.
  - Expanded: dengan media placeholder.
  - Details membuka project detail, Code membuka GitHub.
- About teaser + link ke About.
- Contact CTA + tombol ke Contact.
- Footer.

### Work

- Judul "All projects", subtitle "Three highlights, plus nine more projects."
- Highlights: 3 kartu.
- More projects: tabel 9 baris, nomor 04 sampai 12.
- Kolom: #, Project, Platform, Stack, Year.
- Klik baris membuka GitHub di tab baru.
- Compact: filter chip memfilter daftar berdasarkan kategori project.
- Footer.

### About

- Judul "About", subtitle "Software engineer based in Medan, Indonesia."
- About me: bio.
- Tab Experience/Education memakai state lokal di `AboutScreen`, bukan route, supaya berpindah tab tidak mengembalikan scroll ke atas.
- Experience: IT Intern Caritas KAM 2026, Programming Lab Assistant 2024, Event Division 2022.
- Education: Universitas Sumatera Utara, Computer Science 2022-sekarang; Coding Camp, AI Engineer DBS Foundation 2026.
- Skills and stack: 5 grup (Languages, Frameworks, AI, Data, Tools), 23 chip dengan logo asli.
- Footer.

### Contact

- Judul "Contact", subtitle "Projects, questions, or just a hello."
- "Say hello." + copy.
- Detail: Andreas Manatar Lumban Gaol, lgandre45@gmail.com, Medan, Indonesia.
- Social icons: GitHub, LinkedIn, Instagram, Email.
- Form: Name, Email, Message, Send message.
- Validasi: nama tidak kosong, email valid, pesan minimal 10 karakter.
- Submit ke Cloudflare Pages Function `/api/contact`.
- Function mengirim email lewat Resend API (`RESEND_API_KEY`, `CONTACT_TO`, `CONTACT_FROM`).
- Jika endpoint atau env belum dikonfigurasi, form menampilkan pesan error. Tidak ada fallback otomatis ke `mailto`.
- GitHub Pages hanya menyajikan file statis, jadi `functions/api/contact.ts` tidak bisa jalan di sana. Untuk GH Pages, deploy `worker/src/index.ts` sebagai Cloudflare Worker, lalu isi `<meta name="contact-endpoint">` di `index.html` dengan URL Worker (contoh `https://studio-contact.<subdomain>.workers.dev/api/contact`). `CONTACT_TO` default `lgandre45@gmail.com`.
- Status: loading, sukses, error, dengan pesan yang jelas.
- Footer.

### Project Detail

Berlaku untuk AGallery, TomaVue, Caritas Kopi.

- Header: Back to work, Hire me.
- Tags: platform, stack, tahun.
- Judul besar, lead, tombol aksi:
  - AGallery: Download APK, Source code.
  - TomaVue: Live demo, Source code.
  - Caritas Kopi: Live app, Source code.
- Media placeholder besar.
- Metadata: Role, Timeline, Stack, Platform.
- Overview: Problem, Approach, Outcome, body.
- Results: 3 angka.
- Gallery: 3 placeholder dengan label.
- Next project.
- Footer.

## 9. Data model

```kotlin
data class Project(
    val slug: String,
    val name: String,
    val tagline: String,
    val description: String,
    val platform: String,
    val stack: String,
    val year: String,
    val tags: List<String>,
    val githubUrl: String?,
    val liveUrl: String?,
    val apkUrl: String?,
    val highlight: Boolean,
    val detail: ProjectDetail?,
)

data class ProjectDetail(
    val role: String,
    val timeline: String,
    val stack: String,
    val platform: String,
    val problem: String,
    val approach: String,
    val outcome: String,
    val body: List<String>,
    val results: List<ResultStat>,
    val gallery: List<String>,
)

data class Skill(val name: String, val logo: DrawableResource, val monochrome: Boolean)
data class SkillGroup(val title: String, val skills: List<Skill>)
```

Semua data EN/ID ada di `PortfolioContent.kt`. Tidak ada duplikasi string di UI.

## 10. Assets

Disimpan di `shared/src/commonMain/composeResources/`:

- `drawable/avatar.jpg` (sudah ada).
- `drawable/logo_kotlin.png`, `logo_java.png`, `logo_typescript.png`, `logo_python.png`, `logo_dart.png`, `logo_rust.png`, `logo_compose.png`, `logo_compose_multiplatform.png`, `logo_ktor.png`, `logo_spring_boot.png`, `logo_react.png`, `logo_flutter.png`, `logo_nextjs.png`, `logo_tensorflow.png`, `logo_pytorch.png`, `logo_postgresql.png`, `logo_firebase.png`, `logo_supabase.png`, `logo_docker.png`, `logo_git.png`, `logo_cloudflare.png`, `logo_vercel.png`, `logo_zorin.png`.
- `drawable/icon_github.png`, `icon_linkedin.png`, `icon_instagram.png`, `icon_email.png`.
- `font/` sudah berisi Google Sans Flex.

Ketentuan:

- Logo brand diambil dari Simple Icons / Devicon / official Compose Multiplatform artwork.
- Logo monokrom (Rust, Vercel, Next.js) disimpan putih dan di-tint dengan `MaterialTheme.colorScheme.onSurface` saat dirender, supaya terbaca di Light dan Dark.
- Logo berwarna (Kotlin, Java, TypeScript, dan lainnya) dirender apa adanya.
- Ukuran render: 18dp di desktop, 16dp di mobile.
- Avatar dipotong bulat dengan `CircleShape`.
- Logo aplikasi "Studio" digambar dengan Compose (rounded square + bintang 4 sudut) sebagai placeholder. Nanti tinggal diganti asset asli.
- Screenshot project masih placeholder ikon Material. Asset asli ditambahkan belakangan.
- Tidak memakai SVG langsung di `drawable`, karena `painterResource` hanya mendukung raster dan XML vector drawable. PNG dipilih agar aman di semua platform dan tetap tajam di web.

## 11. Pemetaan komponen Figma ke Compose

| Figma | Compose |
|---|---|
| Header | `Surface` + `Row`, `StudioLogo`, `ButtonGroup`, `IconButton` |
| Connected Button Group | `ButtonGroup` + `ToggleButton` dengan `ButtonGroupDefaults.connected*Shapes()`, mengikuti `ConnectedButtonGroupExample.kt` |
| Language toggle | `ButtonGroup` + `ToggleButton` (EN/ID) |
| Theme toggle | `IconButton` + ikon `DarkMode`/`LightMode` |
| Hire me | `Button` (filled) |
| Hero | `Column`, `Text`, `Row`/`Box` untuk shape collage, `Button` |
| Project card | `Card`/`OutlinedCard`, `Column`, `Button`/`TextButton` |
| Skill chip | `Surface`/`AssistChip` dengan logo + label |
| About tabs | `SecondaryTabRow` + `Tab`, atau connected group bila lebih cocok |
| Work table | `Column` + `Row`, `HorizontalDivider`, `Text` |
| Contact form | `OutlinedTextField`, `Button`, validasi state |
| Footer | `Row`, `IconButton`, `Text` |
| Project detail | `Column`, `Row`, `Card`, `Button`, `HorizontalDivider` |

Gunakan `MaterialExpressiveTheme`, `MotionScheme.expressive()`, dan shape/tipografi yang sudah ada.

## 12. Kesesuaian desain dan deviasi yang disengaja

Sesuai:

- Warna, tipografi, shape, spacing, radius, dan komponen utama mengikuti Figma.
- Layout desktop 1440 dan mobile 390 direplikasi.
- Copy EN dan ID mengikuti frame Figma.
- Logo skill memakai logo brand asli, bukan logo Kotlin semua.
- Home mobile tanpa gambar di selected work.

Deviasi:

- Figma punya 3 section statis; aplikasi memakai toggle runtime.
- Figma memakai Material Symbols ligatures; aplikasi memakai `material-icons-extended` dan PNG brand logo.
- Screenshot project masih placeholder ikon, karena asset asli belum tersedia.
- Download CV masih placeholder sampai link diberikan.
- Form kontak memakai Cloudflare Pages Function; API key disimpan sebagai secret di server, tidak pernah dikirim ke client. Tanpa env yang valid, form menampilkan error.
- Logo aplikasi masih placeholder bintang, sesuai permintaan.
- Work filter hanya muncul di Compact, mengikuti Figma mobile. Desktop tetap tanpa filter.

## 13. Urutan pengerjaan

1. Update dependency dan build config:
   - Hapus target `js`.
   - Tambah adaptive, navigation, settings, serialization, browser, coroutines.
   - Pindahkan entry point ke `wasmJsMain`.
2. Tambah assets:
   - Download/convert 23 logo skill.
   - Tambah ikon sosial.
   - Pastikan avatar dan font terbaca dari Compose Resources.
3. Bangun design system dan app shell:
   - Theme, settings, language, adaptive scaffold, header, bottom nav.
4. Bangun Home dan komponen bersama.
5. Bangun Work, filter mobile, dan tabel project.
6. Bangun About, tab Experience/Education, dan skills.
7. Bangun Contact dan Cloudflare function.
8. Bangun 3 project detail.
9. Polish responsive, browser history, accessibility, dan transisi.
10. Verifikasi build dan bandingkan dengan Figma.

## 14. Verifikasi

Perintah:

```bash
./gradlew :shared:wasmJsTest
./gradlew :webApp:wasmJsBrowserDevelopmentRun
./gradlew :webApp:wasmJsBrowserDistribution
```

Checklist manual:

- Semua route bisa dibuka dan refresh tetap di route yang sama.
- Back/Forward browser bekerja.
- Toggle EN/ID dan Light/Dark langsung berubah dan tersimpan setelah refresh.
- Layout Compact dan Expanded tidak overflow.
- Home mobile tanpa gambar, desktop dengan gambar.
- Work filter mobile berfungsi.
- About tab Experience/Education berfungsi tanpa mengubah URL dan tanpa reset scroll.
- Form contact validasi dan submit/fallback berjalan.
- Semua link GitHub/Live/APK terbuka di tab baru.
- Tidak ada error di console browser.
- Screenshot dibandingkan dengan frame Figma yang relevan.

## 15. Open items

- Screenshot project asli.
- Konfigurasi Cloudflare: `RESEND_API_KEY`, `CONTACT_TO`, `CONTACT_FROM`, domain pengirim.
- Logo aplikasi final.
- Asset project gallery final.

## 16. Status implementasi

Semua halaman, route, tema, bahasa, form, dan project detail sudah diimplementasikan di `commonMain`.

Yang sudah berjalan:

- Tema Light/Dark/System dan bahasa EN/ID tersimpan di `localStorage` (`studio.theme`, `studio.language`).
- Route `#home`, `#work`, `#about`, `#contact`, `#project/{slug}` dengan browser Back/Forward lewat `bindToBrowserNavigation()`.
- Tab Experience/Education di About memakai `rememberSaveable` lokal, bukan route. Berpindah tab tidak mengubah URL dan tidak mereset posisi scroll.
- Judul tab browser dinamis per halaman lewat `setDocumentTitle()` (`Home`, `Work`, `About`, `Contact`, `Nama Project`).
- Layout Compact (<600dp) dan Expanded (>=600dp) memakai `currentWindowAdaptiveInfoV2()`.
- Header dan bottom navigation dirender di `App.kt` di luar `NavHost`, jadi tidak ikut fade saat pindah halaman. `NavHost` berada di atas `Surface(background)` opaque supaya tidak ada kedip terang di Dark mode.
- Di bawah 960dp, connected nav group di header diganti bottom navigation. Tabel Work memakai baris Compact di bawah 960dp.
- Semua `Button`, `FilledTonalButton`, `TextButton`, `IconButton`, dan `FilledTonalIconButton` memakai `ButtonDefaults.shapes()` atau `IconButtonDefaults.shapes()`, sehingga shape morph dari pill ke rounded rectangle saat ditekan.
- Bottom navigation memakai ikon filled untuk item aktif dan outlined untuk item lain.
- Hero collage mengikuti Figma: pill 38 derajat di belakang avatar, burst `MaterialShapes.Cookie12Sided` untuk robot, dan ring avatar 8dp (desktop) / 5dp (mobile).
- Kartu highlight memakai `Row(height(IntrinsicSize.Max))` + `fillMaxHeight` + `Arrangement.SpaceBetween`, jadi tingginya sama tanpa `minLines` hardcoded.
- Top bar Compact auto-hide saat scroll ke bawah dan muncul lagi saat scroll ke atas (ambang 24dp hide / 8dp show, animasi 320ms). Di project detail top bar selalu tampil karena berisi tombol Back.
- Area logo dan nama "Studio" di header bisa diklik untuk kembali ke Home, termasuk versi Compact.
- Saat pertama dimuat, canvas Compose fade-in 620ms lewat CSS (`compose-component` opacity + class `app-ready`). `App.kt` memanggil `markAppReady()` setelah frame pertama, jadi rasterisasi bertahap Skia yang terlihat seperti reveal dari kiri atas tidak tampak.
- `navigateTopLevel()` punya guard: kalau sudah di route tujuan, tidak ada navigasi ulang. Ini mencegah kedip dan reset form saat menekan Hire me dari halaman Contact.
- Highlights punya tombol "View all" di desktop yang scroll ke bagian More projects (`BringIntoViewRequester`).
- Chip filter Work hanya dirender di Compact dan posisinya di bawah Highlights, tepat sebelum tabel More projects.
- Setiap chip skill membuka situs resmi skill tersebut di tab baru.
- Form kontak memanggil `POST /api/contact`. Kalau gagal, form menampilkan error dan tidak membuka `mailto`.
- CV diambil dari Compose Resources: `composeResources/files/cv.pdf`, dibuka lewat `Res.getUri("files/cv.pdf")`.
- Tombol Share dihapus dari project detail.

Deviasi kecil dari Figma:

- Header project detail di desktop ikut menampilkan toggle bahasa dan tema supaya konsisten dengan halaman lain.
- Top bar mobile halaman Work/About/Contact menampilkan toggle tema di samping toggle bahasa (frame Figma hanya menggambar toggle bahasa).
- Warna container kartu project diseragamkan ke `surfaceContainerLow`; variasi warna hanya di blok media dan statistik.
- Semua kartu project di Work mobile memakai media, sedangkan Home mobile tetap tanpa media sesuai Figma.
- Hero media project detail memakai radius 24dp di kedua breakpoint.

