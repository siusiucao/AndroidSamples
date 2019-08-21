package com.scurab.android.features.security.keystore

import javax.crypto.Cipher

private val ciphers = Ciphers.values().associateBy { it.value }

enum class Ciphers(val value: String) {
    AES_CBC_NoPadding("AES/CBC/NoPadding"),
    AES_CBC_PKCS7Padding("AES/CBC/PKCS7Padding"),
    AES_CTR_NoPadding("AES/CTR/NoPadding"),
    AES_ECB_NoPadding("AES/ECB/NoPadding"),
    AES_ECB_PKCS7Padding("AES/ECB/PKCS7Padding"),
    AES_GCM_NoPadding("AES/GCM/NoPadding"),
    RSA_ECB_NoPadding("RSA/ECB/NoPadding"),
    RSA_ECB_PKCS1Padding("RSA/ECB/PKCS1Padding"),
    RSA_ECB_OAEPWithSHA_1AndMGF1Padding("RSA/ECB/OAEPWithSHA-1AndMGF1Padding"),
    RSA_ECB_OAEPWithSHA_224AndMGF1Padding("RSA/ECB/OAEPWithSHA-224AndMGF1Padding"),
    RSA_ECB_OAEPWithSHA_256AndMGF1Padding("RSA/ECB/OAEPWithSHA-256AndMGF1Padding"),
    RSA_ECB_OAEPWithSHA_384AndMGF1Padding("RSA/ECB/OAEPWithSHA-384AndMGF1Padding"),
    RSA_ECB_OAEPWithSHA_512AndMGF1Padding("RSA/ECB/OAEPWithSHA-512AndMGF1Padding"),
    RSA_ECB_OAEPPadding("RSA/ECB/OAEPPadding");

    fun cipher() = Cipher.getInstance(value)

    companion object {
        fun fromValue(value: String): Cipher {
            return ciphers[value]
                ?.cipher()
                ?: throw IllegalArgumentException("Unknown value:'$value'")
        }
    }
}