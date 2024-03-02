/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.desugaring.internal.parsing

import com.itsaky.androidide.desugaring.dsl.MethodOpcode
import com.itsaky.androidide.desugaring.dsl.ReplaceMethodInsn
import com.itsaky.androidide.desugaring.internal.parsing.InsnLexer.Token
import com.itsaky.androidide.desugaring.internal.parsing.InsnLexer.TokenType

/**
 * A parser implementation to parse method invocation instructions.
 *
 * @author Akash Yadav
 */
internal class InsnParser(
  private val lexer: InsnLexer
) {

  private val cName = StringBuilder(128)
  private val md = StringBuilder(128)

  /**
   * Parse the input and return a list of [ReplaceMethodInsn].
   */
  fun parse(): List<ReplaceMethodInsn> {
    val inss = mutableListOf<ReplaceMethodInsn>()
    while (true) {
      inss.add(nextInsn() ?: break)
    }
    return inss
  }

  private fun nextInsn(): ReplaceMethodInsn? {
    if (peekToken().type == TokenType.EOF) {
      return null
    }

    val opcode = acceptOpcode()
    val fromClass = acceptClassName()
    accept(TokenType.HYPHEN)
    accept(TokenType.RANGULAR)
    val methodName = accept(TokenType.IDENTIFIER).text
    val methodDescriptor = acceptMethodDescriptor()

    accept(TokenType.EQUALS)
    accept(TokenType.RANGULAR)

    val toOpcode = acceptOpcode()
    val toClass = acceptClassName()
    accept(TokenType.HYPHEN)
    accept(TokenType.RANGULAR)
    val toMethod = accept(TokenType.IDENTIFIER).text
    val toDescriptor = acceptMethodDescriptor()

    accept(TokenType.SEMICOLON)
    accept(TokenType.SEMICOLON)

    return ReplaceMethodInsn.builder()
      .requireOpcode(opcode)
      .fromClass(fromClass)
      .methodName(methodName)
      .methodDescriptor(methodDescriptor)
      .toOpcode(toOpcode)
      .toClass(toClass)
      .toMethod(toMethod)
      .toMethodDescriptor(toDescriptor)
      .build()
  }

  private fun acceptOpcode(): MethodOpcode {
    val invoke = accept(TokenType.IDENTIFIER)
    val hyphen = accept(TokenType.HYPHEN)
    val op = accept(TokenType.IDENTIFIER)

    val opcode = MethodOpcode.find("${invoke.text}${hyphen.text}${op.text}")
    checkNotNull(opcode,
      "Unknown opcode ${invoke.text}${hyphen.text}${op.text}")
    return opcode!!
  }

  private fun acceptClassName(binaryName: Boolean = false): String {
    cName.setLength(0)

    var isFirst = true
    while (true) {
      val token = nextToken()

      if (token.type == TokenType.EOF) {
        throw ParseException("Unexpected EOF while parsing class name")
      }

      if (token.type == TokenType.IDENTIFIER) {
        if (isFirst) {
          if (token.text[0] != 'L') {
            throw ParseException(
              "Invalid class name: ${token.text} (${token.errDesc()})")
          }
          if (binaryName) {
            cName.append(token.text)
          } else {
            cName.append(token.text.substring(1))
          }
          isFirst = false
        } else {
          cName.append(token.text)
        }
        continue
      }

      if (token.type == TokenType.SLASH) {
        cName.append(if (binaryName) token.text else '.')
        continue
      }

      if (token.type == TokenType.SEMICOLON) {
        if (binaryName) {
          cName.append(token.text)
        }
        break
      }

      throw ParseException(
        "Error parsing class name. Unexpected ${token.errDesc()}")
    }

    return cName.toString()
  }

  private fun acceptMethodDescriptor(): String {
    md.setLength(0)

    md.append(accept(tokenType = TokenType.LPAR).text)

    // we could simply append everything between the LPAR and RPAR
    // but we need to validate the parameter types
    while (true) {
      val token = peekToken()

      if (token.type == TokenType.RPAR) {
        // end of method descriptor
        md.append(accept(TokenType.RPAR).text)
        break
      }

      if (token.text[0] == 'L') {
        md.append(acceptClassName(binaryName = true))
      } else {
        for (char in token.text) {
          when (char) {
            '[',
            'V',
            'Z',
            'C',
            'B',
            'S',
            'I',
            'J',
            'F',
            'D' -> continue
            else -> throw ParseException("Invalid parameter type: ${token.errDesc()}")
          }
        }

        md.append(accept(tokenType = token.type).text)
      }
    }

    // the return type
    var token = peekToken()

    // in case the method returns a multi-dimensional array
    while (token.type == TokenType.LBRAC) {
      md.append(accept(tokenType = TokenType.LBRAC).text)
      token = peekToken()
    }

    if (token.type == TokenType.IDENTIFIER) {
      when (token.text[0]) {
        'L' -> md.append(acceptClassName(binaryName = true))
        'V',
        'Z',
        'C',
        'B',
        'S',
        'I',
        'J',
        'F',
        'D' -> md.append(accept(tokenType = TokenType.IDENTIFIER).text)
        else -> throw ParseException("Invalid return type: ${token.errDesc()}")
      }
    } else {
      throw ParseException("Invalid return type: ${token.errDesc()}")
    }

    return md.toString()
  }

  /**
   * Advance to the next token and check if it has the given expected token type.
   */
  private fun accept(tokenType: TokenType): Token {
    val token = nextToken()
    if (token.type == TokenType.EOF) {
      throw ParseException("Unexpected EOF while parsing")
    }

    checkType(token, tokenType)
    return token
  }

  /**
   * Advance to the next token.
   */
  private fun nextToken(): Token {
    return lexer.nextToken()
  }

  /**
   * Peek at the next token.
   */
  private fun peekToken(): Token {
    return lexer.peek()
  }

  private fun checkNotNull(token: Any?, message: String) {
    if (token == null) {
      throw ParseException(message)
    }
  }

  private fun checkType(token: Token, type: TokenType) {
    if (token.type == type) {
      return
    }

    throw ParseException("Unexpected ${token.errDesc()}, expected: $type")
  }

  class ParseException(message: String) : RuntimeException(message)
}