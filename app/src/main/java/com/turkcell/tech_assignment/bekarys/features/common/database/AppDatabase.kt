package com.turkcell.tech_assignment.bekarys.features.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.turkcell.tech_assignment.bekarys.features.productlist.data.model.local.ProductEntry
import com.turkcell.tech_assignment.bekarys.features.productlist.data.model.local.ProductsDao


@Database(entities = arrayOf(
    ProductEntry::class
),
    version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun products(): ProductsDao
}