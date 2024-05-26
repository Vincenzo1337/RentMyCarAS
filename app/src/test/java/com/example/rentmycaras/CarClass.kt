package com.example.rentmycaras

import com.example.rentmycaras.models.Car
import com.example.rentmycaras.models.CarCategory
import com.example.rentmycaras.models.TimeBlock
import org.junit.Assert.assertEquals
import org.junit.Test

class CarTest {
    @Test
    fun testCarModel() {
        val timeBlock = TimeBlock(1622644800000, 1622731200000)
        val car = Car(1, "TestBrand", "TestType", CarCategory.BEV, true, listOf(timeBlock), "TestDescription", 1, null)
        assertEquals(1, car.id)
        assertEquals("TestBrand", car.brand)
        assertEquals("TestType", car.type)
        assertEquals(CarCategory.BEV, car.category)
        assertEquals(true, car.availability)
        assertEquals(listOf(timeBlock), car.timeBlock)
        assertEquals("TestDescription", car.description)
        assertEquals(1, car.ownerId)
        assertEquals(null, car.owner)
    }
    @Test
    fun testCarBrand() {
        val car = Car(brand = "TestBrand", type = "TestType", category = CarCategory.BEV, availability = true, timeBlock = listOf(TimeBlock(1622644800000, 1622731200000)), description = "TestDescription", ownerId = 1)
        assertEquals("TestBrand", car.brand)
    }

    @Test
    fun testCarType() {
        val car = Car(brand = "TestBrand", type = "TestType", category = CarCategory.BEV, availability = true, timeBlock = listOf(TimeBlock(1622644800000, 1622731200000)), description = "TestDescription", ownerId = 1)
        assertEquals("TestType", car.type)
    }

    @Test
    fun testCarCategory() {
        val car = Car(brand = "TestBrand", type = "TestType", category = CarCategory.BEV, availability = true, timeBlock = listOf(TimeBlock(1622644800000, 1622731200000)), description = "TestDescription", ownerId = 1)
        assertEquals(CarCategory.BEV, car.category)
    }

    @Test
    fun testCarAvailability() {
        val car = Car(brand = "TestBrand", type = "TestType", category = CarCategory.BEV, availability = true, timeBlock = listOf(TimeBlock(1622644800000, 1622731200000)), description = "TestDescription", ownerId = 1)
        assertEquals(true, car.availability)
    }
}
