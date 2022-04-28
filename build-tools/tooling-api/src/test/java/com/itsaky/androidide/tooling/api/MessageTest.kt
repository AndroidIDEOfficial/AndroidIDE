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

package com.itsaky.androidide.tooling.api

import com.google.common.truth.Truth.assertThat
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.itsaky.androidide.utils.ILogger
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/** @author Akash Yadav */
@RunWith(JUnit4::class)
class MessageTest {

    private val log = ILogger.newInstance(javaClass.simpleName)

    @Test
    fun testStringToMessage() {
        val message =
            "{'name': 'My name', 'otherName': 'Other other name', 'className': 'com.itsaky.androidide.tooling.api.MessageTest\$MessageToString'}"
        val obj = Message.fromJson(message)
        assertThat(obj).isInstanceOf(MessageToString::class.java)
        assertThat((obj as MessageToString).name).isEqualTo("My name")
        assertThat(obj.otherName).isEqualTo("Other other name")

        log.debug("obj.name is ${obj.name}")
    }

    @Test
    fun testMessageToString() {
        val message = MessageToString()
        val string = message.asJsonString()
        log.debug("MessageToString:$string")
        assertThat(string)
            .isEqualTo(
                "{\"name\":\"Your name\",\"otherName\":\"Other name\",\"className\":\"com.itsaky.androidide.tooling.api.MessageTest\$MessageToString\"}")
        assertThat((JsonParser.parseString(string) as JsonObject)["name"].asString)
            .isEqualTo("Your name")
        assertThat((JsonParser.parseString(string) as JsonObject)["otherName"].asString)
            .isEqualTo("Other name")
    }
    
    private open class Parent: Message() {
        val otherName: String = "Other name"
    }
    
    private class MessageToString : Parent() {
        val name: String = "Your name"
    }
}
