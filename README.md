# Wild Rift Assistant V2

Native Android UI for a practical Wild Rift game companion.

## V2 implemented
- Native dark gaming dashboard (no WebView)
- Role selector: MID / TOP / JGL / ADC / SUP
- Champion list with tier display
- Champion detail screen
- Build screen with Core / Counter / Situational structure
- Counter-build concept based on enemy threat
- Quick Guide: combo, laning, power spike, objectives
- Android Back navigation

## Cloud build
GitHub Actions builds with Gradle 8.11.1 + JDK 17.
APK: `app/build/outputs/apk/debug/app-debug.apk`

## Next planned engine
The next iteration will move the hard-coded demo data into structured champion/item data and add the real Item Recommendation Engine: enemy damage type, healing, armor/MR, shields, attack speed, CC and threat scoring.
