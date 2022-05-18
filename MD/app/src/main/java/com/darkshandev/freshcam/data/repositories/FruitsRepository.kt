package com.darkshandev.freshcam.data.repositories

import com.darkshandev.freshcam.data.datasources.FruitsDatasource
import javax.inject.Inject

class FruitsRepository @Inject constructor(private val dataSource: FruitsDatasource)