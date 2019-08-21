package com.scurab.android.features.security.keystore

import java.security.Signature

private val signatures = Signatures.values().associateBy { it.value }

enum class Signatures(val value: String) {
    MD5withRSA("MD5withRSA"),    //18+
    NONEwithECDSA("NONEwithECDSA"),    //23+
    NONEwithRSA("NONEwithRSA"),    //18+
    SHA1withDSA("SHA1withDSA"),    //19–22
    SHA1withECDSA("SHA1withECDSA"),    //19+
    SHA1withRSA("SHA1withRSA"),    //18+
    SHA1withRSA_PSS("SHA1withRSA/PSS"),    //23+
    SHA224withDSA("SHA224withDSA"),    //20–22
    SHA224withECDSA("SHA224withECDSA"),    //20+
    SHA224withRSA("SHA224withRSA"),    //20+
    SHA224withRSA_PSS("SHA224withRSA/PSS"),    //23+
    SHA256withDSA("SHA256withDSA"),    //19–22
    SHA256withECDSA("SHA256withECDSA"),    //19+
    SHA256withRSA("SHA256withRSA"),    //18+
    SHA256withRSA_PSS("SHA256withRSA/PSS"),    //23+
    SHA384withDSA("SHA384withDSA"),    //19–22
    SHA384withECDSA("SHA384withECDSA"),    //19+
    SHA384withRSA("SHA384withRSA"),    //18+
    SHA384withRSA_PSS("SHA384withRSA/PSS"),    //23+
    SHA512withDSA("SHA512withDSA"),    //19–22
    SHA512withECDSA("SHA512withECDSA"),    //19+
    SHA512withRSA("SHA512withRSA"),    //18+
    SHA512withRSA_PSS("SHA512withRSA/PSS");    //23+

    fun signature(): Signature = Signature.getInstance(value)

    companion object {
        fun fromValue(value: String): Signature {
            return signatures[value]
                ?.signature()
                ?: throw IllegalArgumentException("Unknown value:'$value'")
        }
    }
}