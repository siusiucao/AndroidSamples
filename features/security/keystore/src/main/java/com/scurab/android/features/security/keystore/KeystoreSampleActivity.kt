package com.scurab.android.features.security.keystore

import android.annotation.TargetApi
import android.app.Activity
import android.app.KeyguardManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.security.ConfirmationCallback
import android.security.ConfirmationPrompt
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.StrongBoxUnavailableException
import android.security.keystore.UserNotAuthenticatedException
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.scurab.android.features.security.keystore.app.R
import kotlinx.android.synthetic.main.activity_keystore.*
import java.security.*
import java.util.*
import java.util.concurrent.Executor
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.security.auth.x500.X500Principal

private const val STORE = "AndroidKeyStore"

private const val ALIAS_ASYMMETRIC_SIGN = "AS_SignVerify"
private const val ALIAS_ASYMMETRIC_ENCRYPT = "AS_EncryptDecrypt"
private const val ALIAS_ASYMMETRIC_ENCRYPT_TIME = "AS_EncryptDecrypt_Time"
private const val ALIAS_ASYMMETRIC_ENCRYPT_STRONG_BOX = "AS_EncryptDecrypt_StrongBox"
private const val ALIAS_ASYMMETRIC_ENCRYPT_CONFIRMATION = "AS_EncryptDecrypt_Confirmation"
private const val ALIAS_SYMMETRIC_ENCRYPT = "S_EncryptDecrypt"

/**
 * https://android.googlesource.com/platform/cts/+/master/tests/tests/keystore/src/android/keystore/cts/ImportWrappedKeyTest.java
 * https://proandroiddev.com/android-keystore-what-is-the-difference-between-strongbox-and-hardware-backed-keys-4c276ea78fd0
 * https://arxiv.org/pdf/1904.05572.pdf
 * https://source.android.com/security/trusty - TEE
 * https://source.android.com/security/keystore - HW keystore
 * https://www.youtube.com/watch?v=r54roADX2MI
 * https://github.com/googlesamples/android-BasicAndroidKeyStore
 * https://developer.android.com/training/articles/keystore#kotlin
 * https://developer.android.com/training/articles/keystore#WhichShouldIUse
 * https://gist.github.com/alphamu/cf44b2783fb2fd81cc53aca91276d481
 * https://android-developers.googleblog.com/2018/12/new-keystore-features-keep-your-slice.html
 * https://developer.android.com/training/articles/keystore#UsingAndroidKeyStore
 * https://developer.android.com/guide/topics/security/cryptography
 * https://proandroiddev.com/secure-data-in-android-encryption-in-android-part-1-e5fd150e316f
 * https://developer.android.com/reference/javax/crypto/KeyGenerator
 */

@TargetApi(23)
class KeystoreSampleActivity : Activity() {

