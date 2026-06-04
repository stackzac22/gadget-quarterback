# Gadget Quarterback 🏈📡

An all-in-one HUD app for managing your ESP32 ecosystem, featuring device mapping, ESPHome integration, WiFi/CSI radar visualization, and intelligent capability checking. Includes an SD Card setup helper for pre-configuration before device insertion.

## 🎯 Overview

Gadget Quarterback is a comprehensive management suite for ESP32-based devices (M5Stack, CYD, ESP32-C5, ESP32-C6, etc.). Inspired by Biscuit Manager and GhostESP Companion, it provides a unified dashboard for:

- **Device Ecosystem Mapping** - Visualize and organize all connected gadgets
- **ESPHome Integration** - Seamless device management and configuration
- **WiFi/CSI Radar** - Real-time network visualization and analysis
- **Bin File Manager** - Firmware storage, organization, and OTA flashing
- **Capability System** - Device ranking, performance metrics, and optimization suggestions
- **Schematic Helper** - Board pinouts, antenna recommendations, screen compatibility
- **Kismet Integration** - Network sniffing and analysis capabilities
- **SD Card Helper** - Pre-setup tool (Desktop CLI/GUI + Android module)

## 📱 Target Platform

- **Primary:** Android (Rooted Nexus 5X and compatible devices)
- **Minimum SDK:** Android 5.0 (API 21)
- **Features:** Full filesystem access for rooted devices, raw socket access for WiFi monitoring

## 🏗️ Project Structure

```
gadget-quarterback/
├── android-app/                    # Main HUD application
│   ├── app/
│   │   ├── src/main/
│   │   │   ├── java/com/stackzac22/quarterback/
│   │   │   │   ├── ui/                    # UI Components
│   │   │   │   ├── dashboard/            # HUD Dashboard
│   │   │   │   ├── esphome/              # ESPHome integration
│   │   │   │   ├── wifi_radar/           # WiFi/CSI visualization
│   │   │   │   ├── bin_manager/          # Firmware management
│   │   │   │   ├── capability/           # Device capability system
│   │   │   │   ├── schematic/            # Pinout & schematic helper
│   │   │   │   ├── device_manager/       # Device registry & sync
│   │   │   │   ├── sd_helper/            # SD Card setup module
│   │   │   │   ├── network/              # Network operations
│   │   │   │   └── utils/                # Utilities & helpers
│   │   │   └── res/                      # Resources
│   │   └── build.gradle
│   └── settings.gradle
├── sd-card-helper/
│   ├── cli/
│   │   ├── sdhelper-cli.py              # Command-line tool
│   │   ├── requirements.txt
│   │   ├── config_templates.py          # Config generation
│   │   └── validators.py                # Validation logic
│   └── gui/
│       ├── package.json
���       ├── main.js                      # Electron entry
│       ├── src/
│       │   ├── App.vue
│       │   ├── components/
│       │   └── assets/
│       └── README.md
├── docs/
│   ├── ARCHITECTURE.md
│   ├── SETUP_NEXUS5X.md
│   ├── ESPHOME_INTEGRATION.md
│   ├── WIFI_RADAR_GUIDE.md
│   ├── SD_HELPER_GUIDE.md
│   ├── CONTRIBUTING.md
│   └── API.md
├── resources/
│   ├── schemas/                   # Device schemas
│   ├── schematics/               # Board diagrams
│   └── templates/                # Config templates
├── .github/
│   ├── workflows/
│   │   ├── android-build.yml
│   │   ├── pr-tests.yml
│   │   └── release.yml
│   ├── ISSUE_TEMPLATE/
│   └── PULL_REQUEST_TEMPLATE.md
├── LICENSE                        # MIT License
├── README.md
├── ROADMAP.md
└── gradle/
```

## ✨ Key Features

### 1. HUD Dashboard
- Real-time device status overview
- Quick-access controls
- Network visualization
- System metrics

### 2. ESPHome Integration
- Automatic device discovery
- Configuration management
- OTA update handling
- Logs and diagnostics

### 3. Device Ecosystem Mapper
- Visual network representation
- Relationship mapping
- Optimization suggestions
- Setup templates

### 4. Bin File Manager
- Organized firmware storage
- Supported boards: M5Stack, CYD, ESP32-C5, ESP32-C6
- One-tap OTA flashing
- Version tracking & rollback

### 5. WiFi/CSI Radar
- Real-time network visualization
- Channel utilization analysis
- Signal strength mapping
- Kismet integration

### 6. Capability System
- Device capability scoring
- Performance metrics
- Compatibility checking
- Improvement suggestions

