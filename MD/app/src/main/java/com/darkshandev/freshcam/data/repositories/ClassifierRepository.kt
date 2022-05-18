package com.darkshandev.freshcam.data.repositories

import com.darkshandev.freshcam.data.datasources.ClassifierDatasource
import javax.inject.Inject

class ClassifierRepository @Inject constructor(private val dataSource: ClassifierDatasource)