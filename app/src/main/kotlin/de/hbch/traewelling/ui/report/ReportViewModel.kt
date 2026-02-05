package de.hbch.traewelling.ui.report

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.hbch.traewelling.TraewelldroidApplication
import de.hbch.traewelling.api.models.report.Report
import de.hbch.traewelling.logging.Logger

class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val traewellingApi = (application as TraewelldroidApplication).traewellingApi

    suspend fun createReport(report: Report): Boolean {
        try {
            val response = traewellingApi.reportService.createReport(report)
            return response.isSuccessful
        } catch (ex: Exception) {
            Logger.captureException(ex)
            return false
        }
    }
}
