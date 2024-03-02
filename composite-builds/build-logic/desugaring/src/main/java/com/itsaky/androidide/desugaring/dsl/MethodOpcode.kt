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

package com.itsaky.androidide.desugaring.dsl

import org.objectweb.asm.Opcodes

/**
 * The opcode for method invocation instructions.
 *
 * @author Akash Yadav
 */
enum class MethodOpcode(val insnName: String, val opcode: Int
) {

  /**
   * The opcode for `invokestatic`.
   */
  INVOKESTATIC("invoke-static", Opcodes.INVOKESTATIC),

  /**
   * The opcode for `invokespecial`.
   */
  INVOKESPECIAL("invoke-special", Opcodes.INVOKESPECIAL),

  /**
   * The opcode for `invokevirtual`.
   */
  INVOKEVIRTUAL("invoke-virtual", Opcodes.INVOKEVIRTUAL),

  /**
   * The opcode for `invokeinterface`.
   */
  INVOKEINTERFACE("invoke-interface", Opcodes.INVOKEINTERFACE),

  /**
   * Any opcode. This is for internal use only.
   */
  ANY("invoke-any", 0);

  companion object {

    /**
     * Finds the [MethodOpcode] with the given instruction name.
     */
    @JvmStatic
    fun find(insn: String): MethodOpcode? {
      return MethodOpcode.values().find { it.insnName == insn }
    }
  }
}