    private val keyguardManager by lazy { (getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager) }
    private val ivParameterSpec = IvParameterSpec(byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15))
    private val aliasConfigs = listOf(
        KeyEntry(ALIAS_ASYMMETRIC_SIGN, Signatures.SHA256withECDSA.value) {
            loadKeyStoreKey(alias)
                ?: {
                    val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, STORE)
                    val parameterSpec: KeyGenParameterSpec =
                        KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY)
                        .run {
                            //region optional
                            setCertificateSubject(X500Principal("CN=$alias CA Certificate"))
                            setCertificateNotBefore(Date())
                            //setCertificateNotAfter(Date(System.currentTimeMillis()))
                            //endregion
                            setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)

                            build()
                        }
                    kpg.initialize(parameterSpec)
                    kpg.generateKeyPair()
                    loadKeyStoreKey(alias)
                }() ?: throw IllegalStateException("Not generated for alias:'$alias'")
        },
        KeyEntry(ALIAS_ASYMMETRIC_ENCRYPT, Ciphers.RSA_ECB_PKCS1Padding.value) {
            loadKeyStoreKey(alias)
                ?: {
                    val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, STORE)
                    val parameterSpec: KeyGenParameterSpec =
                        KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                            .run {
                                //region optional
                                setCertificateSubject(X500Principal("CN=$alias CA Certificate"))
                                setCertificateNotBefore(Date())
                                //setCertificateNotAfter(Date(System.currentTimeMillis()))
                                //endregion
                                setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                                build()
                            }
                    kpg.initialize(parameterSpec)
                    kpg.generateKeyPair()
                    loadKeyStoreKey(alias)
                }() ?: throw IllegalStateException("Not generated for alias:'$alias'")
        },
        KeyEntry(ALIAS_SYMMETRIC_ENCRYPT, Ciphers.AES_GCM_NoPadding.value) {
            val kpg = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES)
            val symmetricKey = kpg.generateKey()
            Keys(private = symmetricKey)
        },
        KeyEntry(ALIAS_ASYMMETRIC_ENCRYPT_STRONG_BOX, Ciphers.RSA_ECB_PKCS1Padding.value) {
            loadKeyStoreKey(alias)
                ?: {
                    val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, STORE)
                    val parameterSpec: KeyGenParameterSpec =
                        KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY)
                            .run {
                                //region optional
                                setIsStrongBoxBacked(true)
                                setCertificateSubject(X500Principal("CN=$alias CA Certificate"))
                                setCertificateNotBefore(Date())
                                //setCertificateNotAfter(Date(System.currentTimeMillis()))
                                //endregion
                                setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)

                                build()
                            }
                    kpg.initialize(parameterSpec)
                    kpg.generateKeyPair()
                    loadKeyStoreKey(alias)
                }() ?: throw IllegalStateException("Not generated for alias:'$alias'")
        },
        KeyEntry(ALIAS_ASYMMETRIC_ENCRYPT_TIME, Ciphers.RSA_ECB_PKCS1Padding.value) {
            loadKeyStoreKey(alias)
                ?: {
                    val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, STORE)
                    val parameterSpec: KeyGenParameterSpec =
                        KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                            .run {
                                //region optional
                                setUserAuthenticationRequired(true)
                                setUserAuthenticationValidityDurationSeconds(15)
                                //endregion
                                setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                                build()
                            }
                    kpg.initialize(parameterSpec)
                    kpg.generateKeyPair()
                    loadKeyStoreKey(alias)
                }() ?: throw IllegalStateException("Not generated for alias:'$alias'")
        },
        KeyEntry(ALIAS_ASYMMETRIC_ENCRYPT_CONFIRMATION, Ciphers.RSA_ECB_PKCS1Padding.value) {
            loadKeyStoreKey(alias)
                ?: {
                    if (!ConfirmationPrompt.isSupported(this@KeystoreSampleActivity)) {
                        throw IllegalStateException("ConfirmationPrompt is not supported!")
                    }
                    val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, STORE)
                    val parameterSpec: KeyGenParameterSpec =
                        KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                            .run {
                                //region optional
                                setUserConfirmationRequired(true)
                                //endregion
                                setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                                build()
                            }
                    kpg.initialize(parameterSpec)
                    kpg.generateKeyPair()
                    loadKeyStoreKey(alias)
                }() ?: throw IllegalStateException("Not generated for alias:'$alias'")
        }
    ).associateBy { it.alias }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keystore)
        sign.setSafeOnClickListener { sign() }
        verify.setSafeOnClickListener { verify() }
        encrypt.setSafeOnClickListener { encrypt() }
        decrypt.setSafeOnClickListener { decrypt() }
        clear.setSafeOnClickListener { output.text = null }

        reinitSpinner()
    }

    private fun View.setSafeOnClickListener(block: () -> Unit) {
        this.setOnClickListener { swallowing { block() } }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun swallowing(block: () -> Unit) {
        try {
            block()
        }  catch (e: UserNotAuthenticatedException) {
            Snackbar
                .make(findViewById(android.R.id.content), "UserNotAuthenticatedException", Snackbar.LENGTH_SHORT)
                .setAction("Authenticate") {
                    keyguardManager
                        .createConfirmDeviceCredentialIntent("Test", "Test")
                        .let { startActivity(it) }
                }
                .show()

        } catch (e: Throwable) {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && e is StrongBoxUnavailableException -> {
                    Snackbar
                        .make(findViewById(android.R.id.content), "StrongBoxUnavailableException", Snackbar.LENGTH_SHORT)
                        .show()
                }
                e is UnsecureDeviceException -> {
                    Snackbar
                        .make(findViewById(android.R.id.content), "Screen lock required", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Settings") {
                            startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
                        }
                        .show()
                }
                else -> {
                    val msg = e.message ?: e.javaClass.simpleName
                    Snackbar
                        .make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_INDEFINITE)
                        .show()
                    Log.e(TAG, msg)
                }
            }
        }
    }

    private fun reinitSpinner() {
        val items = aliasConfigs.keys.sorted().toMutableList()
        items.add(0, "Select alias...")

        aliases.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, items).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun generateKeyPairs() {
        val instance = KeyStore.getInstance(STORE)
        instance.load(null)

        if (false) {
            instance.aliases().toList().forEach {
                instance.deleteEntry(it)
            }
            return
        }

        val items = instance.aliases().toList()

        if (items.isEmpty()) {
            aliasConfigs.forEach { it.value.keys }
            toast("OK")
        } else {
            toast("Ignored, there are already aliases defined!")
        }
    }


    fun loadKeyStoreKey(alias: String): Keys? {
        val ks: KeyStore = KeyStore.getInstance(STORE).apply { load(null) }
        return if (ks.containsAlias(alias)) {
            val privateKey = ks.getKey(alias, null) as? PrivateKey
            val publicKey = ks.getCertificate(alias).publicKey
            Keys(publicKey, privateKey)
        } else null
    }

    fun getAlias(): String {
        return aliases.selectedItemPosition
            .takeIf { it > 0 }
            ?.let { aliases.selectedItem as String }
            ?: throw IllegalStateException("Pick alias")
    }

    @TargetApi(Build.VERSION_CODES.P)
    fun testWithTEEConfirmation() {
        data class ConfirmationPromptData(
            val sender: String,
            val receiver: String, val amount: String
        )

        val signature: ByteArray = byteArrayOf()
        val myDialogData = ConfirmationPromptData("Ashlyn", "Jordan", "$500")
        val threadReceivingCallback = Executor { runnable -> runnable.run() }
        val callback = MyConfirmationCallback()

        val dialog = ConfirmationPrompt.Builder(this)
            .setPromptText("${myDialogData.sender}, send ${myDialogData.amount} to ${myDialogData.receiver}?")
            .setExtraData(signature)
            .build()
        dialog.presentPrompt(threadReceivingCallback, callback)
    }

    //region verification
    private fun sign() {
        val data = input.text.toString()
        val dataBytes = data.toByteArray()
        val alias = getAlias()

        val keyEntry = aliasConfigs[alias]
        val keys = keyEntry?.keys ?: npe("Alias:$alias not found!")

        val signature = keyEntry.signature.run {
            initSign(keys.privateKey)
            update(dataBytes)
            sign()
        }

        key.setTextBase(signature)
        key.tag = signature
    }

    private fun verify() {
        val data = input.text.toString()
        val dataBytes = data.toByteArray()
        val signatureBytes = key.getTextBase()

        if (data.isEmpty() || signatureBytes == null) {
            toast("No key!")
            return
        }
        val alias = getAlias()
        val keyEntry = aliasConfigs[alias]
        val keys = keyEntry?.keys ?: npe("Alias:$alias not found!")

        val verified = keyEntry.signature.run {
            initVerify(keys.publicKey)
            update(dataBytes)
            verify(signatureBytes)
        }

        output.setText("Verified:$verified")
    }
    //endregion

    //region encryption
    fun encrypt() {
        val data = input.text.toString()
        val dataBytes = data.toByteArray()
        val alias = getAlias()

        val keyEntry = aliasConfigs[alias]
        val keys = keyEntry?.keys ?: npe("Alias:$alias not found!")

        val isAsymmetric = alias.isAsymmetricAlias()
        val keyForEncryption = if (isAsymmetric) keys.publicKey else keys.symmetricKey
        val encrypted = keyEntry.cipher.run {
            init(Cipher.ENCRYPT_MODE, keyForEncryption, ivParameterSpec.takeIf { !isAsymmetric })
            doFinal(dataBytes)
        }

        key.setTextBase(encrypted)
        key.tag = encrypted
    }

    fun decrypt() {
        val alias = getAlias()
        val encryptedBytes = key.getTextBase()

        if (encryptedBytes == null) {
            toast("No encrypted!")
            return
        }

        val keyEntry = aliasConfigs[alias]
        val keys = keyEntry?.keys ?: npe("Alias:$alias not found!")

        val isAsymmetric = alias.isAsymmetricAlias()
        val keyForDecryption: Key = if (isAsymmetric) keys.privateKey else keys.symmetricKey
        val decrypted = keyEntry.cipher.run {
            init(Cipher.DECRYPT_MODE, keyForDecryption, ivParameterSpec.takeIf { !isAsymmetric })
            doFinal(encryptedBytes)
        }

        output.setText(String(decrypted))
    }
    //endregion
}

