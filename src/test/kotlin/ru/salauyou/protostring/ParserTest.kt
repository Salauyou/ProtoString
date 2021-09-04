package ru.salauyou.protostring

import org.junit.Test
import ru.salauyou.protostring.parser.MyParser

import java.math.BigDecimal
import kotlin.test.assertEquals

/**
 * <p>Created on 2021-09-04
 * @author Aliaksandr Salauyou (sbt-solovev-an@mail.ca.sbrf.ru)
 */

class ParserTest {

    @Test
    fun `simple object`() {
        val string = """
            { simple : "ABC" escaped : "escaped\n\rtext\\\\" quoted : "\"quoted\" text" 
            int : 5006 decimal : -34.560 
            truly : true falsy : false 
            enumy : SOME }
            """
        val result = MyParser.parse(string)
        mapOf(
            "simple" to "ABC",
            "escaped" to "escaped\n\rtext\\\\",
            "quoted" to "\"quoted\" text",
            "int" to BigDecimal("5006"),
            "decimal" to BigDecimal("-34.560"),
            "truly" to true,
            "falsy" to false,
            "enumy" to "SOME"
        ).let {
            assertEquals(it, result)
        }
    }

}