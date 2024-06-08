package views

class ConsoleTable {
    private val middle = "╋"
    private val leftTop = "┏"
    private val rightTop = "┓"
    private val middleTop = "┳"
    private val leftSide = "┣"
    private val rightSide = "┫"
    private val leftBottom = "┗"
    private val rightBottom = "┛"
    private val middleBottom = "┻"
    private val horizontal = "━"
    private val vertical = "┃"

    fun createTableWithText(text: String): String {
        var table = leftTop
        for (i in 0..text.length + 6) {
            table += horizontal
        }
        table += rightTop + "\n"

        table += vertical
        for (j in 0..text.length + 6) {
            table += " "
        }
        table += vertical + "\n"

        table += "$vertical   $text    $vertical\n"

        table += vertical
        for (j in 0..text.length + 6) {
            table += " "
        }
        table += vertical + "\n"


        table += leftBottom
        for (i in 0..text.length + 6) {
            table += horizontal
        }
        table += rightBottom

        return table
    }

    fun createTableFromList(lines: List<String>): String {
        val longerLine:String = lines.maxByOrNull { it.length } ?: return ""
        var table = leftTop
        for (i in 0..longerLine.length + 6) {
            table += horizontal
        }
        table += rightTop + "\n"

        for (line in lines) {
            table += "$vertical $line${" ".repeat(longerLine.length + 5 - line.length)} $vertical\n"
        }

        table += leftBottom
        for (i in 0..longerLine.length + 6) {
            table += horizontal
        }
        table += rightBottom

        return table
    }
}