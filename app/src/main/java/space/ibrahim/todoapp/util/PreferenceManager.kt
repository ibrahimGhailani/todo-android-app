package space.ibrahim.todoapp.util

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Credentials
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext context: Context
) {

    private val sharedPreferences by lazy {
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            MasterKey.DEFAULT_MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        val masterKeyAlias = MasterKey.Builder(context)
            .setKeyGenParameterSpec(keyGenParameterSpec)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "todo_secure_prefs",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private var editor = sharedPreferences.edit()

    fun getBasicAuth(): String? {
        return sharedPreferences.getString(BASIC_AUTH, null)
    }

    fun saveBasicAuth(username: String, password: String) {
        val credentials = Credentials.basic(username, password)
        Log.d(TAG, "saveBasicAuth: $credentials")
        editor.putString(BASIC_AUTH, credentials).apply()
    }

    fun clearBasicAuth() {
        editor.clear()
    }

    companion object {
        const val BASIC_AUTH = "basic_auth"
        const val TAG = "PreferenceManager"
    }
}