package com.scurab.android.features.security.keystore

import android.annotation.TargetApi
import android.app.Activity
import android.app.KeyguardManager
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.security.ConfirmationCallback
import android.security.ConfirmationPrompt
import android.security.keystore.KeyProperties
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.UserNotAuthenticatedException
import android.util.Log
import android.view.View
import android.widget.Toast
import com.scurab.android.features.security.keystore.app.R
import kotlinx.android.synthetic.main.activity_keystore.*
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.Signature
import java.util.concurrent.Executor

//private const val ALIAS = "KeystoreSampleActivity1"
private const val ALIAS = "KeystoreSampleActivity2"
private const val STORE = "AndroidKeyStore"

class KeystoreSampleActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keystore)
        btn1.setSafeOnClickListener { generateKeyPair() }
        btn2.setSafeOnClickListener { testSimple() }
        btn3.setSafeOnClickListener { testWithTEEConfirmation() }
    }

    private fun View.setSafeOnClickListener(block: () -> Unit) {
        this.setOnClickListener { swallowing { block() } }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun swallowing(block: () -> Unit) {
        try {
            block()
        } catch (e: UserNotAuthenticatedException) {
            (getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager)
                .createConfirmDeviceCredentialIntent("Test", "Test")
                .let { startActivity(it) }
        } catch (e: Throwable) {
            val msg = "Type:${e.javaClass.name}\nMsg:${e.message}"
            Log.e(TAG, msg)
            e.printStackTrace()
            toast(msg)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun generateKeyPair() {
        /*
        * Generate a new EC key pair entry in the Android Keystore by
        * using the KeyPairGenerator API. The private key can only be
        * used for signing or verification and only with SHA-256 or
        * SHA-512 as the message digest.
        */
        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, STORE)

        val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec
            .Builder(ALIAS, KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY)
            .run {
                setUserAuthenticationRequired(true)
                setUserAuthenticationValidityDurationSeconds(60)
                setAttestationChallenge(null)
                setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                build()
            }

        kpg.initialize(parameterSpec)

        val kp = kpg.generateKeyPair()
    }

    fun testSimple() {
        val aliases = KeyStore.getInstance(STORE).apply {
            load(null)
        }.aliases()
        val toList = aliases.toList()
        toList.toString()

        /*
         * Use a PrivateKey in the KeyStore to create a signature over
         * some data.
         */
        val ks: KeyStore = KeyStore.getInstance(STORE).apply {
            load(null)
        }
        val entry: KeyStore.Entry = ks.getEntry(ALIAS, null)
        if (entry !is KeyStore.PrivateKeyEntry) {
            Log.w(TAG, "Not an instance of a PrivateKeyEntry")
            return
        }

        val data = "Hovno".toByteArray()
        val signature: ByteArray = Signature.getInstance("SHA256withECDSA").run {
            initSign(entry.privateKey)
            update(data)
            sign()
        }

        val verify = Signature.getInstance("SHA256withECDSA").run {
            initVerify(entry.certificate)
            update(data)
            verify(signature)
        }

        toast("Verify:$verify")
    }

    @TargetApi(Build.VERSION_CODES.P)
    fun testWithTEEConfirmation() {
        data class ConfirmationPromptData(
            val sender: String,
            val receiver: String, val amount: String
        )

        val myExtraData: ByteArray = byteArrayOf()
        val myDialogData = ConfirmationPromptData("Ashlyn", "Jordan", "$500")
        val threadReceivingCallback = Executor { runnable -> runnable.run() }
        val callback = MyConfirmationCallback()

        val dialog = ConfirmationPrompt.Builder(this)
            .setPromptText("${myDialogData.sender}, send ${myDialogData.amount} to ${myDialogData.receiver}?")
            .setExtraData(myExtraData)
            .build()
        dialog.presentPrompt(threadReceivingCallback, callback)
    }


    private fun toast(msg: CharSequence) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
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