# CV Rehberi – Project Structure

## Compose Multiplatform layout

```
composeApp/src/
├── commonMain/kotlin/.../
│   ├── domain/model/          # Domain models
│   │   ├── CvAnalysis.kt
│   │   ├── Navigation.kt
│   │   └── DateFormatter.kt
│   ├── data/mock/             # Mock & preview data
│   │   ├── MockData.kt
│   │   └── PreviewData.kt
│   ├── presentation/
│   │   ├── ui/
│   │   │   ├── theme/         # Design system
│   │   │   │   ├── Color.kt
│   │   │   │   ├── Type.kt
│   │   │   │   ├── Spacing.kt
│   │   │   │   ├── Shape.kt
│   │   │   │   ├── Theme.kt
│   │   │   │   └── DesignSystem.kt
│   │   │   └── components/    # Reusable composables
│   │   │       ├── PrimaryButton.kt
│   │   │       ├── SecondaryButton.kt
│   │   │       ├── BackButton.kt
│   │   │       ├── SectionHeader.kt
│   │   │       ├── ScoreCard.kt
│   │   │       ├── ScoreBreakdownCard.kt
│   │   │       ├── UploadArea.kt
│   │   │       ├── ProfileSummaryCard.kt
│   │   │       ├── KeywordChip.kt
│   │   │       ├── SuggestionCard.kt
│   │   │       ├── StrengthWeaknessItem.kt
│   │   │       └── HistoryListItem.kt
│   │   ├── landing/
│   │   ├── upload/
│   │   ├── analysis/
│   │   ├── history/
│   │   └── navigation/
│   ├── App.kt
│   └── PlatformTime.kt        # expect/actual
├── androidMain/
├── iosMain/
├── jvmMain/
└── ...
```

## Design system (presentation.ui.theme)

- **Colors** – `AppColors`: primary, surface, semantic (success, warning, error).
- **Typography** – `AppTypography`: display, headline, title, body, label.
- **Spacing** – `Spacing`: xs, sm, md, lg, xl, xxl.
- **Shapes** – `AppShape`: small, medium, large, extraLarge, chip.
- **Theme** – `CvRehberiTheme` applies the above via Material 3.

## Screens

- **Landing** – Hero, CTAs (Upload, Example, History), feature list.
- **Upload** – Back, section header, upload area, optional target role, analyze CTA.
- **Analysis** – Back, ATS score card, breakdown, profile, strengths/weaknesses, suggestions, keywords.
- **History** – Back, section header, list of past analyses or empty state.

Navigation is route-based state in `AppNavigation`; no external navigation library.
