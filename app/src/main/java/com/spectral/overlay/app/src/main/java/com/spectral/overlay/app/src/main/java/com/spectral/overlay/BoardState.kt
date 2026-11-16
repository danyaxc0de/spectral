package com.spectral.overlay

object BoardState {
    private val squares = HashMap<String, String>()

    fun update(labels: List<String>) {
        squares.clear()
        val re = Regex("^([a-h][1-8])[, ]+(.+)$", RegexOption.IGNORE_CASE)
        for (s in labels) {
            val m = re.find(s) ?: continue
            squares[m.groupValues[1].lowercase()] = m.groupValues[2].lowercase()
        }
        if (squares.isEmpty()) return
        val fen = buildFen() ?: return
        OverlayService.requestHint(fen)
    }

    private fun piece(cell: String?): Char {
        cell ?: return '1'
        val s = cell.lowercase()
        return when {
            s.contains("king")   && s.contains("white") -> 'K'
            s.contains("king")   && s.contains("black") -> 'k'
            s.contains("queen")  && s.contains("white") -> 'Q'
            s.contains("queen")  && s.contains("black") -> 'q'
            s.contains("rook")   && s.contains("white") -> 'R'
            s.contains("rook")   && s.contains("black") -> 'r'
            s.contains("bishop") && s.contains("white") -> 'B'
            s.contains("bishop") && s.contains("black") -> 'b'
            s.contains("knight") && s.contains("white") -> 'N'
            s.contains("knight") && s.contains("black") -> 'n'
            s.contains("pawn")   && s.contains("white") -> 'P'
            s.contains("pawn")   && s.contains("black") -> 'p'
            else -> '1'
        }
    }

    private fun buildFen(): String? {
        val ranks = StringBuilder()
        for (r in 8 downTo 1) {
            var empties = 0
            for (f in 'a'..'h') {
                val p = piece(squares["$f$r"])
                if (p == '1') empties++
                else {
                    if (empties > 0) { ranks.append(empties); empties = 0 }
                    ranks.append(p)
                }
            }
            if (empties > 0) ranks.append(empties)
            if (r > 1) ranks.append('/')
        }
        return "$ranks w KQkq - 0 1"
    }
}
