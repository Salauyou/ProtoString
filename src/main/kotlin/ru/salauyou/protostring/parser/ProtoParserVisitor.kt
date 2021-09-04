package ru.salauyou.protostring.parser

import java.math.BigDecimal

/**
 * <p>Created on 2021-09-04
 * @author Aliaksandr Salauyou (sbt-solovev-an@mail.ca.sbrf.ru)
 */
open class ProtoParserVisitor : ProtoBaseVisitor<Unit>() {

    private val root = "root"
    private var currentMap : MutableMap<String, Any> = LinkedHashMap()
    private var currentKey : String = root

    override fun visitKeyValue(ctx: ProtoParser.KeyValueContext) {
        currentKey = ctx.ID().text
        visitChildren(ctx)
    }

    override fun visitObj(ctx: ProtoParser.ObjContext) {
        val parent = currentMap
        currentMap = LinkedHashMap()
        parent[currentKey] = currentMap
        visitChildren(ctx)
        currentMap = parent
    }

    override fun visitString(ctx: ProtoParser.StringContext) {
        currentMap[currentKey] = ctx.STRING().text.unquote()
    }

    override fun visitEnum(ctx: ProtoParser.EnumContext) {
        currentMap[currentKey] = ctx.ID().text
    }

    override fun visitNumber(ctx: ProtoParser.NumberContext) {
        currentMap[currentKey] = BigDecimal(ctx.text)
    }

    override fun visitTrue(ctx: ProtoParser.TrueContext) {
        currentMap[currentKey] = true
    }

    override fun visitFalse(ctx: ProtoParser.FalseContext) {
        currentMap[currentKey] = false
    }

    fun getResult() = currentMap[root] as Map<String, Any>

    companion object {

        private const val ESC = '\\'.code
        private val SKIPPED_CHARS = '\u0000'.code until '\u001f'.code
        private val ESCAPED_CHARS = mapOf(
            '\\'.code to "\\",
            '"'.code to "\"",
            'r'.code to "\r",
            'n'.code to "\n",
            't'.code to "\t")

        fun String.unquote(): String {
            return if (chars().noneMatch { c -> c == ESC || c in SKIPPED_CHARS }) {
                slice(1 .. length - 2)
            } else {
                val sb = StringBuilder(0)
                var escape = false
                chars().skip(1).limit((length - 2).toLong()).forEach { c ->
                    if (escape) {
                        ESCAPED_CHARS[c]
                            ?.let { sb.append(it) }
                            ?: sb.append('\\').append(c.toChar())
                        escape = false
                    } else if (c == ESC) {
                        escape = true
                    } else if (c in SKIPPED_CHARS) {
                        // skip
                    } else {
                        sb.append(c.toChar())
                    }
                }
                sb.toString()
            }
        }
    }

}