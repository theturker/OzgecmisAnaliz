package com.alperenturker.cviews.data.mock

import com.alperenturker.cviews.domain.model.CvAnalysis

/**
 * Sample data for @Preview composables.
 * Uses [MockData] for consistency with the app.
 */
object PreviewData {
    val sampleAnalysis: CvAnalysis get() = MockData.sampleAnalysis
    /** Preview için tek örnek kayıt (geçmiş boş başladığı için). */
    val historyItems: List<CvAnalysis> get() = listOf(MockData.sampleAnalysis)
    val landingFeatures: List<String> get() = MockData.landingFeatures
}
