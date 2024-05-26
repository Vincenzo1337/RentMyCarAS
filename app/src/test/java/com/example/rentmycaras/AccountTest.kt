package com.example.rentmycaras

import com.example.rentmycaras.models.Account
import org.junit.Assert.assertEquals
import org.junit.Test

class AccountTest {
    @Test
    fun testAccountModel() {
        val account = Account("testUsername", "testPassword", 1, "testPhone", "testEmail")
        assertEquals("testUsername", account.userName)
        assertEquals("testPassword", account.password)
        assertEquals(1, account.userId)
        assertEquals("testPhone", account.phone)
        assertEquals("testEmail", account.email)
    }

    @Test
    fun testAccountCopy() {
        val account = Account("testUsername", "testPassword", 1, "testPhone", "testEmail")
        val copiedAccount = account.copy()

        assertEquals(account.userName, copiedAccount.userName)
        assertEquals(account.password, copiedAccount.password)
        assertEquals(account.userId, copiedAccount.userId)
        assertEquals(account.phone, copiedAccount.phone)
        assertEquals(account.email, copiedAccount.email)
    }

}
