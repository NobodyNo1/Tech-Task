package com.turkcell.tech_assignment.bekarys.features.productlist.data.model.local

import androidx.room.*

@Dao
interface ProductsDao {

    @Transaction
    fun updateData(users: List<ProductEntry>) {
        deleteAllProducts()
        insertAll(users)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<ProductEntry>)

    @Query("SELECT * FROM products")
    fun getProducts(): List<ProductEntry>

    @Query("SELECT * FROM products WHERE productId LIKE :productId")
    fun getProduct(productId: String): List<ProductEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(productEntry: ProductEntry)

    @Delete
    fun delete(productEntry: ProductEntry)

    @Update
    fun update(productEntry: ProductEntry)

    @Query("DELETE FROM products")
    fun deleteAllProducts()
}