private fun String.isAsymmetricAlias() : Boolean = toLowerCase().startsWith("as_")

private fun Context.toast(msg: CharSequence) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

private fun TextView.setTextBase(data: ByteArray?) {
    text = data?.let {
        String(Base64.encode(it, Base64.NO_WRAP))
            .chunked(48)
            .joinToString("\n")
    }
}

private fun TextView.getTextBase(): ByteArray? {
    return text.toString().replace("\n", "")
        .takeIf { it.isNotEmpty() }
        ?.let { Base64.decode(it, Base64.NO_WRAP) }
}

fun npe(msg: String): Nothing {
    throw NullPointerException(msg)
}

@TargetApi(Build.VERSION_CODES.P)
class MyConfirmationCallback : ConfirmationCallback() {
    override fun onConfirmed(dataThatWasConfirmed: ByteArray) {
        super.onConfirmed(dataThatWasConfirmed)
        // Sign dataThatWasConfirmed using your generated signing key.
        // By completing this process, you generate a "signed statement".
    }

    override fun onDismissed() {
        super.onDismissed()
        // Handle case where user declined the prompt in the
        // confirmation dialog.
    }

    override fun onCanceled() {
        super.onCanceled()
        // Handle case where your app closed the dialog before the user
        // could respond to the prompt.
    }

    fun onError(e: Exception?) {
        super.onError(e)
        // Handle the exception that the callback captured.
    }
}

class KeyEntry(
    val alias: String,
    val algorithmType: String,
    createKeysAction: KeyEntry.() -> Keys
) {

    val keys: Keys by lazy {
        createKeysAction()
    }
    val signature: Signature get() = Signatures.fromValue(algorithmType)
    val cipher: Cipher get() = Ciphers.fromValue(algorithmType)
}

class Keys(
    /*val entry: KeyStore.Entry,*/
    private val public: PublicKey? = null,
    private val private: Key? = null
) {
    val publicKey: PublicKey get() = public ?: npe("public")
    val privateKey: PrivateKey get() = private as? PrivateKey ?: npe("private key missing")
    val symmetricKey: SecretKey get() = private as? SecretKey ?: npe("symmetric key missing")
}


class UnsecureDeviceException : IllegalStateException()