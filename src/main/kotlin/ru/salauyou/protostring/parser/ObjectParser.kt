package ru.salauyou.protostring.parser

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream


/**
 * <p>Created on 2021-09-04
 * @author Aliaksandr Salauyou (sbt-solovev-an@mail.ca.sbrf.ru)
 */
class ObjectParser {

    companion object {
        fun parse(input : String): Map<String, Any> {
            val lexer = ProtoLexer(CharStreams.fromString(input))
            val tokenStream = CommonTokenStream(lexer)
            val parser = ProtoParser(tokenStream)
            val visitor = ProtoParserVisitor()
            visitor.visitObj(parser.obj())
            return visitor.getResult()
        }
    }
}