### 7. Schematic Helper
- Interactive pinout diagrams
- Screen compatibility matrix
- Antenna recommendations
- I2C/SPI bus analyzer

### 8. SD Card Helper ⭐
- **Desktop CLI** - Fast, scriptable setup
- **Desktop GUI** - User-friendly interface
- **Android Module** - On-device setup
- Auto-partition & format
- Template generation
- Pre-validation

## 🛠️ Technology Stack

- **Language:** Kotlin (primary), Java (legacy)
- **UI Framework:** Jetpack Compose / Material Design 3
- **Network:** OkHttp, Retrofit
- **Database:** Room (SQLite)
- **Threading:** Coroutines
- **ESPHome:** HTTP API (via Retrofit)
- **WiFi Monitoring:** Raw sockets, tcpdump
- **Visualization:** MPAndroidChart, Canvas
- **Desktop Helper:** Python (CLI), Electron (GUI)

## 🚀 Quick Start

### Android App

```bash
# Clone repository
git clone https://github.com/stackzac22/gadget-quarterback.git
cd gadget-quarterback/android-app

# Build
./gradlew build

# Deploy to device
./gradlew installDebug
```

### SD Card Helper - CLI (Desktop)

```bash
cd gadget-quarterback/sd-card-helper/cli

# Install dependencies
pip install -r requirements.txt

# Run with auto-detection
python3 sdhelper-cli.py --auto-detect

# Or specify device
python3 sdhelper-cli.py --device /dev/sdb --template full
```

### SD Card Helper - GUI (Desktop)

```bash
cd gadget-quarterback/sd-card-helper/gui

# Install dependencies
npm install

# Start GUI
npm start
```

## 📋 Prerequisites

- **Android Development:**
  - Android Studio 2023.1+
  - JDK 11+
  - Gradle 8.0+

- **Device:**
  - Rooted Nexus 5X or compatible (Android 5.0+)
  - At least one ESP32-based device

- **ESPHome:**
  - Local or cloud ESPHome instance
  - Network connectivity

- **Desktop Helper:**
  - Python 3.8+ (CLI)
  - Node.js 16+ (GUI)

## 📚 Documentation

- **[ARCHITECTURE.md](docs/ARCHITECTURE.md)** - System design & data flow
- **[SETUP_NEXUS5X.md](docs/SETUP_NEXUS5X.md)** - Device setup guide
- **[ESPHOME_INTEGRATION.md](docs/ESPHOME_INTEGRATION.md)** - Device management
- **[WIFI_RADAR_GUIDE.md](docs/WIFI_RADAR_GUIDE.md)** - Network monitoring
- **[SD_HELPER_GUIDE.md](docs/SD_HELPER_GUIDE.md)** - SD card setup
- **[CONTRIBUTING.md](docs/CONTRIBUTING.md)** - Developer guidelines
- **[API.md](docs/API.md)** - API reference

## 🔄 GitHub App Integration

This project uses a GitHub App for automation:

- **PR Automation** - Automated testing on pull requests
- **Issue Management** - Auto-labeling and triage
- **Release Management** - Automated version bumping and releases
- **Deployment** - CI/CD pipeline for testing and building

See [`.github/workflows/`](.github/workflows/) for configuration.

## 🎯 Roadmap

- [x] Project structure & documentation
- [ ] Android HUD Dashboard UI
- [ ] ESPHome device discovery
- [ ] SD Card Helper CLI (Python)
- [ ] SD Card Helper GUI (Electron)
- [ ] Bin file manager backend
- [ ] WiFi visualization (basic)
- [ ] Capability system MVP
- [ ] Schematic helper (first 5 boards)
- [ ] Android app compilation & testing
- [ ] Kismet integration
- [ ] CSI radar advanced features
- [ ] Play Store release

## 🤝 Contributing

Contributions welcome! Please:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

See [CONTRIBUTING.md](docs/CONTRIBUTING.md) for detailed guidelines.

## 📄 License

MIT License - See [LICENSE](LICENSE) file for details

## 🙏 Acknowledgments

- Inspired by **Biscuit Manager** and **GhostESP Companion**
- ESPHome community for excellent device support
- Kismet project for WiFi analysis tools
- Android community for best practices

## 📞 Support & Community

- **Issues:** [GitHub Issues](https://github.com/stackzac22/gadget-quarterback/issues)
- **Discussions:** [GitHub Discussions](https://github.com/stackzac22/gadget-quarterback/discussions)
- **Wiki:** [GitHub Wiki](https://github.com/stackzac22/gadget-quarterback/wiki)

---

**Status:** Early Development | **Version:** 0.1.0-alpha | **Last Updated:** 2026-06-04
