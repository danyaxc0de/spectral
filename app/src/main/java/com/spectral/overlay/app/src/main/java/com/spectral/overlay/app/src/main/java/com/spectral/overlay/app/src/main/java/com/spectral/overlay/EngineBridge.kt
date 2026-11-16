package com.spectral.overlay

import android.content.Context
import java.io.File

object EngineBridge {
    lateinit var ctx: Context
    private var engineFile: File? = null

    data class Result(val move: String, val score: Float)

    fun ensureEngine(): File {
        if (engineFile?.exists() == true) return engineFile!!
        val f = File(ctx.filesDir, "stockfish")
        if (!f.exists()) {
            ctx.assets.open("stockfish").use { input ->
                f.outputStream().use { output -> input.copyTo(output) }
            }
            f.setExecutable(true)
        }
        engineFile = f
        return f
    }

    fun analyze(fen: String): Result {
        val exe = ensureEngine()
        val process = ProcessBuilder(exe.absolutePath).redirectErrorStream(true).start()
        var bestMove = "e2e4"
        var score = 0f

        try {
            process.outputStream.bufferedWriter().use { writer ->
                writer.write("uci\nisready\nposition fen $fen\ngo depth 12\n")
                writer.flush()

                val reader = process.inputStream.bufferedReader()
                while (true) {
                    val line = reader.readLine() ?: break
                    if (line.startsWith("info") && line.contains("score cp")) {
                        val parts = line.split(" ")
                        val idx = parts.indexOf("cp")
                        if (idx != -1 && idx + 1 < parts.size) {
                            score = parts[idx + 1].toFloat() / 100f
                        }
                    }
                    if (line.startsWith("bestmove")) {
                        bestMove = line.split(" ")[1]
                        break
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            process.destroy()
        }
        return Result(bestMove, score)
    }
}
