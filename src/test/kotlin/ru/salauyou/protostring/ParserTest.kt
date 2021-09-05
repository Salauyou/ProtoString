package ru.salauyou.protostring

import org.junit.Test
import ru.salauyou.protostring.parser.ObjectParser

import java.math.BigDecimal
import kotlin.test.assertEquals

/**
 * <p>Created on 2021-09-04
 * @author Aliaksandr Salauyou (sbt-solovev-an@mail.ca.sbrf.ru)
 */

class ParserTest {

    @Test
    fun `empty object`() {
        val result = ObjectParser.parse("{ }")
        mapOf<String, Any>().let {
            assertEquals(it, result)
        }
    }

    @Test
    fun `simple object`() {
        val string = """
            { simple : "ABC" escaped : "e\scaped\n\rtext\\\\" quoted : "\"quoted\" text" 
            int : 5006 decimal : -34.560 
            truly : true falsy : false 
            enumy : SOME }
        """
        val result = ObjectParser.parse(string)
        mapOf(
            "simple" to "ABC",
            "escaped" to "e\\scaped\n\rtext\\\\",
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

    @Test
    fun `compound object`() {
        val string = """
           { 
              object { property: "ABC" number: 100 nested { truly: true falsy: false } } 
              prop: "PROP"
              emptyObject {} 
           } 
        """
        val result = ObjectParser.parse(string)
        mapOf(
            "object" to mapOf(
                "property" to "ABC",
                "number" to BigDecimal(100),
                "nested" to mapOf(
                    "truly" to true,
                    "falsy" to false
                )
            ),
            "prop" to "PROP",
            "emptyObject" to mapOf<String, Any>()
        ).let {
            assertEquals(it, result)
        }
    }

}